package MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine.state;

import MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine.VendingMachine;
import MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine.model.Product;
import MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine.model.TransactionResult;

/**
 * Money is on the balance. More money is accepted; a selection runs the purchase check;
 * cancel refunds everything and returns to idle.
 *
 * <p>Guards run in order — existence → stock → funds — and nothing is committed until every
 * guard passes, so a rejected purchase never leaves the machine in a half-updated state.
 */
public class HasMoneyState implements VendingState {

    @Override
    public boolean insertMoney(VendingMachine machine, int amount) {
        machine.addToBalance(amount); // stack more money, stay in HAS_MONEY
        return true;
    }

    @Override
    public TransactionResult selectProduct(VendingMachine machine, String code) {
        Product product = machine.catalog().get(code);
        if (product == null) {
            return TransactionResult.invalidSelection(code);
        }
        if (!machine.inStock(code)) {
            return TransactionResult.outOfStock(product);
        }
        if (machine.balance() < product.price()) {
            return TransactionResult.insufficientFunds(product, product.price() - machine.balance());
        }

        int change = machine.commitDispense(code, product.price());
        machine.setState(machine.idleState());
        return TransactionResult.dispensed(product, change);
    }

    @Override
    public int cancel(VendingMachine machine) {
        return machine.refund(); // returns inserted amount and resets to idle
    }

    @Override
    public String name() {
        return "HAS_MONEY";
    }
}
