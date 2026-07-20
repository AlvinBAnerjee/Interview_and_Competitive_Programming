package MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.command;

import MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.engine.MatchingEngine;

import java.util.concurrent.CompletableFuture;

/** Cancel a resting order; completes the promise with whether it was found. */
public class CancelOrderCommand implements EngineCommand {
    private final String orderId;
    private final CompletableFuture<Boolean> result;

    public CancelOrderCommand(String orderId, CompletableFuture<Boolean> result) {
        this.orderId = orderId;
        this.result = result;
    }

    @Override
    public void execute(MatchingEngine engine) {
        try {
            result.complete(engine.cancel(orderId));
        } catch (RuntimeException e) {
            result.completeExceptionally(e);
        }
    }
}
