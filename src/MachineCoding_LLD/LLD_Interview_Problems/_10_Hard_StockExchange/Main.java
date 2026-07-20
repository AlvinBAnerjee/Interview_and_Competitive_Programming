package MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange;

import MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.matching.PriceTimePriorityStrategy;
import MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.model.Order;
import MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.model.Side;
import MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.observer.LoggingTradeListener;

/**
 * Walks the matching engine through resting, crossing, partial fills, a market
 * order, and a cancel — printing the book after each step.
 */
public class Main {

    public static void main(String[] args) {
        Exchange exchange = new Exchange(new PriceTimePriorityStrategy());
        LoggingTradeListener feed = new LoggingTradeListener();
        exchange.addTradeListener(feed);
        exchange.listSymbol("ACME");

        System.out.println("=== 1. Rest three sell orders (asks) ===");
        exchange.placeOrder(Order.limit("s1", "ACME", Side.SELL, 101, 10));
        exchange.placeOrder(Order.limit("s2", "ACME", Side.SELL, 101, 5));   // same price, later -> behind s1
        exchange.placeOrder(Order.limit("s3", "ACME", Side.SELL, 102, 8));
        System.out.print(exchange.getBook("ACME", 5).render());

        System.out.println("=== 2. Rest two buy orders (bids) ===");
        exchange.placeOrder(Order.limit("b1", "ACME", Side.BUY, 99, 7));
        exchange.placeOrder(Order.limit("b2", "ACME", Side.BUY, 100, 4));
        System.out.print(exchange.getBook("ACME", 5).render());

        System.out.println("=== 3. Aggressive BUY 12 @ 101 crosses (price-time: s1 fully, then s2 partial) ===");
        var trades = exchange.placeOrder(Order.limit("b3", "ACME", Side.BUY, 101, 12));
        trades.forEach(t -> System.out.println("  -> " + t));
        System.out.print(exchange.getBook("ACME", 5).render());

        System.out.println("=== 4. MARKET SELL 6 sweeps the best bids (100 then 99) ===");
        var mkt = exchange.placeOrder(Order.market("b4sell", "ACME", Side.SELL, 6));
        mkt.forEach(t -> System.out.println("  -> " + t));
        System.out.print(exchange.getBook("ACME", 5).render());

        System.out.println("=== 5. Cancel resting ask s3 ===");
        System.out.println("  cancel s3 -> " + exchange.cancelOrder("s3"));
        System.out.print(exchange.getBook("ACME", 5).render());

        System.out.printf("Feed saw %d trades, total volume %d.%n", feed.tradeCount(), feed.volume());
        exchange.shutdown();
    }
}
