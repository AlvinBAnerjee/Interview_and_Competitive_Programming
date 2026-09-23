package MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.strategy;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.PriorityBlockingQueue;

import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model.ParkingFloor;
import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model.ParkingSlot;
import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model.VehicleType;

/**
 * Hands out the slot nearest the entrance: lowest floor first, then lowest slot number.
 *
 * HOW IT WORKS
 * Each vehicle type gets its own queue of free slots, sorted by (floor, slotNumber).
 * So the front of the CAR queue is always the nearest free car slot in the whole lot.
 *
 *     park    -> take the front slot OUT of the queue  (poll)
 *     unpark  -> put the slot BACK into the queue      (offer)
 *
 * WHY IT IS THREAD-SAFE
 * The queue is a PriorityBlockingQueue, and its poll() returns the front item and
 * removes it as ONE atomic step. Two gates polling at the same instant are therefore
 * handed two DIFFERENT slots, so the classic "both cars were given F0-CAR-0" bug cannot
 * happen -- with no synchronized block and no lock of our own.
 *
 * An empty queue just returns null, which we report as "lot full" instead of an error.
 */
public class NearestSlotStrategy implements SlotAssignmentStrategy {

    /** Nearest = lowest floor, then the lowest slot number on that floor. */
    private static final Comparator<ParkingSlot> NEAREST_FIRST =
            Comparator.comparingInt(ParkingSlot::getFloorNumber)
                      .thenComparingInt(ParkingSlot::getSlotNumber);

    private final Map<VehicleType, PriorityBlockingQueue<ParkingSlot>> freeSlotsByType =
            new EnumMap<>(VehicleType.class);

    public NearestSlotStrategy(List<ParkingFloor> floors) {
        // One queue per vehicle type...
        for (VehicleType type : VehicleType.values()) {
            freeSlotsByType.put(type, new PriorityBlockingQueue<>(16, NEAREST_FIRST));
        }
        // ...and at the start every slot is free.
        for (ParkingFloor floor : floors) {
            for (ParkingSlot slot : floor.getSlots()) {
                freeSlotsByType.get(slot.getType()).offer(slot);
            }
        }
    }

    @Override
    public Optional<ParkingSlot> allocate(VehicleType type) {
        // poll() hands back the nearest free slot and removes it in one atomic step,
        // so two threads can never receive the same slot. null = nothing left.
        ParkingSlot slot = freeSlotsByType.get(type).poll();
        return Optional.ofNullable(slot);
    }

    @Override
    public void release(ParkingSlot slot) {
        freeSlotsByType.get(slot.getType()).offer(slot);
    }

    @Override
    public int availableSlots(VehicleType type) {
        return freeSlotsByType.get(type).size();
    }
}
