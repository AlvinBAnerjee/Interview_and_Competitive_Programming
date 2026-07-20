package MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.matching;

import java.util.Comparator;
import java.util.List;

/** Closest driver first — the default ride-hailing policy. */
public class NearestDriverStrategy implements DriverMatchingStrategy {
    @Override
    public List<Candidate> rank(List<Candidate> candidates) {
        return candidates.stream()
                .sorted(Comparator.comparingDouble(Candidate::distanceToPickup))
                .toList();
    }
}
