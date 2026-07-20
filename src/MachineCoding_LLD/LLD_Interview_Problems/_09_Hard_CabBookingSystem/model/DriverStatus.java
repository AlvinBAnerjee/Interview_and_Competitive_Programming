package MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.model;

/** Availability of a driver. The AVAILABLE→ASSIGNED flip is the atomic claim. */
public enum DriverStatus {
    OFFLINE,     // not taking rides
    AVAILABLE,   // online and claimable
    ASSIGNED     // committed to exactly one ride
}
