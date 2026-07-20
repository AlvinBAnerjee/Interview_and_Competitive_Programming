# Print in Order — Alternating Threads  (Easy)

## Problem
Two threads must print `foo` and `bar` **alternately**, `n` times each, always in the order
`foobarfoobar...` — no matter how the scheduler interleaves them. (LeetCode 1115 "Print FooBar".)

Variants interviewers escalate to:
- **ZeroEvenOdd**: 3 threads print `0`, odd, `0`, even → `0102030405...`.
- **Print in Order** (LeetCode 1114): force `first()` → `second()` → `third()` across 3 threads.
- **FizzBuzz Multithreaded** (LeetCode 1195): the full **N-way** generalization where the next
  thread is chosen by a predicate on shared state — see [`_07_FizzBuzzMultithreaded`](../_07_FizzBuzzMultithreaded/PROBLEM.md).

## Why it's asked
Tests whether you can **signal between threads** correctly — the core of all coordination. The trap
is a busy-wait or a solution that only *usually* works. The correct answer uses a real
handshake so one thread can only proceed after the other has released it.

## Approaches
| Approach | Idea |
|----------|------|
| **`Semaphore` (cleanest)** | Two semaphores, `fooTurn` (1 permit) and `barTurn` (0). Each thread `acquire`s its own, prints, `release`s the other's. Ping-pong. |
| `synchronized` + `wait/notify` | A shared `boolean fooTurn`; wait while it's not your turn (in a loop), flip + `notifyAll()`. |
| `ReentrantLock` + `Condition` | Same as above but with explicit `Condition`. |

## Key points to say
- Never busy-wait (`while(!turn){}` burns CPU) — block via a primitive.
- The Semaphore version needs **no lock** because the permits *are* the handshake.
- `wait()` must be in a **`while`** loop (spurious wakeups).

See [`FooBar.java`](./FooBar.java) (Semaphore) and [`FooBarTurnVariable.java`](./FooBarTurnVariable.java) (shared `turn` variable + wait/notify).
