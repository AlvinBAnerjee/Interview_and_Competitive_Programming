package MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.model;

/**
 * A physical seat in a screen's layout (e.g. "A1"). Immutable: the same Seat is
 * reused across every show on that screen. Per-show state (booked / held) lives
 * on {@link ShowSeat}, never here.
 */
public record Seat(String id, int row, int col, SeatCategory category) { }
