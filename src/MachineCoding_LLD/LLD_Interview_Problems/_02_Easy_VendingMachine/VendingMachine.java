package MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine;

import java.util.HashMap;
import java.util.Map;

import MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine.model.Inventory;
import MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine.model.Product;
import MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine.model.TransactionResult;
import MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine.state.HasMoneyState;
import MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine.state.IdleState;
import MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine.state.VendingState;

/**
 * The Context in the State pattern. Its three public actions ({@link #insertMoney},
 * {@link #selectProduct}, {@link #cancel}) simply <em>delegate to the current state</em> —
 * the machine itself has no {@code if (mode == …)} logic. The state objects, in turn, call
 * back into the mutation helpers here so that all money/stock changes live in one place.
 */
public class VendingMachine {

    private final Map<String, Product> catalog = new HashMap<>();
    private final Inventory<String> productStock = new Inventory<>();
    private int balance;

    // ---- state (one shared instance each; states are data-free) ----
    private final VendingState idle = new IdleState();
    private final VendingState hasMoney = new HasMoneyState();
    private VendingState state = idle;

    // ================= public API — pure delegation to the current state =================

    public boolean insertMoney(int amount) {
        return state.insertMoney(this, amount);
    }

    public TransactionResult selectProduct(String code) {
        return state.selectProduct(this, code);
    }

    public int cancel() {
        return state.cancel(this);
    }

    // ================= admin =================

    public void restock(Product product, int qty) {
        catalog.put(product.code(), product);
        productStock.add(product.code(), qty);
    }

    // ================= state callbacks (invoked by the state classes) =================

    public void setState(VendingState newState) {
        this.state = newState;
    }

    public VendingState idleState() {
        return idle;
    }

    public VendingState hasMoneyState() {
        return hasMoney;
    }

    public VendingState currentState() {
        return state;
    }

    public void addToBalance(int amount) {
        balance += amount;
    }

    public int balance() {
        return balance;
    }

    public Map<String, Product> catalog() {
        return catalog;
    }

    public boolean inStock(String code) {
        return productStock.hasStock(code);
    }

    /**
     * Commit a validated purchase: charge the price, drop one unit of stock, and return the
     * change. Called only from {@code HasMoneyState} after every guard has passed.
     */
    public int commitDispense(String code, int price) {
        int change = balance - price;
        balance = 0;
        productStock.removeOne(code);
        return change;
    }

    public int refund() {
        int refunded = balance;
        balance = 0;
        setState(idle);
        return refunded;
    }

    // ---- read-only helpers (handy for demos / tests) ----

    public int stockOf(String code) {
        return productStock.count(code);
    }
}
