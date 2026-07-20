package MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.model;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * All resting orders at one price, in FIFO arrival order — this deque IS the
 * "time" in price-time priority. The head is the next order to fill.
 *
 * Not thread-safe by design: it lives inside one {@code OrderBook}, which is
 * mutated only by that symbol's single matcher thread.
 */
public class PriceLevel {
    private final long price;
    private final Deque<Order> orders = new ArrayDeque<>();
    private long totalQty;                       // sum of remaining qty, kept for O(1) depth

    public PriceLevel(long price) { this.price = price; }

    public void add(Order order) {
        orders.addLast(order);                   // newest goes to the back
        totalQty += order.remainingQty();
    }

    public Order peek() { return orders.peekFirst(); }

    public void pollFilled() { orders.pollFirst(); }   // remove a head that's now fully filled

    /** Remove a specific resting order (used by cancel). */
    public boolean remove(Order order) {
        if (orders.remove(order)) {
            totalQty -= order.remainingQty();
            return true;
        }
        return false;
    }

    /** Account for a partial fill of the head without removing it. */
    public void reduceTotal(int qty) { totalQty -= qty; }

    public boolean isEmpty() { return orders.isEmpty(); }
    public long price() { return price; }
    public long totalQty() { return totalQty; }
    public int orderCount() { return orders.size(); }
}
