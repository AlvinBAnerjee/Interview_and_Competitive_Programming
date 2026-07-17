package MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.factory.PlayerFactory;
import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.model.Board;
import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.model.GameStatus;
import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.model.Move;
import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.model.Player;
import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.model.Symbol;
import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.server.GameServer;
import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.strategy.LastMoveScanStrategy;
import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.strategy.RuleBasedBotStrategy;
import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.strategy.RunningCounterStrategy;
import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.strategy.WinningStrategy;

/**
 * Dependency-free harness (plain {@code main}, no JUnit — matching this repo). Each check prints
 * PASS/FAIL; the process exits non-zero if anything fails so {@code &&} chains / CI catch it.
 *
 * Covers: win detection on every axis (both strategies), draw, all four illegal-move rejections,
 * counter-vs-scan agreement over 500 random games, K&lt;N non-line wins, the smart bot's
 * win/block priority, and a concurrency test driving many games through the actor-based
 * {@link GameServer} at once.
 */
public final class TicTacToeTest {

    private static int failures = 0;

    public static void main(String[] args) throws Exception {
        testRowWin();
        testColumnWin();
        testDiagonalWin();
        testAntiDiagonalWin();
        testDraw();
        testIllegalMovesRejected();
        testCounterAndScanAgreeOver500Games();
        testKInARowNonLineWin();
        testCounterStrategyRejectsKLessThanN();
        testSmartBotTakesWinOverBlock();
        testSmartBotBlocks();
        testConcurrentGamesOnServer();

        System.out.println();
        if (failures == 0) {
            System.out.println("ALL TESTS PASSED ✅");
        } else {
            System.out.println(failures + " TEST(S) FAILED ❌");
            System.exit(1);
        }
    }

    // ---- win detection on each axis (run against BOTH strategies) ----

    private static void testRowWin() {
        // X: (0,0)(0,1)(0,2) top row; O: (1,0)(1,1). X wins on its 3rd move.
        int[] xs = {0, 0, 0, 1, 0, 2};
        int[] os = {1, 0, 1, 1};
        for (WinningStrategy ws : strategies3()) {
            Outcome out = playScripted(3, 3, ws, xs, os);
            check("row win (" + name(ws) + ")",
                    out.status == GameStatus.WIN && out.winner == Symbol.X, out.toString());
        }
    }

    private static void testColumnWin() {
        int[] xs = {0, 0, 1, 0, 2, 0}; // column 0
        int[] os = {0, 1, 1, 1};
        for (WinningStrategy ws : strategies3()) {
            Outcome out = playScripted(3, 3, ws, xs, os);
            check("column win (" + name(ws) + ")",
                    out.status == GameStatus.WIN && out.winner == Symbol.X, out.toString());
        }
    }

    private static void testDiagonalWin() {
        int[] xs = {0, 0, 1, 1, 2, 2}; // main diagonal
        int[] os = {0, 1, 0, 2};
        for (WinningStrategy ws : strategies3()) {
            Outcome out = playScripted(3, 3, ws, xs, os);
            check("diagonal win (" + name(ws) + ")",
                    out.status == GameStatus.WIN && out.winner == Symbol.X, out.toString());
        }
    }

    private static void testAntiDiagonalWin() {
        int[] xs = {0, 2, 1, 1, 2, 0}; // anti-diagonal
        int[] os = {0, 0, 0, 1};
        for (WinningStrategy ws : strategies3()) {
            Outcome out = playScripted(3, 3, ws, xs, os);
            check("anti-diagonal win (" + name(ws) + ")",
                    out.status == GameStatus.WIN && out.winner == Symbol.X, out.toString());
        }
    }

    private static void testDraw() {
        // Cat's game:  X O X / X O O / O X X
        int[] xs = {0, 0, 0, 2, 2, 1, 1, 0, 2, 2};
        int[] os = {1, 1, 0, 1, 1, 2, 2, 0};
        for (WinningStrategy ws : strategies3()) {
            Outcome out = playScripted(3, 3, ws, xs, os);
            check("draw (" + name(ws) + ")", out.status == GameStatus.DRAW, out.toString());
        }
    }

    // ---- illegal moves ----

    private static void testIllegalMovesRejected() {
        Player x = dummy("X", Symbol.X);
        Player o = dummy("O", Symbol.O);

        // out of turn: O moves first
        check("out-of-turn rejected", throwsInvalid(() -> {
            Game g = new Game(new Board(3, 3, new RunningCounterStrategy(3)), x, o);
            g.makeMove(o, 0, 0);
        }), "no exception");

        // out of bounds
        check("out-of-bounds rejected", throwsInvalid(() -> {
            Game g = new Game(new Board(3, 3, new RunningCounterStrategy(3)), x, o);
            g.makeMove(x, 3, 3);
        }), "no exception");

        // occupied cell
        check("occupied-cell rejected", throwsInvalid(() -> {
            Game g = new Game(new Board(3, 3, new RunningCounterStrategy(3)), x, o);
            g.makeMove(x, 1, 1);
            g.makeMove(o, 1, 1);
        }), "no exception");

        // move after game over
        check("move-after-over rejected", throwsInvalid(() -> {
            Game g = new Game(new Board(3, 3, new RunningCounterStrategy(3)), x, o);
            g.makeMove(x, 0, 0);
            g.makeMove(o, 1, 0);
            g.makeMove(x, 0, 1);
            g.makeMove(o, 1, 1);
            g.makeMove(x, 0, 2); // X wins
            g.makeMove(o, 2, 2); // must be rejected
        }), "no exception");
    }

