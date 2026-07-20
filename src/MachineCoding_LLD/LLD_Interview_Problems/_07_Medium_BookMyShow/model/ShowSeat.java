package MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.model;

/**
 * Per-show state for one physical {@link Seat}.
 *
 * The ONLY durable state here is {@code booked} — the permanent record that a
 * seat is sold. The transient "HELD" state deliberately does NOT live here; it
 * lives in the SeatLockProvider as a time-boxed lock. That split means an
 * expired hold needs no cleanup on this object (the lock just vanishes), and
 * availability is simply: booked ? BOOKED : (live lock ? HELD : AVAILABLE).
 *
 * {@code booked} is written only by the thread that owns the seat's hold at
 * confirm time, so a volatile flag is enough for visibility.
 */
public class ShowSeat {
    private final Seat seat;
    private volatile boolean booked;

    public ShowSeat(Seat seat) { this.seat = seat; }

    public Seat seat() { return seat; }
    public boolean isBooked() { return booked; }
    public void markBooked() { this.booked = true; }
}
