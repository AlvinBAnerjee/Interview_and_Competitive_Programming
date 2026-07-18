package MachineCoding_LLD.LLD_Interview_Problems._06_Medium_ElevatorSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import MachineCoding_LLD.LLD_Interview_Problems._06_Medium_ElevatorSystem.command.VisitFloorCommand;
import MachineCoding_LLD.LLD_Interview_Problems._06_Medium_ElevatorSystem.strategy.DispatchStrategy;
import MachineCoding_LLD.LLD_Interview_Problems._06_Medium_ElevatorSystem.strategy.SchedulingStrategy;

/**
 * FACADE — the single entry point a hall button or a cabin panel talks to. Owns every
 * {@link Elevator} and its worker thread, and the {@link DispatchStrategy} that scores them
 * for a hall call.
 *
 * <p>{@link #dispatchLock} guards <em>choose-then-enqueue</em> as one atomic step: the
 * strategy reads live elevator state (current floor, direction, pending count) to score
 * candidates, and without the lock two hall calls racing on that read could reason about a
 * half-updated fleet. It does <em>not</em> guard correctness of "served exactly once" —
 * that already holds structurally, since each request becomes exactly one
 * {@code VisitFloorCommand} handed to exactly one car's queue, drained by that car's single
 * worker thread. The lock instead keeps dispatch <em>decisions</em> consistent under load.
 */
public final class ElevatorSystem {

    private final List<Elevator> elevators;
    private final DispatchStrategy dispatchStrategy;
    private final ExecutorService pool;
    private final ReentrantLock dispatchLock = new ReentrantLock();

    public ElevatorSystem(int numElevators, int startFloor,
                           DispatchStrategy dispatchStrategy, SchedulingStrategy schedulingStrategy) {
        this(numElevators, startFloor, dispatchStrategy, schedulingStrategy, 150L, 150L);
    }

    public ElevatorSystem(int numElevators, int startFloor, DispatchStrategy dispatchStrategy,
                           SchedulingStrategy schedulingStrategy, long floorTravelMillis, long doorDwellMillis) {
        if (numElevators < 1) {
            throw new IllegalArgumentException("need at least one elevator");
        }
        this.dispatchStrategy = Objects.requireNonNull(dispatchStrategy, "dispatchStrategy");
        Objects.requireNonNull(schedulingStrategy, "schedulingStrategy");

        List<Elevator> built = new ArrayList<>();
        for (int i = 1; i <= numElevators; i++) {
            built.add(new Elevator(i, startFloor, schedulingStrategy, floorTravelMillis, doorDwellMillis));
        }
        this.elevators = List.copyOf(built);

        this.pool = Executors.newFixedThreadPool(numElevators, r -> {
            Thread t = new Thread(r, "elevator-worker");
            t.setDaemon(true);
            return t;
        });
        this.elevators.forEach(e -> pool.execute(e::run));
    }

    /** External hall call: someone on floor {@code floor} wants to go {@code direction}. */
    public int requestElevator(int floor, Direction direction) {
        return requestElevator(floor, direction, () -> { });
    }

    public int requestElevator(int floor, Direction direction, Runnable onArrive) {
        if (direction == Direction.IDLE) {
            throw new IllegalArgumentException("hall call must request UP or DOWN");
        }
        dispatchLock.lock();
        try {
            Elevator chosen = dispatchStrategy.choose(elevators, floor, direction);
            chosen.submit(new VisitFloorCommand(floor, onArrive));
            return chosen.id();
        } finally {
            dispatchLock.unlock();
        }
    }

    /** Internal cabin call: a rider already inside car {@code elevatorId} presses a floor. */
    public void pressFloor(int elevatorId, int destFloor) {
        pressFloor(elevatorId, destFloor, () -> { });
    }

    public void pressFloor(int elevatorId, int destFloor, Runnable onArrive) {
        elevatorById(elevatorId).submit(new VisitFloorCommand(destFloor, onArrive));
    }

    public List<Elevator> elevators() {
        return elevators;
    }

    public void shutdown() throws InterruptedException {
        elevators.forEach(Elevator::stop);
        pool.shutdown();
        pool.awaitTermination(5, TimeUnit.SECONDS);
    }

    private Elevator elevatorById(int id) {
        for (Elevator e : elevators) {
            if (e.id() == id) {
                return e;
            }
        }
        throw new IllegalArgumentException("no such elevator: " + id);
    }
}
