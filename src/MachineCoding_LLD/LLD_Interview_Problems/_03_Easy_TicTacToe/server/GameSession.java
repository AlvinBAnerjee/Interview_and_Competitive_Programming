package MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.server;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.Game;
import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.model.GameStatus;
import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.model.Player;

/**
 * One hosted game plus the single thread that owns it — the actor. Every mutation of the wrapped
 * {@link Game} is submitted to {@link #actor}, a single-thread executor, so the game is touched by
 * exactly one thread and needs no internal locking. Because each session has its own thread,
 * different games proceed in parallel.
 *
 * <p>Threads are daemon + named ({@code ttt-game-<id>}) so they never block JVM shutdown and are
 * easy to spot in a stack dump.
 */
final class GameSession {

    private final Game game;
    private final ExecutorService actor;

    GameSession(String gameId, Game game) {
        this.game = game;
        ThreadFactory threads = runnable -> {
            Thread t = new Thread(runnable, "ttt-game-" + gameId);
            t.setDaemon(true);
            return t;
        };
        this.actor = Executors.newSingleThreadExecutor(threads);
    }

    Game game() {
        return game;
    }

    /** Serialize this move onto the game's own thread. */
    CompletableFuture<GameStatus> submitMove(Player player, int row, int col) {
        return CompletableFuture.supplyAsync(() -> game.makeMove(player, row, col), actor);
    }

    void shutdown() {
        actor.shutdownNow();
    }
}
