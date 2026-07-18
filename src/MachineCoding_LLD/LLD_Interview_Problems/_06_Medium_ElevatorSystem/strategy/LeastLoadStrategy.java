package MachineCoding_LLD.LLD_Interview_Problems._06_Medium_ElevatorSystem.strategy;

import java.util.List;

import MachineCoding_LLD.LLD_Interview_Problems._06_Medium_ElevatorSystem.Direction;
import MachineCoding_LLD.LLD_Interview_Problems._06_Medium_ElevatorSystem.Elevator;

/**
 * Load-balancing alternative to {@link NearestCarStrategy}: send the call to whichever car
 * has the fewest pending stops, breaking ties by distance. Spreads work evenly across the
 * fleet instead of always favouring proximity, at the cost of sometimes picking a farther car.
 */
public final class LeastLoadStrategy implements DispatchStrategy {

    @Override
    public Elevator choose(List<Elevator> elevators, int floor, Direction requestedDirection) {
        Elevator best = null;
        int bestLoad = Integer.MAX_VALUE;
        int bestDistance = Integer.MAX_VALUE;
        for (Elevator e : elevators) {
            int load = e.pendingCount();
            int distance = Math.abs(e.currentFloor() - floor);
            if (load < bestLoad || (load == bestLoad && distance < bestDistance)) {
                best = e;
                bestLoad = load;
                bestDistance = distance;
            }
        }
        return best;
    }
}
