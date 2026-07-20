package MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem;

import MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.location.GridLocationService;
import MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.matching.HighestRatedDriverStrategy;
import MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.matching.NearestDriverStrategy;
import MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.model.Driver;
import MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.model.DriverStatus;
import MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.model.Location;
import MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.model.Ride;
import MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.model.RideType;
import MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.model.Rider;
import MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.pricing.DistanceBasedPricingStrategy;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * PASS/FAIL harness (plain main, no JUnit — matching this repo). Exits non-zero
 * on any failure. The headline is {@code concurrentManyRiders_fewDrivers}.
 */
public class CabBookingTest {

    private static int passed = 0, failed = 0;
    private static final Location ORIGIN = new Location(0, 0);
    private static final Location DROP = new Location(3, 4);   // distance 5 from ORIGIN

    public static void main(String[] args) throws Exception {
        requestRide_assignsNearestDriver();
        secondRider_fallsThroughToNextDriver();
        completeRide_releasesDriverAndMovesToDrop();
        cancelRide_rollsBackDriver();
        invalidTransition_throws();
        noDriverInRange_throws();
        pricing_distanceTypeAndSurge();
        highestRatedStrategy_prefersRatingOverDistance();
        concurrentTwoRiders_bothGetDistinctDrivers();
        concurrentManyRiders_fewDrivers_noDoubleBooking();
        locationUpdates_concurrentWithMatching_stayAtomic();

        System.out.printf("%n==== %d passed, %d failed ====%n", passed, failed);
        if (failed > 0) System.exit(1);
    }

    // ---- functional ----

    private static void requestRide_assignsNearestDriver() {
        CabBookingService svc = newService(new NearestDriverStrategy());
        svc.registerDriver(new Driver("d1", "Ravi", 4.9, new Location(1, 1)));   // dist ~1.41
        svc.registerDriver(new Driver("d2", "Meena", 4.6, new Location(2, 0)));  // dist 2.0
        svc.registerRider(new Rider("r1", "Asha"));
        Ride ride = svc.requestRide("r1", ORIGIN, DROP, RideType.REGULAR);
        check("nearest driver (d1) assigned", ride.driver().id().equals("d1"));
        check("driver marked ASSIGNED", svc.ride(ride.id()).driver().status() == DriverStatus.ASSIGNED);
    }

    private static void secondRider_fallsThroughToNextDriver() {
        CabBookingService svc = newService(new NearestDriverStrategy());
        svc.registerDriver(new Driver("d1", "Ravi", 4.9, new Location(1, 1)));
        svc.registerDriver(new Driver("d2", "Meena", 4.6, new Location(2, 0)));
        svc.registerRider(new Rider("r1", "Asha"));
        svc.registerRider(new Rider("r2", "Bilal"));
        Ride a = svc.requestRide("r1", ORIGIN, DROP, RideType.REGULAR);
        Ride b = svc.requestRide("r2", ORIGIN, DROP, RideType.REGULAR);
        check("first rider got d1", a.driver().id().equals("d1"));
        check("second rider fell through to d2", b.driver().id().equals("d2"));
    }

    private static void completeRide_releasesDriverAndMovesToDrop() {
        CabBookingService svc = newService(new NearestDriverStrategy());
        Driver d1 = new Driver("d1", "Ravi", 4.9, new Location(1, 1));
        svc.registerDriver(d1);
        svc.registerRider(new Rider("r1", "Asha"));
        Ride ride = svc.requestRide("r1", ORIGIN, DROP, RideType.REGULAR);
        svc.startRide(ride.id());
        svc.completeRide(ride.id());
        check("driver released to AVAILABLE", d1.status() == DriverStatus.AVAILABLE);
        check("driver relocated to drop", d1.location().equals(DROP));
    }

    private static void cancelRide_rollsBackDriver() {
        CabBookingService svc = newService(new NearestDriverStrategy());
        Driver d1 = new Driver("d1", "Ravi", 4.9, new Location(1, 1));
        svc.registerDriver(d1);
        svc.registerRider(new Rider("r1", "Asha"));
        Ride ride = svc.requestRide("r1", ORIGIN, DROP, RideType.REGULAR);
        svc.cancelRide(ride.id());
        check("cancel rolls driver back to AVAILABLE", d1.status() == DriverStatus.AVAILABLE);
    }

