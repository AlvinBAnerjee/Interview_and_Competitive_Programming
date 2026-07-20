package MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.matching;

import java.util.List;

/**
 * Ranks in-range candidates best-first. It ONLY orders them — it never claims a
 * driver. The service walks the ranked list and does the atomic claim, so a
 * strategy stays a pure, side-effect-free policy that's trivial to swap or test.
 */
public interface DriverMatchingStrategy {
    List<Candidate> rank(List<Candidate> candidates);
}
