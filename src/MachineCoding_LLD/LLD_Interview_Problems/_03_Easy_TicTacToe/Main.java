package MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe;

import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.factory.PlayerFactory;
import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.model.Board;
import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.model.GameStatus;
import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.model.Player;
import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.model.Symbol;
import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.observer.ConsoleGameObserver;
import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.strategy.LastMoveScanStrategy;
import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.strategy.RunningCounterStrategy;

/**
 * A scripted, non-interactive walkthrough (like the other problems' {@code Main}s). Three demos:
 * the O(1) counter win-check on classic 3×3, a smart bot vs smart bot game, and generic
 * K-in-a-row on a bigger board — all narrated through the Observer-driven console UI. For the
 * correctness + concurrency proof, see {@code TicTacToeTest}.
 */
public final class Main {

    public static void main(String[] args) {
        classic3x3_humanWinsTopRow();
        separator();
        smartBotVsSmartBot();
        separator();
        generic5x5_fourInARow();
    }

    /** Classic 3×3 (K == N) → O(1) {@link RunningCounterStrategy}. Two scripted humans; X wins. */
    private static void classic3x3_humanWinsTopRow() {
        System.out.println("=== 3×3, K=3, O(1) running-counter win check ===\n");

        Board board = new Board(3, 3, new RunningCounterStrategy(3));
        Player alice = PlayerFactory.human("Alice", Symbol.X, 0, 0, 0, 1, 0, 2); // top row
        Player bob = PlayerFactory.human("Bob", Symbol.O, 1, 0, 1, 1);

        play(board, alice, bob);
    }

    /** Same 3×3 board, but two heuristic bots play themselves — shows the Strategy seam + AI. */
    private static void smartBotVsSmartBot() {
        System.out.println("=== 3×3, K=3, smart bot vs smart bot (win/block/position) ===\n");

        Board board = new Board(3, 3, new RunningCounterStrategy(3));
        Player x = PlayerFactory.smartBot("BotX", Symbol.X, 1L);
        Player o = PlayerFactory.smartBot("BotO", Symbol.O, 2L);

        play(board, x, o);
    }

    /** Generic 5×5 where 4-in-a-row wins (K < N) → O(K) {@link LastMoveScanStrategy}. */
    private static void generic5x5_fourInARow() {
        System.out.println("=== 5×5, K=4, O(K) directional-scan win check ===\n");

        Board board = new Board(5, 4, new LastMoveScanStrategy());
        // X builds a diagonal 4-run (1,1)(2,2)(3,3)(4,4) — a win that is NOT a full line, which
        // is exactly why K<N needs the scan and can't use the counter trick.
        Player x = PlayerFactory.human("X", Symbol.X, 1, 1, 2, 2, 3, 3, 4, 4);
        Player o = PlayerFactory.human("O", Symbol.O, 0, 0, 0, 1, 0, 2, 0, 4);

        play(board, x, o);
    }

    private static void play(Board board, Player first, Player second) {
        Game game = new Game(board, first, second);
        game.addObserver(new ConsoleGameObserver());
        GameStatus result = game.play();
        System.out.println("Final status: " + result
                + (game.winner() != null ? " by " + game.winner() : ""));
        System.out.println();
    }

    private static void separator() {
        System.out.println("======================================================\n");
    }
}
