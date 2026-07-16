# Rate Limiter — Token Bucket  (Medium — atomic counters under load)

## Problem
Allow at most **R requests per second**, shared across many threads. `tryAcquire()` returns
`true` if a request is permitted right now, `false` otherwise. Must be correct and cheap under
high concurrency.

## Why it's asked
Tests **atomic state updates without a coarse lock**: many threads racing to decrement a shared
token count. Also a good vehicle to discuss algorithm choice (token bucket vs sliding window) and
lock granularity (global vs per-key).

## Algorithms (know the trade-offs)
| Algorithm | Idea | Note |
|-----------|------|------|
| **Token bucket** | Bucket refills at a fixed rate up to a cap; each request spends 1 token | Allows short **bursts** up to bucket size. Most common. |
| Leaky bucket | Requests drain at a fixed rate | Smooths output, no bursts. |
| Fixed window | Count per wall-clock window | Simple but allows 2× burst at window edges. |
| Sliding window log/counter | Weighted recent windows | Accurate, more memory. |

## Concurrency design (token bucket)
- Track `tokens` and `lastRefillTime`. On each `tryAcquire`: refill based on elapsed time, then
  try to spend a token.
- **Under contention** the refill+spend must be atomic. Options:
  - `synchronized` around refill+CAS — simplest, correct.
  - Pure CAS loop on a packed `(tokens, timestamp)` in an `AtomicLong` — lock-free, harder.
- **Per-key limiting** (per user/IP): `ConcurrentHashMap<Key, Bucket>` with
  `computeIfAbsent` to create buckets atomically. Use `AtomicLong`/`LongAdder` for hot counts.

## Key points to say
- Separate the **algorithm** (token bucket) from the **concurrency mechanism** (atomic refill).
- Mention `computeIfAbsent` for atomic per-key bucket creation (avoids the check-then-put race).
- For distributed rate limiting, state must move to Redis (INCR + EXPIRE / Lua) — single JVM
  atomics don't span nodes.

See [`TokenBucketRateLimiter.java`](./TokenBucketRateLimiter.java). See also the LLD spec:
[`../../../LLD_Interview_Problems/08_Medium_RateLimiter`](../../../LLD_Interview_Problems/08_Medium_RateLimiter/PROBLEM.md).
