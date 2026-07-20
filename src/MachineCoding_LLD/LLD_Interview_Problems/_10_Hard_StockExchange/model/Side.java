package MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.model;

/** Order side. */
public enum Side {
    BUY,
    SELL;

    public Side opposite() { return this == BUY ? SELL : BUY; }
}
