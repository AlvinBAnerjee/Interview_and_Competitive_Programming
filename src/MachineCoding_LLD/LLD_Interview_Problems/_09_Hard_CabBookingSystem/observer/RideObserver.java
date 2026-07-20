package MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.observer;

import MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.model.Ride;

/**
 * Ride lifecycle listener — push notifications to rider/driver apps without the
 * service knowing who's listening (Observer pattern).
 */
public interface RideObserver {
    void onDriverAssigned(Ride ride);
    void onRideStarted(Ride ride);
    void onRideCompleted(Ride ride);
    void onRideCancelled(Ride ride);
    void onNoDriverFound(String riderId);
}
