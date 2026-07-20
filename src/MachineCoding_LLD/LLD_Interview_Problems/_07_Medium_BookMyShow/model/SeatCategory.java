package MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.model;

/**
 * Seat tier. The multiplier is applied to a show's base price by the pricing
 * strategy, so categories stay pure data and pricing policy lives elsewhere.
 */
public enum SeatCategory {
    SILVER(1.0),
    GOLD(1.5),
    RECLINER(2.5);

    private final double multiplier;

    SeatCategory(double multiplier) { this.multiplier = multiplier; }

    public double multiplier() { return multiplier; }
}
