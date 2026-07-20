package MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange;

import MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.command.CancelOrderCommand;
import MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.command.PlaceOrderCommand;
import MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.command.SnapshotCommand;
import MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.engine.MatchingEngine;
import MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.matching.MatchingStrategy;
import MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.model.Order;
import MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.model.OrderBookSnapshot;
import MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.model.Trade;
import MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.observer.TradeListener;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Facade over the exchange. Two ideas make it scale:
 *
 *   1. SHARD BY SYMBOL — each symbol gets its own {@link MatchingEngine} with its
 *      own thread and book, so unrelated symbols match fully in parallel.
 *   2. SYNC API OVER ASYNC CORE — {@code placeOrder} enqueues a command carrying
 *      a {@link CompletableFuture} and awaits it, so callers get a simple blocking
 *      call while the engine stays a lock-free single-writer underneath.
 *
 * Trades are published on ONE shared background executor so a slow subscriber
 * never stalls any matcher thread.
 */
public class Exchange {

    private final MatchingStrategy strategy;
    private final ConcurrentHashMap<String, MatchingEngine> engines = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> orderToSymbol = new ConcurrentHashMap<>();
    private final List<TradeListener> listeners = new CopyOnWriteArrayList<>();
    private final ExecutorService publisher = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "trade-publisher");
        t.setDaemon(true);
        return t;
    });

    public Exchange(MatchingStrategy strategy) { this.strategy = strategy; }

    /** Bring a symbol online (creates its dedicated engine + thread). */
    public void listSymbol(String symbol) {
        engines.computeIfAbsent(symbol, s -> new MatchingEngine(s, strategy, listeners, publisher));
    }

    /** Subscribe to the trade feed. The list is shared live with every engine. */
    public void addTradeListener(TradeListener listener) { listeners.add(listener); }

    /** Place an order and return the trades it generated (blocks on the matcher). */
    public List<Trade> placeOrder(Order order) {
        MatchingEngine engine = engineFor(order.symbol());
        orderToSymbol.put(order.id(), order.symbol());
        CompletableFuture<List<Trade>> result = new CompletableFuture<>();
        engine.submit(new PlaceOrderCommand(order, result));
        return result.join();
    }

    /** Cancel a resting order by id; returns false if unknown or already filled. */
    public boolean cancelOrder(String orderId) {
        String symbol = orderToSymbol.get(orderId);
        if (symbol == null) return false;
        CompletableFuture<Boolean> result = new CompletableFuture<>();
        engineFor(symbol).submit(new CancelOrderCommand(orderId, result));
        return result.join();
    }

    /** Consistent top-{@code depth} view of a symbol's book. */
    public OrderBookSnapshot getBook(String symbol, int depth) {
        CompletableFuture<OrderBookSnapshot> result = new CompletableFuture<>();
        engineFor(symbol).submit(new SnapshotCommand(depth, result));
        return result.join();
    }

    public void shutdown() {
        engines.values().forEach(MatchingEngine::shutdown);
        engines.values().forEach(MatchingEngine::awaitTermination);
        publisher.shutdown();
        try {
            publisher.awaitTermination(2, TimeUnit.SECONDS);   // let queued trade notifications drain
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private MatchingEngine engineFor(String symbol) {
        MatchingEngine engine = engines.get(symbol);
        if (engine == null) throw new IllegalArgumentException("Symbol not listed: " + symbol);
        return engine;
    }
}
