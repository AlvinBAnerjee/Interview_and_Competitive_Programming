package MachineCoding_LLD.LLD_Interview_Problems._08_Medium_RateLimiter.strategy;

import java.util.concurrent.atomic.AtomicReference;

import MachineCoding_LLD.LLD_Interview_Problems._08_Medium_RateLimiter.time.TimeSource;

/**
 * Sliding-window counter: smooths the fixed-window boundary burst without storing a timestamp per
 * request (that's the sliding-window <em>log</em>, which is O(limit) memory). It keeps just two
 * counters — the current window and the previous one — and estimates the rolling count by weighting
 * the previous window by how much of it still overlaps the trailing window:
 *
 * <pre>
 *   estimated = currentCount + previousCount × (1 − elapsedIntoCurrentWindow / windowSize)
 *   allow iff estimated &lt; limit
 * </pre>
 *
 * At a window's start the previous window counts fully (weight 1) and fades to 0 by its end, so a
 * burst that fixed-window would wave through at the boundary is throttled. O(1) time and memory.
 *
 * <h3>Atomicity</h3>
 * The three fields {@code (windowId, currentCount, previousCount)} must advance together, so they
 * live in one immutable snapshot behind an {@link AtomicReference}, CAS-updated like the others.
 */
public final class SlidingWindowStrategy implements RateLimitStrategy {

    private final long limit;
    private final long windowNanos;
    private final TimeSource time;
    private final AtomicReference<State> state;

    private record State(long windowId, long currentCount, long previousCount) { }

    public SlidingWindowStrategy(long limit, long windowNanos, TimeSource time) {
        if (limit < 1) {
            throw new IllegalArgumentException("limit must be >= 1, got " + limit);
        }
        if (windowNanos < 1) {
            throw new IllegalArgumentException("window must be >= 1ns, got " + windowNanos);
        }
        this.limit = limit;
        this.windowNanos = windowNanos;
        this.time = time;
        this.state = new AtomicReference<>(new State(time.nanoTime() / windowNanos, 0, 0));
    }

    @Override
    public boolean tryAcquire() {
        while (true) {
            long now = time.nanoTime();
            long id = now / windowNanos;
            State current = state.get();

            long windowId = current.windowId;
            long curCount = current.currentCount;
            long prevCount = current.previousCount;

            if (id == windowId + 1) {
                prevCount = curCount; // slid forward exactly one window
                curCount = 0;
                windowId = id;
            } else if (id != windowId) {
                prevCount = 0;        // jumped ahead (idle) — both windows are stale
                curCount = 0;
                windowId = id;
            }

            long elapsedIntoWindow = now - id * windowNanos;          // [0, windowNanos)
            double prevWeight = (double) (windowNanos - elapsedIntoWindow) / windowNanos;
            double estimated = curCount + prevCount * prevWeight;

            if (estimated >= limit) {
                return false;
            }
            State next = new State(windowId, curCount + 1, prevCount);
            if (state.compareAndSet(current, next)) {
                return true;
            }
            // CAS lost — retry
        }
    }

    @Override
    public String name() {
        return "SlidingWindow";
    }
}
