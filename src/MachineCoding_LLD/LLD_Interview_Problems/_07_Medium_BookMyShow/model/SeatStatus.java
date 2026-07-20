package MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.model;

/**
 * Availability of a seat in a show, as seen by a browsing user. This is a
 * derived view, computed on the fly from {@link ShowSeat#isBooked()} plus
 * whether a live hold exists in the lock provider — it is not stored anywhere.
 */
public enum SeatStatus {
    AVAILABLE,   // not booked, no live hold
    HELD,        // a live (non-expired) hold exists
    BOOKED       // sold
}