    private static void invalidTransition_throws() {
        CabBookingService svc = newService(new NearestDriverStrategy());
        svc.registerDriver(new Driver("d1", "Ravi", 4.9, new Location(1, 1)));
        svc.registerRider(new Rider("r1", "Asha"));
        Ride ride = svc.requestRide("r1", ORIGIN, DROP, RideType.REGULAR);   // DRIVER_ASSIGNED
        boolean threw;
        try { svc.completeRide(ride.id()); threw = false; }                  // must START first
        catch (InvalidRideTransitionException e) { threw = true; }
        check("completing a non-started ride throws", threw);
    }

    private static void noDriverInRange_throws() {
        CabBookingService svc = newService(new NearestDriverStrategy());
        svc.registerDriver(new Driver("d1", "Ravi", 4.9, new Location(100, 100)));  // outside radius
        svc.registerRider(new Rider("r1", "Asha"));
        boolean threw;
        try { svc.requestRide("r1", ORIGIN, DROP, RideType.REGULAR); threw = false; }
        catch (NoDriverAvailableException e) { threw = true; }
        check("no driver in range -> NoDriverAvailableException", threw);
    }

    private static void pricing_distanceTypeAndSurge() {
        // base 3000 + 1200*5 = 9000 paise for REGULAR@1.0
        CabBookingService normal = new CabBookingService(new GridLocationService(1.0),
                new NearestDriverStrategy(), new DistanceBasedPricingStrategy(3000, 1200, 1.0), 50);
        check("REGULAR fare = ₹90", normal.estimateFarePaise(ORIGIN, DROP, RideType.REGULAR) == 9000);
        check("PREMIUM fare = ₹144 (×1.6)", normal.estimateFarePaise(ORIGIN, DROP, RideType.PREMIUM) == 14400);
        CabBookingService surge = new CabBookingService(new GridLocationService(1.0),
                new NearestDriverStrategy(), new DistanceBasedPricingStrategy(3000, 1200, 2.0), 50);
        check("surge ×2 fare = ₹180", surge.estimateFarePaise(ORIGIN, DROP, RideType.REGULAR) == 18000);
    }

    private static void highestRatedStrategy_prefersRatingOverDistance() {
        CabBookingService svc = newService(new HighestRatedDriverStrategy());
        svc.registerDriver(new Driver("near", "Near", 4.2, new Location(1, 0)));   // closer, lower rating
        svc.registerDriver(new Driver("good", "Good", 4.9, new Location(3, 0)));   // farther, higher rating
        svc.registerRider(new Rider("r1", "Asha"));
        Ride ride = svc.requestRide("r1", ORIGIN, DROP, RideType.REGULAR);
        check("highest-rated driver picked over nearest", ride.driver().id().equals("good"));
    }

    // ---- concurrency ----

    private static void concurrentTwoRiders_bothGetDistinctDrivers() throws InterruptedException {
        CabBookingService svc = newService(new NearestDriverStrategy());
        svc.registerDriver(new Driver("d1", "Ravi", 4.9, ORIGIN));    // both at the SAME spot
        svc.registerDriver(new Driver("d2", "Meena", 4.6, ORIGIN));
        svc.registerRider(new Rider("r1", "Asha"));
        svc.registerRider(new Rider("r2", "Bilal"));

        Set<String> assigned = ConcurrentHashMap.newKeySet();
        AtomicInteger wins = new AtomicInteger();
        CountDownLatch go = new CountDownLatch(1);
        ExecutorService pool = Executors.newFixedThreadPool(2);
        pool.execute(() -> tryRide(svc, "r1", assigned, wins, go));
        pool.execute(() -> tryRide(svc, "r2", assigned, wins, go));
        go.countDown();
        pool.shutdown();
        pool.awaitTermination(5, TimeUnit.SECONDS);

        check("both riders matched", wins.get() == 2);
        check("two DISTINCT drivers assigned", assigned.size() == 2);
    }

