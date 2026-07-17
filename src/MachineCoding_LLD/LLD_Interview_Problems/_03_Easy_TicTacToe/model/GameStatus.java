package MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe.model;

/**
 * The result of the game after a move. Deliberately a plain enum, not a State-pattern
 * hierarchy: unlike the vending machine (where each mode has genuinely different behavior for
 * the same action), a tic-tac-toe game only ever moves IN_PROGRESS → {WIN | DRAW} and then
 * accepts no more moves. Modeling that with State classes would be ceremony, so we don't —
 * "right pattern for the right reason". See {@code SOLUTION.md} §4.
 */
public enum GameStatus {
    IN_PROGRESS,
    WIN,
    DRAW;

    public boolean isTerminal() {
        return this != IN_PROGRESS;
    }
}
