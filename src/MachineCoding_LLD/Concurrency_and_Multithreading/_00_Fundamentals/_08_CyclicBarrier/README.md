# Lesson 8 — `CyclicBarrier` (reusable rendezvous)

**Goal:** make **N** threads wait for **each other** — not for a fixed set of external
signalers — at a checkpoint, then release them all together. Unlike `CountDownLatch`, it
resets and can be used again for the next round.

Demo: [`CyclicBarrierDemo.java`](./CyclicBarrierDemo.java) — 3 "runners" each finish a
round at their own pace, then all `await()` at the barrier. Nobody starts round 2 until
**all three** have finished round 1.

## The core pieces
| Piece | Effect |
|-------|--------|
| `new CyclicBarrier(n)` | Barrier that trips once `n` threads call `await()`. |
| `new CyclicBarrier(n, barrierAction)` | Same, but runs `barrierAction` once — on one of the arriving threads — right after the last thread arrives and before anyone is released. |
| `await()` | Block until all `n` parties have called it. The barrier then **automatically resets** for the next round — that's the "cyclic" part. |

## vs `CountDownLatch` (Lesson 7)
| | `CountDownLatch` | `CyclicBarrier` |
|---|---|---|
| Who waits, who signals | One set waits; a *different* set signals (`countDown()`) | The **same** threads both signal and wait, by calling `await()` |
| Reusable? | No — one-shot, count never resets | Yes — resets automatically after each trip |
| Typical use | "Wait for N things to finish, once" | "Sync N threads at a checkpoint, repeatedly" |

## Rules of thumb
- If one thread in the group throws or times out at `await()`, the barrier **breaks**
  (`BrokenBarrierException` for every other thread still waiting) — there's no partial
  success.
- Use `barrierAction` for cheap per-round bookkeeping (e.g. merging partial results),
  not slow work — it delays releasing every thread that's waiting.

## Run it
```bash
javac MachineCoding_LLD/Concurrency_and_Multithreading/_00_Fundamentals/_08_CyclicBarrier/CyclicBarrierDemo.java
java  MachineCoding_LLD.Concurrency_and_Multithreading._00_Fundamentals._08_CyclicBarrier.CyclicBarrierDemo
```
Watch round 1: all 3 "finished round 1" lines print (order varies) before the "all
runners reached the barrier" line — then the same pattern repeats for round 2.

➡️ You've finished Fundamentals. Now try the [Solved Problems](../../_04_SolvedProblems/) —
start with [Print in Order](../../_04_SolvedProblems/_01_PrintInOrder/PROBLEM.md).
