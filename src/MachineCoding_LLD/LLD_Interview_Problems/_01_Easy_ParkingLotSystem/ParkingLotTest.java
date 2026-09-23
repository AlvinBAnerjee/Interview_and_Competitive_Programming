package MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.factory.VehicleFactory;
import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model.ParkingSlot;
import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model.Ticket;
import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model.Vehicle;
import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model.VehicleType;
import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.strategy.HourlyPricingStrategy;

/**
 * Tests, written as a plain main() because this repo does not use JUnit.
 * Every check prints PASS or FAIL, and the process exits non-zero if anything failed.
 *
 * Each test builds its OWN lot, so tests never affect each other.
 */
public class ParkingLotTest {

    private static int failures = 0;

    public static void main(String[] args) throws InterruptedException {
        nearestSlotIsGivenOutFirst();
        fullLotTurnsVehiclesAwayCleanly();
        sameTicketCannotBeUsedTwice();
        hourlyFareRoundsUpToTheNextHour();
        manyThreadsNeverGetTheSameSlot();

        System.out.println();
        if (failures == 0) {
            System.out.println("ALL TESTS PASSED");
        } else {
            System.out.println(failures + " TEST(S) FAILED");
            System.exit(1);
        }
    }

    /** Nearest means lowest floor first, then lowest slot number, and it spans floors. */
    private static void nearestSlotIsGivenOutFirst() {
        ParkingLot lot = newLot(2, 2);   // 2 floors x 2 car slots

        String first = parkCar(lot).getSlot().getId();
        String second = parkCar(lot).getSlot().getId();
        String third = parkCar(lot).getSlot().getId();

        check("1st car gets F0-CAR-0", first.equals("F0-CAR-0"), first);
        check("2nd car gets F0-CAR-1", second.equals("F0-CAR-1"), second);
        check("3rd car moves up to floor 1", third.equals("F1-CAR-0"), third);
    }

    /** A full lot returns empty instead of throwing or double-booking. */
    private static void fullLotTurnsVehiclesAwayCleanly() {
        ParkingLot lot = newLot(1, 2);   // room for 2 cars

        Optional<Ticket> firstCar = lot.park(newCar("A"));
        Optional<Ticket> secondCar = lot.park(newCar("B"));
        Optional<Ticket> thirdCar = lot.park(newCar("C")); // one too many

        check("both cars fit", firstCar.isPresent() && secondCar.isPresent(),
                firstCar.isPresent() + ", " + secondCar.isPresent());
        check("3rd car is turned away with empty", thirdCar.isEmpty(), thirdCar.toString());
        check("no car slots left", lot.availableSlots(VehicleType.CAR) == 0,
                String.valueOf(lot.availableSlots(VehicleType.CAR)));
    }

    /** Re-using a ticket must be rejected, and must not free the slot twice. */
    private static void sameTicketCannotBeUsedTwice() {
        ParkingLot lot = newLot(1, 1);
        Ticket ticket = parkCar(lot);

        lot.unpark(ticket);                       // first exit is fine

        boolean wasRejected;
        try {
            lot.unpark(ticket);                   // second exit must blow up
            wasRejected = false;
        } catch (IllegalStateException expected) {
            wasRejected = true;
        }

        check("re-using a ticket throws IllegalStateException", wasRejected, "no exception");
        check("slot is free exactly once", lot.availableSlots(VehicleType.CAR) == 1,
                String.valueOf(lot.availableSlots(VehicleType.CAR)));
    }

    /** 2h30m in a car rounds up to 3 hours, at 20.0 an hour = 60.0 */
    private static void hourlyFareRoundsUpToTheNextHour() {
        HourlyPricingStrategy pricing = HourlyPricingStrategy.withDefaults();

        Instant exitTime = Instant.now();
        Instant entryTime = exitTime.minus(150, ChronoUnit.MINUTES);

        ParkingSlot slot = new ParkingSlot("F0-CAR-0", 0, 0, VehicleType.CAR);
        Ticket ticket = new Ticket("T-1", newCar("Z"), slot, entryTime);

        double fare = pricing.calculateFare(ticket, exitTime);
        check("2h30m in a car costs 60.0", fare == 60.0, String.valueOf(fare));
    }

