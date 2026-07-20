package MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.payment;

/** Outcome of a payment attempt. */
public record PaymentResult(boolean success, String transactionId) {
    public static PaymentResult ok(String transactionId) { return new PaymentResult(true, transactionId); }
    public static PaymentResult declined() { return new PaymentResult(false, null); }
}
