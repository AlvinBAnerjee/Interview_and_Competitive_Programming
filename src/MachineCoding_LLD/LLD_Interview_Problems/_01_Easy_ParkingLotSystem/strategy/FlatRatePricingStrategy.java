package MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.strategy;

import java.time.Instant;

import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model.Ticket;

/**
 * One flat fee per visit, however long you stay.
 *
 * It exists to prove the Strategy point: swapping this in for HourlyPricingStrategy
 * needs zero changes inside ParkingLot.
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
