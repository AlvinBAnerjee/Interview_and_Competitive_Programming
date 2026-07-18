package MachineCoding_LLD.LLD_Interview_Problems._08_Medium_RateLimiter.strategy;

/**
 * The pluggable algorithm — Token Bucket, Fixed Window, Sliding Window — behind one method. Each
 * instance owns the state for <b>one client</b> (the {@code RateLimiter} keeps a map of them), and
 * {@link #tryAcquire} must be <b>atomic and thread-safe</b> because it's the hot path: many threads
 * may call it for the same client at once, and the limit must hold exactly.
 *
 * <p>Every implementation here is <b>lock-free</b> — a CAS loop over an immutable state snapshot in
 * an {@code AtomicReference} — so there's no per-bucket lock on the request path.
 *
 * <p>Pattern reference: {@code DesignPatterns/_10_StrategyDesignPattern}.
 */
public interface RateLimitStrategy {

    /** Attempt to consume one permit. @return true = allowed, false = rate-limited. */
    boolean tryAcquire();

    /** Short label (e.g. "TokenBucket") for demos/tests. */
    String name();
}
