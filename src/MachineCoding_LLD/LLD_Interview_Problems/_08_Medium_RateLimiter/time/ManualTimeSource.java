package MachineCoding_LLD.LLD_Interview_Problems._08_Medium_RateLimiter.time;

import java.util.concurrent.atomic.AtomicLong;

/**
 * A hand-driven {@link TimeSource} for tests and demos: it only moves when you tell it to. Freezing
 * time turns the concurrency test into an exact assertion — with no refill/window-advance possible
 * mid-race, a limiter of L must allow <em>exactly</em> L, so any extra means a lost-update race.
 *
 * <p>Backed by an {@link AtomicLong} so many threads can read it during a stress test.
 */
public final class ManualTimeSource implements TimeSource {

    private final AtomicLong nanos;

    public ManualTimeSource() {
        this(0L);
    }

    public ManualTimeSource(long startNanos) {
        this.nanos = new AtomicLong(startNanos);
    }

    @Override
    public long nanoTime() {
        return nanos.get();
    }

    public void advanceNanos(long deltaNanos) {
        nanos.addAndGet(deltaNanos);
    }

    public void advanceMillis(long deltaMillis) {
        nanos.addAndGet(deltaMillis * 1_000_000L);
    }

    public void advanceSeconds(double seconds) {
        nanos.addAndGet((long) (seconds * 1_000_000_000L));
    }
}
