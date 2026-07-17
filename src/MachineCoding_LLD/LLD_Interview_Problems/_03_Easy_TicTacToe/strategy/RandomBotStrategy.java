package MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.strategy;

import java.util.List;
import java.util.Random;

import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.model.Board;
import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.model.Move;
import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.model.Symbol;

/**
 * The trivial bot: pick a uniformly-random empty cell. Seeded {@link Random} so games are
 * reproducible in tests. Useful as a baseline opponent and to stress the engine with many fast
 * self-play games.
 */
public final class RandomBotStrategy implements PlayerStrategy {

    private final Random rng;

    public RandomBotStrategy(long seed) {
        this.rng = new Random(seed);
    }

    @Override
    public Move chooseMove(Board board, Symbol symbol) {
        List<Move> empty = board.emptyCells();
        if (empty.isEmpty()) {
            throw new IllegalStateException("no empty cell to play — game should already be over");
        }
        return empty.get(rng.nextInt(empty.size()));
    }
}
