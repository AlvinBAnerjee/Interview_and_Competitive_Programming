package MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model.ParkingFloor;
import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model.ParkingSlot;
import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model.Ticket;
import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model.Vehicle;
import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model.VehicleType;
import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.strategy.HourlyPricingStrategy;
import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.strategy.NearestSlotStrategy;

/**
 * A dependency-free test harness (no JUnit — this repo runs plain mains). Each check
 * prints PASS/FAIL; the process exits non-zero if anything fails, so CI/`&&` chains catch it.
 *
 * Lives in the same base package as {@link ParkingLot} so it can use that class's
 * package-private constructor to build ISOLATED lots — no shared singleton state
 * bleeding between tests.
 */
public class ParkingLotTest {

    private static int failures = 0;

    public static void main(String[] args) throws InterruptedException {
        testNearestAllocationOrder();
        testFullLotReturnsEmpty();
        testDoubleUnparkIsRejected();
        testHourlyFareRoundsUp();
        testConcurrentParkingNoDoubleAllocation();

        System.out.println();
        if (failures == 0) {
            System.out.println("ALL TESTS PASSED ✅");
        } else {
            System.out.println(failures + " TEST(S) FAILED ❌");
            System.exit(1);
        }
    }

    /** Nearest = lowest floor, then lowest slot number, and it spans floors. */
    private static void testNearestAllocationOrder() {
        ParkingLot lot = newLot(2, typeCounts(0, 2, 0)); // 2 floors, 2 car slots each = 4
        String s1 = park(lot, VehicleType.CAR).getSlot().getId();
        String s2 = park(lot, VehicleType.CAR).getSlot().getId();
        String s3 = park(lot, VehicleType.CAR).getSlot().getId();
        check("nearest #1 is F0-CAR-0", s1.equals("F0-CAR-0"), s1);
        check("nearest #2 is F0-CAR-1", s2.equals("F0-CAR-1"), s2);
        check("nearest #3 rolls to floor 1", s3.equals("F1-CAR-0"), s3);
    }

    /** A full lot returns empty rather than throwing or double-booking. */
    private static void testFullLotReturnsEmpty() {
        ParkingLot lot = newLot(1, typeCounts(0, 2, 0)); // capacity 2 cars
        Optional<Ticket> a = lot.park(car("A"));
        Optional<Ticket> b = lot.park(car("B"));
        Optional<Ticket> c = lot.park(car("C")); // one too many
        check("2 cars fit", a.isPresent() && b.isPresent(), a.isPresent() + "," + b.isPresent());
        check("3rd car is turned away cleanly (empty)", c.isEmpty(), c.toString());
        check("availability is 0 when full", lot.availableSlots(VehicleType.CAR) == 0,
                String.valueOf(lot.availableSlots(VehicleType.CAR)));
    }

    /** Using the same ticket twice must be rejected. */
    private static void testDoubleUnparkIsRejected() {
        ParkingLot lot = newLot(1, typeCounts(0, 1, 0));
        Ticket t = park(lot, VehicleType.CAR);
        lot.unpark(t); // first exit ok
        boolean threw;
        try {
            lot.unpark(t); // second exit must blow up
            threw = false;
        } catch (IllegalStateException expected) {
            threw = true;
        }
        check("double-unpark throws IllegalStateException", threw, "no exception");
        check("slot freed after single exit", lot.availableSlots(VehicleType.CAR) == 1,
                String.valueOf(lot.availableSlots(VehicleType.CAR)));
    }

    /** 2h30m for a car => rounds up to 3h => 3 * 20.0 = 60.0. */
    private static void testHourlyFareRoundsUp() {
        HourlyPricingStrategy pricing = HourlyPricingStrategy.withDefaults();
        Instant exit = Instant.now();
        Instant entry = exit.minus(150, ChronoUnit.MINUTES);
        ParkingSlot slot = new ParkingSlot("F0-CAR-0", 0, 0, VehicleType.CAR);
        Ticket ticket = new Ticket("T-1", car("Z"), slot, entry);
        double fare = pricing.calculateFare(ticket, exit);
        check("2h30m car fare rounds up to 60.0", fare == 60.0, String.valueOf(fare));
    }

