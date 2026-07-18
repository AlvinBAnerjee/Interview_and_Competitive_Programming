package MachineCoding_LLD.LLD_Interview_Problems._08_Medium_RateLimiter;

import MachineCoding_LLD.LLD_Interview_Problems._08_Medium_RateLimiter.strategy.FixedWindowStrategy;
import MachineCoding_LLD.LLD_Interview_Problems._08_Medium_RateLimiter.strategy.SlidingWindowStrategy;
import MachineCoding_LLD.LLD_Interview_Problems._08_Medium_RateLimiter.strategy.TokenBucketStrategy;
import MachineCoding_LLD.LLD_Interview_Problems._08_Medium_RateLimiter.time.TimeSource;

/**
 * Builds a {@link RateLimiter} for a chosen algorithm + config, so callers say <em>what limiting
 * they want</em> ({@code tokenBucket(100, 10)}) without wiring a per-client strategy supplier by
 * hand. Each method captures the config in a {@code Supplier} that mints a fresh strategy instance
 * per client. Adding a new algorithm is one method here plus one strategy class.
 *
 * <p>Pattern reference: {@code DesignPatterns/_01_FactoryDesignPattern}.
 */
public final class RateLimiterFactory {

    /** Default: forget a client after 10 minutes idle (bounds memory). */
    private static final long DEFAULT_IDLE_EVICTION_NANOS = 10L * 60 * 1_000_000_000L;

    private RateLimiterFactory() {
    }

    // ---- Token Bucket (bursty, smooth average) ----

    public static RateLimiter tokenBucket(long capacity, double refillPerSecond) {
        return tokenBucket(capacity, refillPerSecond, TimeSource.SYSTEM);
    }

    public static RateLimiter tokenBucket(long capacity, double refillPerSecond, TimeSource time) {
        // Fail fast: validate config now, not lazily on the first per-client request.
        if (capacity < 1) {
            throw new IllegalArgumentException("capacity must be >= 1, got " + capacity);
        }
        if (refillPerSecond <= 0) {
            throw new IllegalArgumentException("refillPerSecond must be > 0, got " + refillPerSecond);
        }
        return new RateLimiter(() -> new TokenBucketStrategy(capacity, refillPerSecond, time),
                time, DEFAULT_IDLE_EVICTION_NANOS);
    }

    // ---- Fixed Window (simple, exact within a window) ----

    public static RateLimiter fixedWindow(long limit, long windowMillis) {
        return fixedWindow(limit, windowMillis, TimeSource.SYSTEM);
    }

    public static RateLimiter fixedWindow(long limit, long windowMillis, TimeSource time) {
        validateWindow(limit, windowMillis);
        long windowNanos = windowMillis * 1_000_000L;
        return new RateLimiter(() -> new FixedWindowStrategy(limit, windowNanos, time),
                time, DEFAULT_IDLE_EVICTION_NANOS);
    }

    // ---- Sliding Window (smooths the boundary burst) ----

    public static RateLimiter slidingWindow(long limit, long windowMillis) {
        return slidingWindow(limit, windowMillis, TimeSource.SYSTEM);
    }

    public static RateLimiter slidingWindow(long limit, long windowMillis, TimeSource time) {
        validateWindow(limit, windowMillis);
        long windowNanos = windowMillis * 1_000_000L;
        return new RateLimiter(() -> new SlidingWindowStrategy(limit, windowNanos, time),
                time, DEFAULT_IDLE_EVICTION_NANOS);
    }

    private static void validateWindow(long limit, long windowMillis) {
        if (limit < 1) {
            throw new IllegalArgumentException("limit must be >= 1, got " + limit);
        }
        if (windowMillis < 1) {
            throw new IllegalArgumentException("windowMillis must be >= 1, got " + windowMillis);
        }
    }
}
