package MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine;

import java.util.List;

import MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine.model.Denomination;
import MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine.model.Product;
import MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine.model.TransactionResult;
import MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine.model.TransactionResult.Status;
import MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine.strategy.GreedyChangeStrategy;

/**
 * Dependency-free harness (no JUnit — this repo runs plain mains). Prints PASS/FAIL and
 * exits non-zero on any failure so `&&`/CI catch it. Covers the state machine, change-making,
 * refunds, and every rejection path.
 */
public class VendingMachineTest {

    private static int failures = 0;

    public static void main(String[] args) {
        selectBeforeMoneyIsRejected();
        happyPathReturnsCorrectChange();
        exactMoneyGivesNoChange();
        insufficientFundsKeepsMoney();
        invalidSelectionRejected();
        outOfStockAfterLastUnit();
        cancelRefundsExactlyWhatWasInserted();
        cannotMakeChangeKeepsMoneyThenRefunds();
        insertedCoinsFundTheChange();

        System.out.println();
        if (failures == 0) {
            System.out.println("ALL TESTS PASSED ✅");
        } else {
            System.out.println(failures + " TEST(S) FAILED ❌");
            System.exit(1);
        }
    }

    private static void selectBeforeMoneyIsRejected() {
        VendingMachine m = machineWith(new Product("A1", "Water", 20), 2);
        TransactionResult r = m.selectProduct("A1");
        check("select before inserting money → NEED_MONEY", r.status() == Status.NEED_MONEY, r.toString());
        check("state stays IDLE", m.currentState().name().equals("IDLE"), m.currentState().name());
    }

    private static void happyPathReturnsCorrectChange() {
        VendingMachine m = machineWith(new Product("A2", "Soda", 25), 1);
        m.loadCoins(Denomination.FIVE, 3);
        m.insertCoin(Denomination.TWENTY);
        m.insertCoin(Denomination.TEN); // balance 30
        TransactionResult r = m.selectProduct("A2");
        check("30 for a 25 soda → DISPENSED", r.isDispensed(), r.toString());
        check("change totals 5", value(r.change()) == 5, String.valueOf(value(r.change())));
        check("stock decremented to 0", m.stockOf("A2") == 0, String.valueOf(m.stockOf("A2")));
        check("balance reset to 0", m.balance() == 0, String.valueOf(m.balance()));
        check("returns to IDLE", m.currentState().name().equals("IDLE"), m.currentState().name());
    }

    private static void exactMoneyGivesNoChange() {
        VendingMachine m = machineWith(new Product("A1", "Water", 20), 1);
        m.insertCoin(Denomination.TWENTY);
        TransactionResult r = m.selectProduct("A1");
        check("exact money → DISPENSED", r.isDispensed(), r.toString());
        check("no change returned", r.change().isEmpty(), r.change().toString());
    }

    private static void insufficientFundsKeepsMoney() {
        VendingMachine m = machineWith(new Product("A2", "Soda", 25), 1);
        m.insertCoin(Denomination.TWENTY); // only 20
        TransactionResult r = m.selectProduct("A2");
        check("20 for a 25 soda → INSUFFICIENT_FUNDS", r.status() == Status.INSUFFICIENT_FUNDS, r.toString());
        check("money retained (balance 20)", m.balance() == 20, String.valueOf(m.balance()));
        check("still in HAS_MONEY", m.currentState().name().equals("HAS_MONEY"), m.currentState().name());
    }

    private static void invalidSelectionRejected() {
        VendingMachine m = machineWith(new Product("A1", "Water", 20), 1);
        m.insertCoin(Denomination.TWENTY);
        TransactionResult r = m.selectProduct("ZZ");
        check("unknown code → INVALID_SELECTION", r.status() == Status.INVALID_SELECTION, r.toString());
    }

    private static void outOfStockAfterLastUnit() {
        VendingMachine m = machineWith(new Product("A1", "Water", 20), 1);
        m.insertCoin(Denomination.TWENTY);
        m.selectProduct("A1"); // buys the only unit
        m.insertCoin(Denomination.TWENTY);
        TransactionResult r = m.selectProduct("A1");
        check("buying past last unit → OUT_OF_STOCK", r.status() == Status.OUT_OF_STOCK, r.toString());
    }

    private static void cancelRefundsExactlyWhatWasInserted() {
        VendingMachine m = machineWith(new Product("A1", "Water", 20), 1);
        m.insertCoin(Denomination.TEN);
        m.insertCoin(Denomination.FIVE);
        List<Denomination> refund = m.cancel();
        check("refund totals exactly 15", value(refund) == 15, String.valueOf(value(refund)));
        check("balance cleared", m.balance() == 0, String.valueOf(m.balance()));
        check("back to IDLE after cancel", m.currentState().name().equals("IDLE"), m.currentState().name());
    }

    private static void cannotMakeChangeKeepsMoneyThenRefunds() {
        VendingMachine m = machineWith(new Product("A2", "Soda", 25), 1);
        m.loadCoins(Denomination.TWENTY, 1); // no small coins to make 5
        m.insertCoin(Denomination.FIFTY);    // owes 25 change; reserve {20, +inserted 50}
        TransactionResult r = m.selectProduct("A2");
        check("no way to make 25 change → CANNOT_MAKE_CHANGE", r.status() == Status.CANNOT_MAKE_CHANGE, r.toString());
        check("money still held (balance 50)", m.balance() == 50, String.valueOf(m.balance()));
        List<Denomination> refund = m.cancel();
        check("cancel refunds the 50", value(refund) == 50, String.valueOf(value(refund)));
    }

    /** Coins the customer just inserted are banked first, so they can fund the change. */
    private static void insertedCoinsFundTheChange() {
        VendingMachine m = machineWith(new Product("A1", "Gum", 5), 1); // empty coin reserve
        m.insertCoin(Denomination.FIVE);
        m.insertCoin(Denomination.FIVE); // balance 10, owes 5 change
        TransactionResult r = m.selectProduct("A1");
        check("change funded from just-inserted coins → DISPENSED", r.isDispensed(), r.toString());
        check("change is a single 5", value(r.change()) == 5 && r.change().size() == 1, r.change().toString());
    }

    // ---- helpers ----

    private static VendingMachine machineWith(Product p, int qty) {
        VendingMachine m = new VendingMachine(new GreedyChangeStrategy());
        m.restock(p, qty);
        return m;
    }

    private static int value(List<Denomination> coins) {
        return coins.stream().mapToInt(c -> c.value).sum();
    }

    private static void check(String name, boolean ok, String actual) {
        System.out.printf("[%s] %s%s%n", ok ? "PASS" : "FAIL", name, ok ? "" : "  -> got: " + actual);
        if (!ok) {
            failures++;
        }
    }
}