    /**
     * THE headline test. Capacity N; fire 4N threads that all try to park the same instant.
     * Exactly N must succeed, every assigned slot id must be unique (no slot handed out
     * twice), and the rest must be turned away. Then everyone exits and capacity is restored.
     */
    private static void testConcurrentParkingNoDoubleAllocation() throws InterruptedException {
        int capacity = 200;
        int threads = capacity * 4;
        ParkingLot lot = newLot(1, typeCounts(0, capacity, 0));

        ExecutorService pool = Executors.newFixedThreadPool(64);
        CountDownLatch startGun = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(threads);

        Set<String> assignedSlots = ConcurrentHashMap.newKeySet(); // detects duplicates
        AtomicInteger doubleAssigned = new AtomicInteger();
        AtomicInteger parked = new AtomicInteger();
        List<Ticket> tickets = new ArrayList<>();
        Object ticketLock = new Object();

        for (int i = 0; i < threads; i++) {
            final int id = i;
            pool.submit(() -> {
                try {
                    startGun.await(); // everyone waits, then rushes the gates together
                    Optional<Ticket> t = lot.park(car("C-" + id));
                    if (t.isPresent()) {
                        parked.incrementAndGet();
                        if (!assignedSlots.add(t.get().getSlot().getId())) {
                            doubleAssigned.incrementAndGet(); // same slot seen twice => bug
                        }
                        synchronized (ticketLock) {
                            tickets.add(t.get());
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    done.countDown();
                }
            });
        }

        startGun.countDown();          // release the stampede
        done.await();                  // wait for all threads
        pool.shutdown();
        pool.awaitTermination(10, TimeUnit.SECONDS);

        check("exactly capacity cars parked", parked.get() == capacity,
                parked.get() + " (expected " + capacity + ")");
        check("no slot assigned twice", doubleAssigned.get() == 0,
                doubleAssigned.get() + " duplicates");
        check("distinct slots == capacity", assignedSlots.size() == capacity,
                String.valueOf(assignedSlots.size()));
        check("lot reports full", lot.availableSlots(VehicleType.CAR) == 0,
                String.valueOf(lot.availableSlots(VehicleType.CAR)));

        for (Ticket t : tickets) {
            lot.unpark(t);
        }
        check("capacity fully restored after everyone exits",
                lot.availableSlots(VehicleType.CAR) == capacity,
                String.valueOf(lot.availableSlots(VehicleType.CAR)));
    }

    // ---- tiny test utilities ----

    private static ParkingLot newLot(int floors, Map<VehicleType, Integer> perType) {
        List<ParkingFloor> f = ParkingLots.buildFloors(floors, perType);
        return new ParkingLot(f, new NearestSlotStrategy(f), HourlyPricingStrategy.withDefaults());
    }

    private static Map<VehicleType, Integer> typeCounts(int moto, int car, int truck) {
        Map<VehicleType, Integer> m = new EnumMap<>(VehicleType.class);
        m.put(VehicleType.MOTORCYCLE, moto);
        m.put(VehicleType.CAR, car);
        m.put(VehicleType.TRUCK, truck);
        return m;
    }

    private static Ticket park(ParkingLot lot, VehicleType type) {
        return lot.park(new MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model.Car("X")).orElseThrow(); // cars only in these tests
    }

    private static Vehicle car(String plate) {
        return new MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model.Car(plate);
    }

    private static void check(String name, boolean ok, String actual) {
        System.out.printf("[%s] %s%s%n", ok ? "PASS" : "FAIL", name, ok ? "" : "  -> got: " + actual);
        if (!ok) {
            failures++;
        }
    }
}
