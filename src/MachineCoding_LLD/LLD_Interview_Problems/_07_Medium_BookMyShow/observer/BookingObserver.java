package MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.observer;

import MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.lock.ExpiredHold;
import MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.model.Booking;

import java.util.List;

/**
 * Booking lifecycle listener. The service fires these; concrete observers push
 * email/SMS/analytics without the service knowing they exist (Observer pattern).
 */
public interface BookingObserver {
    void onSeatsHeld(String showId, List<String> seatIds, String userId);
    void onBookingConfirmed(Booking booking);
    void onHoldExpired(ExpiredHold hold);
}
