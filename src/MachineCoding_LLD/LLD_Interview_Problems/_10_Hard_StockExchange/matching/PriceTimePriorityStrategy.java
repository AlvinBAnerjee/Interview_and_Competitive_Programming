package MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.matching;

import MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.model.Order;
import MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.model.OrderBook;
import MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.model.OrderType;
import MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.model.PriceLevel;
import MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.model.Side;
import MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.model.Trade;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Price-time priority (a.k.a. FIFO) matching:
 *   1. Always fill against the BEST opposite price first (price priority).
 *   2. Within a price level, fill the oldest resting order first (time priority)
 *      — that's just the head of the level's FIFO deque.
 *
 * A limit order stops crossing once the best opposite price is worse than its
 * limit, and rests its remainder. A market order ignores price and takes
 * whatever's there until filled or the book runs dry; its remainder is dropped
 * (never rests). Every fill executes at the resting order's price (maker price).
 */
public class PriceTimePriorityStrategy implements MatchingStrategy {

    @Override
    public List<Trade> match(OrderBook book, Order incoming, Supplier<String> tradeIdGenerator) {
        List<Trade> trades = new ArrayList<>();
        boolean isBuy = incoming.side() == Side.BUY;

        while (incoming.remainingQty() > 0) {
            PriceLevel best = isBuy ? book.bestAskLevel() : book.bestBidLevel();
            if (best == null) break;                          // opposite side empty
            if (!crosses(incoming, best.price())) break;      // limit price no longer satisfied

            fillAtLevel(book, incoming, best, trades, tradeIdGenerator);

            if (best.isEmpty()) {                             // level exhausted -> drop it
                if (isBuy) book.removeAskLevel(best.price());
                else book.removeBidLevel(best.price());
            }
        }

        // Limit remainder rests; market remainder is discarded.
        if (incoming.type() == OrderType.LIMIT && incoming.remainingQty() > 0) {
            book.addResting(incoming);
        }
        return trades;
    }

    /** Does the incoming order's price permit trading at {@code restingPrice}? */
    private boolean crosses(Order incoming, long restingPrice) {
        if (incoming.type() == OrderType.MARKET) return true;
        return incoming.side() == Side.BUY
                ? restingPrice <= incoming.limitPrice()      // willing to pay at least this ask
                : restingPrice >= incoming.limitPrice();     // willing to sell at most this bid
    }

    private void fillAtLevel(OrderBook book, Order incoming, PriceLevel level,
                             List<Trade> trades, Supplier<String> tradeIdGenerator) {
        while (incoming.remainingQty() > 0 && !level.isEmpty()) {
            Order resting = level.peek();
            int qty = Math.min(incoming.remainingQty(), resting.remainingQty());
            long price = resting.limitPrice();               // maker price

            String buyId  = incoming.side() == Side.BUY ? incoming.id() : resting.id();
            String sellId = incoming.side() == Side.BUY ? resting.id()  : incoming.id();
            trades.add(new Trade(tradeIdGenerator.get(), incoming.symbol(), price, qty,
                    buyId, sellId, incoming.side()));

            incoming.fill(qty);
            resting.fill(qty);
            level.reduceTotal(qty);

            if (resting.isFilled()) {
                level.pollFilled();                          // remove the spent head
                book.removeFromIndex(resting.id());
            }
        }
    }
}
