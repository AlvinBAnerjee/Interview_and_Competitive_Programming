package MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.strategy;

import java.time.Instant;

import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model.Ticket;

/**
 * STRATEGY for turning parked time into money. Kept separate from slot allocation
 * because pricing rules change far more often than the physical lot does.
 */
public interface PricingStrategy {

    /** Fare for this ticket, given when the vehicle left. Never negative. */
    double calculateFare(Ticket ticket, Instant exitTime);
}
