package MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem;

import MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.model.RideState;

/** Thrown when a ride is asked to make a transition its state machine forbids. */
public class InvalidRideTransitionException extends RuntimeException {
    public InvalidRideTransitionException(RideState from, RideState to) {
        super("Illegal ride transition: " + from + " -> " + to);
    }
}
