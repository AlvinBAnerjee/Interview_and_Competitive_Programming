package MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem;

import MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.location.LocationService;
import MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.matching.Candidate;
import MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.matching.DriverMatchingStrategy;
import MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.model.Driver;
import MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.model.Location;
import MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.model.Ride;
import MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.model.RideState;
import MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.model.RideType;
import MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.model.Rider;
import MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.model.DriverStatus;
import MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.observer.RideObserver;
import MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.pricing.PricingStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Facade over the whole ride-hailing flow. It wires together the spatial index
 * ({@link LocationService}), the matching and pricing {@link DriverMatchingStrategy}
 * / {@link PricingStrategy} strategies, the ride {@link RideState} machine, and
 * {@link RideObserver}s — but the no-double-booking guarantee is delegated to a
 * single atomic op on the driver ({@link Driver#tryClaim()}).
 *
 * The matching hot path is deliberately shaped as: <b>search lock-free → rank →
 * attempt an atomic claim, retrying the next candidate on failure</b>. There is
 * NO service-wide lock around matching, so thousands of riders match in parallel
 * and only ever contend on the individual driver they're both trying to claim.
 */
public class CabBookingService {

    private final LocationService locationService;
    private final DriverMatchingStrategy matchingStrategy;
    private final PricingStrategy pricingStrategy;
    private final double searchRadius;

    private final ConcurrentHashMap<String, Rider> riders = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Driver> drivers = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Ride> rides = new ConcurrentHashMap<>();
    private final List<RideObserver> observers = new CopyOnWriteArrayList<>();
    private final AtomicLong rideSeq = new AtomicLong(1000);

    public CabBookingService(LocationService locationService, DriverMatchingStrategy matchingStrategy,
                             PricingStrategy pricingStrategy, double searchRadius) {
        this.locationService = locationService;
        this.matchingStrategy = matchingStrategy;
        this.pricingStrategy = pricingStrategy;
        this.searchRadius = searchRadius;
    }

    // ---- registration ----

    public void registerRider(Rider rider) { riders.put(rider.id(), rider); }

    public void registerDriver(Driver driver) {
        drivers.put(driver.id(), driver);
        driver.goOnline();                                   // AVAILABLE
        locationService.updateLocation(driver.id(), driver.location());
    }

    public void addObserver(RideObserver observer) { observers.add(observer); }

    /** A driver's live location ping; updates both the index and the driver mirror. */
    public void updateDriverLocation(String driverId, Location location) {
        Driver driver = drivers.get(driverId);
        if (driver == null) return;
        driver.setLocation(location);
        locationService.updateLocation(driverId, location);
    }

    // ---- read side ----

    public long estimateFarePaise(Location pickup, Location drop, RideType type) {
        return pricingStrategy.estimatePaise(pickup, drop, type);
    }

    // ---- the match ----

    /**
     * Request a ride: find nearby drivers, rank them, and claim the best one
     * that's still free. Throws {@link NoDriverAvailableException} if every
     * candidate is taken or none are in range.
     */
    public Ride requestRide(String riderId, Location pickup, Location drop, RideType type) {
        Rider rider = riders.get(riderId);
        if (rider == null) throw new IllegalArgumentException("Unknown rider: " + riderId);

        // 1. Lock-free search + snapshot of currently-available candidates.
        List<Candidate> candidates = new ArrayList<>();
        for (String driverId : locationService.nearbyDriverIds(pickup, searchRadius)) {
            Driver driver = drivers.get(driverId);
            if (driver != null && driver.status() == DriverStatus.AVAILABLE) {
                Location loc = locationService.locationOf(driverId);
                if (loc != null) candidates.add(new Candidate(driver, loc.distanceTo(pickup)));
            }
        }

        // 2. Rank (pure policy), then 3. claim the first that CAS-succeeds.
        for (Candidate candidate : matchingStrategy.rank(candidates)) {
            Driver driver = candidate.driver();
            if (driver.tryClaim()) {                          // atomic AVAILABLE -> ASSIGNED
                return assign(rider, driver, pickup, drop, type);
            }
            // lost the race for this driver -> fall through to the next candidate
        }

        observers.forEach(o -> o.onNoDriverFound(riderId));
        throw new NoDriverAvailableException(riderId);
    }

    private Ride assign(Rider rider, Driver driver, Location pickup, Location drop, RideType type) {
        Ride ride = new Ride("RIDE-" + rideSeq.incrementAndGet(), rider, pickup, drop, type);
        ride.setDriver(driver);
        ride.setFarePaise(pricingStrategy.estimatePaise(pickup, drop, type));
        ride.transitionTo(RideState.DRIVER_ASSIGNED);
        rides.put(ride.id(), ride);
        observers.forEach(o -> o.onDriverAssigned(ride));
        return ride;
    }

    // ---- lifecycle ----

    public void startRide(String rideId) {
        Ride ride = requireRide(rideId);
        ride.transitionTo(RideState.STARTED);
        observers.forEach(o -> o.onRideStarted(ride));
    }

    public void completeRide(String rideId) {
        Ride ride = requireRide(rideId);
        ride.transitionTo(RideState.COMPLETED);
        // recompute final fare (same route here; could differ with actual distance/time)
        ride.setFarePaise(pricingStrategy.estimatePaise(ride.pickup(), ride.drop(), ride.type()));
        releaseDriver(ride, ride.drop());
        observers.forEach(o -> o.onRideCompleted(ride));
    }

    /** Cancel from any non-terminal state; rolls the driver back to AVAILABLE. */
    public void cancelRide(String rideId) {
        Ride ride = requireRide(rideId);
        ride.transitionTo(RideState.CANCELLED);
        releaseDriver(ride, ride.driver() == null ? null : ride.driver().location());
        observers.forEach(o -> o.onRideCancelled(ride));
    }

    private void releaseDriver(Ride ride, Location newLocation) {
        Driver driver = ride.driver();
        if (driver == null) return;
        if (newLocation != null) {
            driver.setLocation(newLocation);
            locationService.updateLocation(driver.id(), newLocation);
        }
        driver.release();                                    // ASSIGNED -> AVAILABLE
    }

    public Ride ride(String rideId) { return rides.get(rideId); }

    private Ride requireRide(String rideId) {
        Ride ride = rides.get(rideId);
        if (ride == null) throw new IllegalArgumentException("Unknown ride: " + rideId);
        return ride;
    }
}
