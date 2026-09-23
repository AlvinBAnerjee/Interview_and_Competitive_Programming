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
 * The one object every gate talks to. It does very little itself -- it hands the two
 * real decisions to the strategies it was given:
 *
 *     which slot?  -> SlotAssignmentStrategy
 *     how much?    -> PricingStrategy
 *
 * SINGLETON: a real car park has exactly one lot, so configure() builds it once and
 * getInstance() hands that same one to everybody. The constructor stays public so tests
 * (and Main) can also build a throwaway lot without touching the shared one.
 *
 * THREADING: all the slot racing lives inside the slot strategy. The only shared state
 * here is activeTickets, a ConcurrentHashMap -- put on entry, remove on exit. Because
 * remove() returns the ticket to exactly ONE caller, using the same ticket twice is
 * rejected without any lock of our own.
 */
public final class ParkingLot {

    private static ParkingLot instance;

    private final List<ParkingFloor> floors;
    private final SlotAssignmentStrategy slotStrategy;
    private final PricingStrategy pricingStrategy;

    /** Tickets for vehicles currently inside, keyed by ticket id. */
    private final ConcurrentMap<String, Ticket> activeTickets = new ConcurrentHashMap<>();
    private final AtomicLong ticketCounter = new AtomicLong();

    public ParkingLot(List<ParkingFloor> floors,
                      SlotAssignmentStrategy slotStrategy,
                      PricingStrategy pricingStrategy) {
        this.floors = List.copyOf(floors);
        this.slotStrategy = slotStrategy;
        this.pricingStrategy = pricingStrategy;
    }

    /** Builds the shared lot. Call this once at start-up. */
    public static synchronized ParkingLot configure(List<ParkingFloor> floors,
                                                    SlotAssignmentStrategy slotStrategy,
                                                    PricingStrategy pricingStrategy) {
        instance = new ParkingLot(floors, slotStrategy, pricingStrategy);
        return instance;
    }

    /** The shared lot created by configure(). */
    public static synchronized ParkingLot getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Call ParkingLot.configure(...) first");
        }
        return instance;
    }

    /**
     * Parks a vehicle at the nearest free slot for its type.
     *
     * 1. ask the strategy for a slot   (empty  -> lot is full, we stop here)
     * 2. make a ticket for that slot
     * 3. remember the ticket as active
     *
     * @return the ticket, or empty if there is no free slot for this vehicle type.
     */
    public Optional<Ticket> park(Vehicle vehicle) {
        // 1. which slot?
        Optional<ParkingSlot> freeSlot = slotStrategy.allocate(vehicle.getType());
        if (freeSlot.isEmpty()) {
            return Optional.empty();          // full is a normal answer, not an error
        }

        // 2. write the ticket
        String ticketId = "T-" + ticketCounter.incrementAndGet();
        Ticket ticket = new Ticket(ticketId, vehicle, freeSlot.get(), Instant.now());

        // 3. the vehicle is now inside
        activeTickets.put(ticketId, ticket);
        return Optional.of(ticket);
    }

    /**
     * Exits using a ticket.
     *
     * 1. claim the ticket  (remove() succeeds for ONE caller, so a reused or fake
     *    ticket is rejected right here)
     * 2. work out the fare
     * 3. free the slot for the next driver
     *
     * @return the fare owed.
     * @throws IllegalStateException if the ticket is unknown or was already used.
     */
    public double unpark(Ticket ticket) {
        // 1. claim it
        Ticket activeTicket = activeTickets.remove(ticket.getId());
        if (activeTicket == null) {
            throw new IllegalStateException("Unknown or already-used ticket: " + ticket.getId());
        }

        // 2. how much?
        Instant exitTime = Instant.now();
        double fare = pricingStrategy.calculateFare(activeTicket, exitTime);

        // 3. give the slot back
        slotStrategy.release(activeTicket.getSlot());

        activeTicket.close(exitTime, fare);
        return fare;
    }

    /** How many slots of this type are free right now. */
    public int availableSlots(VehicleType type) {
        return slotStrategy.availableSlots(type);
    }

    public List<ParkingFloor> getFloors() {
        return floors;
    }
}
