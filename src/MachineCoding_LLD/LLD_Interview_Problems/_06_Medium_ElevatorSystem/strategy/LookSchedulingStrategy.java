package MachineCoding_LLD.LLD_Interview_Problems._06_Medium_ElevatorSystem.strategy;

import MachineCoding_LLD.LLD_Interview_Problems._06_Medium_ElevatorSystem.Direction;

/**
 * LOOK: keep travelling in the current direction as long as stops remain that way; only
 * reverse once that side is exhausted. Unlike full SCAN, a car never sweeps all the way to
 * a building's extreme floor once its last stop in that direction is served — it turns
 * around immediately, which is what real elevators do.
 */
public final class LookSchedulingStrategy implements SchedulingStrategy {

    @Override
    public Direction decideNext(boolean hasUpStops, boolean hasDownStops, Direction currentDirection) {
        if (currentDirection == Direction.UP && hasUpStops) {
            return Direction.UP;
        }
        if (currentDirection == Direction.DOWN && hasDownStops) {
            return Direction.DOWN;
        }
        if (hasUpStops) {
            return Direction.UP;
        }
        if (hasDownStops) {
            return Direction.DOWN;
        }
        return Direction.IDLE;
    }
}
