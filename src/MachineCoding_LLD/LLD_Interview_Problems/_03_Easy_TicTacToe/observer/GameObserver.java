package MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.observer;

import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.Game;
import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.model.Move;
import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.model.Player;

/**
 * The Observer seam: the {@code Game} publishes state changes without knowing who's listening, so
 * a console UI, a GUI, a move log, or a network broadcaster all attach the same way and the core
 * engine stays UI-free. (Pattern reference: {@code DesignPatterns/_11_ObserverDesignPattern}.)
 */
public interface GameObserver {

    /** A valid move was just applied. */
    void onMove(Game game, Player player, Move move);

    /** The game ended. {@code winner} is null for a draw. */
    void onGameOver(Game game, Player winner);
}
