package MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.payment;

/** Pluggable payment method. Passed per-booking to {@code confirmBooking}. */
public interface PaymentStrategy {
    PaymentResult pay(long amountPaise);
}
