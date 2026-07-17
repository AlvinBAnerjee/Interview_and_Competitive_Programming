package MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.strategy;

import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.model.Board;
import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.model.Symbol;

/**
 * The general O(K) win check: a completed line must pass through the cell that was just played,
 * so we only look outward from that one cell along the four axes — horizontal, vertical, and the
 * two diagonals — counting how far the same symbol runs each way.
 *
 * <pre>
 *   run = 1 (the placed cell) + steps forward + steps backward   // along one axis
 *   win if run >= K for any axis
 * </pre>
 *
 * Correct for <b>any</b> K ≤ N (unlike the counter trick, which needs K == N), stateless, and
 * therefore shareable across boards/games. Cost is O(K) per move — we walk at most K-1 cells in
 * each of 8 directions before the run breaks.
 *
 * <p>The core {@link #completesLine} is {@code static} and hypothesis-free — it counts neighbours
 * of {@code symbol} without reading the origin cell — so the {@code RuleBasedBotStrategy} reuses
 * it to answer "would playing here win?" without mutating the board.
 */
public final class LastMoveScanStrategy implements WinningStrategy {

    /** The four axes as (dRow, dCol): →, ↓, ↘, ↗. Each is scanned in both directions. */
    private static final int[][] AXES = {{0, 1}, {1, 0}, {1, 1}, {1, -1}};

    @Override
    public boolean isWinningMove(Board board, int row, int col, Symbol symbol) {
        return completesLine(board, row, col, symbol);
    }

    /**
     * Would a {@code symbol} at (row, col) sit in a run of at least K? Only neighbours are read,
     * so this is safe to call on an <em>empty</em> target cell (bot look-ahead) or a just-filled
     * one (the live win check).
     */
    public static boolean completesLine(Board board, int row, int col, Symbol symbol) {
        int k = board.k();
        for (int[] axis : AXES) {
            int run = 1
                    + countRun(board, row, col, axis[0], axis[1], symbol)    // one way
                    + countRun(board, row, col, -axis[0], -axis[1], symbol); // and the opposite
            if (run >= k) {
                return true;
            }
        }
        return false;
    }

    /** Walk from (row,col) in step (dr,dc), counting consecutive cells equal to {@code symbol}. */
    private static int countRun(Board board, int row, int col, int dr, int dc, Symbol symbol) {
        int count = 0;
        int r = row + dr;
        int c = col + dc;
        while (board.inBounds(r, c) && board.at(r, c) == symbol) {
            count++;
            r += dr;
            c += dc;
        }
        return count;
    }
}
