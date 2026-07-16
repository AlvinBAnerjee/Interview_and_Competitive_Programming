# Dining Philosophers  (Medium — deadlock avoidance)

## Problem
5 philosophers sit around a table; 1 fork between each pair (5 forks). A philosopher needs **both**
neighbouring forks to eat, then puts them back. Design so they can all eat without **deadlock** or
**starvation**. (LeetCode 1226.)

## Why it's asked
The textbook deadlock scenario. If every philosopher grabs their **left** fork first, then all
wait forever on the right → **circular wait** = deadlock. The interview is about **breaking one of
the four Coffman conditions**.

## Fixes (each breaks a different Coffman condition)
| Fix | Breaks | How |
|-----|--------|-----|
| **Resource ordering** | Circular wait | Number the forks; always pick up the **lower-numbered fork first**. One philosopher ends up grabbing right-then-left → no cycle. |
| **`tryLock` + backoff** | Hold-and-wait | If you can't get both forks, release the one you hold and retry. Risk: livelock (add jitter). |
| **Waiter / Semaphore(4)** | Hold-and-wait | Allow at most 4 philosophers to reach for forks at once → at least one can always finish. |
| **Odd/even asymmetry** | Circular wait | Even philosophers grab left-first, odd grab right-first. |

## Key points to say
- State the four Coffman conditions and name which one your fix breaks.
- **Lock ordering** is the cleanest, most common production answer.
- `Semaphore(n-1)` is elegant: it caps concurrent contenders so a cycle can't close.

See [`DiningPhilosophers.java`](./DiningPhilosophers.java) — lock-ordering solution with a
`Semaphore(4)` variant noted.
