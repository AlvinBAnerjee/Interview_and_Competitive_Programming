package MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.model;

/**
 * LIMIT rests in the book at its price if it can't fully match; MARKET takes
 * whatever price is available and never rests (its unfilled remainder is dropped).
 */
public enum OrderType {
    LIMIT,
    MARKET
}
