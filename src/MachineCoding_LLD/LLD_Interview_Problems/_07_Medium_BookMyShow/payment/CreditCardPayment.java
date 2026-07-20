package MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.payment;

import java.util.UUID;

/** Stub credit-card gateway that always approves (real impl would call a PSP). */
public class CreditCardPayment implements PaymentStrategy {
    private final String cardLast4;

    public CreditCardPayment(String cardLast4) { this.cardLast4 = cardLast4; }

    @Override
    public PaymentResult pay(long amountPaise) {
        // pretend to charge the card...
        return PaymentResult.ok("CC-" + cardLast4 + "-" + UUID.randomUUID().toString().substring(0, 8));
    }
}
