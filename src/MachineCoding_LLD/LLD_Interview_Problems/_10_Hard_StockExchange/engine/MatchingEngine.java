package MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.engine;

import MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.command.EngineCommand;
import MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.matching.MatchingStrategy;
import MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.model.Order;
import MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.model.OrderBook;
import MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.model.OrderBookSnapshot;
import MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.model.Trade;
import MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.observer.TradeListener;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * One symbol's matching engine — the heart of the single-writer model.
 *
 * Many client threads {@link #submit} {@link EngineCommand}s onto this engine's
 * queue; a SINGLE worker thread drains that queue and is the only thread that
 * ever touches the {@link OrderBook}. That's why the book needs no locks: all
 * mutation (and reads, via snapshot commands) is serialized through one thread.
 * Determinism, cache locality, and zero lock contention fall out of that.
 *
 * The {@code process}/{@code cancel}/{@code snapshot} methods are package-visible
 * to the command classes and MUST be invoked only from the worker thread.
 */
public class MatchingEngine {

    private final String symbol;
    private final OrderBook book;
    private final MatchingStrategy strategy;
    private final BlockingQueue<EngineCommand> queue = new LinkedBlockingQueue<>();
    private final Thread worker;
    private final List<TradeListener> listeners;      // shared, live list owned by the Exchange
    private final Executor publisher;                 // off-thread trade publishing
    private volatile boolean running = true;
    private long tradeSeq = 0;                         // matcher-thread only -> no atomic needed

    public MatchingEngine(String symbol, MatchingStrategy strategy,
                          List<TradeListener> listeners, Executor publisher) {
        this.symbol = symbol;
        this.strategy = strategy;
        this.listeners = listeners;
        this.publisher = publisher;
        this.book = new OrderBook(symbol);
        this.worker = new Thread(this::runLoop, "engine-" + symbol);
        this.worker.setDaemon(true);
        this.worker.start();
    }

    /** Enqueue a command (called by any client thread). Producer side. */
    public void submit(EngineCommand command) {
        if (!running) throw new IllegalStateException("engine " + symbol + " is shut down");
        try {
            queue.put(command);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("interrupted while submitting", e);
        }
    }

    private void runLoop() {
        // Drain the queue until shut down AND empty, so no submitted command is lost.
        while (running || !queue.isEmpty()) {
            try {
                EngineCommand cmd = queue.poll(100, TimeUnit.MILLISECONDS);
                if (cmd != null) cmd.execute(this);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    // ---- worker-thread-only operations (invoked from commands) ----

    public List<Trade> process(Order order) {
        List<Trade> trades = strategy.match(book, order, this::nextTradeId);
        publish(trades);
        return trades;
    }

    public boolean cancel(String orderId) { return book.cancel(orderId); }

    public OrderBookSnapshot snapshot(int depth) { return book.snapshot(depth); }

    private String nextTradeId() { return symbol + "-T" + (++tradeSeq); }

    /** Hand trades to subscribers OFF the matcher thread so publishing never blocks matching. */
    private void publish(List<Trade> trades) {
        if (trades.isEmpty() || listeners.isEmpty()) return;
        for (Trade trade : trades) {
            for (TradeListener listener : listeners) {
                publisher.execute(() -> listener.onTrade(trade));
            }
        }
    }

    /**
     * Graceful stop: stop accepting new commands and let the worker drain what's
     * already queued (it notices {@code running} within one poll timeout), so no
     * in-flight command is dropped and no client blocked on a future is stranded.
     */
    public void shutdown() {
        running = false;
    }

    /** Block until the worker has drained and exited (best-effort, for tests/clean exit). */
    public void awaitTermination() {
        try {
            worker.join(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public String symbol() { return symbol; }
}
