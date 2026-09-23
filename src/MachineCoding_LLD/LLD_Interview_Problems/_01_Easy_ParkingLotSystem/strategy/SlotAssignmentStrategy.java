package MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.strategy;

import java.util.Optional;

import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model.ParkingSlot;
import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model.VehicleType;

/**
 * STRATEGY for picking which free slot a vehicle gets.
 *
 * It also owns the list of free slots, on purpose: allocate() and release() are the only
 * two places where threads can collide, so keeping both behind this one interface means
 * "make parking thread-safe" is the same job as "make one class thread-safe".
 *
 * Swapping the policy (nearest / random / spread across floors) needs no change inside
 * ParkingLot.
 */
public interface SlotAssignmentStrategy {

    /** Takes a free slot of this type. Empty means the lot is full for that type. */
    Optional<ParkingSlot> allocate(VehicleType type);

    /** Puts a slot back into the free pool. */
    void release(ParkingSlot slot);

    /** How many slots of this type are free right now. */
    int availableSlots(VehicleType type);
}
