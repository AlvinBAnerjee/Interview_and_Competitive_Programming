package MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.matching;

import MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.model.Driver;

/** A driver in range of a pickup, with the pre-computed distance for ranking. */
public record Candidate(Driver driver, double distanceToPickup) { }
