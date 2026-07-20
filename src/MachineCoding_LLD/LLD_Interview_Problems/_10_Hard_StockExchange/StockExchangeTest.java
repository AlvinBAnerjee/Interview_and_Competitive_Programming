package MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange;

import MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.matching.PriceTimePriorityStrategy;
import MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.model.Order;
import MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.model.OrderBookSnapshot;
import MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.model.Side;
import MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.model.Trade;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * PASS/FAIL harness (plain main, no JUnit — matching this repo). Exits non-zero
 * on any failure. The headline is {@code concurrentFlood_conservationAndNoCross}.
 */
public class StockExchangeTest {

    private static int passed = 0, failed = 0;

    public static void main(String[] args) throws Exception {
        limitOrder_restsWhenNoMatch();
        crossingBuy_priceTimePriority();
        tradeExecutesAtMakerPrice();
        partialFill_restsRemainder();
        marketOrder_sweepsAndDoesNotRest();
        cancel_removesRestingOrder();
        shardsBySymbol_areIndependent();
        concurrentFlood_conservationAndNoCross();

        System.out.printf("%n==== %d passed, %d failed ====%n", passed, failed);
        if (failed > 0) System.exit(1);
    }

    // ---- deterministic matching ----

    private static void limitOrder_restsWhenNoMatch() {
        Exchange ex = newExchange();
        List<Trade> t = ex.placeOrder(Order.limit("b1", "ACME", Side.BUY, 99, 7));
        check("no trade when book is empty", t.isEmpty());
        OrderBookSnapshot book = ex.getBook("ACME", 5);
        check("order rests as best bid 99", book.bestBid() != null && book.bestBid() == 99);
        ex.shutdown();
    }

    private static void crossingBuy_priceTimePriority() {
        Exchange ex = newExchange();
        ex.placeOrder(Order.limit("s1", "ACME", Side.SELL, 101, 10));   // oldest at 101
        ex.placeOrder(Order.limit("s2", "ACME", Side.SELL, 101, 5));    // newer at 101
        ex.placeOrder(Order.limit("s3", "ACME", Side.SELL, 102, 8));    // worse price
        List<Trade> trades = ex.placeOrder(Order.limit("b3", "ACME", Side.BUY, 101, 12));

        check("two fills produced", trades.size() == 2);
        check("time priority: s1 filled first for 10", trades.get(0).sellOrderId().equals("s1") && trades.get(0).qty() == 10);
        check("then s2 filled for 2", trades.get(1).sellOrderId().equals("s2") && trades.get(1).qty() == 2);
        check("did not touch worse-priced s3 (102)", trades.stream().noneMatch(t -> t.sellOrderId().equals("s3")));

        OrderBookSnapshot book = ex.getBook("ACME", 5);
        check("best ask still 101 with 3 left (s2 remainder)", book.bestAsk() == 101 && book.asks().get(0).qty() == 3);
        check("aggressor fully filled -> no new bid", book.bestBid() == null);
        ex.shutdown();
    }

    private static void tradeExecutesAtMakerPrice() {
        Exchange ex = newExchange();
        ex.placeOrder(Order.limit("s1", "ACME", Side.SELL, 101, 5));
        List<Trade> trades = ex.placeOrder(Order.limit("b1", "ACME", Side.BUY, 105, 5));  // willing to pay 105
        check("single fill", trades.size() == 1);
        check("executes at MAKER price 101, not 105", trades.get(0).price() == 101);
        check("aggressor recorded as BUY", trades.get(0).aggressor() == Side.BUY);
        ex.shutdown();
    }

    private static void partialFill_restsRemainder() {
        Exchange ex = newExchange();
        ex.placeOrder(Order.limit("s1", "ACME", Side.SELL, 101, 10));
        List<Trade> trades = ex.placeOrder(Order.limit("b1", "ACME", Side.BUY, 101, 25));
        check("fills the available 10", trades.size() == 1 && trades.get(0).qty() == 10);
        OrderBookSnapshot book = ex.getBook("ACME", 5);
        check("remainder 15 rests as bid @101", book.bestBid() != null && book.bestBid() == 101 && book.bids().get(0).qty() == 15);
        check("asks now empty", book.bestAsk() == null);
        ex.shutdown();
    }

    private static void marketOrder_sweepsAndDoesNotRest() {
        Exchange ex = newExchange();
        ex.placeOrder(Order.limit("s1", "ACME", Side.SELL, 101, 5));
        ex.placeOrder(Order.limit("s2", "ACME", Side.SELL, 102, 8));
        List<Trade> trades = ex.placeOrder(Order.market("m1", "ACME", Side.BUY, 20));   // wants 20, only 13 exist
        check("market sweeps both levels", trades.size() == 2);
        check("filled 13 total (5 + 8)", trades.stream().mapToInt(Trade::qty).sum() == 13);
        OrderBookSnapshot book = ex.getBook("ACME", 5);
        check("book fully cleared", book.bestAsk() == null && book.bestBid() == null);  // market remainder NOT rested
        ex.shutdown();
    }

