package MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.model;

/** Terminal outcome of a confirm attempt. */
public enum BookingStatus {
    CONFIRMED,        // payment succeeded, seats sold
    PAYMENT_FAILED    // payment declined, seats released
}
