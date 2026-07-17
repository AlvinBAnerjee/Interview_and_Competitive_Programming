package MachineCoding_LLD.LLD_Interview_Problems._04_Easy_LRUCache;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import MachineCoding_LLD.LLD_Interview_Problems._04_Easy_LRUCache.observer.EvictionListener;

/**
 * Dependency-free harness (plain {@code main}, no JUnit — matching this repo). Each check prints
 * PASS/FAIL; the process exits non-zero if anything fails so {@code &&} chains / CI catch it.
 *
 * Covers: O(1)-LRU eviction order, read-refreshes-recency, update-counts-as-use, FIFO ignoring
 * access, capacity bound, miss/null handling, the eviction Observer, striped effective-capacity,
 * and two concurrency stress tests (striped + global-lock) proving no corruption under contention.
 */
public final class LRUCacheTest {

    private static int failures = 0;

    public static void main(String[] args) throws InterruptedException {
        testLruEvictionOrder();
        testReadRefreshesRecency();
        testUpdateCountsAsUse();
        testFifoIgnoresAccess();
        testCapacityNeverExceeded();
        testMissReturnsNull();
        testNullRejected();
        testEvictionListenerFires();
        testStripedEffectiveCapacity();
        testConcurrentStripedNoCorruption();
        testConcurrentGlobalLockNoCorruption();

        System.out.println();
        if (failures == 0) {
            System.out.println("ALL TESTS PASSED ✅");
        } else {
            System.out.println(failures + " TEST(S) FAILED ❌");
            System.exit(1);
        }
    }

    /** Classic: fill, then an overflow evicts the least-recently-used key. */
    private static void testLruEvictionOrder() {
        Cache<String, Integer> c = Caches.lru(2);
        c.put("a", 1);
        c.put("b", 2);
        c.put("c", 3); // capacity 2 → "a" (LRU) evicted
        check("LRU evicts the least-recently-used", c.get("a") == null && c.get("b") == 2 && c.get("c") == 3,
                "a=" + c.get("a") + " b=" + c.get("b") + " c=" + c.get("c"));
    }

    /** A get() must move the key to most-recent, changing the next eviction victim. */
    private static void testReadRefreshesRecency() {
        Cache<String, Integer> c = Caches.lru(2);
        c.put("a", 1);
        c.put("b", 2);
        c.get("a");    // touch a → b becomes LRU
        c.put("c", 3); // evicts b, not a
        check("read refreshes recency (b evicted, a kept)", c.get("a") == 1 && c.get("b") == null,
                "a=" + c.get("a") + " b=" + c.get("b"));
    }

    /** Updating an existing key counts as a use under LRU. */
    private static void testUpdateCountsAsUse() {
        Cache<String, Integer> c = Caches.lru(2);
        c.put("a", 1);
        c.put("b", 2);
        c.put("a", 11); // update a → b becomes LRU
        c.put("c", 3);  // evicts b
        check("update counts as recent use (b evicted)", c.get("a") == 11 && c.get("b") == null,
                "a=" + c.get("a") + " b=" + c.get("b"));
    }

    /** FIFO must ignore reads: the oldest insertion is evicted regardless of access. */
    private static void testFifoIgnoresAccess() {
        Cache<String, Integer> c = Caches.fifo(2);
        c.put("a", 1);
        c.put("b", 2);
        c.get("a");    // FIFO ignores this
        c.put("c", 3); // evicts a (oldest inserted), not b
        check("FIFO ignores access (a evicted)", c.get("a") == null && c.get("b") == 2 && c.get("c") == 3,
                "a=" + c.get("a") + " b=" + c.get("b") + " c=" + c.get("c"));
    }

    /** Size must never exceed capacity, even after many inserts. */
    private static void testCapacityNeverExceeded() {
        Cache<Integer, Integer> c = Caches.lru(5);
        boolean ok = true;
        for (int i = 0; i < 1000; i++) {
            c.put(i, i);
            if (c.size() > 5) {
                ok = false;
                break;
            }
        }
        check("size never exceeds capacity", ok && c.size() == 5, "size=" + c.size());
    }

    private static void testMissReturnsNull() {
        Cache<String, Integer> c = Caches.lru(2);
        check("miss returns null", c.get("nope") == null, "non-null");
    }