    private static void cancel_removesRestingOrder() {
        Exchange ex = newExchange();
        ex.placeOrder(Order.limit("b1", "ACME", Side.BUY, 99, 7));
        check("cancel resting order -> true", ex.cancelOrder("b1"));
        check("book empty after cancel", ex.getBook("ACME", 5).bestBid() == null);
        check("cancel again -> false", !ex.cancelOrder("b1"));
        check("cancel unknown id -> false", !ex.cancelOrder("nope"));
        ex.shutdown();
    }

    private static void shardsBySymbol_areIndependent() {
        Exchange ex = new Exchange(new PriceTimePriorityStrategy());
        ex.listSymbol("ACME");
        ex.listSymbol("GLOBEX");
        ex.placeOrder(Order.limit("a-sell", "ACME", Side.SELL, 101, 5));
        List<Trade> t = ex.placeOrder(Order.limit("g-buy", "GLOBEX", Side.BUY, 101, 5)); // different symbol
        check("order on GLOBEX does not match ACME's book", t.isEmpty());
        check("ACME ask still resting", ex.getBook("ACME", 5).bestAsk() == 101);
        check("GLOBEX bid resting on its own book", ex.getBook("GLOBEX", 5).bestBid() == 101);
        ex.shutdown();
    }

    // ---- concurrency headline ----

    /**
     * Flood one engine from many threads with random buys/sells. Afterwards:
     *   - shares are conserved: total BUY-side filled == total SELL-side filled;
     *   - the book is never left crossed (best bid < best ask);
     *   - no order is overfilled and no command errors;
     *   - the async trade feed received exactly the executed volume.
     */
    private static void concurrentFlood_conservationAndNoCross() throws InterruptedException {
        Exchange ex = newExchange();
        LoggingTradeListenerSilent feed = new LoggingTradeListenerSilent();
        ex.addTradeListener(feed);

        int threads = 16, ordersPerThread = 100;
        ConcurrentLinkedQueue<Order> allOrders = new ConcurrentLinkedQueue<>();
        AtomicLong tradedVolume = new AtomicLong();
        AtomicInteger errors = new AtomicInteger();
        CountDownLatch ready = new CountDownLatch(threads);
        CountDownLatch go = new CountDownLatch(1);
        ExecutorService pool = Executors.newFixedThreadPool(threads);

        for (int tIdx = 0; tIdx < threads; tIdx++) {
            final int base = tIdx * ordersPerThread;
            pool.execute(() -> {
                Random rnd = new Random(base + 1);
                ready.countDown();
                try { go.await(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); return; }
                for (int i = 0; i < ordersPerThread; i++) {
                    Side side = rnd.nextBoolean() ? Side.BUY : Side.SELL;
                    int qty = 1 + rnd.nextInt(10);
                    Order order = rnd.nextInt(10) == 0
                            ? Order.market("o" + (base + i), "ACME", side, qty)
                            : Order.limit("o" + (base + i), "ACME", side, 95 + rnd.nextInt(11), qty); // 95..105
                    try {
                        List<Trade> trades = ex.placeOrder(order);
                        for (Trade tr : trades) tradedVolume.addAndGet(tr.qty());
                        allOrders.add(order);
                    } catch (RuntimeException e) {
                        errors.incrementAndGet();
                    }
                }
            });
        }
        ready.await();
        go.countDown();
        pool.shutdown();
        pool.awaitTermination(20, TimeUnit.SECONDS);

        long buyFilled = allOrders.stream().filter(o -> o.side() == Side.BUY).mapToLong(Order::filledQty).sum();
        long sellFilled = allOrders.stream().filter(o -> o.side() == Side.SELL).mapToLong(Order::filledQty).sum();
        OrderBookSnapshot book = ex.getBook("ACME", 1);

        check("no command errors under load", errors.get() == 0);
        check("all " + threads * ordersPerThread + " orders processed", allOrders.size() == threads * ordersPerThread);
        check("conservation: BUY filled == SELL filled (" + buyFilled + ")", buyFilled == sellFilled);
        check("each trade fills a buyer and a seller -> tradedVolume == buyFilled",
                tradedVolume.get() == buyFilled);
        boolean uncrossed = book.bestBid() == null || book.bestAsk() == null || book.bestBid() < book.bestAsk();
        check("book is never left crossed", uncrossed);

        ex.shutdown();   // drains engines + trade publisher
        check("async feed received exactly the executed volume", feed.volume() == tradedVolume.get());
    }

    // ---- helpers ----

    /** A silent, thread-safe volume counter (avoids flooding stdout in the stress test). */
    private static final class LoggingTradeListenerSilent implements
            MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.observer.TradeListener {
        private final AtomicLong volume = new AtomicLong();
        @Override public void onTrade(Trade trade) { volume.addAndGet(trade.qty()); }
        long volume() { return volume.get(); }
    }

    private static Exchange newExchange() {
        Exchange ex = new Exchange(new PriceTimePriorityStrategy());
        ex.listSymbol("ACME");
        return ex;
    }

    private static void check(String name, boolean ok) {
        if (ok) { passed++; System.out.println("[PASS] " + name); }
        else    { failed++; System.out.println("[FAIL] " + name); }
    }
}
