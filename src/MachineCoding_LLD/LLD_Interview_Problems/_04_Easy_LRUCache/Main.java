package MachineCoding_LLD.LLD_Interview_Problems._04_Easy_LRUCache;

import java.util.List;

import MachineCoding_LLD.LLD_Interview_Problems._04_Easy_LRUCache.observer.LoggingEvictionListener;

/**
 * A scripted, non-interactive walkthrough (like the other problems' {@code Main}s): LRU eviction
 * order, how LRU and FIFO diverge on the same access pattern, and a quick striped-cache smoke test.
 * For correctness + the concurrency stress test, see {@code LRUCacheTest}.
 */
public final class Main {

    public static void main(String[] args) {
        lruEvictionOrder();
        separator();
        lruVsFifo();
        separator();
        stripedSmokeTest();
    }

    /** Capacity 3. Access reorders recency, so a get() changes who gets evicted. */
    private static void lruEvictionOrder() {
        System.out.println("=== LRU eviction order (capacity 3) ===\n");
        Cache<String, Integer> cache = Caches.lru(3, new LoggingEvictionListener<>());

        cache.put("A", 1);
        cache.put("B", 2);
        cache.put("C", 3);              // cache: [C, B, A]  (front → back)
        System.out.println("put A,B,C  -> size " + cache.size());

        System.out.println("get A      -> " + cache.get("A") + "   (A is now most-recent: [A, C, B])");
        System.out.println("put D      -> evicts the LRU, which is B (not A):");
        cache.put("D", 4);              // evicts B
        System.out.println("get B      -> " + cache.get("B") + "   (evicted)");
        System.out.println("get A      -> " + cache.get("A") + "   (survived because we touched it)");
        System.out.println("size       -> " + cache.size());
    }

    /** Same operations, two policies: LRU keeps the recently-read key; FIFO doesn't care. */
    private static void lruVsFifo() {
        System.out.println("=== LRU vs FIFO on the same access pattern (capacity 2) ===\n");

        Cache<String, Integer> lru = Caches.lru(2);
        Cache<String, Integer> fifo = Caches.fifo(2);

        for (Cache<String, Integer> c : List.of(lru, fifo)) {
            c.put("a", 1);
            c.put("b", 2);
            c.get("a");     // touch "a"
            c.put("c", 3);  // overflow → one of a/b is evicted
        }

        System.out.println("After put a,b; get a; put c:");
        System.out.println("  LRU  keeps a (touched), evicts b -> get a=" + lru.get("a") + ", get b=" + lru.get("b"));
        System.out.println("  FIFO ignores the read, evicts a  -> get a=" + fifo.get("a") + ", get b=" + fifo.get("b"));
    }

    /** A striped cache is just a Cache — same API, thread-safe, scales via shards. */
    private static void stripedSmokeTest() {
        System.out.println("=== Striped LRU (capacity 8, 4 shards) ===\n");
        Cache<Integer, Integer> cache = Caches.stripedLru(8, 4);
        for (int i = 0; i < 20; i++) {
            cache.put(i, i * i);
        }
        System.out.println("put 0..19 into an 8-slot striped cache");
        System.out.println("effective capacity = " + cache.capacity() + " (shards x ceil(cap/shards))");
        System.out.println("size after churn    = " + cache.size() + " (<= effective capacity)");
        System.out.println("get 19              = " + cache.get(19) + " (recent keys survive)");
    }

    private static void separator() {
        System.out.println("\n======================================================\n");
    }
}
