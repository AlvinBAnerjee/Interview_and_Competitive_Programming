package MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine;

import MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine.model.Product;
import MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine.model.TransactionResult;

/**
 * A quick walkthrough of the common paths. Correctness assertions live in
 * {@code VendingMachineTest}.
 */
public class Main {

    public static void main(String[] args) {
        VendingMachine machine = new VendingMachine();
        machine.restock(new Product("A1", "Water", 20), 2);
        machine.restock(new Product("A2", "Soda", 25), 1);

        System.out.println("State: " + machine.currentState().name());

        // Select before paying → rejected.
        System.out.println(machine.selectProduct("A2"));

        // Pay 30 for a 25 soda → dispense + 5 change.
        machine.insertMoney(30);
        System.out.println("Balance: " + machine.balance() + ", State: " + machine.currentState().name());
        TransactionResult r = machine.selectProduct("A2");
        System.out.println(r + " | back to " + machine.currentState().name());

        // Soda now out of stock.
        machine.insertMoney(30);
        System.out.println(machine.selectProduct("A2"));
        int refund = machine.cancel();
        System.out.println("Cancelled, refunded: " + refund);

        // Buy water with exact money → no change.
        machine.insertMoney(20);
        System.out.println(machine.selectProduct("A1"));
    }
}
