# Cab Booking System (Uber / Ola)  🚕  (Hard)

## Problem Statement
Design a ride-hailing system: riders request rides, the system matches a nearby available
driver, tracks the ride lifecycle, and computes fare. Handle concurrent requests competing
for the same drivers.

## Clarifying Questions to Ask
- Matching goal: nearest driver, ETA, surge-aware, or driver rating?
- How is location modeled/queried (grid/geohash/quadtree)?
- Ride states and cancellation policy?
- Surge pricing? Multiple ride types (pool/premium)?
- Scale/concurrency expectations?

## Functional Requirements
- Riders and drivers register; drivers publish live location + availability.
- Rider requests a ride (pickup, drop) → system **matches a driver**.
- Ride lifecycle: REQUESTED → DRIVER_ASSIGNED → STARTED → COMPLETED / CANCELLED.
- Fare estimation and final fare (distance/time + surge).
- Find **nearby available drivers** efficiently.

## Non-Functional Requirements
- A driver is assigned to **at most one ride at a time** under concurrency.
- Fast nearby-driver lookup (spatial index).
- Extensible matching & pricing strategies.

## Core Entities
- `Rider`, `Driver`, `Ride`, `Location`, `RideState`
- `DriverMatchingStrategy` (nearest/ETA/rating), `PricingStrategy` (base + surge)
- `LocationService` / spatial index (geohash / quadtree / grid)

## APIs
```
Ride    requestRide(String riderId, Location pickup, Location drop, RideType type)
void    updateDriverLocation(String driverId, Location loc)
void    updateRideStatus(String rideId, RideState state)
double  estimateFare(Location pickup, Location drop, RideType type)
```

## Design Patterns
- **Strategy** — matching & pricing.
- **State** — ride lifecycle.
- **Observer** — notify rider/driver on status changes.

## Concurrency / Multithreading  🔴 HIGH
- Two riders may match the **same driver** at once → the driver assignment must be an
  **atomic state transition** AVAILABLE → ASSIGNED (CAS on driver status, or a per-driver
  lock). Losers re-match to the next candidate.
- Driver **location updates** stream concurrently with searches → store locations in a
  concurrent spatial index; searches read a consistent snapshot.
- Keep the **matching critical section minimal**: search candidates lock-free, then attempt
  atomic claim on the chosen driver; on failure, retry with the next.
- Use `ConcurrentHashMap` for driver registry, `AtomicReference<DriverStatus>` for claims,
  and an `ExecutorService` for async notifications.
- **Pitfalls:** double-assigned driver, a driver stuck ASSIGNED after a failed ride (need
  timeout/rollback), lost location updates.
- **Test:** many riders request simultaneously with few available drivers → each driver
  assigned to at most one ride; unmatched riders handled gracefully.

## Evaluation Metrics
- [ ] Efficient nearby-driver search (spatial index, not O(N) scan).
- [ ] **Atomic driver assignment** — no driver double-booked (proven by test).
- [ ] Correct ride state machine incl. cancellation/rollback.
- [ ] Pricing with surge; matching strategy pluggable.
- [ ] Concurrent location updates + matching coexist safely.
- [ ] Clean separation of location service, matching, pricing, lifecycle.
