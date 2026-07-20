package MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.payment;

import java.util.UUID;

/** Stub UPI gateway — second concrete strategy, to show payment is pluggable. */
public class UpiPayment implements PaymentStrategy {
    private final String vpa;   // e.g. "alice@bank"

    public UpiPayment(String vpa) { this.vpa = vpa; }

    @Override
    public PaymentResult pay(long amountPaise) {
        return PaymentResult.ok("UPI-" + vpa + "-" + UUID.randomUUID().toString().substring(0, 8));
    }
}
