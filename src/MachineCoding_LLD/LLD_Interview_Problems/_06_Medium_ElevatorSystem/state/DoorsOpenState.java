package MachineCoding_LLD.LLD_Interview_Problems._06_Medium_ElevatorSystem.state;

import MachineCoding_LLD.LLD_Interview_Problems._06_Medium_ElevatorSystem.Direction;
import MachineCoding_LLD.LLD_Interview_Problems._06_Medium_ElevatorSystem.Elevator;

/**
 * Arrived. Serves every {@code ElevatorCommand} queued for this exact floor (so two people
 * calling the same floor share one door-open), dwells, then asks the
 * {@code SchedulingStrategy} where to go next: continue the current sweep, reverse, or park.
 */
public final class DoorsOpenState implements ElevatorState {

    @Override
    public ElevatorState next(Elevator elevator) throws InterruptedException {
        Direction arrivedFrom = elevator.direction();
        elevator.openDoorsAndServe(arrivedFrom);
        elevator.dwellAtDoors();

        boolean hasUp = elevator.hasStops(Direction.UP);
        boolean hasDown = elevator.hasStops(Direction.DOWN);
        Direction next = elevator.schedulingStrategy().decideNext(hasUp, hasDown, arrivedFrom);

        if (next == Direction.IDLE) {
            return new IdleState();
        }
        elevator.setDirection(next);
        return new MovingState();
    }
}
