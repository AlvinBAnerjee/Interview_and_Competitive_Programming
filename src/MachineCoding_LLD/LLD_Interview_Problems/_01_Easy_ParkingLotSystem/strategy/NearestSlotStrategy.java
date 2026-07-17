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
 * Hands out the slot nearest to an entrance — lowest floor first, then lowest slot
 * number — and does it thread-safely.
 *
 * <h3>The concurrency design (the heart of this problem)</h3>
 * Free slots for each {@link VehicleType} live in a {@link PriorityBlockingQueue} ordered
 * by (floor, slotNumber). That single data structure gives us three things at once:
 * <ul>
 *   <li><b>Nearest-first:</b> the queue head is always the globally nearest free slot.</li>
 *   <li><b>Atomic hand-out:</b> {@code poll()} removes-and-returns the head in one atomic
 *       step, so two threads at two gates can never receive the same slot — the classic
 *       race is gone without us writing a single {@code synchronized} block.</li>
 *   <li><b>Clean "full" signal:</b> {@code poll()} returns {@code null} when empty, which
 *       we surface as {@link Optional#empty()} — no exceptions for the expected full case.</li>
 * </ul>
 * The critical section is exactly the {@code poll}/{@code offer} on the queue; fare
 * calculation and ticket creation happen entirely outside it.
 */
public class NearestSlotStrategy implements SlotAssignmentStrategy {

    private static final Comparator<ParkingSlot> NEAREST =
            Comparator.comparingInt(ParkingSlot::getFloorNumber)
                      .thenComparingInt(ParkingSlot::getSlotNumber);

    private final Map<VehicleType, PriorityBlockingQueue<ParkingSlot>> freeByType =
            new EnumMap<>(VehicleType.class);

    public NearestSlotStrategy(List<ParkingFloor> floors) {
        for (VehicleType type : VehicleType.values()) {
            freeByType.put(type, new PriorityBlockingQueue<>(16, NEAREST));
        }
        for (ParkingFloor floor : floors) {
            for (ParkingSlot slot : floor.getSlots()) {
                freeByType.get(slot.getType()).offer(slot);
            }
        }
    }

    @Override
    public Optional<ParkingSlot> allocate(VehicleType type) {
        PriorityBlockingQueue<ParkingSlot> free = freeByType.get(type);
        ParkingSlot slot;
        // poll() is atomic, so normally the very first slot is ours. The loop + occupy()
        // is belt-and-suspenders: it also holds if a slot were ever offered back twice.
        while ((slot = free.poll()) != null) {
            if (slot.occupy()) {
                return Optional.of(slot);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean release(ParkingSlot slot) {
        // vacate() only wins for a genuinely-occupied slot, so a double release can't
        // push the same slot into the free queue twice.
        if (slot.vacate()) {
            freeByType.get(slot.getType()).offer(slot);
            return true;
        }
        return false;
    }

    @Override
    public int availableSlots(VehicleType type) {
        return freeByType.get(type).size();
    }
}
