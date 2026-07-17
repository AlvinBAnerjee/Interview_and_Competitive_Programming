package MachineCoding_LLD.LLD_Interview_Problems._04_Easy_LRUCache;

/**
 * The one abstraction every cache in this package implements — the single-threaded core
 * ({@code PolicyCache}), the global-lock wrapper ({@code SynchronizedCache}) and the sharded
 * concurrent cache ({@code StripedCache}) are all just {@code Cache}s. That uniformity is what
 * lets thread-safety be <em>composed on</em> (wrap/shard a plain cache) instead of baked into the
 * data structure.
 *
 * <p>Contract: {@code null} keys and values are not supported (they collide with the "miss"
 * sentinel and with real caches like Guava/Caffeine). Both operations are amortized <b>O(1)</b>.
 *
 * @param <K> key type (must have sane {@code hashCode}/{@code equals})
 * @param <V> value type
 */
public interface Cache<K, V> {

    /** @return the value, or {@code null} on a miss. A hit marks the key most-recently-used. */
    V get(K key);

    /** Insert or update. If this pushes size over capacity, the eviction policy drops one entry. */
    void put(K key, V value);

    /** Current number of entries. */
    int size();

    /** Maximum entries this cache will hold before evicting. */
    int capacity();
}
