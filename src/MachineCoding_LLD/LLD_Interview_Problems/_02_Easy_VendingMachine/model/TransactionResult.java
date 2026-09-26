package MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine.model;

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
        INSUFFICIENT_FUNDS    // balance < price
    }

    private final Status status;
    private final Product product; // null unless relevant
    private final int change;      // 0 unless DISPENSED with change
    private final String message;

    private TransactionResult(Status status, Product product, int change, String message) {
        this.status = status;
        this.product = product;
        this.change = change;
        this.message = message;
    }

    public static TransactionResult dispensed(Product p, int change) {
        return new TransactionResult(Status.DISPENSED, p, change,
                "Dispensed " + p.name() + (change == 0 ? " (no change)" : " with change " + change));
    }

    public static TransactionResult needMoney() {
        return new TransactionResult(Status.NEED_MONEY, null, 0, "Insert money before selecting");
    }

    public static TransactionResult invalidSelection(String code) {
        return new TransactionResult(Status.INVALID_SELECTION, null, 0, "No product with code " + code);
    }

    public static TransactionResult outOfStock(Product p) {
        return new TransactionResult(Status.OUT_OF_STOCK, p, 0, p.name() + " is out of stock");
    }

    public static TransactionResult insufficientFunds(Product p, int shortBy) {
        return new TransactionResult(Status.INSUFFICIENT_FUNDS, p, 0, "Need " + shortBy + " more for " + p.name());
    }

    public Status status() {
        return status;
    }

    public Product product() {
        return product;
    }

    public int change() {
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
