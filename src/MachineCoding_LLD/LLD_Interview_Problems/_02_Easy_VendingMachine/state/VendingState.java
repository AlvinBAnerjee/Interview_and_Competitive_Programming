package MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine.state;

import MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine.VendingMachine;
import MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine.model.TransactionResult;

/**
 * STATE pattern. Each machine mode is a class that answers the same actions differently —
 * that per-mode difference (accept vs. reject vs. process) is the whole point, and it
 * replaces a tangle of {@code if (mode == …)} checks scattered through the machine.
 *
 * <p>States decide <em>what's allowed</em> and drive the <em>transition</em>; the actual
 * mutation of money/stock lives on {@link VendingMachine}. States hold no data, so a single
 * shared instance of each is reused.
 */
public interface VendingState {

    /** @return true if the money was accepted, false if rejected. */
    boolean insertMoney(VendingMachine machine, int amount);

    TransactionResult selectProduct(VendingMachine machine, String code);

    /** @return the amount refunded (0 if there was nothing to refund). */
    int cancel(VendingMachine machine);

    String name();
}
