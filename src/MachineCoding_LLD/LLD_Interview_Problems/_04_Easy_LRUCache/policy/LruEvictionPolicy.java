package MachineCoding_LLD.LLD_Interview_Problems._04_Easy_LRUCache.policy;

/**
 * Least-Recently-Used: every access bumps the key to the front, so the back is always the key
 * untouched longest — the one we evict. Backed by {@link LinkedKeyList}, so all three operations
 * are O(1). This is the canonical HashMap + doubly-linked-list LRU, with the list factored out so
 * the policy is swappable.
 */
public final class LruEvictionPolicy<K> implements EvictionPolicy<K> {

    private final LinkedKeyList<K> order = new LinkedKeyList<>();

    @Override
    public void recordAccess(K key) {
        order.moveToFirst(key); // the read-is-a-write step that makes LRU work
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
        return "LRU";
    }
}
