package MachineCoding_LLD.LLD_Interview_Problems._08_Medium_RateLimiter.strategy;

import java.util.concurrent.atomic.AtomicReference;

import MachineCoding_LLD.LLD_Interview_Problems._08_Medium_RateLimiter.time.TimeSource;

/**
 * Fixed window: chop time into fixed slots of {@code windowNanos} and allow at most {@code limit}
 * requests per slot. Dead simple and exact <em>within</em> a window — its known weakness is the
 * boundary, where a burst at the end of one window plus a burst at the start of the next can pass
 * up to {@code 2 × limit} in a short span. (That's what {@link SlidingWindowStrategy} fixes.)
 *
 * <h3>Atomicity</h3>
 * State is {@code (windowId, count)} in an {@link AtomicReference}, CAS-updated. On each call:
 * a new window CASes to {@code (id, 1)}; within the window, {@code count < limit} CASes the
 * increment; otherwise reject. CAS makes the check-and-increment atomic, so concurrent callers can
 * never push {@code count} past {@code limit}.
 */
public final class FixedWindowStrategy implements RateLimitStrategy {

    private final long limit;
    private final long windowNanos;
    private final TimeSource time;
    private final AtomicReference<Window> window;

    private record Window(long id, long count) { }

    public FixedWindowStrategy(long limit, long windowNanos, TimeSource time) {
        if (limit < 1) {
            throw new IllegalArgumentException("limit must be >= 1, got " + limit);
        }
        if (windowNanos < 1) {
            throw new IllegalArgumentException("window must be >= 1ns, got " + windowNanos);
        }
        this.limit = limit;
        this.windowNanos = windowNanos;
        this.time = time;
        this.window = new AtomicReference<>(new Window(currentWindowId(), 0));
    }

    @Override
    public boolean tryAcquire() {
        while (true) {
            long id = currentWindowId();
            Window current = window.get();

            if (current.id != id) {
                // First request of a new window — reset the counter to this one request.
                if (window.compareAndSet(current, new Window(id, 1))) {
                    return true;
                }
            } else if (current.count < limit) {
                if (window.compareAndSet(current, new Window(id, current.count + 1))) {
                    return true;
                }
            } else {
                return false; // limit already reached in this window
            }
            // CAS lost the race — retry with the fresh window
        }
    }

    private long currentWindowId() {
        return time.nanoTime() / windowNanos;
    }

    @Override
    public String name() {
        return "FixedWindow";
    }
}
