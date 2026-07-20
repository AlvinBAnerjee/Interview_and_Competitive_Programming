package MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.model;

/**
 * A single order. {@code remainingQty} is the only mutable field, and it is
 * mutated exclusively by the one matching thread that owns this symbol's book —
 * so no synchronization is needed on it. The client thread that placed the order
 * reads {@code filledQty()} only after the placeOrder future completes, which
 * establishes a happens-before edge, so the read is safe.
 *
 * Price is an integer number of ticks (e.g. paise) — never floating point.
 */
public class Order {
    private final String id;
    private final String symbol;
    private final Side side;
    private final OrderType type;
    private final long limitPrice;      // ticks; ignored for MARKET
    private final int originalQty;
    private int remainingQty;           // touched only by the matcher thread

    private Order(String id, String symbol, Side side, OrderType type, long limitPrice, int qty) {
        if (qty <= 0) throw new IllegalArgumentException("qty must be > 0");
        if (type == OrderType.LIMIT && limitPrice <= 0) throw new IllegalArgumentException("limit price must be > 0");
        this.id = id;
        this.symbol = symbol;
        this.side = side;
        this.type = type;
        this.limitPrice = limitPrice;
        this.originalQty = qty;
        this.remainingQty = qty;
    }

    public static Order limit(String id, String symbol, Side side, long price, int qty) {
        return new Order(id, symbol, side, OrderType.LIMIT, price, qty);
    }

    public static Order market(String id, String symbol, Side side, int qty) {
        return new Order(id, symbol, side, OrderType.MARKET, 0, qty);
    }

    /** Reduce remaining by an executed quantity. Matcher-thread only. */
    public void fill(int qty) {
        if (qty > remainingQty) throw new IllegalStateException("overfill of " + id);
        remainingQty -= qty;
    }

    public boolean isFilled() { return remainingQty == 0; }
    public int filledQty() { return originalQty - remainingQty; }

    public String id() { return id; }
    public String symbol() { return symbol; }
    public Side side() { return side; }
    public OrderType type() { return type; }
    public long limitPrice() { return limitPrice; }
    public int originalQty() { return originalQty; }
    public int remainingQty() { return remainingQty; }

    @Override
    public String toString() {
        String px = type == OrderType.MARKET ? "MKT" : String.valueOf(limitPrice);
        return String.format("%s %s %s %d@%s (rem %d)", id, side, type, originalQty, px, remainingQty);
    }
}
