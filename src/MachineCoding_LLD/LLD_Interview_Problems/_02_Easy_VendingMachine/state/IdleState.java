package MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine.state;

import MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine.VendingMachine;
import MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine.model.TransactionResult;

/**
 * No money inserted yet. Inserting money moves us to {@code HAS_MONEY}; selecting a product
 * now is rejected because there's nothing to pay with.
 */
public class IdleState implements VendingState {

    @Override
    public boolean insertMoney(VendingMachine machine, int amount) {
        machine.addToBalance(amount);
        machine.setState(machine.hasMoneyState());
        return true;
    }

    @Override
    public TransactionResult selectProduct(VendingMachine machine, String code) {
        return TransactionResult.needMoney();
    }

    @Override
    public int cancel(VendingMachine machine) {
        return 0; // nothing inserted, nothing to refund
    }

    @Override
    public String name() {
        return "IDLE";
    }
}
