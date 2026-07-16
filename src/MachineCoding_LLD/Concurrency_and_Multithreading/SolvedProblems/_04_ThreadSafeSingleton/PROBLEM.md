# Thread-Safe Singleton  (Easy — but a visibility trap)

## Problem
Implement a Singleton whose instance is created **lazily**, exactly once, and safely under
concurrent `getInstance()` calls.

## Why it's asked
It's a compact test of the **Java Memory Model**: naive lazy init has a race, and the classic
fix (double-checked locking) is **wrong without `volatile`**. Interviewers love watching whether
you know *why*.

## The options (know the trade-offs)
| Approach | Lazy? | Thread-safe? | Notes |
|----------|-------|--------------|-------|
| Eager (`static final`) | No | Yes | Simplest; instance built at class load. Fine if creation is cheap. |
| `synchronized getInstance()` | Yes | Yes | Correct but locks on **every** call → contention. |
| **Double-checked locking + `volatile`** | Yes | Yes | The canonical lazy answer. `volatile` is mandatory. |
| **Bill Pugh holder idiom** | Yes | Yes | Best of both — lazy via class-loading, no locks. Preferred. |
| **`enum` singleton** | No | Yes | Serialization- and reflection-safe; Effective Java's pick. |

## Why `volatile` in DCL?
`instance = new Singleton()` is **three steps**: (1) allocate, (2) run constructor, (3) assign the
reference. Without `volatile`, steps 2 and 3 can be **reordered**, so another thread may see a
non-null reference to a **half-constructed** object. `volatile` forbids that reordering and
publishes the fully-built object (happens-before).

## Key points to say
- Prefer the **holder idiom** (lazy, lock-free, uses the JVM's class-init guarantee) or **enum**.
- If asked for DCL, stress the `volatile` requirement and the "half-constructed object" hazard.

See [`Singletons.java`](./Singletons.java).
