package MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model;

import java.time.Instant;

/**
 * The slip handed out at entry and redeemed at exit.
 *
 * Entry details never change. exitTime and fare get filled in once, by
 * ParkingLot.unpark(), which makes sure only one caller ever reaches that point for a
 * given ticket. They are volatile so the thread that later reads the ticket sees the
 * values the exiting thread wrote.
 */
public class Ticket {

    private final String id;
    private final Vehicle vehicle;
    private final ParkingSlot slot;
    private final Instant entryTime;

    private volatile Instant exitTime;   // null until the vehicle leaves
    private volatile double fare;

    public Ticket(String id, Vehicle vehicle, ParkingSlot slot, Instant entryTime) {
        this.id = id;
        this.vehicle = vehicle;
        this.slot = slot;
        this.entryTime = entryTime;
    }

    /** Stamps the exit time and fare. Called once, from ParkingLot.unpark(). */
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
