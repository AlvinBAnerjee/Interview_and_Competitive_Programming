package MachineCoding_LLD.LLD_Interview_Problems._04_Easy_LRUCache;

import MachineCoding_LLD.LLD_Interview_Problems._04_Easy_LRUCache.concurrent.StripedCache;
import MachineCoding_LLD.LLD_Interview_Problems._04_Easy_LRUCache.concurrent.SynchronizedCache;
import MachineCoding_LLD.LLD_Interview_Problems._04_Easy_LRUCache.observer.EvictionListener;
import MachineCoding_LLD.LLD_Interview_Problems._04_Easy_LRUCache.policy.FifoEvictionPolicy;
import MachineCoding_LLD.LLD_Interview_Problems._04_Easy_LRUCache.policy.LruEvictionPolicy;

/**
 * Static factory that hides the wiring: callers ask for the <em>kind</em> of cache they want
 * ({@code lru}, {@code stripedLru}) instead of assembling a policy, a core, and a lock strategy by
 * hand. Adding a new flavour is a method here — nothing else changes.
 */
public final class Caches {

    private Caches() {
    }

    /** Single-threaded LRU. */
    public static <K, V> Cache<K, V> lru(int capacity) {
        return new PolicyCache<>(capacity, new LruEvictionPolicy<>());
    }

    /** Single-threaded LRU that reports evictions to {@code listener}. */
    public static <K, V> Cache<K, V> lru(int capacity, EvictionListener<K, V> listener) {
        return new PolicyCache<>(capacity, new LruEvictionPolicy<>(), listener);
    }

    /** Single-threaded FIFO (same engine, different policy). */
    public static <K, V> Cache<K, V> fifo(int capacity) {
        return new PolicyCache<>(capacity, new FifoEvictionPolicy<>());
    }

    /** Thread-safe LRU via one global lock — correct, simple, doesn't scale. */
    public static <K, V> Cache<K, V> synchronizedLru(int capacity) {
        return new SynchronizedCache<>(new PolicyCache<>(capacity, new LruEvictionPolicy<>()));
    }

    /** Thread-safe LRU via lock striping — scales with shard count (approximate global LRU). */
    public static <K, V> Cache<K, V> stripedLru(int capacity, int shards) {
        return new StripedCache<>(capacity, shards, LruEvictionPolicy::new, EvictionListener.none());
    }

    /** Striped LRU that reports evictions. */
    public static <K, V> Cache<K, V> stripedLru(int capacity, int shards, EvictionListener<K, V> listener) {
        return new StripedCache<>(capacity, shards, LruEvictionPolicy::new, listener);
    }
}
