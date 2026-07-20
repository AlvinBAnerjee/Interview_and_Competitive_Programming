package MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.observer;

import MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.model.Trade;

import java.util.concurrent.atomic.AtomicLong;

/** Prints trades and counts total executed volume — stands in for a market-data feed. */
public class LoggingTradeListener implements TradeListener {
    private final AtomicLong tradeCount = new AtomicLong();
    private final AtomicLong volume = new AtomicLong();

    @Override
    public void onTrade(Trade trade) {
        tradeCount.incrementAndGet();
        volume.addAndGet(trade.qty());
        System.out.println("  " + trade);
    }

    public long tradeCount() { return tradeCount.get(); }
    public long volume() { return volume.get(); }
}