    private static void testNullRejected() {
        Cache<String, Integer> c = Caches.lru(2);
        boolean nullKey = throwsIae(() -> c.put(null, 1));
        boolean nullVal = throwsIae(() -> c.put("k", null));
        check("null key/value rejected", nullKey && nullVal, "key=" + nullKey + " val=" + nullVal);
    }

    /** The Observer must fire with the evicted key/value. */
    private static void testEvictionListenerFires() {
        List<String> evicted = new ArrayList<>();
        EvictionListener<String, Integer> listener = (k, v) -> evicted.add(k + "=" + v);
        Cache<String, Integer> c = Caches.lru(1, listener);
        c.put("a", 1);
        c.put("b", 2); // evicts a=1
        check("eviction listener fired with evicted entry",
                evicted.size() == 1 && evicted.get(0).equals("a=1"), evicted.toString());
    }

    /** Striped capacity is shards × ceil(cap/shards); size stays within it. */
    private static void testStripedEffectiveCapacity() {
        Cache<Integer, Integer> c = Caches.stripedLru(10, 4); // 4 shards × ceil(10/4)=3 = 12
        for (int i = 0; i < 500; i++) {
            c.put(i, i);
        }
        check("striped size within effective capacity",
                c.capacity() == 12 && c.size() <= 12, "cap=" + c.capacity() + " size=" + c.size());
    }

    /**
     * THE headline concurrency test: hammer a small striped cache from many threads and prove no
     * corruption — no exceptions, the capacity invariant holds live (sampled by a watcher), and the
     * cache still works correctly afterwards.
     */
    private static void testConcurrentStripedNoCorruption() throws InterruptedException {
        Cache<Integer, Integer> cache = Caches.stripedLru(64, 8);
        stressAndAssert("striped", cache);
    }

    /** Same storm against the single global-lock cache — also corruption-free (just less scalable). */
    private static void testConcurrentGlobalLockNoCorruption() throws InterruptedException {
        Cache<Integer, Integer> cache = Caches.synchronizedLru(64);
        stressAndAssert("global-lock", cache);
    }

    private static void stressAndAssert(String label, Cache<Integer, Integer> cache) throws InterruptedException {
        int threads = 32;
        int opsPerThread = 20_000;
        int keyspace = 500;

        ExecutorService pool = Executors.newFixedThreadPool(threads);
        CountDownLatch startGun = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(threads);
        AtomicInteger errors = new AtomicInteger();
        AtomicBoolean overCapacity = new AtomicBoolean(false);
        AtomicBoolean stop = new AtomicBoolean(false);

        // Watcher: continuously sample size and flag any breach of the capacity invariant.
        Thread watcher = new Thread(() -> {
            while (!stop.get()) {
                if (cache.size() > cache.capacity()) {
                    overCapacity.set(true);
                }
            }
        });
        watcher.setDaemon(true);
        watcher.start();

        for (int t = 0; t < threads; t++) {
            final long seed = t;
            pool.submit(() -> {
                Random rng = new Random(seed);
                try {
                    startGun.await();
                    for (int i = 0; i < opsPerThread; i++) {
                        int key = rng.nextInt(keyspace);
                        if (rng.nextBoolean()) {
                            cache.put(key, key);
                        } else {
                            Integer v = cache.get(key);
                            if (v != null && v != key) {
                                errors.incrementAndGet(); // a key must always map to its own value
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (RuntimeException e) {
                    errors.incrementAndGet(); // NPE / corrupted-link would land here
                } finally {
                    done.countDown();
                }
            });
        }

        startGun.countDown();
        done.await();
        pool.shutdown();
        pool.awaitTermination(30, TimeUnit.SECONDS);
        stop.set(true);

        check(label + ": no exceptions/bad reads under contention", errors.get() == 0, errors.get() + " errors");
        check(label + ": capacity invariant held throughout", !overCapacity.get(), "capacity exceeded");
        check(label + ": size within capacity after storm", cache.size() <= cache.capacity(),
                "size=" + cache.size() + " cap=" + cache.capacity());

        // Still functional after the storm — proves the structure isn't corrupted.
        cache.put(999_999, 42);
        check(label + ": cache still works after storm", cache.get(999_999) == 42,
                String.valueOf(cache.get(999_999)));
    }

    // ---- tiny utilities ----

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
