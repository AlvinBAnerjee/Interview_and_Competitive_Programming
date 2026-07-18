package MachineCoding_LLD.LLD_Interview_Problems._06_Medium_ElevatorSystem.strategy;

import java.util.List;

import MachineCoding_LLD.LLD_Interview_Problems._06_Medium_ElevatorSystem.Direction;
import MachineCoding_LLD.LLD_Interview_Problems._06_Medium_ElevatorSystem.Elevator;

/**
 * STRATEGY — picks which car answers a hall call. Swappable so the dispatcher's
 * choose-and-enqueue step never has to branch on a policy name.
 */
public interface DispatchStrategy {
    Elevator choose(List<Elevator> elevators, int floor, Direction requestedDirection);
}
