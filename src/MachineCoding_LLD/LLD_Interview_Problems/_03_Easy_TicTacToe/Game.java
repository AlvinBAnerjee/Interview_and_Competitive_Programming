package MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe;

import java.util.ArrayList;
import java.util.List;

import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.model.Board;
import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.model.GameStatus;
import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.model.Move;
import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.model.Player;
import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.observer.GameObserver;

/**
 * The engine / mediator. It owns the {@link Board} and the turn order, enforces the rules, and
 * publishes state changes to {@link GameObserver}s. Players are polymorphic (human or bot behind
 * a strategy), so the engine never branches on player type — it just asks for a move and applies
 * it.
 *
 * <h3>The one public write path</h3>
 * {@link #makeMove(Player, int, int)} is the whole rulebook in one place: reject illegal moves,
 * place the mark, ask the board whether it won, then transition to WIN / DRAW / next turn. Two
 * convenience drivers sit on top of it — {@link #playTurn()} (let the current player's strategy
 * choose) and {@link #play()} (run to completion) — but both funnel through {@code makeMove},
 * so there is exactly one place that mutates game state.
 *
 * <h3>Threading</h3>
 * A {@code Game} is <b>not</b> thread-safe and doesn't try to be — a match is turn-based, so
 * concurrent moves are nonsensical within one game. Hosting many games at once is the
 * {@code GameServer}'s job (one thread per game); see {@code SOLUTION.md} §5.
 */
public final class Game {

    private final Board board;
    private final List<Player> players;      // exactly two, alternating
    private final List<GameObserver> observers = new ArrayList<>();

    private int currentIndex;                // whose turn: 0 or 1
    private GameStatus status = GameStatus.IN_PROGRESS;
    private Player winner;                    // null until/unless someone wins

    /** {@code first} moves first. The two players must use different symbols. */
    public Game(Board board, Player first, Player second) {
        if (first.symbol() == second.symbol()) {
            throw new IllegalArgumentException("players must use different symbols");
        }
        this.board = board;
        this.players = List.of(first, second);
    }

    public void addObserver(GameObserver observer) {
        observers.add(observer);
    }

    public Board board() {
        return board;
    }

    public GameStatus status() {
        return status;
    }

    public Player winner() {
        return winner;
    }

    public Player currentPlayer() {
        return players.get(currentIndex);
    }

    /**
     * Apply {@code player}'s move at (row, col) and return the resulting status.
     *
     * @throws InvalidMoveException if the game is over, it isn't this player's turn, the cell is
     *                              out of bounds, or the cell is already taken.
     */
    public GameStatus makeMove(Player player, int row, int col) {
        if (status.isTerminal()) {
            throw new InvalidMoveException("game is already over: " + status);
        }
        if (player != currentPlayer()) {
            throw new InvalidMoveException("out of turn: it's " + currentPlayer().name() + "'s move");
        }
        if (!board.inBounds(row, col)) {
            throw new InvalidMoveException("out of bounds: (" + row + "," + col + ")");
        }
        if (!board.isEmpty(row, col)) {
            throw new InvalidMoveException("cell already occupied: (" + row + "," + col + ")");
        }

        boolean won = board.placeAndCheckWin(row, col, player.symbol());
        Move move = new Move(row, col);
        for (GameObserver o : observers) {
            o.onMove(this, player, move);
        }

        if (won) {
            status = GameStatus.WIN;
            winner = player;
            notifyOver(player);
        } else if (board.isFull()) {
            status = GameStatus.DRAW;
            notifyOver(null);
        } else {
            currentIndex = 1 - currentIndex;   // alternate turns
        }
        return status;
    }

    /** Ask the current player's strategy for a move and apply it. */
    public GameStatus playTurn() {
        Player player = currentPlayer();
        Move move = player.nextMove(board);
        return makeMove(player, move.row(), move.col());
    }

    /** Drive both players (via their strategies) until the game ends. */
    public GameStatus play() {
        while (!status.isTerminal()) {
            playTurn();
        }
        return status;
    }

    private void notifyOver(Player winnerOrNull) {
        for (GameObserver o : observers) {
            o.onGameOver(this, winnerOrNull);
        }
    }
}
