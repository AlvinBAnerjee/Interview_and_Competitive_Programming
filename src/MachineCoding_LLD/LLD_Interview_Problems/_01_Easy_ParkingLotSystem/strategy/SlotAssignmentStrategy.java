package MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.strategy;

import java.util.Optional;

import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model.ParkingSlot;
import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model.VehicleType;

/**
 * STRATEGY for choosing which free slot a vehicle gets. It also OWNS the free-slot
 * bookkeeping, which is deliberate: allocation and release are the only race-prone
 * operations in the whole system, so concentrating them behind this interface means
 * "make parking thread-safe" == "make one implementation thread-safe".
 *
 * <p>Swapping the whole allocation policy (nearest vs random vs per-floor balancing) is
 * a one-line change in {@code ParkingLot}, with zero edits to the lot itself (OCP).
 */
public interface SlotAssignmentStrategy {

    /** Atomically claim a slot for {@code type}, or empty if none are free (lot full). */
    Optional<ParkingSlot> allocate(VehicleType type);

    /** Return a slot to the free pool. {@code false} if it was already free (double release). */
    boolean release(ParkingSlot slot);

    /** Current number of free slots for {@code type}. */
    int availableSlots(VehicleType type);
}
