package MachineCoding_LLD.LLD_Interview_Problems._04_Easy_LRUCache.observer;

/**
 * Observer hook fired when the cache drops an entry — the same idea as Guava's
 * {@code removalListener} / Caffeine's eviction callbacks. The cache is the subject; a listener
 * reacts (log it, flush to disk, update a metric) without the cache knowing who's watching.
 *
 * <p>Kept to a single method so it stays a lambda target. Use {@link #none()} for the default
 * no-op instead of null-checking on the hot path.
 *
 * <p>Pattern reference: {@code DesignPatterns/_11_ObserverDesignPattern}.
 */
@FunctionalInterface
public interface EvictionListener<K, V> {

    /** Called after {@code key} (with its last {@code value}) has been removed by eviction. */
    void onEvict(K key, V value);

    @SuppressWarnings("rawtypes")
    EvictionListener NONE = (k, v) -> { };

    /** A shared no-op listener — avoids a null check in the cache's eviction path. */
    @SuppressWarnings("unchecked")
    static <K, V> EvictionListener<K, V> none() {
        return (EvictionListener<K, V>) NONE;
    }
}
