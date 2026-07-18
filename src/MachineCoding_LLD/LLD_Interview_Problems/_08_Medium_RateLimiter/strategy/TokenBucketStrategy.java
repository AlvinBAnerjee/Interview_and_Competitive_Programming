package MachineCoding_LLD.LLD_Interview_Problems._08_Medium_RateLimiter.strategy;

import java.util.concurrent.atomic.AtomicReference;

import MachineCoding_LLD.LLD_Interview_Problems._08_Medium_RateLimiter.time.TimeSource;

/**
 * Token bucket: the bucket holds up to {@code capacity} tokens and refills continuously at
 * {@code refillPerSecond}. Each request consumes one token; empty bucket → rejected. This is the
 * algorithm that allows <b>bursts</b> up to capacity while bounding the long-run average — usually
 * the best general-purpose choice.
 *
 * <h3>Lock-free refill + consume</h3>
 * Refill and consume must happen together atomically or requests slip past the limit. Instead of a
 * lock, the whole state — {@code (tokens, lastRefillNanos)} — lives in one immutable snapshot inside
 * an {@link AtomicReference}, updated with a <b>CAS loop</b>:
 * <ol>
 *   <li>read the snapshot,</li>
 *   <li>lazily refill = {@code min(capacity, tokens + elapsed × rate)} (no background thread — time
 *       does the refilling),</li>
 *   <li>if &lt; 1 token, reject (no write — the next call recomputes from the same timestamp, so no
 *       refill progress is lost);</li>
 *   <li>else CAS in {@code (refilled − 1, now)}; on failure, retry.</li>
 * </ol>
 */
public final class TokenBucketStrategy implements RateLimitStrategy {

    private final double capacity;
    private final double refillPerNano;
    private final TimeSource time;
    private final AtomicReference<State> state;

    /** Immutable snapshot — the unit of the CAS. */
    private record State(double tokens, long lastRefillNanos) { }

    public TokenBucketStrategy(long capacity, double refillPerSecond, TimeSource time) {
        if (capacity < 1) {
            throw new IllegalArgumentException("capacity must be >= 1, got " + capacity);
        }
        if (refillPerSecond <= 0) {
            throw new IllegalArgumentException("refillPerSecond must be > 0, got " + refillPerSecond);
        }
        this.capacity = capacity;
        this.refillPerNano = refillPerSecond / 1_000_000_000.0;
        this.time = time;
        this.state = new AtomicReference<>(new State(capacity, time.nanoTime())); // start full
    }

    @Override
    public boolean tryAcquire() {
        while (true) {
            State current = state.get();
            long now = time.nanoTime();
            long elapsed = Math.max(0, now - current.lastRefillNanos);
            double tokens = Math.min(capacity, current.tokens + elapsed * refillPerNano);

            if (tokens < 1.0) {
                return false; // empty — recomputed from lastRefillNanos next time, nothing lost
            }
            State next = new State(tokens - 1.0, now);
            if (state.compareAndSet(current, next)) {
                return true;
            }
            // another thread won the CAS — loop and retry with the fresh snapshot
        }
    }

    @Override
    public String name() {
        return "TokenBucket";
    }
}
