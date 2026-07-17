package MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.model;

/**
 * An immutable "place a mark at (row, col)" intent. The player/symbol isn't carried here — the
 * {@code Game} already knows whose turn it is, so a move is just a coordinate. Keeping it a
 * value object makes scripted moves (for humans/tests) and bot picks interchangeable.
 */
public record Move(int row, int col) {

    @Override
    public String toString() {
        return "(" + row + "," + col + ")";
    }
}
