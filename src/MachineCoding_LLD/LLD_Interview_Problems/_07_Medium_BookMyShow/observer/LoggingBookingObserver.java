package MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.observer;

import MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.lock.ExpiredHold;
import MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.model.Booking;

import java.util.List;

/** Prints lifecycle events — stands in for an email/SMS notifier. */
public class LoggingBookingObserver implements BookingObserver {
    @Override
    public void onSeatsHeld(String showId, List<String> seatIds, String userId) {
        System.out.printf("  [notify] %s held %s on show %s%n", userId, seatIds, showId);
    }

    @Override
    public void onBookingConfirmed(Booking booking) {
        System.out.printf("  [notify] booking confirmed -> %s%n", booking);
    }

    @Override
    public void onHoldExpired(ExpiredHold hold) {
        System.out.printf("  [notify] hold expired: seat %s on show %s (was %s) -> released%n",
                hold.seatId(), hold.showId(), hold.userId());
    }
}
