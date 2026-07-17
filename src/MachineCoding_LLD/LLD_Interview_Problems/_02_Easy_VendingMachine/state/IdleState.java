package MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine.state;

import java.util.List;

import MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine.VendingMachine;
import MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine.model.Denomination;
import MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine.model.TransactionResult;

/**
 * No money inserted yet. The first coin moves us to {@code HAS_MONEY}; selecting a product
 * now is rejected because there's nothing to pay with.
 */
public class IdleState implements VendingState {

    @Override
    public boolean insertCoin(VendingMachine machine, Denomination coin) {
        machine.acceptCoin(coin);
        machine.setState(machine.hasMoneyState());
        return true;
    }

    @Override
    public TransactionResult selectProduct(VendingMachine machine, String code) {
        return TransactionResult.needMoney();
    }

    @Override
    public List<Denomination> cancel(VendingMachine machine) {
        return List.of(); // nothing inserted, nothing to refund
    }

    @Override
    public String name() {
        return "IDLE";
    }
}
