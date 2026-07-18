package MachineCoding_LLD.LLD_Interview_Problems._06_Medium_ElevatorSystem.strategy;

import MachineCoding_LLD.LLD_Interview_Problems._06_Medium_ElevatorSystem.Direction;

/**
 * STRATEGY — decides which way a car should travel next once it's idle or has just
 * finished serving a floor, given only which directions still have pending stops. This is
 * the SCAN/LOOK policy itself; per-direction ordering is handled by the car's own
 * ascending/descending stop queues.
 */
public interface SchedulingStrategy {
    Direction decideNext(boolean hasUpStops, boolean hasDownStops, Direction currentDirection);
}
