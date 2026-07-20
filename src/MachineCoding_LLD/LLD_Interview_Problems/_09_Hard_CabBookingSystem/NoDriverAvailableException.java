package MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem;

/** Thrown when no claimable driver is in range for a ride request. */
public class NoDriverAvailableException extends RuntimeException {
    public NoDriverAvailableException(String riderId) {
        super("No available driver found for rider " + riderId);
    }
}
