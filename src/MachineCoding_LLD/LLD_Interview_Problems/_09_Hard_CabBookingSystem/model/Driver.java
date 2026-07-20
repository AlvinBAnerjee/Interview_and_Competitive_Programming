package MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.model;

import java.util.concurrent.atomic.AtomicReference;

/**
 * A driver. The whole no-double-booking guarantee lives in one field: an
 * {@link AtomicReference}&lt;{@link DriverStatus}&gt;. Two matching threads may
 * both pick this driver as their best candidate, but {@link #tryClaim()} is a
 * single compare-and-set — exactly one wins the AVAILABLE→ASSIGNED transition,
 * the other sees {@code false} and moves on to its next candidate.
 *
 * {@code location} is volatile: it's written by the driver's own location-update
 * stream and read by matching threads, and a stale-by-one-tick read is fine
 * (the spatial index is the authoritative source for search).
 */
public class Driver {
    private final String id;
    private final String name;
    private final double rating;                 // 0..5, used by rating-based matching
    private final AtomicReference<DriverStatus> status = new AtomicReference<>(DriverStatus.OFFLINE);
    private volatile Location location;

    public Driver(String id, String name, double rating, Location location) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.location = location;
    }

    /** Atomic claim: AVAILABLE→ASSIGNED. Returns true iff THIS caller won it. */
    public boolean tryClaim() {
        return status.compareAndSet(DriverStatus.AVAILABLE, DriverStatus.ASSIGNED);
    }

    /** Return an assigned driver to the pool (ride completed or cancelled). */
    public void release() {
        status.compareAndSet(DriverStatus.ASSIGNED, DriverStatus.AVAILABLE);
    }

    public void goOnline() { status.set(DriverStatus.AVAILABLE); }
    public void goOffline() { status.set(DriverStatus.OFFLINE); }

    public String id() { return id; }
    public String name() { return name; }
    public double rating() { return rating; }
    public DriverStatus status() { return status.get(); }
    public Location location() { return location; }
    public void setLocation(Location location) { this.location = location; }
}
