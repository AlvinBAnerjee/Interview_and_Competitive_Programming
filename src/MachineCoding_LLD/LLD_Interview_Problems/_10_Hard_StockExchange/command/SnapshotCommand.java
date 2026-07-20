package MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.command;

import MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.engine.MatchingEngine;
import MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.model.OrderBookSnapshot;

import java.util.concurrent.CompletableFuture;

/**
 * Read the book as a consistent snapshot. Routing reads through the same queue
 * as writes means the snapshot is taken between two mutations, never mid-way
 * through one — so no reader ever sees a half-updated book, with zero locking.
 */
public class SnapshotCommand implements EngineCommand {
    private final int depth;
    private final CompletableFuture<OrderBookSnapshot> result;

    public SnapshotCommand(int depth, CompletableFuture<OrderBookSnapshot> result) {
        this.depth = depth;
        this.result = result;
    }

    @Override
    public void execute(MatchingEngine engine) {
        try {
            result.complete(engine.snapshot(depth));
        } catch (RuntimeException e) {
            result.completeExceptionally(e);
        }
    }
}
