package MachineCoding_LLD.LLD_Interview_Problems._08_Medium_RateLimiter;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import MachineCoding_LLD.LLD_Interview_Problems._08_Medium_RateLimiter.strategy.FixedWindowStrategy;
import MachineCoding_LLD.LLD_Interview_Problems._08_Medium_RateLimiter.time.ManualTimeSource;

/**
 * Dependency-free harness (plain {@code main}, no JUnit — matching this repo). Each check prints
 * PASS/FAIL; the process exits non-zero if anything fails so {@code &&} chains / CI catch it.
 *
 * Covers all three algorithms (limit, refill, boundary behaviour), per-client isolation, idle
 * eviction, input validation, and — the headline — two concurrency stress tests proving the limit
 * is never exceeded under contention (frozen clock → an exact "at most L" assertion).
 */
public final class RateLimiterTest {

    private static int failures = 0;

    public static void main(String[] args) throws InterruptedException {
        testTokenBucketBurstThenRefill();
        testFixedWindowResetsAtBoundary();
        testFixedWindowBoundaryBurstIsPossible();
        testSlidingWindowSmoothsBoundary();
        testPerClientIsolation();
        testIdleEviction();
        testInvalidConfigRejected();
        testNullClientRejected();
        testConcurrentFixedWindowNeverExceedsLimit();
        testConcurrentTokenBucketNeverExceedsCapacity();

        System.out.println();
        if (failures == 0) {
            System.out.println("ALL TESTS PASSED ✅");
        } else {
            System.out.println(failures + " TEST(S) FAILED ❌");
            System.exit(1);
        }
    }

    /** Burst up to capacity, then refill by elapsed time. */
    private static void testTokenBucketBurstThenRefill() {
        ManualTimeSource clock = new ManualTimeSource();
        RateLimiter limiter = RateLimiterFactory.tokenBucket(5, 2.0, clock);

        int burst = countAllowed(limiter, "u", 7);
        check("token bucket allows exactly capacity in a burst", burst == 5, "allowed=" + burst);

        clock.advanceSeconds(1.0); // +2 tokens
        int afterRefill = countAllowed(limiter, "u", 5);
        check("token bucket refills 2 tokens after 1s", afterRefill == 2, "allowed=" + afterRefill);
    }

    /** Fixed window resets its allowance at the next window. */
    private static void testFixedWindowResetsAtBoundary() {
        ManualTimeSource clock = new ManualTimeSource();
        RateLimiter limiter = RateLimiterFactory.fixedWindow(3, 1000, clock);

        int w0 = countAllowed(limiter, "u", 5);
        clock.advanceMillis(1000); // next window
        int w1 = countAllowed(limiter, "u", 5);
        check("fixed window allows limit then resets next window", w0 == 3 && w1 == 3,
                "w0=" + w0 + " w1=" + w1);
    }

    /** Fixed window's known flaw: up to 2×limit across a boundary. */
    private static void testFixedWindowBoundaryBurstIsPossible() {
        ManualTimeSource clock = new ManualTimeSource();
        RateLimiter limiter = RateLimiterFactory.fixedWindow(3, 1000, clock);

        clock.advanceMillis(900);
        int end = countAllowed(limiter, "u", 3);   // end of window 0
        clock.advanceMillis(200);                   // t=1100 → window 1
        int start = countAllowed(limiter, "u", 3);  // start of window 1
        check("fixed window lets 2x limit cross the boundary", end + start == 6,
                "end=" + end + " start=" + start);
    }

    /** Sliding window throttles that same boundary burst. */
    private static void testSlidingWindowSmoothsBoundary() {
        ManualTimeSource clock = new ManualTimeSource();
        RateLimiter limiter = RateLimiterFactory.slidingWindow(3, 1000, clock);

        clock.advanceMillis(900);
        int end = countAllowed(limiter, "u", 3);    // fills window 0 (curCount=3)
        clock.advanceMillis(200);                    // t=1100, prevWeight=0.9 → est=3*0.9=2.7
        int start = countAllowed(limiter, "u", 3);   // only 1 fits before est >= 3
        check("sliding window smooths the boundary (fewer than fixed's 6)",
                end == 3 && start == 1 && (end + start) < 6, "end=" + end + " start=" + start);
    }

    /** Each client has its own independent bucket. */
    private static void testPerClientIsolation() {
        ManualTimeSource clock = new ManualTimeSource();
        RateLimiter limiter = RateLimiterFactory.fixedWindow(2, 1000, clock);

        int a = countAllowed(limiter, "A", 5);
        int b = countAllowed(limiter, "B", 5);
        check("per-client isolation (each gets its own limit)", a == 2 && b == 2, "A=" + a + " B=" + b);
    }

