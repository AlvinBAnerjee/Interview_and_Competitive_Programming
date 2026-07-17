package MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model.ParkingFloor;
import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model.ParkingSlot;
import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model.Ticket;
import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model.Vehicle;
import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model.VehicleType;
import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.strategy.PricingStrategy;
import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.strategy.SlotAssignmentStrategy;

/**
 * The facade every gate talks to. Owns the floors and delegates the two policy decisions
 * — which slot, and how much — to injected strategies.
 *
 * <h3>Singleton, but testable</h3>
 * A real deployment wants exactly one lot, so we expose a classic
 * {@link #getInstance()} global access point configured once via {@link #configure}.
 * The constructor is left <b>package-private</b> rather than private: that keeps the
 * production singleton guarantee while letting the test harness build isolated lots for
 * stress tests without stomping on global state. (A pure private-constructor singleton is
 * notoriously hard to test — this is the pragmatic middle ground.)
 *
 * <h3>Thread-safety</h3>
 * All the slot-level racing lives inside {@link SlotAssignmentStrategy}. Ticket lifecycle
 * is guarded by a {@link ConcurrentHashMap}: {@code put} on entry, {@code remove} on exit.
 * Because {@code remove} returns the value only for the thread that actually removed it, a
 * double-unpark (or a bogus ticket) is rejected without any explicit lock.
 */
public final class ParkingLot {

    private static volatile ParkingLot instance;

    private final List<ParkingFloor> floors;
    private final SlotAssignmentStrategy slotStrategy;
    private final PricingStrategy pricingStrategy;

    private final ConcurrentMap<String, Ticket> activeTickets = new ConcurrentHashMap<>();
    private final AtomicLong ticketSeq = new AtomicLong();

    ParkingLot(List<ParkingFloor> floors,
               SlotAssignmentStrategy slotStrategy,
               PricingStrategy pricingStrategy) {
        this.floors = List.copyOf(floors);
        this.slotStrategy = slotStrategy;
        this.pricingStrategy = pricingStrategy;
    }

    /** Configure and publish the process-wide singleton (idempotent under the lock). */
    public static synchronized ParkingLot configure(List<ParkingFloor> floors,
                                                     SlotAssignmentStrategy slotStrategy,
                                                     PricingStrategy pricingStrategy) {
        instance = new ParkingLot(floors, slotStrategy, pricingStrategy);
        return instance;
    }

    public static ParkingLot getInstance() {
        ParkingLot local = instance;
        if (local == null) {
            throw new IllegalStateException("ParkingLot.configure(...) must be called first");
        }
        return local;
    }

    /**
     * Park a vehicle at the nearest free slot for its type.
     *
     * @return the issued ticket, or empty if the lot is full for that type.
     */
    public Optional<Ticket> park(Vehicle vehicle) {
        Optional<ParkingSlot> slot = slotStrategy.allocate(vehicle.getType());
        if (slot.isEmpty()) {
            return Optional.empty(); // full — an expected outcome, not an exception
        }
        Ticket ticket = new Ticket(
                "T-" + ticketSeq.incrementAndGet(),
                vehicle,
                slot.get(),
                Instant.now());
        activeTickets.put(ticket.getId(), ticket);
        return Optional.of(ticket);
    }

    /**
     * Exit using a ticket: compute the fare, free the slot, close the ticket.
     *
     * @return the fare owed.
     * @throws IllegalStateException if the ticket is unknown or already used.
     */
    public double unpark(Ticket ticket) {
        // Whoever wins this remove() owns the exit; a second call gets null -> rejected.
        Ticket active = activeTickets.remove(ticket.getId());
        if (active == null) {
            throw new IllegalStateException("Invalid or already-exited ticket: " + ticket.getId());
        }
        Instant exit = Instant.now();
        double fare = pricingStrategy.calculateFare(active, exit); // outside any lock
        slotStrategy.release(active.getSlot());
        active.close(exit, fare);
        return fare;
    }

    public int availableSlots(VehicleType type) {
        return slotStrategy.availableSlots(type);
    }

    public List<ParkingFloor> getFloors() {
        return floors;
    }
}
