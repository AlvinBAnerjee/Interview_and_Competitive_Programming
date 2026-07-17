package MachineCoding_LLD.LLD_Interview_Problems._04_Easy_LRUCache.observer;

/**
 * A concrete {@link EvictionListener} that simply prints each eviction — used by the {@code Main}
 * walkthrough to make the eviction order visible. Real deployments would swap in a metrics or
 * write-back listener; the cache doesn't change.
 */
public final class LoggingEvictionListener<K, V> implements EvictionListener<K, V> {

    @Override
    public void onEvict(K key, V value) {
        System.out.println("    [evicted] " + key + " = " + value);
    }
}
