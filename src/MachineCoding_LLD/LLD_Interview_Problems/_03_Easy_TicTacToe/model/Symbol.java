package MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.model;

/**
 * A mark on the board. {@link #EMPTY} is the "no mark yet" sentinel so the grid never holds
 * {@code null} — every cell always answers {@code at(r,c)} with a real value.
 *
 * <p>Only two players are supported ({@link #X} / {@link #O}), which is what lets the O(1)
 * running-counter win check encode a line as a single signed sum (see
 * {@code RunningCounterStrategy}).
 */
public enum Symbol {
    X, O, EMPTY;

    public boolean isMark() {
        return this != EMPTY;
    }

    /** The other player's mark. Only valid for {@link #X}/{@link #O}. */
    public Symbol opponent() {
        return switch (this) {
            case X -> O;
            case O -> X;
            case EMPTY -> throw new IllegalStateException("EMPTY has no opponent");
        };
    }
}
