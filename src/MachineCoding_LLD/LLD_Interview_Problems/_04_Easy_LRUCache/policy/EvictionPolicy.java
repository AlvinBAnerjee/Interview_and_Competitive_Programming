package MachineCoding_LLD.LLD_Interview_Problems._04_Easy_LRUCache.policy;

/**
 * The Strategy that decides <b>which key to drop</b> when the cache is full. Pulling this out is
 * what turns a hard-coded LRU cache into a policy-agnostic one: LRU, FIFO (and LFU, MRU, …) differ
 * only in how they react to access/insertion and which key they surrender.
 *
 * <p>The policy tracks <em>ordering over keys only</em> — it never sees values. The cache owns the
 * {@code key → value} map; the policy owns the recency/insertion bookkeeping. Every method must be
 * <b>O(1)</b> so the cache stays O(1).
 *
 * <p>Not thread-safe: a policy instance belongs to exactly one single-threaded {@code PolicyCache}.
 * The concurrent caches get their own policy <em>per shard</em> (never shared) — see
 * {@code StripedCache}.
 *
 * <p>Pattern reference: {@code DesignPatterns/_10_StrategyDesignPattern}.
 */
public interface EvictionPolicy<K> {

    /** The key was read (or updated). LRU treats this as "made most-recent"; FIFO ignores it. */
    void recordAccess(K key);

    /** A brand-new key was inserted into the cache. */
    void recordInsertion(K key);

    /** Remove and return the key that should be evicted, or {@code null} if the policy is empty. */
    K evict();

    /** Short label for demos/tests (e.g. "LRU"). */
    String name();
}
