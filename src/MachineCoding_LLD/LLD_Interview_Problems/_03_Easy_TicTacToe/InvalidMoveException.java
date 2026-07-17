package MachineCoding_LLD.LLD_Interview_Problems._03_Easy_TicTacToe;

/**
 * Thrown when a move breaks the rules — out of turn, out of bounds, onto an occupied cell, or
 * after the game is already decided. It's an {@link IllegalArgumentException} so callers can
 * treat it as "your input was bad" without a bespoke checked exception; the message always names
 * the concrete violation so a UI can surface it directly.
 */
public class InvalidMoveException extends IllegalArgumentException {

    public InvalidMoveException(String message) {
        super(message);
    }
}
