package MachineCoding_LLD.LLD_Interview_Problems._04_Easy_LRUCache.concurrent;

import java.util.concurrent.locks.ReentrantLock;

import MachineCoding_LLD.LLD_Interview_Problems._04_Easy_LRUCache.Cache;

/**
 * The <b>baseline</b> thread-safe cache: wrap any {@link Cache} and guard every operation with a
 * single {@link ReentrantLock}. This is a <b>synchronization Proxy</b> — same interface, same
 * behaviour, plus mutual exclusion — so thread-safety is <em>composed onto</em> the plain cache
 * rather than tangled into it.
 *
 * <p>Correct but coarse: one lock serializes <em>all</em> access, so throughput doesn't scale with
 * cores. It's the honest starting point in an interview ("first make it correct"), and it's the
 * per-shard building block that {@link StripedCache} multiplies to claw back concurrency.
 *
 * <p>A read/write lock wouldn't help here: {@code get} mutates recency, so reads aren't read-only.
 *
 * <p>Pattern reference: {@code DesignPatterns/_09_Proxy}.
 */
public final class SynchronizedCache<K, V> implements Cache<K, V> {

    private final Cache<K, V> delegate;
    private final ReentrantLock lock = new ReentrantLock();

    public SynchronizedCache(Cache<K, V> delegate) {
        this.delegate = delegate;
    }

    @Override
    public V get(K key) {
        lock.lock();
        try {
            return delegate.get(key);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void put(K key, V value) {
        lock.lock();
        try {
            delegate.put(key, value);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int size() {
        lock.lock();
        try {
            return delegate.size();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int capacity() {
        return delegate.capacity(); // immutable — no lock needed
    }
}
