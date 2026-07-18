package MachineCoding_LLD.LLD_Interview_Problems._06_Medium_ElevatorSystem;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import MachineCoding_LLD.LLD_Interview_Problems._06_Medium_ElevatorSystem.strategy.DispatchStrategy;
import MachineCoding_LLD.LLD_Interview_Problems._06_Medium_ElevatorSystem.strategy.LeastLoadStrategy;
import MachineCoding_LLD.LLD_Interview_Problems._06_Medium_ElevatorSystem.strategy.LookSchedulingStrategy;
import MachineCoding_LLD.LLD_Interview_Problems._06_Medium_ElevatorSystem.strategy.NearestCarStrategy;

/**
 * Dependency-free harness (plain {@code main}, no JUnit — matching this repo). Each check
 * prints PASS/FAIL; the process exits non-zero if anything fails so {@code &&} chains / CI
 * catch it.
 *
 * <p>Covers dispatch correctness, LOOK ordering (same-direction stops before a reversal,
 * a closer stop overtaking a farther one mid-flight), least-load balancing, invalid input,
 * and — the headline — a concurrent burst of hall + car calls asserting every request is
 * served <b>exactly once</b> with no elevator dropping or double-serving a stop.
 */
public final class ElevatorTest {

    private static int failures = 0;

    public static void main(String[] args) throws InterruptedException {
        testHallCallGoesToNearestIdleCar();
        testLookServesSameDirectionBeforeReversing();
        testCloserStopOvertakesFartherOneMidFlight();
        testDuplicateStopsAtSameFloorServedTogether();
        testLeastLoadBalancesAcrossCars();
        testInvalidHallCallDirectionRejected();
        testUnknownElevatorIdRejected();
        testConcurrentBurstServesEveryRequestExactlyOnce();

        System.out.println();
        if (failures == 0) {
            System.out.println("ALL TESTS PASSED ✅");
        } else {
            System.out.println(failures + " TEST(S) FAILED ❌");
            System.exit(1);
        }
    }

    /** All cars idle at floor 0: the nearest-car strategy must pick a deterministic winner. */
    private static void testHallCallGoesToNearestIdleCar() throws InterruptedException {
        ElevatorSystem system = newSystem(3, new NearestCarStrategy());
        try {
            CountDownLatch arrived = new CountDownLatch(1);
            int carId = system.requestElevator(6, Direction.UP, arrived::countDown);
            boolean ok = arrived.await(5, TimeUnit.SECONDS);
            check("hall call dispatched and served", ok && carId >= 1 && carId <= 3,
                    "ok=" + ok + " carId=" + carId);
        } finally {
            system.shutdown();
        }
    }

    /** Car climbing to 9 with a stop at 5 added mid-flight must serve 5 before 9 (LOOK). */
    private static void testLookServesSameDirectionBeforeReversing() throws InterruptedException {
        ElevatorSystem system = newSystem(1, new NearestCarStrategy());
        try {
            CountDownLatch done = new CountDownLatch(2);
            StringBuilder order = new StringBuilder();

            // Both submits happen back-to-back with no sleep between them: a car can't move a
            // floor without first completing a full floorTravelMillis sleep, so both are
            // guaranteed to land while the car is still parked at floor 0 - i.e. both "ahead".
            system.pressFloor(1, 9, () -> { order.append("9,"); done.countDown(); });
            system.pressFloor(1, 2, () -> { order.append("2,"); done.countDown(); });

            done.await(5, TimeUnit.SECONDS);
            check("closer same-direction stop (2) served before farther one (9)",
                    order.toString().equals("2,9,"), "order=" + order);
        } finally {
            system.shutdown();
        }
    }

    /** A stop behind the car queues for the reversal; ahead-stops are served first. */
    private static void testCloserStopOvertakesFartherOneMidFlight() throws InterruptedException {
        ElevatorSystem system = newSystem(1, new NearestCarStrategy());
        try {
            CountDownLatch done = new CountDownLatch(3);
            StringBuilder order = new StringBuilder();

            system.pressFloor(1, 8, () -> { order.append("8,"); done.countDown(); });
            Thread.sleep(50); // let the car climb past floor 2 (well under the ~64ms trip to floor 8)
            system.pressFloor(1, 2, () -> { order.append("2,"); done.countDown(); }); // behind
            system.pressFloor(1, 9, () -> { order.append("9,"); done.countDown(); }); // ahead

            done.await(5, TimeUnit.SECONDS);
            check("finishes the UP sweep (8, then 9) before reversing DOWN to 2",
                    order.toString().equals("8,9,2,"), "order=" + order);
        } finally {
            system.shutdown();
        }
    }

