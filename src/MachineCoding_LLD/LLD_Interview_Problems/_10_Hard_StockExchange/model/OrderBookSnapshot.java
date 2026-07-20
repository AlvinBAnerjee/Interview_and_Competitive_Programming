package MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.model;

import java.util.List;

/**
 * An immutable top-N view of a book, produced ON the matcher thread (via a
 * command) so it's always internally consistent. Safe to hand to any caller.
 */
public record OrderBookSnapshot(String symbol, List<Level> bids, List<Level> asks) {

    /** One aggregated price level: price and total resting quantity. */
    public record Level(long price, long qty) { }

    public Long bestBid() { return bids.isEmpty() ? null : bids.get(0).price(); }
    public Long bestAsk() { return asks.isEmpty() ? null : asks.get(0).price(); }

    public String render() {
        StringBuilder sb = new StringBuilder("  book ").append(symbol)
                .append("   (asks top-down, then bids)\n");
        for (int i = asks.size() - 1; i >= 0; i--) {
            sb.append(String.format("    ASK  %6d  x %d%n", asks.get(i).price(), asks.get(i).qty()));
        }
        sb.append("    ----------------\n");
        for (Level bid : bids) {
            sb.append(String.format("    BID  %6d  x %d%n", bid.price(), bid.qty()));
        }
        return sb.toString();
    }
}
