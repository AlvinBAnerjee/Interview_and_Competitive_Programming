package MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.observer;

import MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.model.Trade;

/**
 * Subscriber to the trade feed (Observer). Notified off the matcher thread by a
 * separate publisher, so a slow listener never stalls matching.
 */
public interface TradeListener {
    void onTrade(Trade trade);
}
