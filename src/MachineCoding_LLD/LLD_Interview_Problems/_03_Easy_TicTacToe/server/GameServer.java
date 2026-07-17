package MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.server;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.Game;
import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.model.GameStatus;
import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.model.Player;

/**
 * Where concurrency actually belongs in tic-tac-toe: hosting <b>many independent games at once</b>,
 * not parallelising a single (turn-based) match.
 *
 * <h3>Design: shard by game id, one actor per game</h3>
 * A single {@link Game} is not thread-safe — and making it so would mean locking every move on a
 * board that only one player touches at a time. Instead we <b>confine</b> each game to its own
 * single-thread executor ({@link GameSession}). Moves for a game are routed to that game's thread,
 * so a game's state is only ever mutated by one thread — the actor model. No locks, no shared
 * mutable board across threads, yet thousands of games run truly in parallel across the pool of
 * per-game threads.
 *
 * <ul>
 *   <li>The {@link ConcurrentMap} of sessions is the only cross-thread shared state, and it's
 *       just a lookup table (concurrent by construction).</li>
 *   <li>{@link #submitMove} is non-blocking: it hands the move to the owning game's queue and
 *       returns a {@link CompletableFuture} the caller can await or chain.</li>
 *   <li>Illegal moves surface as a failed future (the {@code InvalidMoveException} from
 *       {@code Game}), never as a corrupted board.</li>
 * </ul>
 *
 * This is the "make each game single-threaded via an actor/queue" answer the problem hints at —
 * the clean way to scale without turning the engine into a lock salad.
 */
public final class GameServer implements AutoCloseable {

    private final ConcurrentMap<String, GameSession> sessions = new ConcurrentHashMap<>();

    /** Register a game under an id. Rejects a duplicate id. */
    public void host(String gameId, Game game) {
        GameSession existing = sessions.putIfAbsent(gameId, new GameSession(gameId, game));
        if (existing != null) {
            throw new IllegalArgumentException("game id already hosted: " + gameId);
        }
    }

    /**
     * Route a move onto its game's dedicated thread. The returned future completes with the new
     * {@link GameStatus}, or completes exceptionally if the move was illegal.
     */
    public CompletableFuture<GameStatus> submitMove(String gameId, Player player, int row, int col) {
        GameSession session = sessions.get(gameId);
        if (session == null) {
            throw new IllegalArgumentException("no such game: " + gameId);
        }
        return session.submitMove(player, row, col);
    }

    public GameStatus statusOf(String gameId) {
        GameSession session = sessions.get(gameId);
        if (session == null) {
            throw new IllegalArgumentException("no such game: " + gameId);
        }
        return session.game().status();
    }

    public int hostedCount() {
        return sessions.size();
    }

    /** Shut down every game's thread. Idempotent. */
    @Override
    public void close() {
        sessions.values().forEach(GameSession::shutdown);
        sessions.clear();
    }
}
