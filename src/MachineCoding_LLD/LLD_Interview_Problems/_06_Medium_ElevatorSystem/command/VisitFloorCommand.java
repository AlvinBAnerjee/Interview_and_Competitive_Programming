package MachineCoding_LLD.LLD_Interview_Problems._06_Medium_ElevatorSystem.command;

import java.util.Objects;

import MachineCoding_LLD.LLD_Interview_Problems._06_Medium_ElevatorSystem.Elevator;

/**
 * CONCRETE COMMAND — "stop at this floor and open the doors." {@code onArrive} is a
 * caller-supplied hook run at the moment of arrival (e.g. a UI callback, or a test's
 * countdown latch) without {@link Elevator} needing to know that caller exists.
 */
public final class VisitFloorCommand implements ElevatorCommand {

    private final int floor;
    private final Runnable onArrive;

    public VisitFloorCommand(int floor) {
        this(floor, () -> { });
    }

    public VisitFloorCommand(int floor, Runnable onArrive) {
        this.floor = floor;
        this.onArrive = Objects.requireNonNull(onArrive, "onArrive");
    }

    @Override
    public int floor() {
        return floor;
    }

    @Override
    public void execute(Elevator elevator) {
        onArrive.run();
    }
}
