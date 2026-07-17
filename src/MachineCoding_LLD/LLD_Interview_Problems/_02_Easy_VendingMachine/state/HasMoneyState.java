package MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine.state;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine.VendingMachine;
import MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine.model.Denomination;
import MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine.model.Product;
import MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine.model.TransactionResult;

/**
 * Money is on the balance. More coins are accepted; a selection runs the full purchase
 * check; cancel refunds everything and returns to idle.
 *
 * <p>Note the ordering of the guards in {@link #selectProduct}: existence → stock → funds →
 * <em>can we make change</em>. The change check is last and is done against the reserve
 * <b>plus the just-inserted coins</b> (a real machine banks your coins before paying out),
 * and it's a dry run — nothing is committed until every guard passes.
 */
public class HasMoneyState implements VendingState {

    @Override
    public boolean insertCoin(VendingMachine machine, Denomination coin) {
        machine.acceptCoin(coin); // stack more money, stay in HAS_MONEY
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

        int changeOwed = machine.balance() - product.price();
        Map<Denomination, Integer> projected = machine.projectedReserve(); // reserve + inserted
        Optional<List<Denomination>> plan = machine.changeStrategy().makeChange(changeOwed, projected);
        if (plan.isEmpty()) {
            return TransactionResult.cannotMakeChange(product); // keep money; user can cancel
        }

        // All guards passed — commit through the transient DISPENSING state.
        machine.setState(machine.dispensingState());
        List<Denomination> change = machine.commitDispense(code, plan.get());
        machine.setState(machine.idleState());
        return TransactionResult.dispensed(product, change);
    }

    @Override
    public List<Denomination> cancel(VendingMachine machine) {
        return machine.refund(); // returns inserted coins and resets to idle
    }

    @Override
    public String name() {
        return "HAS_MONEY";
    }
}
