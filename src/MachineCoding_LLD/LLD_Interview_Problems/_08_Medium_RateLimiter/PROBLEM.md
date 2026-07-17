# Rate Limiter  🚦  (Medium)

## Problem Statement
Design a rate limiter that decides whether a request from a given client should be allowed,
supporting multiple algorithms (Token Bucket, Leaky Bucket, Fixed Window, Sliding Window).

## Clarifying Questions to Ask
- Per-user / per-IP / per-API-key limits? Global or per-endpoint?
- Which algorithm(s)? Do we need smooth bursts (token bucket) or hard windows?
- Single node (in-memory) or distributed (Redis-backed)?
- Throughput expectations (this is a hot path — must be fast & thread-safe)?

## Functional Requirements
- `allow(clientId)` → boolean (allowed / rejected).
- Configurable limit (e.g. 100 req / 60s) per client.
- Support at least two algorithms, switchable via strategy.
- Correct behavior at window boundaries.

## Non-Functional Requirements
- **Extremely high concurrency** — many threads calling `allow` for many keys.
- Low latency; minimal locking on the hot path.
- Memory-bounded (evict idle clients).

## Core Entities
- `RateLimiter` (facade), `RateLimitStrategy` (TokenBucket/LeakyBucket/FixedWindow/SlidingWindow)
- Per-client state: `TokenBucket` / window counters
- `RateLimiterFactory`

## APIs
```
boolean allow(String clientId)
```

## Design Patterns
- **Strategy** — pluggable algorithm.
- **Factory** — create limiter by algorithm/config.

## Concurrency / Multithreading  🔴 HIGH — atomicity on the hot path is everything
- Per-client state lives in a `ConcurrentHashMap<clientId, State>`; use
  `computeIfAbsent` to create lazily.
- **Token Bucket:** refill + consume must be atomic. Options:
  - `synchronized`/`ReentrantLock` per bucket (simple, correct), or
  - CAS loop on an `AtomicLong` packing tokens+timestamp (lock-free, fast).
- **Fixed/Sliding Window:** increment counters with `AtomicLong`/`LongAdder`; sliding
  window needs care to atomically read the previous+current window.
- Lock **per client**, never a single global lock — global lock kills throughput.
- **Pitfalls:** read-modify-write races letting requests slip past the limit; refill math
  double-counting elapsed time; unbounded map growth (evict stale clients).
- **Test:** many threads fire `allow` for one client with limit L over a window → **at most
  L** are allowed (not L + races). Repeat across many clients for isolation.

## Evaluation Metrics
- [ ] At least two algorithms, correct at window boundaries.
- [ ] **Atomic** allow/refill — never exceeds the limit under concurrency (proven by test).
- [ ] Per-key locking, not a global lock (throughput).
- [ ] Bounded memory (idle-client eviction).
- [ ] Algorithm swappable via strategy; distributed extension discussed.
- [ ] Candidate reasons about CAS vs lock trade-offs on the hot path.
