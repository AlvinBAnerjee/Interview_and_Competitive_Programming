package MachineCoding_LLD.LLD_Interview_Problems._06_Medium_ElevatorSystem.state;

import MachineCoding_LLD.LLD_Interview_Problems._06_Medium_ElevatorSystem.Direction;
import MachineCoding_LLD.LLD_Interview_Problems._06_Medium_ElevatorSystem.Elevator;

/**
 * Travelling toward the nearest pending stop in the car's current direction. Re-reads that
 * queue's head on every step, so a closer stop added mid-flight is picked up automatically
 * — that re-check, against an ascending (or descending) priority queue, *is* the LOOK
 * algorithm. Hands off to {@link DoorsOpenState} on arrival.
 */
public final class MovingState implements ElevatorState {

    @Override
    public ElevatorState next(Elevator elevator) throws InterruptedException {
        Direction direction = elevator.direction();
        Integer target = elevator.peekTarget(direction);
        if (target == null) {
            // This direction's queue drained (or a same-floor request briefly confused it);
            // let IdleState re-consult the scheduling strategy instead of guessing here.
            return new IdleState();
        }

        elevator.stepToward(target);
        if (elevator.currentFloor() == target) {
            return new DoorsOpenState();
        }
        return this;
    }
}
