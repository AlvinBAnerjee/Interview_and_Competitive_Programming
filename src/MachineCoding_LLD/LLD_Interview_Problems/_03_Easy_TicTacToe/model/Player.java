package MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.model;

import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.strategy.PlayerStrategy;

/**
 * A participant: a name, the {@link Symbol} they place, and a {@link PlayerStrategy} that decides
 * where. The strategy is the only thing that differs between a human and a bot, so {@code Game}
 * treats every player identically — it just asks {@link #nextMove}.
 */
public final class Player {

    private final String name;
    private final Symbol symbol;
    private final PlayerStrategy strategy;

    public Player(String name, Symbol symbol, PlayerStrategy strategy) {
        if (!symbol.isMark()) {
            throw new IllegalArgumentException("a player must be X or O, not EMPTY");
        }
        this.name = name;
        this.symbol = symbol;
        this.strategy = strategy;
    }

    public String name() {
        return name;
    }

    public Symbol symbol() {
        return symbol;
    }

    /** Delegate the pick to the strategy — the whole point of the Strategy seam. */
    public Move nextMove(Board board) {
        return strategy.chooseMove(board, symbol);
    }

    @Override
    public String toString() {
        return name + "(" + symbol + ")";
    }
}
