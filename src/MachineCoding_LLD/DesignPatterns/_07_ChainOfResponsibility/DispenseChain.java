package MachineCoding_LLD.DesignPatterns._07_ChainOfResponsibility;

public interface DispenseChain {

    void setNextChain(DispenseChain nextChain);

    void dispense(Currency cur);

    /**
     * Forward to the next link if there is one; otherwise this is the end of the chain, so fail
     * loudly instead of NPEing on a null {@code next}. Whichever dispenser ends up last in the
     * chain relies on this instead of assuming a next link always exists.
     */
    default void forward(DispenseChain next, Currency cur) {
        if (next != null) {
            next.dispense(cur);
        } else if (cur.getAmount() != 0) {
            System.out.println("Cannot dispense remaining $" + cur.getAmount()
                    + " - no smaller denomination available");
        }
    }
}
