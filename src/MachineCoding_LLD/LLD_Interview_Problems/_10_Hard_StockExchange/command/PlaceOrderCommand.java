package MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.command;

import MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.engine.MatchingEngine;
import MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.model.Order;
import MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.model.Trade;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Place an order. Carries a promise the matcher completes with the resulting
 * trades, so the client's {@code placeOrder} can present a synchronous API on
 * top of the async single-writer core (it just awaits this future).
 */
public class PlaceOrderCommand implements EngineCommand {
    private final Order order;
    private final CompletableFuture<List<Trade>> result;

    public PlaceOrderCommand(Order order, CompletableFuture<List<Trade>> result) {
        this.order = order;
        this.result = result;
    }

    @Override
    public void execute(MatchingEngine engine) {
        try {
            result.complete(engine.process(order));
        } catch (RuntimeException e) {
            result.completeExceptionally(e);
        }
    }
}
