package MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.model;

import MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.InvalidRideTransitionException;

/**
 * One ride and its lifecycle. Acts as the State-machine context: all state
 * changes go through {@link #transitionTo}, which the {@link RideState} enum
 * validates. Transitions are {@code synchronized} because a rider-cancel and a
 * driver-start can race on the same ride — the lock makes "check legal then
 * move" atomic so only one wins.
 */
public class Ride {
    private final String id;
    private final Rider rider;
    private final Location pickup;
    private final Location drop;
    private final RideType type;

    private volatile Driver driver;                 // set at assignment
    private volatile RideState state = RideState.REQUESTED;
    private volatile long farePaise;                // estimate at assign, final at complete

    public Ride(String id, Rider rider, Location pickup, Location drop, RideType type) {
        this.id = id;
        this.rider = rider;
        this.pickup = pickup;
        this.drop = drop;
        this.type = type;
    }

    public synchronized void transitionTo(RideState next) {
        if (!state.canTransitionTo(next)) {
            throw new InvalidRideTransitionException(state, next);
        }
        this.state = next;
    }

    public String id() { return id; }
    public Rider rider() { return rider; }
    public Location pickup() { return pickup; }
    public Location drop() { return drop; }
    public RideType type() { return type; }
    public Driver driver() { return driver; }
    public void setDriver(Driver driver) { this.driver = driver; }
    public RideState state() { return state; }
    public long farePaise() { return farePaise; }
    public void setFarePaise(long farePaise) { this.farePaise = farePaise; }

    @Override
    public String toString() {
        String driverName = driver == null ? "—" : driver.name();
        return String.format("Ride[%s] %s driver=%s ₹%.2f (%s)",
                id, state, driverName, farePaise / 100.0, rider.name());
    }
}
