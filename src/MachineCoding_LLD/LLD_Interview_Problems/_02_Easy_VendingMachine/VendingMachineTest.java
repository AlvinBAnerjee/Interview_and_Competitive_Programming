package MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine;

import MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine.model.Product;
import MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine.model.TransactionResult;
import MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine.model.TransactionResult.Status;

/**
 * Dependency-free harness (no JUnit — this repo runs plain mains). Prints PASS/FAIL and
 * exits non-zero on any failure so `&&`/CI catch it. Covers the state machine, change, refunds,
 * and every rejection path.
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
        m.insertMoney(30);
        TransactionResult r = m.selectProduct("A2");
        check("30 for a 25 soda → DISPENSED", r.isDispensed(), r.toString());
        check("change is 5", r.change() == 5, String.valueOf(r.change()));
        check("stock decremented to 0", m.stockOf("A2") == 0, String.valueOf(m.stockOf("A2")));
        check("balance reset to 0", m.balance() == 0, String.valueOf(m.balance()));
        check("returns to IDLE", m.currentState().name().equals("IDLE"), m.currentState().name());
    }

    private static void exactMoneyGivesNoChange() {
        VendingMachine m = machineWith(new Product("A1", "Water", 20), 1);
        m.insertMoney(20);
        TransactionResult r = m.selectProduct("A1");
        check("exact money → DISPENSED", r.isDispensed(), r.toString());
        check("no change returned", r.change() == 0, String.valueOf(r.change()));
    }

    private static void insufficientFundsKeepsMoney() {
        VendingMachine m = machineWith(new Product("A2", "Soda", 25), 1);
        m.insertMoney(20); // only 20
        TransactionResult r = m.selectProduct("A2");
        check("20 for a 25 soda → INSUFFICIENT_FUNDS", r.status() == Status.INSUFFICIENT_FUNDS, r.toString());
        check("money retained (balance 20)", m.balance() == 20, String.valueOf(m.balance()));
        check("still in HAS_MONEY", m.currentState().name().equals("HAS_MONEY"), m.currentState().name());
    }

    private static void invalidSelectionRejected() {
        VendingMachine m = machineWith(new Product("A1", "Water", 20), 1);
        m.insertMoney(20);
        TransactionResult r = m.selectProduct("ZZ");
        check("unknown code → INVALID_SELECTION", r.status() == Status.INVALID_SELECTION, r.toString());
    }

    private static void outOfStockAfterLastUnit() {
        VendingMachine m = machineWith(new Product("A1", "Water", 20), 1);
        m.insertMoney(20);
        m.selectProduct("A1"); // buys the only unit
        m.insertMoney(20);
        TransactionResult r = m.selectProduct("A1");
        check("buying past last unit → OUT_OF_STOCK", r.status() == Status.OUT_OF_STOCK, r.toString());
    }

    private static void cancelRefundsExactlyWhatWasInserted() {
        VendingMachine m = machineWith(new Product("A1", "Water", 20), 1);
        m.insertMoney(15);
        int refund = m.cancel();
        check("refund totals exactly 15", refund == 15, String.valueOf(refund));
        check("balance cleared", m.balance() == 0, String.valueOf(m.balance()));
        check("back to IDLE after cancel", m.currentState().name().equals("IDLE"), m.currentState().name());
    }

    // ---- helpers ----

    private static VendingMachine machineWith(Product p, int qty) {
        VendingMachine m = new VendingMachine();
        m.restock(p, qty);
        return m;
    }

    private static void check(String name, boolean ok, String actual) {
        System.out.printf("[%s] %s%s%n", ok ? "PASS" : "FAIL", name, ok ? "" : "  -> got: " + actual);
        if (!ok) {
            failures++;
        }
    }
}
