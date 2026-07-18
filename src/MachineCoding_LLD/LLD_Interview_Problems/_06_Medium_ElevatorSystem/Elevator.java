package MachineCoding_LLD.LLD_Interview_Problems._06_Medium_ElevatorSystem;

import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import MachineCoding_LLD.LLD_Interview_Problems._06_Medium_ElevatorSystem.command.ElevatorCommand;
import MachineCoding_LLD.LLD_Interview_Problems._06_Medium_ElevatorSystem.state.ElevatorState;
import MachineCoding_LLD.LLD_Interview_Problems._06_Medium_ElevatorSystem.state.IdleState;
import MachineCoding_LLD.LLD_Interview_Problems._06_Medium_ElevatorSystem.strategy.SchedulingStrategy;

/**
 * One car. This is both the {@code Receiver} the queued {@link ElevatorCommand}s act on and
 * the {@code Context} the {@link ElevatorState} machine drives — {@link #run} just loops
 * {@code state = state.next(this)} until told to stop.
 *
 * <p>Stops live in two priority queues — {@code upStops} ascending, {@code downStops}
 * descending by floor — so "the nearest stop in the current direction" is always the queue
 * head; that's the entire LOOK algorithm. A single {@link ReentrantLock}/{@link Condition}
 * pair parks the control-loop thread when both queues are empty and wakes it the instant
 * {@link #submit} adds work — classic bounded producer/consumer, no busy-wait.
 */
public final class Elevator {

    private final int id;
    private final SchedulingStrategy schedulingStrategy;
    private final long floorTravelMillis;
    private final long doorDwellMillis;

    private final PriorityBlockingQueue<ElevatorCommand> upStops =
            new PriorityBlockingQueue<>(16, Comparator.comparingInt(ElevatorCommand::floor));
    private final PriorityBlockingQueue<ElevatorCommand> downStops =
            new PriorityBlockingQueue<>(16, Comparator.comparingInt(ElevatorCommand::floor).reversed());

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition workAvailable = lock.newCondition();

    private volatile int currentFloor;
    private volatile Direction direction = Direction.IDLE;
    private volatile boolean doorsOpen = false;
    private volatile boolean running = true;
    private volatile ElevatorState state = new IdleState();

    public Elevator(int id, int startFloor, SchedulingStrategy schedulingStrategy,
                     long floorTravelMillis, long doorDwellMillis) {
        this.id = id;
        this.currentFloor = startFloor;
        this.schedulingStrategy = schedulingStrategy;
        this.floorTravelMillis = floorTravelMillis;
        this.doorDwellMillis = doorDwellMillis;
    }

    /** The control loop: one worker thread per car, run via {@code ExecutorService}. */
    public void run() {
        while (running) {
            try {
                state = state.next(this);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                running = false;
            }
        }
    }

    /** Producer side: queue a stop and wake the control loop if it's parked. */
    public void submit(ElevatorCommand command) {
        lock.lock();
        try {
            int floor = command.floor();
            if (floor > currentFloor) {
                upStops.offer(command);
            } else if (floor < currentFloor) {
                downStops.offer(command);
            } else {
                // Already at this floor: file it under whichever queue the car is (or was)
                // heading in, so it's served on the very next control-loop step.
                (direction == Direction.DOWN ? downStops : upStops).offer(command);
            }
            workAvailable.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public void stop() {
        lock.lock();
        try {
            running = false;
            workAvailable.signalAll();
        } finally {
            lock.unlock();
        }
    }

    // ---- called by ElevatorState implementations ----

    /** Blocks until a stop exists in either direction, then returns which way to go. */
    public Direction awaitWork() throws InterruptedException {
        lock.lock();
        try {
            while (running && upStops.isEmpty() && downStops.isEmpty()) {
                workAvailable.await();
            }
            if (!running) {
                return Direction.IDLE;
            }
            return schedulingStrategy.decideNext(!upStops.isEmpty(), !downStops.isEmpty(), Direction.IDLE);
        } finally {
            lock.unlock();
        }
    }

    public boolean hasStops(Direction dir) {
        if (dir == Direction.UP) {
            return !upStops.isEmpty();
        }
        if (dir == Direction.DOWN) {
            return !downStops.isEmpty();
        }
        return false;
    }

    public Integer peekTarget(Direction dir) {
        ElevatorCommand head = (dir == Direction.UP ? upStops : downStops).peek();
        return head == null ? null : head.floor();
    }

    /** Simulates moving one floor closer to {@code targetFloor}. */
    public void stepToward(int targetFloor) throws InterruptedException {
        Thread.sleep(floorTravelMillis);
        currentFloor += Integer.signum(targetFloor - currentFloor);
    }

    /** Executes every command queued for the current floor in {@code dir}'s queue. */
    public void openDoorsAndServe(Direction dir) {
        PriorityBlockingQueue<ElevatorCommand> queue = dir == Direction.UP ? upStops : downStops;
        doorsOpen = true;
        ElevatorCommand next;
        while ((next = queue.peek()) != null && next.floor() == currentFloor) {
            queue.poll();
            next.execute(this);
        }
    }

    public void dwellAtDoors() throws InterruptedException {
        Thread.sleep(doorDwellMillis);
        doorsOpen = false;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public SchedulingStrategy schedulingStrategy() {
        return schedulingStrategy;
    }

    // ---- accessors ----

    public int id() {
        return id;
    }

    public int currentFloor() {
        return currentFloor;
    }

    public Direction direction() {
        return direction;
    }

    public boolean doorsOpen() {
        return doorsOpen;
    }

    public int pendingCount() {
        return upStops.size() + downStops.size();
    }

    @Override
    public String toString() {
        String motion = doorsOpen ? "DOORS_OPEN" : (direction == Direction.IDLE ? "IDLE" : "MOVING_" + direction);
        return "Elevator#" + id + "[floor=" + currentFloor + ", " + motion + ", pending=" + pendingCount() + "]";
    }
}
