package MachineCoding_LLD.LLD_Interview_Problems._04_Easy_LRUCache.policy;

/**
 * First-In-First-Out: evict in <em>insertion</em> order, ignoring reads entirely. Same
 * {@link LinkedKeyList} as LRU — the only difference is that {@link #recordAccess} does nothing, so
 * a key never moves once inserted and the back stays the oldest insertion. Proof that the Strategy
 * seam is real: a whole different eviction discipline is a one-line behavioural change.
 */
public final class FifoEvictionPolicy<K> implements EvictionPolicy<K> {

    private final LinkedKeyList<K> order = new LinkedKeyList<>();

    @Override
    public void recordAccess(K key) {
        // FIFO deliberately ignores access — position depends only on when the key was inserted.
    }

    @Override
    public void recordInsertion(K key) {
        order.addFirst(key);
    }

    @Override
    public K evict() {
        return order.removeLast();
    }

    @Override
    public String name() {
        return "FIFO";
    }
}
