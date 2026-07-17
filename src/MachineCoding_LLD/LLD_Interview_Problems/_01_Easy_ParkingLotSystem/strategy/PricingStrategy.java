package MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.strategy;

import java.time.Instant;

import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model.Ticket;

/**
 * STRATEGY for turning a parked duration into money. Decoupled from slot allocation on
 * purpose — pricing changes far more often than the physical model, and an interviewer
 * will always ask you to add a new pricing scheme.
 */
public interface PricingStrategy {

    /** Fare for a ticket given when the vehicle leaves. Never negative. */
    double calculateFare(Ticket ticket, Instant exitTime);
}
