package MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.strategy;

import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.model.Board;
import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.model.Symbol;

/**
 * The O(1)-per-move win check for the classic case <b>K == N</b> (a win must fill an entire row,
 * column or main diagonal — standard 3×3). This is the LeetCode-348 trick.
 *
 * <p>Encode X as +1 and O as −1 and keep a signed sum per line:
 * <ul>
 *   <li>{@code rowSum[r]}, {@code colSum[c]} — one per row / column</li>
 *   <li>{@code diagSum} (r == c), {@code antiDiagSum} (r + c == n − 1)</li>
 * </ul>
 * A line is full of one symbol exactly when its sum reaches ±N. Each move touches at most four
 * counters, so the check is O(1) — no scanning at all.
 *
 * <h3>Constraints</h3>
 * <ul>
 *   <li><b>Stateful & single-owner:</b> the counters mirror one board's history, so a
 *       {@code RunningCounterStrategy} instance belongs to exactly one {@code Board}. (The scan
 *       strategy is stateless and shareable; this one is the price of O(1).)</li>
 *   <li><b>K must equal N:</b> it only detects full-length lines. For K &lt; N use
 *       {@link LastMoveScanStrategy}. The constructor enforces this.</li>
 *   <li><b>Exactly-once contract:</b> relies on {@code Board} calling it once per placed mark.</li>
 * </ul>
 */
public final class RunningCounterStrategy implements WinningStrategy {

    private final int n;
    private final int[] rowSum;
    private final int[] colSum;
    private int diagSum;
    private int antiDiagSum;

    public RunningCounterStrategy(int n) {
        this.n = n;
        this.rowSum = new int[n];
        this.colSum = new int[n];
    }

    @Override
    public boolean isWinningMove(Board board, int row, int col, Symbol symbol) {
        if (board.k() != n) {
            throw new IllegalStateException(
                    "RunningCounterStrategy requires K == N (got K=" + board.k() + ", N=" + n
                            + "); use LastMoveScanStrategy for K < N");
        }
        int delta = symbol == Symbol.X ? 1 : -1;

        rowSum[row] += delta;
        colSum[col] += delta;
        if (row == col) {
            diagSum += delta;
        }
        if (row + col == n - 1) {
            antiDiagSum += delta;
        }

        return Math.abs(rowSum[row]) == n
                || Math.abs(colSum[col]) == n
                || Math.abs(diagSum) == n
                || Math.abs(antiDiagSum) == n;
    }
}
