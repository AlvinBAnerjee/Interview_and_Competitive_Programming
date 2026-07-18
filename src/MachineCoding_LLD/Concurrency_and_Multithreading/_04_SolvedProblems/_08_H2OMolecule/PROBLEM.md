# Building H2O — Molecule Assembly  (Medium — group rendezvous)

## Problem
Many `hydrogen` and `oxygen` threads arrive in arbitrary order (LeetCode 1117). They must combine
into complete water molecules: **every group that prints contains exactly 2 H and 1 O**, and no
thread belonging to the *next* molecule may print until the current molecule is fully formed. A
valid output for input `"HHHHHHOOO"` is any string where every consecutive 3 characters have two
`H`s and one `O` (e.g. `HHOHHOHHO`).

## Why it's asked
It's the **grouping / barrier** cousin of the turn-taking problems. Where "print in order" and
FizzBuzz are about *sequencing* threads one at a time, H2O is about **gathering the right multiset
of threads into a batch** before any of them proceeds — the same shape as thread-pool "wait for a
full batch", chunked bulk writes, or quorum assembly. It tests whether you can combine **two**
primitives: one to *count/cap* participants, one to *rendezvous* them.

## Approach — capping semaphores + a cyclic barrier
| Primitive | Role |
|-----------|------|
| `Semaphore hSem = new Semaphore(2)` | Caps admission to **2 hydrogens** per molecule. |
| `Semaphore oSem = new Semaphore(1)` | Caps admission to **1 oxygen** per molecule. |
| `CyclicBarrier(3)` | **Rendezvous**: the 3 admitted atoms wait for each other, so they bond as a group. Cyclic ⇒ auto-resets for the next molecule. |

Flow per thread: `acquire` your atom's semaphore → `barrier.await()` (wait for the full 2H+1O group)
→ print → `release` the semaphore (admits the next molecule's atom). Because permits are released
**only after** the barrier trips, atoms of different molecules can't interleave.

## Key points to say
- The **semaphore counts** (2 and 1) enforce composition; the **barrier(3)** enforces grouping —
  neither alone is enough. A barrier without the caps could admit 3 H; caps without the barrier
  could print a partial molecule.
- `CyclicBarrier` is chosen over `CountDownLatch` precisely because it **resets** — one latch is
  single-use, you'd need a new one per molecule.
- Release permits **after** `await()`, not before, or the next molecule leaks in early.
- Handle `BrokenBarrierException` / interruption so a stuck thread doesn't wedge every future group.

See [`H2O.java`](./H2O.java). Compare with the sequencing family:
[`_01_PrintInOrder`](../_01_PrintInOrder/PROBLEM.md) and
[`_07_FizzBuzzMultithreaded`](../_07_FizzBuzzMultithreaded/PROBLEM.md).
