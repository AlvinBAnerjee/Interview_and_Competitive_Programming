package MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.pricing;

import MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.model.Location;
import MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.model.RideType;

/** Pluggable fare policy. Returns total fare in paise. */
public interface PricingStrategy {
    long estimatePaise(Location pickup, Location drop, RideType type);
}
