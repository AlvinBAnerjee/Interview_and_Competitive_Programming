package MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine.model;

import java.util.List;

/**
 * The outcome of a {@code selectProduct} attempt. Returning a typed result (instead of
 * throwing) is deliberate: "insufficient funds" or "out of stock" are ordinary, expected
 * outcomes a caller must handle — not exceptional control flow. The status enum also makes
 * the state machine trivially testable ("assert status == INSUFFICIENT_FUNDS").
 */
public final class TransactionResult {

    public enum Status {
        DISPENSED,            // success: product + change returned
        NEED_MONEY,           // selected before inserting any money
        INVALID_SELECTION,    // no such product code
        OUT_OF_STOCK,         // product code exists but count is 0
        INSUFFICIENT_FUNDS,   // balance < price
        CANNOT_MAKE_CHANGE,   // can't form exact change from the reserve
        MACHINE_BUSY          // action attempted mid-dispense
    }

    private final Status status;
    private final Product product;               // null unless relevant
    private final List<Denomination> change;     // empty unless DISPENSED with change
    private final String message;

    private TransactionResult(Status status, Product product, List<Denomination> change, String message) {
        this.status = status;
        this.product = product;
        this.change = change == null ? List.of() : List.copyOf(change);
        this.message = message;
    }

    public static TransactionResult dispensed(Product p, List<Denomination> change) {
        return new TransactionResult(Status.DISPENSED, p, change,
                "Dispensed " + p.name() + (change.isEmpty() ? " (no change)" : " with change " + change));
    }

    public static TransactionResult needMoney() {
        return new TransactionResult(Status.NEED_MONEY, null, null, "Insert money before selecting");
    }

    public static TransactionResult invalidSelection(String code) {
        return new TransactionResult(Status.INVALID_SELECTION, null, null, "No product with code " + code);
    }

    public static TransactionResult outOfStock(Product p) {
        return new TransactionResult(Status.OUT_OF_STOCK, p, null, p.name() + " is out of stock");
    }

    public static TransactionResult insufficientFunds(Product p, int shortBy) {
        return new TransactionResult(Status.INSUFFICIENT_FUNDS, p, null, "Need " + shortBy + " more for " + p.name());
    }

    public static TransactionResult cannotMakeChange(Product p) {
        return new TransactionResult(Status.CANNOT_MAKE_CHANGE, p, null,
                "Can't make exact change for " + p.name() + " — cancel for a refund");
    }

    public static TransactionResult machineBusy() {
        return new TransactionResult(Status.MACHINE_BUSY, null, null, "Please wait — dispensing");
    }

    public Status status() {
        return status;
    }

    public Product product() {
        return product;
    }

    public List<Denomination> change() {
        return change;
    }

    public boolean isDispensed() {
        return status == Status.DISPENSED;
    }

    @Override
    public String toString() {
        return "[" + status + "] " + message;
    }
}
