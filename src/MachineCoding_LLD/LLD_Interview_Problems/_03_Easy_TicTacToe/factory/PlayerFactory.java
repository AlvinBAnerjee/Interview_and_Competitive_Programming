package MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.factory;

import java.util.ArrayDeque;
import java.util.Queue;

import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.model.Move;
import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.model.Player;
import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.model.Symbol;
import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.strategy.HumanPlayerStrategy;
import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.strategy.RandomBotStrategy;
import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.strategy.RuleBasedBotStrategy;

/**
 * Assembles a {@link Player} with the right {@link
 * MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.strategy.PlayerStrategy}, so callers
 * say <em>what kind</em> of player they want ({@code smartBot("AI", O)}) without wiring the
 * strategy by hand. Adding a new player type (e.g. a minimax bot) means one new method here and
 * one new strategy class — nothing else changes.
 */
public final class PlayerFactory {

    private PlayerFactory() {
    }

    /** A human whose moves are supplied up front (deterministic demos/tests). */
    public static Player human(String name, Symbol symbol, Queue<Move> scriptedMoves) {
        return new Player(name, symbol, new HumanPlayerStrategy(scriptedMoves));
    }

    /** Convenience overload: build the scripted queue from an even-length {r,c,r,c,...} list. */
    public static Player human(String name, Symbol symbol, int... rowColPairs) {
        if (rowColPairs.length % 2 != 0) {
            throw new IllegalArgumentException("expected row,col pairs (even count)");
        }
        Queue<Move> moves = new ArrayDeque<>();
        for (int i = 0; i < rowColPairs.length; i += 2) {
            moves.add(new Move(rowColPairs[i], rowColPairs[i + 1]));
        }
        return human(name, symbol, moves);
    }

    /** Picks a uniformly random empty cell. */
    public static Player randomBot(String name, Symbol symbol, long seed) {
        return new Player(name, symbol, new RandomBotStrategy(seed));
    }

    /** Win-if-you-can, else block, else play position. */
    public static Player smartBot(String name, Symbol symbol, long seed) {
        return new Player(name, symbol, new RuleBasedBotStrategy(seed));
    }
}
