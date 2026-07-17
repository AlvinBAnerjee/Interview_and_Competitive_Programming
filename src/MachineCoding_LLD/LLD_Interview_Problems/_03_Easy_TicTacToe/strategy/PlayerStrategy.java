package MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.strategy;

import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.model.Board;
import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.model.Move;
import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.model.Symbol;

/**
 * How a player picks their next move — the seam that makes "human vs human", "human vs bot" and
 * "bot vs bot" the same code with different objects. A {@code Player} is just a name + symbol +
 * a {@code PlayerStrategy}; swapping difficulty or a human for an AI is a one-line change.
 *
 * <p>Implementations must return a move on a currently-empty, in-bounds cell; {@code Game} still
 * validates and will reject anything illegal, so a buggy strategy fails loudly rather than
 * corrupting the board.
 */
public interface PlayerStrategy {

    /**
     * @param board  read-only view of the current position
     * @param symbol the mark this player is placing
     * @return the chosen move
     */
    Move chooseMove(Board board, Symbol symbol);
}
