package MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.matching;

import MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.model.Order;
import MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.model.OrderBook;
import MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.model.Trade;

import java.util.List;
import java.util.function.Supplier;

/**
 * The matching algorithm. Given an incoming order, it matches against the book,
 * mutates the book (filling/removing resting orders, resting any remainder), and
 * returns the trades produced. Called only on the matcher thread.
 *
 * Swappable: price-time priority is the default, but a pro-rata algorithm (used
 * by some futures markets) would implement this same interface over the same
 * book primitives.
 */
public interface MatchingStrategy {
    List<Trade> match(OrderBook book, Order incoming, Supplier<String> tradeIdGenerator);
}
