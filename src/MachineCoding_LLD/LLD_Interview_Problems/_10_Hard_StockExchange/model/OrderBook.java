package MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * The order book for one symbol: two sorted maps of price levels plus an index
 * for O(1) cancel lookup. There is deliberately NO locking here — the book is
 * touched only by its owning matcher thread (single-writer model), so it can use
 * plain {@link TreeMap}/{@link HashMap} and stay cache-friendly and deterministic.
 *
 *   - bids: highest price first  (reverse order) -> best bid = firstEntry
 *   - asks: lowest price first   (natural order) -> best ask = firstEntry
 *
 * Best bid/ask is O(1) amortised (TreeMap first entry); insert/remove of a level
 * is O(log n). The book exposes primitive operations; the *algorithm* that walks
 * them lives in a {@link MatchingStrategy}, so price-time vs pro-rata is swappable.
 */
public class OrderBook {
    private final String symbol;
    private final TreeMap<Long, PriceLevel> bids = new TreeMap<>(Comparator.reverseOrder());
    private final TreeMap<Long, PriceLevel> asks = new TreeMap<>();
    private final Map<String, Order> restingIndex = new HashMap<>();

    public OrderBook(String symbol) { this.symbol = symbol; }

    public String symbol() { return symbol; }

    /** Best (most aggressive) level on a side, or null if that side is empty. */
    public PriceLevel bestBidLevel() { var e = bids.firstEntry(); return e == null ? null : e.getValue(); }
    public PriceLevel bestAskLevel() { var e = asks.firstEntry(); return e == null ? null : e.getValue(); }

    public void removeBidLevel(long price) { bids.remove(price); }
    public void removeAskLevel(long price) { asks.remove(price); }

    /** Rest a (limit) order's remaining qty into the book. */
    public void addResting(Order order) {
        TreeMap<Long, PriceLevel> side = order.side() == Side.BUY ? bids : asks;
        side.computeIfAbsent(order.limitPrice(), PriceLevel::new).add(order);
        restingIndex.put(order.id(), order);
    }

    public void removeFromIndex(String orderId) { restingIndex.remove(orderId); }

    /**
     * Cancel a resting order. Returns false if it isn't resting (unknown id, or
     * already fully filled). Runs on the matcher thread, so no locking needed.
     */
    public boolean cancel(String orderId) {
        Order order = restingIndex.remove(orderId);
        if (order == null) return false;
        TreeMap<Long, PriceLevel> side = order.side() == Side.BUY ? bids : asks;
        PriceLevel level = side.get(order.limitPrice());
        if (level != null) {
            level.remove(order);
            if (level.isEmpty()) side.remove(order.limitPrice());
        }
        return true;
    }

    public OrderBookSnapshot snapshot(int depth) {
        return new OrderBookSnapshot(symbol, topLevels(bids, depth), topLevels(asks, depth));
    }

    private static List<OrderBookSnapshot.Level> topLevels(TreeMap<Long, PriceLevel> side, int depth) {
        List<OrderBookSnapshot.Level> out = new ArrayList<>();
        for (PriceLevel level : side.values()) {
            if (out.size() >= depth) break;
            out.add(new OrderBookSnapshot.Level(level.price(), level.totalQty()));
        }
        return out;
    }
}
