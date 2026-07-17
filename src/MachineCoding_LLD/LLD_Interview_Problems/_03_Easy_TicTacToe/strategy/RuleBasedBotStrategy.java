package MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.strategy;

import java.util.List;
import java.util.Random;

import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.model.Board;
import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.model.Move;
import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.model.Symbol;

/**
 * A cheap heuristic bot that plays the two moves that actually matter, then falls back to
 * position:
 *
 * <ol>
 *   <li><b>Win now</b> — if any empty cell completes our own K-in-a-row, take it.</li>
 *   <li><b>Block</b> — else if the opponent has such a cell, take it away.</li>
 *   <li><b>Position</b> — else prefer centre, then corners, then any cell.</li>
 * </ol>
 *
 * The win/block look-ahead reuses {@link LastMoveScanStrategy#completesLine} — the same O(K)
 * scan the live win check uses — so "would this move win?" is answered without mutating the
 * board. Not unbeatable (no full minimax — noted as an extension), but it never misses a forced
 * win or an immediate block, which is what makes bot-vs-bot games interesting instead of random.
 */
public final class RuleBasedBotStrategy implements PlayerStrategy {

    private final Random rng;

    public RuleBasedBotStrategy(long seed) {
        this.rng = new Random(seed);
    }

    @Override
    public Move chooseMove(Board board, Symbol me) {
        List<Move> empty = board.emptyCells();
        if (empty.isEmpty()) {
            throw new IllegalStateException("no empty cell to play — game should already be over");
        }
        Symbol opponent = me.opponent();

        // 1. Take an immediate win.
        Move winning = findDecisive(board, empty, me);
        if (winning != null) {
            return winning;
        }
        // 2. Block the opponent's immediate win.
        Move block = findDecisive(board, empty, opponent);
        if (block != null) {
            return block;
        }
        // 3. Positional fallback: centre, then a corner, then anything.
        Move centre = centre(board, empty);
        if (centre != null) {
            return centre;
        }
        Move corner = firstCorner(board, empty);
        if (corner != null) {
            return corner;
        }
        return empty.get(rng.nextInt(empty.size()));
    }

    /** The first empty cell where placing {@code symbol} would complete K-in-a-row, else null. */
    private static Move findDecisive(Board board, List<Move> empty, Symbol symbol) {
        for (Move m : empty) {
            if (LastMoveScanStrategy.completesLine(board, m.row(), m.col(), symbol)) {
                return m;
            }
        }
        return null;
    }

    private static Move centre(Board board, List<Move> empty) {
        int mid = board.size() / 2;
        for (Move m : empty) {
            if (m.row() == mid && m.col() == mid) {
                return m;
            }
        }
        return null;
    }

    private static Move firstCorner(Board board, List<Move> empty) {
        int last = board.size() - 1;
        for (Move m : empty) {
            boolean rowEdge = m.row() == 0 || m.row() == last;
            boolean colEdge = m.col() == 0 || m.col() == last;
            if (rowEdge && colEdge) {
                return m;
            }
        }
        return null;
    }
}
