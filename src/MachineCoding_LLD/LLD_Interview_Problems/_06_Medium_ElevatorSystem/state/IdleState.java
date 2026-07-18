package MachineCoding_LLD.LLD_Interview_Problems._06_Medium_ElevatorSystem.state;

import MachineCoding_LLD.LLD_Interview_Problems._06_Medium_ElevatorSystem.Direction;
import MachineCoding_LLD.LLD_Interview_Problems._06_Medium_ElevatorSystem.Elevator;

/**
 * Parked with nothing to do. Blocks (no busy-wait) until {@link Elevator#submit} signals a
 * new stop, then hands off to {@link MovingState} in whichever direction the scheduling
 * strategy picks.
 */
public final class IdleState implements ElevatorState {

    @Override
    public ElevatorState next(Elevator elevator) throws InterruptedException {
        elevator.setDirection(Direction.IDLE);
        Direction go = elevator.awaitWork(); // blocks here until work exists or shutdown
        if (go == Direction.IDLE) {
            return this; // shutting down; the run loop's `running` check will end the loop
        }
        elevator.setDirection(go);
        return new MovingState();
    }
}
