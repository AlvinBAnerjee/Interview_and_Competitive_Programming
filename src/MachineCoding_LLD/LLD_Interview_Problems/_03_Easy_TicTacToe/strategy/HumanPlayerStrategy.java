package MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.strategy;

import java.util.Queue;

import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.model.Board;
import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.model.Move;
import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.model.Symbol;

/**
 * A human's moves, supplied ahead of time as a queue. This keeps demos and tests deterministic
 * and non-blocking — a real interactive CLI would instead read each move from a
 * {@code Scanner}/UI callback, but the {@code Game} contract is identical either way (that's the
 * value of the {@link PlayerStrategy} seam).
 */
public final class HumanPlayerStrategy implements PlayerStrategy {

    private final Queue<Move> scriptedMoves;

    public HumanPlayerStrategy(Queue<Move> scriptedMoves) {
        this.scriptedMoves = scriptedMoves;
    }

    @Override
    public Move chooseMove(Board board, Symbol symbol) {
        Move move = scriptedMoves.poll();
        if (move == null) {
            throw new IllegalStateException("no scripted move left for the human player");
        }
        return move;
    }
}
