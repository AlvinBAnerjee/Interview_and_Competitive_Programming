package MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow;

/** Thrown when a requested seat is already held by someone else or already sold. */
public class SeatsUnavailableException extends RuntimeException {
    public SeatsUnavailableException(String showId, String seatId) {
        super("Seat '" + seatId + "' for show '" + showId + "' is not available");
    }
}
