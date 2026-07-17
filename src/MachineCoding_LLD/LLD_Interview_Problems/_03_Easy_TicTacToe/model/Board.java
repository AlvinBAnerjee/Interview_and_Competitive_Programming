package MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.model;

import java.util.ArrayList;
import java.util.List;

import MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.strategy.WinningStrategy;

/**
 * An N×N grid where K-in-a-row wins. The board owns the cells and the {@link WinningStrategy};
 * it exposes just enough for callers ({@code Game}, bots) to read state and place a mark.
 *
 * <p><b>Win detection lives behind the strategy.</b> {@link #placeAndCheckWin} is the single
 * mutating entry point, and it calls the strategy exactly once per placed mark — the contract the
 * O(1) counter strategy depends on. The board never rescans itself.
 *
 * <p>Not thread-safe by design: a single game is turn-based and confined to one thread. Where
 * concurrency actually matters (many games at once) it's handled one level up by the actor-style
 * {@code GameServer}, not by locking every cell. See {@code SOLUTION.md} §5.
 */
public final class Board {

    private final int size;                 // N
    private final int k;                    // K-in-a-row to win
    private final Symbol[][] grid;
    private final WinningStrategy winningStrategy;
    private int filled;

    public Board(int size, int k, WinningStrategy winningStrategy) {
        if (size <= 0) {
            throw new IllegalArgumentException("board size must be positive");
        }
        if (k <= 0 || k > size) {
            throw new IllegalArgumentException("k must be in 1.." + size + " (got " + k + ")");
        }
        this.size = size;
        this.k = k;
        this.winningStrategy = winningStrategy;
        this.grid = new Symbol[size][size];
        for (Symbol[] row : grid) {
            java.util.Arrays.fill(row, Symbol.EMPTY);
        }
    }

    public int size() {
        return size;
    }

    public int k() {
        return k;
    }

    public Symbol at(int row, int col) {
        return grid[row][col];
    }

    public boolean inBounds(int row, int col) {
        return row >= 0 && row < size && col >= 0 && col < size;
    }

    public boolean isEmpty(int row, int col) {
        return grid[row][col] == Symbol.EMPTY;
    }

    public boolean isFull() {
        return filled == size * size;
    }

    /**
     * Place {@code symbol} at (row, col) and report whether it completes a K-in-a-row. Caller
     * ({@code Game}) is responsible for validating turn/bounds/occupancy first; this method
     * asserts the cell is free as a last-line guard against a strategy-desyncing double-place.
     */
    public boolean placeAndCheckWin(int row, int col, Symbol symbol) {
        if (!inBounds(row, col)) {
            throw new IllegalArgumentException("out of bounds: (" + row + "," + col + ")");
        }
        if (grid[row][col] != Symbol.EMPTY) {
            throw new IllegalStateException("cell already occupied: (" + row + "," + col + ")");
        }
        grid[row][col] = symbol;
        filled++;
        return winningStrategy.isWinningMove(this, row, col, symbol);
    }

    /** Every free cell, row-major. Used by bots to enumerate candidate moves. */
    public List<Move> emptyCells() {
        List<Move> cells = new ArrayList<>();
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (grid[r][c] == Symbol.EMPTY) {
                    cells.add(new Move(r, c));
                }
            }
        }
        return cells;
    }

    /** A human-readable grid, e.g. for the console observer. */
    public String render() {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                sb.append(' ').append(cellChar(grid[r][c])).append(' ');
                if (c < size - 1) {
                    sb.append('|');
                }
            }
            sb.append('\n');
            if (r < size - 1) {
                sb.append("---+".repeat(size - 1)).append("---\n");
            }
        }
        return sb.toString();
    }

    private static char cellChar(Symbol s) {
        return switch (s) {
            case X -> 'X';
            case O -> 'O';
            case EMPTY -> '.';
        };
    }
}
