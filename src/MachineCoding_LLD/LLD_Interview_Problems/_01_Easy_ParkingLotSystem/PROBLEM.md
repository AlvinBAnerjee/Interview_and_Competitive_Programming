# Parking Lot System  🅿️  (Easy)

## Problem Statement
Design a parking lot that supports multiple floors, multiple vehicle types, parking a
vehicle at the nearest available slot, unparking, and generating a ticket with fare on exit.

## Clarifying Questions to Ask
- How many floors / entry-exit gates?
- Vehicle types (Motorcycle, Car, Truck/Bus)? Do slots have sizes?
- Pricing model — flat, per-hour, or per-hour-per-vehicle-type?
- Is this in-memory or persisted? Single JVM or distributed?

## Functional Requirements
- Park a vehicle → returns a `Ticket` (id, slot, entry time).
- Unpark using ticket → compute fare, free the slot.
- Find the **nearest available slot** for the vehicle type.
- Query availability per floor / per vehicle type.
- Multiple entry and exit gates.

## Non-Functional Requirements
- Slot allocation must be **thread-safe** across concurrent entry gates.
- O(1) or O(log n) slot lookup.
- Extensible pricing and slot-assignment strategies.

## Core Entities
- `ParkingLot` (Singleton), `ParkingFloor`, `ParkingSlot`, `Vehicle` (+ subtypes)
- `Ticket`, `EntryGate`, `ExitGate`
- `SlotAssignmentStrategy` (nearest / random), `PricingStrategy`

## APIs
```
Ticket park(Vehicle v)
double  unpark(Ticket t)
int     availableSlots(VehicleType type)
```

## Design Patterns
- **Singleton** — single `ParkingLot`.
- **Strategy** — `SlotAssignmentStrategy`, `PricingStrategy`.
- **Factory** — vehicle / slot creation.

## Concurrency / Multithreading  🟡 Medium
The race: two cars arriving at **different gates** must not be assigned the **same slot**.
- Represent free slots per (floor, type) as a **concurrent structure** (e.g. a
  `ConcurrentLinkedQueue` of free slots, or a `Semaphore` sized to capacity).
- Allocation = atomic "poll a free slot"; release = "offer it back".
- Alternative: `AtomicInteger` free-count guard + lock only on the free-slot set.
- Keep the critical section to **just the pick-and-mark** step, not fare calculation.
- **Test:** N threads park concurrently on a lot with capacity N → all succeed, no slot
  assigned twice, capacity+1th park fails cleanly.

## Evaluation Metrics
- [ ] Clean entity model; slots decoupled from pricing.
- [ ] Nearest-slot allocation is correct **and** thread-safe (no double allocation).
- [ ] Strategies pluggable without touching `ParkingLot` (OCP).
- [ ] Handles full lot, invalid ticket, double-unpark.
- [ ] Fare computed correctly across strategies.
- [ ] Concurrency: demonstrated with a stress test (see above).
