package MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.strategy;

import java.time.Instant;

import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model.Ticket;

/**
 * A second pricing scheme (flat fee per visit) that exists purely to prove the point:
 * you can swap the whole pricing policy without touching {@code ParkingLot} — that's the
 * Open/Closed win of the Strategy pattern.
 */
public class FlatRatePricingStrategy implements PricingStrategy {

    private final double flatFee;

    public FlatRatePricingStrategy(double flatFee) {
        this.flatFee = flatFee;
    }

    @Override
    public double calculateFare(Ticket ticket, Instant exitTime) {
        return flatFee;
    }
}
