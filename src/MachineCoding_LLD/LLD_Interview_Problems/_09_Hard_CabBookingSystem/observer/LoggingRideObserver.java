package MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.observer;

import MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.model.Ride;

/** Prints lifecycle events — stands in for push/SMS to the rider and driver apps. */
public class LoggingRideObserver implements RideObserver {
    @Override
    public void onDriverAssigned(Ride ride) {
        System.out.printf("  [notify] driver %s assigned to %s%n", ride.driver().name(), ride.rider().name());
    }

    @Override
    public void onRideStarted(Ride ride) {
        System.out.printf("  [notify] ride %s started%n", ride.id());
    }

    @Override
    public void onRideCompleted(Ride ride) {
        System.out.printf("  [notify] ride %s completed, fare ₹%.2f%n", ride.id(), ride.farePaise() / 100.0);
    }

    @Override
    public void onRideCancelled(Ride ride) {
        System.out.printf("  [notify] ride %s cancelled%n", ride.id());
    }

    @Override
    public void onNoDriverFound(String riderId) {
        System.out.printf("  [notify] no driver found for %s%n", riderId);
    }
}
