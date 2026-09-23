package MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model;

/**
 * One physical parking space. It serves a single VehicleType.
 *
 * Notice there is NO free/occupied flag here, and that is deliberate. "Is this slot
 * free?" is answered in exactly one place: the free-slot queue inside
 * NearestSlotStrategy.
 *
 *     in the queue      = free
 *     out of the queue  = taken
 *
 * One copy of the truth means there is no second copy that could disagree with it,
 * which is what keeps the threading easy to follow.
 *
 * slotNumber doubles as "distance from the entrance" (0 = nearest); the nearest-slot
 * ordering sorts on it.
 */
public class ParkingSlot {

    private final String id;
    private final int floorNumber;
    private final int slotNumber;
    private final VehicleType type;

    public ParkingSlot(String id, int floorNumber, int slotNumber, VehicleType type) {
        this.id = id;
        this.floorNumber = floorNumber;
        this.slotNumber = slotNumber;
        this.type = type;
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
