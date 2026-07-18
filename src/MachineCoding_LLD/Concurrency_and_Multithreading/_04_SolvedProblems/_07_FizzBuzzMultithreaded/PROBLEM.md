# Multithreaded FizzBuzz  (Medium — N-way turn-taking)

## Problem
Four threads share a counter and cooperatively print `1..n` (LeetCode 1195):
- **number** thread — prints `i` when `i` is divisible by neither 3 nor 5
- **fizz** thread — prints `fizz` when `i` is divisible by 3 but not 5
- **buzz** thread — prints `buzz` when `i` is divisible by 5 but not 3
- **fizzbuzz** thread — prints `fizzbuzz` when `i` is divisible by 15

The output must be exactly the single-threaded FizzBuzz sequence, regardless of scheduling.

## Why it's asked
It's the **N-way generalization of "print in order"** ([`_01_PrintInOrder`](../_01_PrintInOrder/PROBLEM.md)).
The two-thread `FooBar` case is a fixed ping-pong you can solve with two semaphores. Here the twist
is that **which thread runs next depends on the data** (`current`'s divisibility), so a hard-wired
hand-off doesn't work — the "turn" is decided by a **predicate on shared state**. Interviewers use it
to see whether you can generalize a signalling pattern instead of pattern-matching one specific case.

## Approaches
| Approach | Idea |
|----------|------|
| **`Lock` + one `Condition` + shared counter (cleanest)** | Each thread `await()`s while `current` isn't a value it owns; the printer advances `current` and `signalAll()`s. One monitor, four predicates. |
| 4 `Semaphore`s driven by a controller | A "dispatcher" thread inspects `current` and releases exactly the one printer semaphore that owns it; that printer prints and signals the dispatcher back. More moving parts. |
| Per-thread `Semaphore` + `AtomicInteger` | Works but you still need the release logic to branch on divisibility — no simpler than the `Condition` version. |

## Key points to say
- Model the **turn as a predicate on shared state**, not a fixed rotation — that's what makes it
  scale to N categories.
- **`signalAll()`, not `signal()`**: after each print, any of the other three threads might own the
  next value, so you must wake all of them (a targeted `signal()` risks waking the wrong one).
- **Termination is part of correctness**: once `current > n`, the final `signalAll()` must let every
  still-waiting thread wake and return, or you leak blocked threads.
- Never busy-wait on `while (current % 3 != 0) {}` — block on the condition.

See [`FizzBuzz.java`](./FizzBuzz.java). Contrast with the 2-thread base case in
[`_01_PrintInOrder/FooBar.java`](../_01_PrintInOrder/FooBar.java).
