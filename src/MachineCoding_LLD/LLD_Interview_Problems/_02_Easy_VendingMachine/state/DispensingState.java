package MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine.state;

import java.util.List;

import MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine.VendingMachine;
import MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine.model.Denomination;
import MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine.model.TransactionResult;

/**
 * The brief window while a product drops and change is counted out. Every user action is
 * refused here.
 *
 * <p>Because our dispense is synchronous (it completes inside {@code selectProduct}), an
 * external caller never actually catches the machine in this state. It's modeled anyway so
 * the "no double-dispense" rule is explicit and correct — and it's the single place you'd
 * hang a lock if dispensing ever became asynchronous (motor delay, card auth, etc.).
 */
public class DispensingState implements VendingState {

    @Override
    public boolean insertCoin(VendingMachine machine, Denomination coin) {
        return false; // coin returned; we're busy
    }

    @Override
    public TransactionResult selectProduct(VendingMachine machine, String code) {
        return TransactionResult.machineBusy();
    }

    @Override
    public List<Denomination> cancel(VendingMachine machine) {
        return List.of(); // can't cancel a dispense in progress
    }

    @Override
    public String name() {
        return "DISPENSING";
    }
}