    /**
     * The O(1) counter and the O(K) scan must declare the same result on every position. Play 500
     * random self-play games, record the exact moves, then replay them through each strategy and
     * assert identical (status, winner).
     */
    private static void testCounterAndScanAgreeOver500Games() {
        int disagreements = 0;
        for (long seed = 0; seed < 500; seed++) {
            List<Move> moves = randomPlayout(3, 3, seed);
            Outcome viaCounter = replay(3, 3, new RunningCounterStrategy(3), moves);
            Outcome viaScan = replay(3, 3, new LastMoveScanStrategy(), moves);
            if (viaCounter.status != viaScan.status || viaCounter.winner != viaScan.winner) {
                disagreements++;
            }
        }
        check("counter and scan agree over 500 games", disagreements == 0,
                disagreements + " disagreements");
    }

    /** 5×5, K=4: a diagonal 4-run is a win that is NOT a full line — only the scan can see it. */
    private static void testKInARowNonLineWin() {
        int[] xs = {1, 1, 2, 2, 3, 3, 4, 4}; // diagonal of length 4
        int[] os = {0, 0, 0, 1, 0, 2, 0, 4};
        Outcome out = playScripted(5, 4, new LastMoveScanStrategy(), xs, os);
        check("5×5 four-in-a-row diagonal win",
                out.status == GameStatus.WIN && out.winner == Symbol.X, out.toString());
    }

    /** The counter strategy must refuse a board where K != N rather than silently miss wins. */
    private static void testCounterStrategyRejectsKLessThanN() {
        check("counter strategy rejects K<N", throwsState(() -> {
            Board board = new Board(5, 4, new RunningCounterStrategy(5));
            Game g = new Game(board, dummy("X", Symbol.X), dummy("O", Symbol.O));
            g.makeMove(g.currentPlayer(), 0, 0);
        }), "no exception");
    }

    /** With both a win and a block available, the smart bot takes the win. */
    private static void testSmartBotTakesWinOverBlock() {
        Board board = new Board(3, 3, new LastMoveScanStrategy());
        board.placeAndCheckWin(0, 0, Symbol.O); // O threatens (0,2) to win
        board.placeAndCheckWin(0, 1, Symbol.O);
        board.placeAndCheckWin(1, 0, Symbol.X); // X threatens (1,2)
        board.placeAndCheckWin(1, 1, Symbol.X);
        Move m = new RuleBasedBotStrategy(1L).chooseMove(board, Symbol.O);
        check("smart bot takes its win over blocking", m.equals(new Move(0, 2)), m.toString());
    }

    /** No win available, but the opponent threatens — the smart bot blocks it. */
    private static void testSmartBotBlocks() {
        Board board = new Board(3, 3, new LastMoveScanStrategy());
        board.placeAndCheckWin(0, 0, Symbol.X); // X threatens (0,2)
        board.placeAndCheckWin(0, 1, Symbol.X);
        board.placeAndCheckWin(1, 1, Symbol.O);
        Move m = new RuleBasedBotStrategy(1L).chooseMove(board, Symbol.O);
        check("smart bot blocks the opponent's win", m.equals(new Move(0, 2)), m.toString());
    }

