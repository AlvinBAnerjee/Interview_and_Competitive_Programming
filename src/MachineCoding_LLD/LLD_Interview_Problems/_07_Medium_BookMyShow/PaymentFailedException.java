package MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow;

/** Thrown when the payment gateway declines; the held seats are released first. */
public class PaymentFailedException extends RuntimeException {
    public PaymentFailedException(String message) { super(message); }
}
