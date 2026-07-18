package MachineCoding_LLD.LLD_Interview_Problems._06_Medium_ElevatorSystem.strategy;

import java.util.List;

import MachineCoding_LLD.LLD_Interview_Problems._06_Medium_ElevatorSystem.Direction;
import MachineCoding_LLD.LLD_Interview_Problems._06_Medium_ElevatorSystem.Elevator;

/**
 * Classic elevator dispatch heuristic: prefer a car that is idle or already travelling
 * toward the caller in a compatible direction (it will simply pass by), scored by plain
 * floor distance. A car moving away or in the wrong direction would have to finish its
 * current sweep and come back, so it's penalized rather than excluded outright — with
 * every car penalized equally, the nearest one among them still wins.
 */
public final class NearestCarStrategy implements DispatchStrategy {

    private static final int WRONG_DIRECTION_PENALTY = 1_000;

    @Override
    public Elevator choose(List<Elevator> elevators, int floor, Direction requestedDirection) {
        Elevator best = null;
        int bestCost = Integer.MAX_VALUE;
        for (Elevator e : elevators) {
            int cost = cost(e, floor, requestedDirection);
            if (cost < bestCost) {
                bestCost = cost;
                best = e;
            }
        }
        return best;
    }

    private int cost(Elevator e, int floor, Direction requestedDirection) {
        int distance = Math.abs(e.currentFloor() - floor);
        Direction carDirection = e.direction();
        if (carDirection == Direction.IDLE) {
            return distance;
        }

        boolean willPassOnCurrentSweep =
                (carDirection == Direction.UP && requestedDirection != Direction.DOWN && floor >= e.currentFloor())
             || (carDirection == Direction.DOWN && requestedDirection != Direction.UP && floor <= e.currentFloor());

        return willPassOnCurrentSweep ? distance : distance + WRONG_DIRECTION_PENALTY;
    }
}
