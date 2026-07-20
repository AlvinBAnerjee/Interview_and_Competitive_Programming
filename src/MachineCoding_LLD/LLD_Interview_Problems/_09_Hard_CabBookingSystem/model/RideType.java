package MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.model;

/** Ride tier; the multiplier scales the base distance/time fare. */
public enum RideType {
    POOL(0.8),
    REGULAR(1.0),
    PREMIUM(1.6);

    private final double fareMultiplier;

    RideType(double fareMultiplier) { this.fareMultiplier = fareMultiplier; }

    public double fareMultiplier() { return fareMultiplier; }
}
