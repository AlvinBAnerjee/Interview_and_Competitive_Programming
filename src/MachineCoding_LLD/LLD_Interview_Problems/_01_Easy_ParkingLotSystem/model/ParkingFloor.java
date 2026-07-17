package MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model;

import java.util.Collections;
import java.util.List;

/**
 * A floor is just an immutable grouping of its slots. It intentionally holds NO
 * free/occupied bookkeeping: allocation state lives in the {@code SlotAssignmentStrategy}
 * so that (a) the concurrency is in one place and (b) "which slot is nearest across all
 * floors" is a single global decision rather than per-floor guesswork.
 */
public class ParkingFloor {

    private final int floorNumber;
    private final List<ParkingSlot> slots;

    public ParkingFloor(int floorNumber, List<ParkingSlot> slots) {
        this.floorNumber = floorNumber;
        this.slots = List.copyOf(slots);
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public List<ParkingSlot> getSlots() {
        return Collections.unmodifiableList(slots);
    }
}
