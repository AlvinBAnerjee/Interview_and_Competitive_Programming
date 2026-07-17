# BookMyShow / Movie Ticket Booking  🎟️  (Medium)

## Problem Statement
Design a movie-ticket booking system: browse movies by city/theatre, view shows and their
seat maps, **hold** seats during checkout, and confirm booking with payment — without ever
double-booking a seat.

## Clarifying Questions to Ask
- Scope: single city or many? Multiple theatres/screens/shows?
- Seat categories (silver/gold/recliner) and pricing?
- Seat-hold timeout during payment? Payment integration in scope?
- Expected concurrency (many users grabbing the same popular seat)?

## Functional Requirements
- Search movies by city → theatres → shows.
- View a show's **seat availability map**.
- **Hold** selected seats (temporary lock with expiry) → pay → **confirm**.
- Release seats if payment not completed within the hold window.
- Prevent **double-booking** of the same seat.

## Non-Functional Requirements
- **No two users book the same seat** — the core correctness guarantee.
- Held seats auto-expire and return to the pool.
- Extensible pricing per seat category.

## Core Entities
- `Movie`, `Theatre`, `Screen`, `Show`, `Seat`, `SeatCategory`
- `Booking`, `SeatLockProvider` (in-memory/distributed), `PaymentStrategy`, `PricingStrategy`

## APIs
```
List<Show> searchShows(String movieId, String city)
SeatMap    getAvailability(String showId)
LockToken  holdSeats(String showId, List<String> seatIds, String userId)  // temporary
Booking    confirmBooking(LockToken token, Payment payment)
```

## Design Patterns
- **Singleton** — booking service / lock provider.
- **Strategy** — pricing, payment.
- **Observer** — notify user on booking/expiry.

## Concurrency / Multithreading  🔴 HIGH — the classic double-booking problem
- Guard each seat with a **per-seat lock** keyed by `(showId, seatId)` in a
  `ConcurrentHashMap`; acquire it atomically (`putIfAbsent`/CAS) before holding.
- A hold stores `{userId, expiryTime}`. Confirm only if the caller still owns a live hold.
- **Expiry:** a `ScheduledExecutorService` reaps expired holds and frees seats (or check
  expiry lazily on access).
- **Optimistic vs pessimistic:** discuss CAS/version checks vs locking; for multi-seat
  bookings acquire seat locks in a **consistent order** to avoid deadlock, and roll back
  (release all) if any seat is unavailable — all-or-nothing.
- **Pitfalls:** TOCTOU (check-then-book gap), two users passing the availability check
  then both booking; a held seat never released after crash (need expiry).
- **Test:** N threads try to book the **same seat** simultaneously → exactly one succeeds,
  N-1 fail cleanly; after hold expiry the seat is bookable again.

## Evaluation Metrics
- [ ] **Zero double-booking** under concurrent contention (proven by test).
- [ ] Seat-hold with correct expiry and release.
- [ ] Multi-seat booking is all-or-nothing and deadlock-free.
- [ ] Search/availability model is clean and extensible.
- [ ] Pricing/payment pluggable via strategy.
- [ ] Candidate discusses optimistic vs pessimistic locking trade-offs.
