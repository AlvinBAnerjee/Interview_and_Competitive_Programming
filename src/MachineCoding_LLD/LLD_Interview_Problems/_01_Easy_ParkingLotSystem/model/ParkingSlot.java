package MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * One physical parking space. Serves a single {@link VehicleType}.
 *
 * <p>{@code slotNumber} doubles as a "distance from the entrance" proxy — lower means
 * nearer — so the nearest-slot strategy can order candidates by (floor, slotNumber).
 *
 * <p>{@code occupied} is an {@link AtomicBoolean} so claiming/releasing a slot is a
 * lock-free compare-and-set. Even though the free-slot queue already hands each slot to
 * one thread, this flag is our second line of defence: {@link #occupy()} can only win
 * once, and {@link #vacate()} only succeeds for a slot that is actually occupied — which
 * is what makes a double-unpark a no-op instead of a corruption.
 */
public class ParkingSlot {

    private final String id;
    private final int floorNumber;
    private final int slotNumber;
    private final VehicleType type;
    private final AtomicBoolean occupied = new AtomicBoolean(false);

    public ParkingSlot(String id, int floorNumber, int slotNumber, VehicleType type) {
        this.id = id;
        this.floorNumber = floorNumber;
        this.slotNumber = slotNumber;
        this.type = type;
    }

    /** Atomically claim this slot. Returns {@code true} only for the thread that won. */
    public boolean occupy() {
        return occupied.compareAndSet(false, true);
    }

    /** Atomically free this slot. Returns {@code false} if it was already free. */
    public boolean vacate() {
        return occupied.compareAndSet(true, false);
    }

    public boolean isOccupied() {
        return occupied.get();
    }

    public String getId() {
        return id;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public int getSlotNumber() {
        return slotNumber;
    }

    public VehicleType getType() {
        return type;
    }

    @Override
    public String toString() {
        return id;
    }
}
