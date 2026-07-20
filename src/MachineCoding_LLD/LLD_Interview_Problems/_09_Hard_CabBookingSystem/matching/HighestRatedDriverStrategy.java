package MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.matching;

import java.util.Comparator;
import java.util.List;

/**
 * Highest-rated driver first, breaking ties by distance. A second concrete
 * strategy, to show matching policy is pluggable without touching the service.
 */
public class HighestRatedDriverStrategy implements DriverMatchingStrategy {
    @Override
    public List<Candidate> rank(List<Candidate> candidates) {
        return candidates.stream()
                .sorted(Comparator.comparingDouble((Candidate c) -> c.driver().rating()).reversed()
                        .thenComparingDouble(Candidate::distanceToPickup))
                .toList();
    }
}
