package MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model;

import java.time.Instant;

/**
 * The record handed out at entry and redeemed at exit. Entry data is immutable;
 * {@code exitTime} and {@code fare} are filled in exactly once on unpark (guarded by
 * the lot's active-ticket map, so they can't be set twice).
 */
public class Ticket {

    private final String id;
    private final Vehicle vehicle;
    private final ParkingSlot slot;
    private final Instant entryTime;

    private volatile Instant exitTime;   // null until the vehicle exits
    private volatile double fare;

    public Ticket(String id, Vehicle vehicle, ParkingSlot slot, Instant entryTime) {
        this.id = id;
        this.vehicle = vehicle;
        this.slot = slot;
        this.entryTime = entryTime;
    }

    /** Stamps exit time and fare. Called once by {@code ParkingLot.unpark}; the lot's
     *  active-ticket map guarantees a single winning caller, so no lock is needed here. */
    public void close(Instant exitTime, double fare) {
        this.exitTime = exitTime;
        this.fare = fare;
    }

    public String getId() {
        return id;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public ParkingSlot getSlot() {
        return slot;
    }

    public Instant getEntryTime() {
        return entryTime;
    }

    public Instant getExitTime() {
        return exitTime;
    }

    public double getFare() {
        return fare;
    }

    @Override
    public String toString() {
        return "Ticket{" + id + ", " + vehicle + ", slot=" + slot + "}";
    }
}