    /** Two riders call the same floor: both are served by one door-open, in one pass. */
    private static void testDuplicateStopsAtSameFloorServedTogether() throws InterruptedException {
        ElevatorSystem system = newSystem(1, new NearestCarStrategy());
        try {
            CountDownLatch done = new CountDownLatch(2);
            system.pressFloor(1, 4, done::countDown);
            system.pressFloor(1, 4, done::countDown);

            boolean ok = done.await(5, TimeUnit.SECONDS);
            check("two calls to the same floor are both served", ok, "await result=" + ok);
        } finally {
            system.shutdown();
        }
    }

    /** Least-load prefers the emptier car over the nearer-but-busier one. */
    private static void testLeastLoadBalancesAcrossCars() throws InterruptedException {
        ElevatorSystem system = newSystem(2, new LeastLoadStrategy());
        try {
            // Load car #1 up with pending stops so it's clearly the busier car.
            for (int floor = 10; floor <= 14; floor++) {
                system.pressFloor(1, floor, () -> { });
            }
            Thread.sleep(20);

            CountDownLatch arrived = new CountDownLatch(1);
            int carId = system.requestElevator(11, Direction.UP, arrived::countDown);
            arrived.await(5, TimeUnit.SECONDS);
            check("least-load sends the new call to the less busy car", carId == 2, "carId=" + carId);
        } finally {
            system.shutdown();
        }
    }

    private static void testInvalidHallCallDirectionRejected() throws InterruptedException {
        ElevatorSystem system = newSystem(1, new NearestCarStrategy());
        try {
            check("hall call with IDLE direction rejected",
                    throwsIae(() -> system.requestElevator(3, Direction.IDLE)), "no exception");
        } finally {
            system.shutdown();
        }
    }

    private static void testUnknownElevatorIdRejected() throws InterruptedException {
        ElevatorSystem system = newSystem(1, new NearestCarStrategy());
        try {
            check("unknown elevator id rejected", throwsIae(() -> system.pressFloor(99, 3)), "no exception");
        } finally {
            system.shutdown();
        }
    }

    /**
     * THE concurrency test. Many threads fire a burst of hall calls and car calls at a small
     * fleet, each carrying its own arrival callback. Asserts: every request is served
     * exactly once (no lost, no duplicated stop), and the whole burst drains within a
     * generous bound (no deadlock, no starved car).
     */
    private static void testConcurrentBurstServesEveryRequestExactlyOnce() throws InterruptedException {
        int numElevators = 4;
        int numRequests = 300;
        ElevatorSystem system = newSystem(numElevators, new NearestCarStrategy());
        try {
            ExecutorService callers = Executors.newFixedThreadPool(32);
            CountDownLatch startGun = new CountDownLatch(1);
            CountDownLatch allServed = new CountDownLatch(numRequests);
            AtomicInteger servedCount = new AtomicInteger();
            List<Integer> duplicateGuardViolations = new CopyOnWriteArrayList<>();

            for (int i = 0; i < numRequests; i++) {
                int floor = 1 + (i % 15);
                boolean hallCall = i % 2 == 0;
                AtomicInteger timesServed = new AtomicInteger();
                Runnable onArrive = () -> {
                    if (timesServed.incrementAndGet() > 1) {
                        duplicateGuardViolations.add(floor);
                    }
                    servedCount.incrementAndGet();
                    allServed.countDown();
                };
                callers.submit(() -> {
                    try {
                        startGun.await();
                        if (hallCall) {
                            Direction dir = floor % 2 == 0 ? Direction.UP : Direction.DOWN;
                            system.requestElevator(floor, dir, onArrive);
                        } else {
                            int carId = 1 + (floor % numElevators);
                            system.pressFloor(carId, floor, onArrive);
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            }

            startGun.countDown();
            boolean drained = allServed.await(60, TimeUnit.SECONDS);
            callers.shutdown();
            callers.awaitTermination(10, TimeUnit.SECONDS);

            check("every one of " + numRequests + " concurrent requests served (no lost stops)",
                    drained && servedCount.get() == numRequests,
                    "drained=" + drained + " served=" + servedCount.get());
            check("no stop executed more than once (no duplicate delivery)",
                    duplicateGuardViolations.isEmpty(), "violations=" + duplicateGuardViolations);
        } finally {
            system.shutdown();
        }
    }

    // ---- helpers ----

    /** Small per-floor / dwell timings so the whole suite runs in well under a few seconds. */
    private static ElevatorSystem newSystem(int numElevators, DispatchStrategy dispatch) {
        return new ElevatorSystem(numElevators, 0, dispatch, new LookSchedulingStrategy(), 8L, 3L);
    }

    private static boolean throwsIae(Runnable r) {
        try {
            r.run();
            return false;
        } catch (IllegalArgumentException expected) {
            return true;
        }
    }

    private static void check(String name, boolean ok, String actual) {
        System.out.printf("[%s] %s%s%n", ok ? "PASS" : "FAIL", name, ok ? "" : "  -> got: " + actual);
        if (!ok) {
            failures++;
        }
    }
}
