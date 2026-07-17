package MachineCoding_LLD.LLD_Interview_Problems._04_Easy_LRUCache;

import java.util.HashMap;
import java.util.Map;

import MachineCoding_LLD.LLD_Interview_Problems._04_Easy_LRUCache.observer.EvictionListener;
import MachineCoding_LLD.LLD_Interview_Problems._04_Easy_LRUCache.policy.EvictionPolicy;

/**
 * The single-threaded engine: a {@code key → value} {@link HashMap} for O(1) storage plus an
 * {@link EvictionPolicy} that owns the eviction ordering. Together they give amortized O(1)
 * {@code get}/{@code put} with pluggable eviction (LRU, FIFO, …).
 *
 * <h3>Why a read is a write</h3>
 * {@link #get} calls {@code policy.recordAccess} on a hit — under LRU that moves the key to the
 * front of the recency list. So even {@code get} mutates state, which is exactly why this class is
 * <b>not</b> thread-safe and why concurrency can't be solved with a plain read/write lock. Wrap it
 * in {@code SynchronizedCache} (one lock) or shard it with {@code StripedCache} (N locks) to make
 * it concurrent — see {@code SOLUTION.md} §5.
 */
public final class PolicyCache<K, V> implements Cache<K, V> {

    private final int capacity;
    private final Map<K, V> store = new HashMap<>();
    private final EvictionPolicy<K> policy;
    private final EvictionListener<K, V> listener;

    public PolicyCache(int capacity, EvictionPolicy<K> policy) {
        this(capacity, policy, EvictionListener.none());
    }

    public PolicyCache(int capacity, EvictionPolicy<K> policy, EvictionListener<K, V> listener) {
        if (capacity < 1) {
            throw new IllegalArgumentException("capacity must be >= 1, got " + capacity);
        }
        this.capacity = capacity;
        this.policy = policy;
        this.listener = listener;
    }

    @Override
    public V get(K key) {
        if (!store.containsKey(key)) {
            return null; // miss — do not touch recency
        }
        policy.recordAccess(key); // hit — a read updates recency (the LRU subtlety)
        return store.get(key);
    }

    @Override
    public void put(K key, V value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("null keys/values are not supported");
        }
        if (store.containsKey(key)) {
            store.put(key, value);      // update in place
            policy.recordAccess(key);   // an update counts as a use
            return;
        }
        if (store.size() >= capacity) {
            K victim = policy.evict();
            if (victim != null) {
                V evicted = store.remove(victim);
                listener.onEvict(victim, evicted);
            }
        }
        store.put(key, value);
        policy.recordInsertion(key);
    }

    @Override
    public int size() {
        return store.size();
    }

    @Override
    public int capacity() {
        return capacity;
    }

    /** Which policy backs this cache — handy for demos/tests. */
    public String policyName() {
        return policy.name();
    }
}