    /** THE test: N riders, M<N drivers, all at one point -> exactly M assigned, no driver twice. */
    private static void concurrentManyRiders_fewDrivers_noDoubleBooking() throws InterruptedException {
        CabBookingService svc = newService(new NearestDriverStrategy());
        int drivers = 10, riders = 50;
        for (int i = 0; i < drivers; i++) svc.registerDriver(new Driver("d" + i, "D" + i, 4.5, ORIGIN));
        for (int i = 0; i < riders; i++) svc.registerRider(new Rider("r" + i, "R" + i));

        ConcurrentHashMap<String, Integer> claimCount = new ConcurrentHashMap<>();
        AtomicInteger matched = new AtomicInteger();
        AtomicInteger rejected = new AtomicInteger();
        AtomicInteger other = new AtomicInteger();
        CountDownLatch ready = new CountDownLatch(riders);
        CountDownLatch go = new CountDownLatch(1);
        ExecutorService pool = Executors.newFixedThreadPool(riders);

        for (int i = 0; i < riders; i++) {
            final String riderId = "r" + i;
            pool.execute(() -> {
                ready.countDown();
                try { go.await(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); return; }
                try {
                    Ride ride = svc.requestRide(riderId, ORIGIN, DROP, RideType.REGULAR);
                    claimCount.merge(ride.driver().id(), 1, Integer::sum);
                    matched.incrementAndGet();
                } catch (NoDriverAvailableException e) {
                    rejected.incrementAndGet();
                } catch (RuntimeException e) {
                    other.incrementAndGet();
                }
            });
        }
        ready.await();
        go.countDown();
        pool.shutdown();
        pool.awaitTermination(10, TimeUnit.SECONDS);

        check("exactly " + drivers + " riders matched", matched.get() == drivers);
        check((riders - drivers) + " riders got NoDriverAvailable", rejected.get() == riders - drivers);
        check("no unexpected exceptions", other.get() == 0);
        check("every driver claimed at most once", claimCount.values().stream().allMatch(c -> c == 1));
        check("all " + drivers + " drivers were used", claimCount.size() == drivers);
    }

    private static void locationUpdates_concurrentWithMatching_stayAtomic() throws InterruptedException {
        CabBookingService svc = newService(new NearestDriverStrategy());
        int drivers = 8, riders = 20;
        for (int i = 0; i < drivers; i++) svc.registerDriver(new Driver("d" + i, "D" + i, 4.5, ORIGIN));
        for (int i = 0; i < riders; i++) svc.registerRider(new Rider("r" + i, "R" + i));

        // spam location updates while matching runs
        AtomicInteger stop = new AtomicInteger(0);
        Thread pinger = new Thread(() -> {
            java.util.Random rnd = new java.util.Random(1);
            while (stop.get() == 0) {
                svc.updateDriverLocation("d" + rnd.nextInt(drivers), new Location(rnd.nextDouble(), rnd.nextDouble()));
            }
        });
        pinger.setDaemon(true);
        pinger.start();

        ConcurrentHashMap<String, Integer> claimCount = new ConcurrentHashMap<>();
        AtomicInteger matched = new AtomicInteger();
        CountDownLatch go = new CountDownLatch(1);
        ExecutorService pool = Executors.newFixedThreadPool(riders);
        for (int i = 0; i < riders; i++) {
            final String riderId = "r" + i;
            pool.execute(() -> {
                try { go.await(); } catch (InterruptedException e) { return; }
                try {
                    Ride ride = svc.requestRide(riderId, ORIGIN, new Location(2, 2), RideType.REGULAR);
                    claimCount.merge(ride.driver().id(), 1, Integer::sum);
                    matched.incrementAndGet();
                } catch (NoDriverAvailableException ignored) { }
            });
        }
        go.countDown();
        pool.shutdown();
        pool.awaitTermination(10, TimeUnit.SECONDS);
        stop.set(1);

        check("no driver double-booked despite live location updates",
                claimCount.values().stream().allMatch(c -> c == 1));
        check("at most " + drivers + " matched under churn", matched.get() <= drivers);
    }

    // ---- helpers ----

    private static void tryRide(CabBookingService svc, String riderId, Set<String> assigned,
                                AtomicInteger wins, CountDownLatch go) {
        try { go.await(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); return; }
        try {
            Ride ride = svc.requestRide(riderId, ORIGIN, DROP, RideType.REGULAR);
            assigned.add(ride.driver().id());
            wins.incrementAndGet();
        } catch (NoDriverAvailableException ignored) { }
    }

    private static CabBookingService newService(
            MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.matching.DriverMatchingStrategy strategy) {
        return new CabBookingService(new GridLocationService(1.0), strategy,
                new DistanceBasedPricingStrategy(3000, 1200, 1.0), 50.0);
    }

    private static void check(String name, boolean ok) {
        if (ok) { passed++; System.out.println("[PASS] " + name); }
        else    { failed++; System.out.println("[FAIL] " + name); }
    }
}
