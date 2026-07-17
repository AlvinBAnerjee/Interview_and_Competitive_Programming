package MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.observer;

import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.Game;
import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.model.Move;
import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.model.Player;

/**
 * A concrete observer that prints the board after every move and announces the result. This is
 * the entire "UI" — and it lives outside the engine, so {@code Game} has zero {@code println}s.
 */
public final class ConsoleGameObserver implements GameObserver {

    @Override
    public void onMove(Game game, Player player, Move move) {
        System.out.println(player + " -> " + move);
        System.out.print(game.board().render());
        System.out.println();
    }

    @Override
    public void onGameOver(Game game, Player winner) {
        if (winner == null) {
            System.out.println("Result: DRAW");
        } else {
            System.out.println("Result: " + winner + " WINS");
        }
    }
}
