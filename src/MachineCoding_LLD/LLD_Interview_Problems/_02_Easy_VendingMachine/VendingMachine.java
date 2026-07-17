package MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine.model.Denomination;
import MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine.model.Inventory;
import MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine.model.Product;
import MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine.model.TransactionResult;
import MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine.state.DispensingState;
import MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine.state.HasMoneyState;
import MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine.state.IdleState;
import MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine.state.VendingState;
import MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine.strategy.ChangeStrategy;

/**
 * The Context in the State pattern. Its three public actions ({@link #insertCoin},
 * {@link #selectProduct}, {@link #cancel}) simply <em>delegate to the current state</em> —
 * the machine itself has no {@code if (mode == …)} logic. The state objects, in turn, call
 * back into the mutation helpers here so that all money/stock changes live in one place.
 *
 * <p>Separation of concerns: <b>inventory</b> (product stock), <b>money handling</b> (coin
 * reserve + inserted balance), and <b>state</b> are three distinct areas, each with its own
 * fields and helpers.
 */
public class VendingMachine {

    // ---- inventory ----
    private final Map<String, Product> catalog = new HashMap<>();
    private final Inventory<String> productStock = new Inventory<>();

    // ---- money handling ----
    private final Inventory<Denomination> coinReserve = new Inventory<>();
    private final List<Denomination> insertedCoins = new ArrayList<>();
    private final ChangeStrategy changeStrategy;

    // ---- state (one shared instance each; states are data-free) ----
    private final VendingState idle = new IdleState();
    private final VendingState hasMoney = new HasMoneyState();
    private final VendingState dispensing = new DispensingState();
    private VendingState state = idle;

    public VendingMachine(ChangeStrategy changeStrategy) {
        this.changeStrategy = changeStrategy;
    }

    // ================= public API — pure delegation to the current state =================

    public boolean insertCoin(Denomination coin) {
        return state.insertCoin(this, coin);
    }

    public TransactionResult selectProduct(String code) {
        return state.selectProduct(this, code);
    }

    public List<Denomination> cancel() {
        return state.cancel(this);
    }

    // ================= admin / maintenance =================

    public void restock(Product product, int qty) {
        catalog.put(product.code(), product);
        productStock.add(product.code(), qty);
    }

    public void loadCoins(Denomination denomination, int qty) {
        coinReserve.add(denomination, qty);
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

    public VendingState dispensingState() {
        return dispensing;
    }

    public VendingState currentState() {
        return state;
    }

    public void acceptCoin(Denomination coin) {
        insertedCoins.add(coin);
    }

    public int balance() {
        return insertedCoins.stream().mapToInt(c -> c.value).sum();
    }

    public Map<String, Product> catalog() {
        return catalog;
    }

    public boolean inStock(String code) {
        return productStock.hasStock(code);
    }

    public ChangeStrategy changeStrategy() {
        return changeStrategy;
    }

    /**
     * The reserve <b>plus</b> the coins the customer just inserted — because a real machine
     * banks your coins before paying out, so those coins are available to make change.
     */
    public Map<Denomination, Integer> projectedReserve() {
        Map<Denomination, Integer> projected = new EnumMap<>(Denomination.class);
        projected.putAll(coinReserve.snapshot());
        for (Denomination d : insertedCoins) {
            projected.merge(d, 1, Integer::sum);
        }
        return projected;
    }

    /**
     * Commit a validated purchase: bank the inserted coins, pay out the pre-computed change
     * plan, and drop one unit of stock. Called only from {@code HasMoneyState} after every
     * guard has passed, so it never partially fails.
     */
    public List<Denomination> commitDispense(String code, List<Denomination> changePlan) {
        for (Denomination d : insertedCoins) {
            coinReserve.addOne(d);
        }
        insertedCoins.clear();
        for (Denomination d : changePlan) {
            coinReserve.removeOne(d);
        }
        productStock.removeOne(code);
        return changePlan;
    }

    public List<Denomination> refund() {
        List<Denomination> refunded = new ArrayList<>(insertedCoins);
        insertedCoins.clear();
        setState(idle);
        return refunded;
    }

    // ---- read-only helpers (handy for demos / tests) ----

    public int stockOf(String code) {
        return productStock.count(code);
    }

    public int reserveCount(Denomination d) {
        return coinReserve.count(d);
    }
}
