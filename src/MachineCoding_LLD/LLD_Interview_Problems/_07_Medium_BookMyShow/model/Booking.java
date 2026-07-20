package MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.model;

import java.util.List;

/** An immutable confirmed booking, created once payment succeeds. */
public class Booking {
    private final String id;
    private final Show show;
    private final List<Seat> seats;
    private final String userId;
    private final long amountPaise;
    private final String paymentTxnId;
    private final BookingStatus status;

    public Booking(String id, Show show, List<Seat> seats, String userId,
                   long amountPaise, String paymentTxnId, BookingStatus status) {
        this.id = id;
        this.show = show;
        this.seats = List.copyOf(seats);
        this.userId = userId;
        this.amountPaise = amountPaise;
        this.paymentTxnId = paymentTxnId;
        this.status = status;
    }

    public String id() { return id; }
    public Show show() { return show; }
    public List<Seat> seats() { return seats; }
    public String userId() { return userId; }
    public long amountPaise() { return amountPaise; }
    public String paymentTxnId() { return paymentTxnId; }
    public BookingStatus status() { return status; }

    @Override
    public String toString() {
        List<String> ids = seats.stream().map(Seat::id).toList();
        return String.format("Booking[%s] %s seats=%s ₹%.2f (%s, txn=%s)",
                id, status, ids, amountPaise / 100.0, userId, paymentTxnId);
    }
}
