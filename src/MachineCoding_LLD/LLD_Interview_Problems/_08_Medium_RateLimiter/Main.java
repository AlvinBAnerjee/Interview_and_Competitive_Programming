package MachineCoding_LLD.LLD_Interview_Problems._08_Medium_RateLimiter;

import MachineCoding_LLD.LLD_Interview_Problems._08_Medium_RateLimiter.time.ManualTimeSource;

/**
 * A scripted, non-interactive walkthrough (like the other problems' {@code Main}s): token-bucket
 * burst + refill, the fixed-window boundary weakness, and how sliding window smooths it — all on a
 * hand-driven clock so the output is deterministic. For correctness + the concurrency stress test,
 * see {@code RateLimiterTest}.
 */
public final class Main {

    public static void main(String[] args) {
        tokenBucketBurstThenRefill();
        separator();
        fixedWindowBoundaryBurst();
        separator();
        slidingWindowSmooths();
    }

    /** Token bucket: burst up to capacity, get throttled, then refill over time. */
    private static void tokenBucketBurstThenRefill() {
        System.out.println("=== Token bucket: capacity 5, refill 2/sec ===\n");
        ManualTimeSource clock = new ManualTimeSource();
        RateLimiter limiter = RateLimiterFactory.tokenBucket(5, 2.0, clock);

        System.out.print("burst 7 requests: ");
        for (int i = 0; i < 7; i++) {
            System.out.print(limiter.allow("user") ? "Y " : "n ");
        }
        System.out.println("  (5 allowed, then empty)");

        clock.advanceSeconds(1.0); // refills 2 tokens
        System.out.print("after +1s (refill 2): ");
        for (int i = 0; i < 3; i++) {
            System.out.print(limiter.allow("user") ? "Y " : "n ");
        }
        System.out.println("  (2 allowed, then empty again)");
    }

    /** Fixed window's weakness: 2×limit can pass across the boundary in a short span. */
    private static void fixedWindowBoundaryBurst() {
        System.out.println("=== Fixed window: limit 3 / 1000ms — the boundary burst ===\n");
        ManualTimeSource clock = new ManualTimeSource();
        RateLimiter limiter = RateLimiterFactory.fixedWindow(3, 1000, clock);

        clock.advanceMillis(900); // near the end of window 0
        System.out.print("t=900ms  (3 requests): ");
        int allowed = countAllowed(limiter, "user", 3);
        System.out.println(allowed + " allowed");

        clock.advanceMillis(200); // t=1100ms → window 1
        System.out.print("t=1100ms (3 requests): ");
        allowed = countAllowed(limiter, "user", 3);
        System.out.println(allowed + " allowed   -> 6 in 200ms across the boundary!");
    }

    /** Sliding window: same boundary, but the previous window's weight throttles the burst. */
    private static void slidingWindowSmooths() {
        System.out.println("=== Sliding window: limit 3 / 1000ms — boundary smoothed ===\n");
        ManualTimeSource clock = new ManualTimeSource();
        RateLimiter limiter = RateLimiterFactory.slidingWindow(3, 1000, clock);

        clock.advanceMillis(900);
        System.out.print("t=900ms  (3 requests): ");
        System.out.println(countAllowed(limiter, "user", 3) + " allowed");

        clock.advanceMillis(200); // t=1100ms → previous window still weighs 0.9
        System.out.print("t=1100ms (3 requests): ");
        System.out.println(countAllowed(limiter, "user", 3) + " allowed   -> throttled, not a full fresh 3");
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

    private static void separator() {
        System.out.println("\n======================================================\n");
    }
}