    /** Idle clients are evicted so the map stays bounded. */
    private static void testIdleEviction() {
        ManualTimeSource clock = new ManualTimeSource();
        long idleNanos = 1_000_000_000L; // 1s
        RateLimiter limiter = new RateLimiter(
                () -> new FixedWindowStrategy(2, 1_000_000_000L, clock), clock, idleNanos);

        limiter.allow("A");
        limiter.allow("B");
        check("two clients tracked", limiter.trackedClients() == 2, "n=" + limiter.trackedClients());

        clock.advanceSeconds(2.0); // both now idle > 1s
        int removed = limiter.evictIdle();
        check("idle clients evicted", removed == 2 && limiter.trackedClients() == 0,
                "removed=" + removed + " tracked=" + limiter.trackedClients());

        limiter.allow("A"); // recreated fresh
        check("client recreated after eviction", limiter.trackedClients() == 1,
                "n=" + limiter.trackedClients());
    }

    private static void testInvalidConfigRejected() {
        boolean cap = throwsIae(() -> RateLimiterFactory.tokenBucket(0, 1.0));
        boolean refill = throwsIae(() -> RateLimiterFactory.tokenBucket(5, 0.0));
        boolean limit = throwsIae(() -> RateLimiterFactory.fixedWindow(0, 1000));
        check("invalid config rejected", cap && refill && limit,
                "cap=" + cap + " refill=" + refill + " limit=" + limit);
    }

    private static void testNullClientRejected() {
        RateLimiter limiter = RateLimiterFactory.fixedWindow(2, 1000);
        check("null clientId rejected", throwsIae(() -> limiter.allow(null)), "no exception");
    }

    /**
     * THE atomicity test. Freeze the clock (no window advance possible), then hammer one client
     * with a limit of L from many threads: <b>exactly L</b> must be allowed. Any more means a
     * check-and-increment race slipped through; any fewer means a lost allow.
     */
    private static void testConcurrentFixedWindowNeverExceedsLimit() throws InterruptedException {
        int limit = 1000;
        ManualTimeSource frozen = new ManualTimeSource(42_000_000_000L); // fixed instant
        RateLimiter limiter = RateLimiterFactory.fixedWindow(limit, 1_000_000, frozen);
        int allowed = stormOneClient(limiter, "hot", 64, 200);
        check("fixed window: exactly the limit allowed under contention", allowed == limit,
                "allowed=" + allowed + " (limit " + limit + ")");
    }

    /** Same, for token bucket: frozen clock → no refill → exactly capacity allowed. */
    private static void testConcurrentTokenBucketNeverExceedsCapacity() throws InterruptedException {
        int capacity = 1000;
        ManualTimeSource frozen = new ManualTimeSource(42_000_000_000L);
        RateLimiter limiter = RateLimiterFactory.tokenBucket(capacity, 1.0, frozen);
        int allowed = stormOneClient(limiter, "hot", 64, 200);
        check("token bucket: exactly capacity allowed under contention", allowed == capacity,
                "allowed=" + allowed + " (capacity " + capacity + ")");
    }

    // ---- helpers ----

    /** Fire {@code threads × opsPerThread} concurrent allow() calls for one client; count allows. */
    private static int stormOneClient(RateLimiter limiter, String client, int threads, int opsPerThread)
            throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        CountDownLatch startGun = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(threads);
        AtomicInteger allowed = new AtomicInteger();

        for (int t = 0; t < threads; t++) {
            pool.submit(() -> {
                try {
                    startGun.await();
                    for (int i = 0; i < opsPerThread; i++) {
                        if (limiter.allow(client)) {
                            allowed.incrementAndGet();
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    done.countDown();
                }
            });
        }

        startGun.countDown();
        done.await();
        pool.shutdown();
        pool.awaitTermination(30, TimeUnit.SECONDS);
        return allowed.get();
    }

    private static int countAllowed(RateLimiter limiter, String client, int n) {
        int allowed = 0;
        for (int i = 0; i < n; i++) {
            if (limiter.allow(client)) {
                allowed++;
            }
        }
        return allowed;
    }

    private static boolean throwsIae(Runnable r) {
        try {
            r.run();
            return false;
        } catch (IllegalArgumentException expected) {
            return true;
        }
    }

    private static void check(String name, boolean ok, String actual) {
        System.out.printf("[%s] %s%s%n", ok ? "PASS" : "FAIL", name, ok ? "" : "  -> got: " + actual);
        if (!ok) {
            failures++;
        }
    }
}
