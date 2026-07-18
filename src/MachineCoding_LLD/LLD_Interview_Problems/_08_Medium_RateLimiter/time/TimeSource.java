package MachineCoding_LLD.LLD_Interview_Problems._08_Medium_RateLimiter.time;

/**
 * A monotonic clock in nanoseconds, injected everywhere instead of calling {@link System#nanoTime}
 * directly. That one seam is what makes a time-based rate limiter <b>testable</b>: production wires
 * in {@link #SYSTEM}, tests wire in a {@code ManualTimeSource} they can freeze or advance, so
 * refill and window-boundary behaviour can be asserted deterministically instead of with
 * {@code Thread.sleep}.
 */
@FunctionalInterface
public interface TimeSource {

    long nanoTime();

    /** The real monotonic system clock — the production default. */
    TimeSource SYSTEM = System::nanoTime;
}
