package MachineCoding_LLD.LLD_Interview_Problems._04_Easy_LRUCache.concurrent;

import java.util.function.Supplier;

import MachineCoding_LLD.LLD_Interview_Problems._04_Easy_LRUCache.Cache;
import MachineCoding_LLD.LLD_Interview_Problems._04_Easy_LRUCache.PolicyCache;
import MachineCoding_LLD.LLD_Interview_Problems._04_Easy_LRUCache.observer.EvictionListener;
import MachineCoding_LLD.LLD_Interview_Problems._04_Easy_LRUCache.policy.EvictionPolicy;

/**
 * The scalable answer: <b>lock striping</b>. Partition the keyspace into N independent shards, each
 * its own {@link SynchronizedCache} over its own {@link PolicyCache} with its own lock. A key
 * always maps to the same shard, so operations on different shards proceed in parallel and
 * contention drops ~N×. This is how Guava/Caffeine and {@code ConcurrentHashMap} get their
 * throughput.
 *
 * <h3>Two honest trade-offs to state out loud</h3>
 * <ul>
 *   <li><b>Approximate LRU.</b> Eviction is per-shard, not global — the evicted key is the LRU
 *       <em>of its shard</em>, not necessarily of the whole cache. With a decent hash spread this
 *       is very close to true LRU, and it's the standard price real caches pay for scaling.</li>
 *   <li><b>Approximate capacity.</b> Total capacity is split evenly, rounding up per shard, so the
 *       effective capacity is {@code shards × ceil(capacity/shards)} (reported by
 *       {@link #capacity()}). One hot shard can evict while another has room.</li>
 * </ul>
 *
 * <p>Each shard gets its <b>own</b> policy instance (via the {@link Supplier}) — sharing one policy
 * across shards would corrupt the recency list, since policies aren't thread-safe.
 */
public final class StripedCache<K, V> implements Cache<K, V> {

    private final Cache<K, V>[] shards;
    private final int mask;             // shards.length is a power of two, so index = hash & mask
    private final int effectiveCapacity;

    @SuppressWarnings("unchecked")
    public StripedCache(int capacity,
                        int requestedShards,
                        Supplier<EvictionPolicy<K>> policyFactory,
                        EvictionListener<K, V> listener) {
        if (capacity < 1) {
            throw new IllegalArgumentException("capacity must be >= 1, got " + capacity);
        }
        if (requestedShards < 1) {
            throw new IllegalArgumentException("shards must be >= 1, got " + requestedShards);
        }
        int n = ceilingPowerOfTwo(requestedShards);
        int perShard = Math.max(1, (capacity + n - 1) / n); // ceil(capacity / n), at least 1
        this.shards = new Cache[n];
        this.mask = n - 1;
        this.effectiveCapacity = n * perShard;
        for (int i = 0; i < n; i++) {
            // Each shard: an independent single-threaded core behind its own lock.
            Cache<K, V> core = new PolicyCache<>(perShard, policyFactory.get(), listener);
            shards[i] = new SynchronizedCache<>(core);
        }
    }

    @Override
    public V get(K key) {
        return shardFor(key).get(key);
    }

    @Override
    public void put(K key, V value) {
        shardFor(key).put(key, value);
    }

    /** Sum of shard sizes. Under concurrency this is a best-effort snapshot, not an instant. */
    @Override
    public int size() {
        int total = 0;
        for (Cache<K, V> shard : shards) {
            total += shard.size();
        }
        return total;
    }

    @Override
    public int capacity() {
        return effectiveCapacity;
    }

    public int shardCount() {
        return shards.length;
    }

    private Cache<K, V> shardFor(K key) {
        return shards[spread(key.hashCode()) & mask];
    }

    /** Mix the bits so poor low-bit distributions don't pile keys onto one shard (à la HashMap). */
    private static int spread(int h) {
        return (h ^ (h >>> 16)) & 0x7fffffff;
    }

    private static int ceilingPowerOfTwo(int x) {
        int p = 1;
        while (p < x) {
            p <<= 1;
        }
        return p;
    }
}