    /**
     * Concurrency: host many games on the {@link GameServer} and drive them all at once. Each game
     * is confined to its own thread, so the concurrent result must equal the single-threaded
     * reference for every game — proving the actor routing keeps each board consistent.
     */
    private static void testConcurrentGamesOnServer() throws InterruptedException {
        int games = 300;

        // Reference: single-threaded playouts.
        List<List<Move>> scripts = new ArrayList<>();
        List<Outcome> reference = new ArrayList<>();
        for (int i = 0; i < games; i++) {
            List<Move> moves = randomPlayout(3, 3, i);
            scripts.add(moves);
            reference.add(replay(3, 3, new RunningCounterStrategy(3), moves));
        }

        AtomicInteger mismatches = new AtomicInteger();
        AtomicInteger errors = new AtomicInteger();
        ConcurrentHashMap<String, Boolean> completed = new ConcurrentHashMap<>();

        try (GameServer server = new GameServer()) {
            List<Player> xs = new ArrayList<>();
            List<Player> os = new ArrayList<>();
            for (int i = 0; i < games; i++) {
                Player x = dummy("X", Symbol.X);
                Player o = dummy("O", Symbol.O);
                xs.add(x);
                os.add(o);
                server.host("g-" + i, new Game(new Board(3, 3, new RunningCounterStrategy(3)), x, o));
            }

            ExecutorService clients = Executors.newFixedThreadPool(32);
            for (int i = 0; i < games; i++) {
                final int idx = i;
                clients.submit(() -> {
                    try {
                        List<Move> moves = scripts.get(idx);
                        GameStatus status = GameStatus.IN_PROGRESS;
                        for (int m = 0; m < moves.size(); m++) {
                            Player mover = (m % 2 == 0) ? xs.get(idx) : os.get(idx);
                            Move move = moves.get(m);
                            CompletableFuture<GameStatus> f =
                                    server.submitMove("g-" + idx, mover, move.row(), move.col());
                            status = f.get(); // await this game's actor thread
                            if (status.isTerminal()) {
                                break;
                            }
                        }
                        Outcome ref = reference.get(idx);
                        if (status != ref.status) {
                            mismatches.incrementAndGet();
                        }
                        completed.put("g-" + idx, true);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        errors.incrementAndGet();
                    } catch (ExecutionException e) {
                        errors.incrementAndGet();
                    }
                });
            }
            clients.shutdown();
            clients.awaitTermination(30, TimeUnit.SECONDS);
        }

        check("all concurrent games completed", completed.size() == games,
                completed.size() + "/" + games);
        check("no errors driving concurrent games", errors.get() == 0, errors.get() + " errors");
        check("concurrent results match single-threaded reference", mismatches.get() == 0,
                mismatches.get() + " mismatches");
    }

    // ================= helpers =================

    /** A tiny immutable result bundle. */
    private static final class Outcome {
        final GameStatus status;
        final Symbol winner; // null unless WIN

        Outcome(GameStatus status, Symbol winner) {
            this.status = status;
            this.winner = winner;
        }

        @Override
        public String toString() {
            return status + (winner != null ? " by " + winner : "");
        }
    }

    private static WinningStrategy[] strategies3() {
        return new WinningStrategy[] {new RunningCounterStrategy(3), new LastMoveScanStrategy()};
    }

    private static String name(WinningStrategy ws) {
        return ws.getClass().getSimpleName();
    }

    /** Play scripted X/O moves (interleaved X first) to completion or exhaustion. */
    private static Outcome playScripted(int n, int k, WinningStrategy ws, int[] xs, int[] os) {
        Player x = PlayerFactory.human("X", Symbol.X, xs);
        Player o = PlayerFactory.human("O", Symbol.O, os);
        Game g = new Game(new Board(n, k, ws), x, o);
        while (!g.status().isTerminal()) {
            g.playTurn();
        }
        return new Outcome(g.status(), g.winner() == null ? null : g.winner().symbol());
    }

    /** Random self-play; returns the exact sequence of moves that were made. */
    private static List<Move> randomPlayout(int n, int k, long seed) {
        Player x = PlayerFactory.randomBot("X", Symbol.X, seed);
        Player o = PlayerFactory.randomBot("O", Symbol.O, seed ^ 0x9E3779B9L);
        Game g = new Game(new Board(n, k, new LastMoveScanStrategy()), x, o);
        List<Move> moves = new ArrayList<>();
        while (!g.status().isTerminal()) {
            Player p = g.currentPlayer();
            Move m = p.nextMove(g.board());
            moves.add(m);
            g.makeMove(p, m.row(), m.col());
        }
        return moves;
    }

    /** Replay a fixed move list (X first, alternating) on a fresh board with the given strategy. */
    private static Outcome replay(int n, int k, WinningStrategy ws, List<Move> moves) {
        Player x = dummy("X", Symbol.X);
        Player o = dummy("O", Symbol.O);
        Game g = new Game(new Board(n, k, ws), x, o);
        for (Move m : moves) {
            if (g.status().isTerminal()) {
                break;
            }
            g.makeMove(g.currentPlayer(), m.row(), m.col());
        }
        return new Outcome(g.status(), g.winner() == null ? null : g.winner().symbol());
    }

    /** A player whose strategy is never consulted (we always pass explicit coordinates). */
    private static Player dummy(String name, Symbol s) {
        return new Player(name, s, (board, symbol) -> {
            throw new IllegalStateException("dummy strategy should not be called");
        });
    }

    private static boolean throwsInvalid(Runnable r) {
        try {
            r.run();
            return false;
        } catch (InvalidMoveException expected) {
            return true;
        }
    }

    private static boolean throwsState(Runnable r) {
        try {
            r.run();
            return false;
        } catch (IllegalStateException expected) {
            return true;
        }
    }

    private static void check(String name, boolean ok, String actual) {
        System.out.printf("[%s] %s%s%n", ok ? "PASS" : "FAIL", name, ok ? "" : "  -> got: " + actual);
        if (!ok) {
            failures++;
        }
    }
}