    /**
     * The important one. The lot holds `capacity` cars; we start 4x that many threads and
     * release them all at the same moment.
     *
     * Expected: exactly `capacity` cars get in, every slot handed out is different, the
     * rest are turned away, and once everyone leaves the lot is empty again.
     */
    private static void manyThreadsNeverGetTheSameSlot() throws InterruptedException {
        int capacity = 200;
        int threadCount = capacity * 4;
        ParkingLot lot = newLot(1, capacity);

        ExecutorService pool = Executors.newFixedThreadPool(64);
        CountDownLatch startSignal = new CountDownLatch(1);      // holds every thread back
        CountDownLatch finished = new CountDownLatch(threadCount);

        Set<String> slotsHandedOut = ConcurrentHashMap.newKeySet();
        AtomicInteger timesASlotWasReused = new AtomicInteger();
        AtomicInteger carsThatGotIn = new AtomicInteger();
        List<Ticket> issuedTickets = Collections.synchronizedList(new ArrayList<>());

        for (int i = 0; i < threadCount; i++) {
            final int carNumber = i;
            pool.submit(() -> {
                try {
                    startSignal.await();          // wait at the barrier...
                    Optional<Ticket> ticket = lot.park(newCar("CAR-" + carNumber));

                    if (ticket.isPresent()) {
                        carsThatGotIn.incrementAndGet();
                        issuedTickets.add(ticket.get());

                        // add() returns false if this slot id was already seen => bug
                        boolean firstTimeThisSlotWasUsed =
                                slotsHandedOut.add(ticket.get().getSlot().getId());
                        if (!firstTimeThisSlotWasUsed) {
                            timesASlotWasReused.incrementAndGet();
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    finished.countDown();
                }
            });
        }

        startSignal.countDown();                  // ...and now everybody rushes the gate
        finished.await();
        pool.shutdown();
        pool.awaitTermination(10, TimeUnit.SECONDS);

        check("exactly " + capacity + " cars got in", carsThatGotIn.get() == capacity,
                String.valueOf(carsThatGotIn.get()));
        check("no slot was handed to two cars", timesASlotWasReused.get() == 0,
                timesASlotWasReused.get() + " duplicates");
        check("distinct slots used == capacity", slotsHandedOut.size() == capacity,
                String.valueOf(slotsHandedOut.size()));
        check("lot reports full", lot.availableSlots(VehicleType.CAR) == 0,
                String.valueOf(lot.availableSlots(VehicleType.CAR)));

        for (Ticket ticket : issuedTickets) {
            lot.unpark(ticket);
        }
        check("all slots free again after everyone leaves",
                lot.availableSlots(VehicleType.CAR) == capacity,
                String.valueOf(lot.availableSlots(VehicleType.CAR)));
    }

    // ---------- small helpers ----------

    /**
     * A fresh, isolated lot with car slots only (these tests need no bikes or trucks).
     * build() rather than buildShared(), so tests never disturb each other.
     */
    private static ParkingLot newLot(int floorCount, int carSlotsPerFloor) {
        return ParkingLot.builder()
                .floors(floorCount)
                .slotsPerFloor(VehicleType.CAR, carSlotsPerFloor)
                .build();
    }

    private static Ticket parkCar(ParkingLot lot) {
        return lot.park(newCar("TEST")).orElseThrow();
    }

    private static Vehicle newCar(String licensePlate) {
        return VehicleFactory.create(VehicleType.CAR, licensePlate);
    }

    private static void check(String what, boolean passed, String actual) {
        System.out.printf("[%s] %s%s%n",
                passed ? "PASS" : "FAIL", what, passed ? "" : "  -> got: " + actual);
        if (!passed) {
            failures++;
        }
    }
}
