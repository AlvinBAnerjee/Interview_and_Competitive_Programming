package MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.model;

/**
 * An execution: {@code qty} shares changed hands at {@code price}. The trade
 * price is always the RESTING (maker) order's price — the aggressor gets price
 * improvement, which is standard exchange behaviour. {@code aggressor} records
 * which side crossed the spread.
 */
public record Trade(
        String id,
        String symbol,
        long price,
        int qty,
        String buyOrderId,
        String sellOrderId,
        Side aggressor) {

    @Override
    public String toString() {
        return String.format("Trade[%s] %d@%d  buy=%s sell=%s (aggressor %s)",
                id, qty, price, buyOrderId, sellOrderId, aggressor);
    }
}
