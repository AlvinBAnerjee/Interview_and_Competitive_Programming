package MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model.ParkingFloor;
import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model.ParkingSlot;
import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model.VehicleType;

/**
 * Small construction helper — keeps the floor/slot wiring out of {@code Main} and the
 * tests. Not a design pattern, just tidy setup.
 */
public final class ParkingLots {

    private ParkingLots() {
    }

    /**
     * Build {@code floorCount} identical floors, each with the given number of slots per
     * vehicle type. Slot ids look like {@code F0-CAR-03}; the slot number encodes distance
     * from the entrance (0 = nearest), which is what the nearest-slot strategy orders by.
     */
    public static List<ParkingFloor> buildFloors(int floorCount, Map<VehicleType, Integer> slotsPerType) {
        List<ParkingFloor> floors = new ArrayList<>();
        for (int f = 0; f < floorCount; f++) {
            List<ParkingSlot> slots = new ArrayList<>();
            for (Map.Entry<VehicleType, Integer> e : slotsPerType.entrySet()) {
                VehicleType type = e.getKey();
                for (int n = 0; n < e.getValue(); n++) {
                    String id = "F" + f + "-" + type + "-" + n;
                    slots.add(new ParkingSlot(id, f, n, type));
                }
            }
            floors.add(new ParkingFloor(f, slots));
        }
        return floors;
    }
}
