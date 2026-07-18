package MachineCoding_LLD.LLD_Interview_Problems._06_Medium_ElevatorSystem;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import MachineCoding_LLD.LLD_Interview_Problems._06_Medium_ElevatorSystem.strategy.LookSchedulingStrategy;
import MachineCoding_LLD.LLD_Interview_Problems._06_Medium_ElevatorSystem.strategy.NearestCarStrategy;

/**
 * A scripted, non-interactive walkthrough (like the other problems' {@code Main}s): a hall
 * call gets dispatched, a rider presses an internal floor, LOOK keeps the car sweeping one
 * direction instead of zig-zagging, and a closer stop added mid-flight is picked up on the
 * way. For correctness + the concurrency stress test, see {@code ElevatorTest}.
 */
public final class Main {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 3 elevators, all starting at floor 0 ===\n");
        ElevatorSystem system = new ElevatorSystem(3, 0, new NearestCarStrategy(), new LookSchedulingStrategy());

        hallCallGetsDispatched(system);
        separator();
        carCallThenMidFlightPickup(system);
        separator();
        lookKeepsSweepingOneDirection(system);

        system.shutdown();
    }

    /** A hall call at floor 5 going UP is dispatched to the nearest idle car. */
    private static void hallCallGetsDispatched(ElevatorSystem system) throws InterruptedException {
        System.out.println("-- Hall call: floor 5, UP --");
        CountDownLatch arrived = new CountDownLatch(1);
        int carId = system.requestElevator(5, Direction.UP, arrived::countDown);
        System.out.println("dispatched to car #" + carId);
        arrived.await(10, TimeUnit.SECONDS);
        System.out.println("served: " + describe(system));
    }

    /** A rider inside a car presses a floor; a nearer stop requested afterward is served first. */
    private static void carCallThenMidFlightPickup(ElevatorSystem system) throws InterruptedException {
        System.out.println("-- Car call: elevator #1 -> floor 9, then a closer floor-7 call arrives mid-flight --");
        CountDownLatch bothArrived = new CountDownLatch(2);
        StringBuilder order = new StringBuilder();

        system.pressFloor(1, 9, () -> {
            order.append("9 ");
            bothArrived.countDown();
        });
        Thread.sleep(80); // let car #1 start moving up before the closer call lands
        system.pressFloor(1, 7, () -> {
            order.append("7 ");
            bothArrived.countDown();
        });

        bothArrived.await(10, TimeUnit.SECONDS);
        System.out.println("arrival order: " + order + " -> floor 7 (closer, same direction) served before floor 9");
    }

    /** While car #3 is mid-sweep UP, a stop below it and another stop above it both arrive. */
    private static void lookKeepsSweepingOneDirection(ElevatorSystem system) throws InterruptedException {
        System.out.println("-- LOOK: car #3 sweeping UP to 8; floor 2 (behind it) and floor 9 (ahead) arrive mid-flight --");
        CountDownLatch done = new CountDownLatch(3);
        StringBuilder order = new StringBuilder();

        system.pressFloor(3, 8, () -> { order.append("8 "); done.countDown(); });
        Thread.sleep(500); // let car #3 climb past floor 2 first
        system.pressFloor(3, 2, () -> { order.append("2 "); done.countDown(); }); // behind -> queued for the reversal
        system.pressFloor(3, 9, () -> { order.append("9 "); done.countDown(); }); // ahead -> served before reversing

        done.await(10, TimeUnit.SECONDS);
        System.out.println("arrival order: " + order + "-> finishes the UP sweep (8, 9) before reversing DOWN to 2");
    }

    private static String describe(ElevatorSystem system) {
        StringBuilder sb = new StringBuilder();
        system.elevators().forEach(e -> sb.append(e).append("  "));
        return sb.toString();
    }

    private static void separator() {
        System.out.println("\n======================================================\n");
    }
}
