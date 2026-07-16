package MachineCoding_LLD.DesignPatterns._07_ChainOfResponsibility;
import java.util.*;
public class ATMDispenseChain {

    private DispenseChain c1;

    public ATMDispenseChain() {
        // initialize the chain
        this.c1 = new Dollar50Dispenser();
        DispenseChain c2 = new Dollar20Dispenser();
        DispenseChain c3 = new Dollar10Dispenser();

        // set the chain of responsibility
        c1.setNextChain(c2);
        c2.setNextChain(c3);
    }

    public static void main(String[] args) {
        ATMDispenseChain atmDispenser = new ATMDispenseChain();
        Scanner input = new Scanner(System.in);
        while (true) {
            System.out.println("Enter amount to dispense (multiple of 10, or -1 to quit)");
            int amount = input.nextInt();
            if (amount == -1) {
                break;
            }
            if (amount <= 0 || amount % 10 != 0) {
                System.out.println("Amount should be a positive multiple of 10s.");
                continue;
            }
            // process the request
            atmDispenser.c1.dispense(new Currency(amount));
        }
        input.close();
    }

}
