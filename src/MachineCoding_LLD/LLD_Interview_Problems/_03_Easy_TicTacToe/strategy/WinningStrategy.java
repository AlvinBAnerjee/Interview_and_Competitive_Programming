package MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.strategy;

import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.model.Board;
import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.model.Symbol;

/**
 * How the board decides a move just won. Pulled out as a Strategy because "did that complete a
 * line?" has two very different good answers depending on the rules:
 *
 * <ul>
 *   <li>{@link RunningCounterStrategy} — O(1) per move via signed row/col/diagonal sums, but
 *       only correct when K == N (a win must span the whole board — classic 3×3).</li>
 *   <li>{@link LastMoveScanStrategy} — O(K) per move by scanning outward from the placed cell,
 *       correct for <em>any</em> K ≤ N (Gomoku-style K-in-a-row on a big board).</li>
 * </ul>
 *
 * Either way we never do the naïve O(N²) full-board rescan. The contract: {@code isWinningMove}
 * is called <b>exactly once, immediately after</b> the mark is placed on the board — which is
 * what lets the counter implementation keep incremental state in sync.
 */
public interface WinningStrategy {

    /**
     * @param board  the board <em>with the mark already placed</em>
     * @param row    row of the mark just placed
     * @param col    column of the mark just placed
     * @param symbol the mark just placed
     * @return true iff that mark completes a K-in-a-row
     */
    boolean isWinningMove(Board board, int row, int col, Symbol symbol);
}
