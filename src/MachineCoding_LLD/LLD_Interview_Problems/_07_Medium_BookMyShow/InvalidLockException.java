package MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow;

/**
 * Thrown at confirm time when the caller no longer owns a live hold on every
 * seat — typically because the hold window expired before payment completed.
 */
public class InvalidLockException extends RuntimeException {
    public InvalidLockException(String message) { super(message); }
}
