package MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A floor is just a named group of slots. It keeps no free/occupied bookkeeping --
 * that lives in the slot-assignment strategy (see ParkingSlot for why).
 */
public class ParkingFloor {

    private final int floorNumber;
    private final List<ParkingSlot> slots;

    public ParkingFloor(int floorNumber, List<ParkingSlot> slots) {
        this.floorNumber = floorNumber;
        this.slots = List.copyOf(slots);
    }

    /**
     * Builds `floorCount` identical floors, each with the given number of slots per
     * vehicle type.
     *
     * Slot ids read as F0-CAR-2 = floor 0, a car slot, 2 spaces from the door.
     * Example: createFloors(2, {CAR=3}) -> floors 0 and 1, each with F?-CAR-0..2.
     */
    public static List<ParkingFloor> createFloors(int floorCount,
                                                  Map<VehicleType, Integer> slotsPerType) {
        List<ParkingFloor> floors = new ArrayList<>();

        for (int floorNumber = 0; floorNumber < floorCount; floorNumber++) {
            List<ParkingSlot> slots = new ArrayList<>();

            for (Map.Entry<VehicleType, Integer> entry : slotsPerType.entrySet()) {
                VehicleType type = entry.getKey();
                int howMany = entry.getValue();

                for (int slotNumber = 0; slotNumber < howMany; slotNumber++) {
                    String id = "F" + floorNumber + "-" + type + "-" + slotNumber;
                    slots.add(new ParkingSlot(id, floorNumber, slotNumber, type));
                }
            }
            floors.add(new ParkingFloor(floorNumber, slots));
        }
        return floors;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public List<ParkingSlot> getSlots() {
        return slots;
    }
}
