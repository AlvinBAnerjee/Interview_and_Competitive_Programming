# Lesson 1 — The Race Condition (the problem)

**Goal:** *see* the bug with your own eyes so the fixes actually mean something.

Two threads each increment a shared `count` 100,000 times. You'd expect **200,000**.
Run [`RaceCondition.java`](./RaceCondition.java) a few times — you'll usually get **less**.

## Why? `count++` is three operations, not one
```
1. READ  count   (e.g. 41)
2. ADD   1       (42, in a CPU register)
3. WRITE 42      (back to memory)
```
If thread A and thread B both **READ 41** before either **WRITES**, both write `42`.
Two increments happened but the counter only moved by **one** — a **lost update**.

> A **race condition** = the result depends on the *timing* of threads.
> The code touching shared data (`count++`) is the **critical section**.

## Run it
```bash
javac MachineCoding_LLD/Concurrency_and_Multithreading/_00_Fundamentals/_01_RaceCondition/RaceCondition.java
java  MachineCoding_LLD.Concurrency_and_Multithreading._00_Fundamentals._01_RaceCondition.RaceCondition
```
The number is different on different runs. That non-determinism is the whole problem.

## Two separate dangers (keep them apart in your head)
1. **Atomicity** — the read-modify-write above can be interrupted → *this lesson*. Fix with `synchronized` (L2) or atomics (L4).
2. **Visibility** — one thread's write may never be *seen* by another → *Lesson 3*. Fix with `volatile`.

➡️ Next: [Lesson 2 — `synchronized`](../_02_Synchronized/README.md) fixes the atomicity half.
