package MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem;

import MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.location.GridLocationService;
import MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.location.LocationService;
import MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.matching.NearestDriverStrategy;
import MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.model.Driver;
import MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.model.Location;
import MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.model.Ride;
import MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.model.RideType;
import MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.model.Rider;
import MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.observer.LoggingRideObserver;
import MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.pricing.DistanceBasedPricingStrategy;

/**
 * Walks the happy path plus the two things this problem is about: nearest-driver
 * matching and the atomic claim when two riders want the same driver.
 */
public class Main {

    public static void main(String[] args) {
        LocationService location = new GridLocationService(1.0);        // 1 km cells
        CabBookingService uber = new CabBookingService(
                location,
                new NearestDriverStrategy(),
                new DistanceBasedPricingStrategy(3000, 1200, 1.0),       // ₹30 base + ₹12/km, no surge
                5.0);                                                    // 5 km search radius
        uber.addObserver(new LoggingRideObserver());

        // drivers around the map
        uber.registerDriver(new Driver("d1", "Ravi",  4.9, new Location(1, 1)));
        uber.registerDriver(new Driver("d2", "Meena", 4.6, new Location(2, 0)));
        uber.registerDriver(new Driver("d3", "Sunil", 4.8, new Location(8, 8)));  // far away
        uber.registerRider(new Rider("r1", "Asha"));
        uber.registerRider(new Rider("r2", "Bilal"));

        Location pickup = new Location(0, 0), drop = new Location(4, 3);

        System.out.println("=== 1. Fare estimate ===");
        System.out.printf("  REGULAR: ₹%.2f   PREMIUM: ₹%.2f%n",
                uber.estimateFarePaise(pickup, drop, RideType.REGULAR) / 100.0,
                uber.estimateFarePaise(pickup, drop, RideType.PREMIUM) / 100.0);

        System.out.println("\n=== 2. Asha requests a ride (nearest of d1/d2 wins) ===");
        Ride r1 = uber.requestRide("r1", pickup, drop, RideType.REGULAR);
        System.out.println("  -> " + r1);

        System.out.println("\n=== 3. Bilal requests from the same spot (d1 taken -> gets d2) ===");
        Ride r2 = uber.requestRide("r2", pickup, drop, RideType.REGULAR);
        System.out.println("  -> " + r2);

        System.out.println("\n=== 4. Ride lifecycle for Asha: start -> complete ===");
        uber.startRide(r1.id());
        uber.completeRide(r1.id());
        System.out.println("  -> " + uber.ride(r1.id()) + "  driver now " + r1.driver().status());

        System.out.println("\n=== 5. A third rider, no driver left nearby ===");
        uber.registerRider(new Rider("r3", "Chitra"));
        // d1 just freed up (Asha's ride done); cancel Bilal to free d2, then exhaust drivers
        uber.cancelRide(r2.id());
        System.out.println("  (Bilal cancelled; d2 released -> " + r2.driver().status() + ")");

        // now occupy both near drivers, then a request should fail
        uber.requestRide("r1", pickup, drop, RideType.REGULAR);
        uber.requestRide("r2", pickup, drop, RideType.REGULAR);
        try {
            uber.requestRide("r3", pickup, drop, RideType.REGULAR);
        } catch (NoDriverAvailableException e) {
            System.out.println("  rejected: " + e.getMessage());
        }
    }
}
