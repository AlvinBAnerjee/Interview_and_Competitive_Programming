package MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.pricing;

import MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.model.Location;
import MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.model.RideType;

/**
 * fare = (base + perKm × distance) × rideType.multiplier × surge, in integer
 * paise. The {@code surgeMultiplier} is a constructor arg, so "normal" vs
 * "surge" pricing is just two instances of this same strategy (a peak-hours
 * scheduler would swap which one the service holds).
 */
public class DistanceBasedPricingStrategy implements PricingStrategy {
    private final long baseFarePaise;
    private final long perKmPaise;
    private final double surgeMultiplier;

    public DistanceBasedPricingStrategy(long baseFarePaise, long perKmPaise, double surgeMultiplier) {
        this.baseFarePaise = baseFarePaise;
        this.perKmPaise = perKmPaise;
        this.surgeMultiplier = surgeMultiplier;
    }

    @Override
    public long estimatePaise(Location pickup, Location drop, RideType type) {
        double distance = pickup.distanceTo(drop);
        double fare = (baseFarePaise + perKmPaise * distance) * type.fareMultiplier() * surgeMultiplier;
        return Math.round(fare);
    }
}
