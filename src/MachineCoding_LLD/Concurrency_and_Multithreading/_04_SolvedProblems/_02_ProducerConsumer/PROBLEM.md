# Producer–Consumer (Bounded Buffer)  (Easy — the canonical concurrency problem)

## Problem
Producers add items to a **bounded** buffer; consumers remove them. A producer must **block when
the buffer is full**; a consumer must **block when it's empty**. No lost items, no corruption.

## Why it's asked
It's the "hello world" of thread coordination and tests: mutual exclusion on the shared buffer +
two blocking conditions (full / empty). Interviewers often want **both** a hand-rolled
`wait/notify` version *and* the idiomatic `BlockingQueue` version, to gauge depth.

## Three solutions (know all three)
| Version | Mechanism | Notes |
|---------|-----------|-------|
| **`wait`/`notify`** | `synchronized` on the buffer; `wait` while full/empty, `notifyAll` after add/remove | The from-scratch answer. `wait` in a `while` loop. |
| **`BlockingQueue`** | `ArrayBlockingQueue.put()/take()` | The production answer — the queue handles all blocking. One line each. |
| **`Semaphore`** | `empty`(=cap), `full`(=0), `mutex`(=1) permits | See sibling [`../../ProducerConsumer`](../../ProducerConsumer). |

## Key points to say
- Keep the **critical section minimal** — only the buffer mutation.
- `notifyAll()` over `notify()` here: producers and consumers wait on the *same* monitor for
  *different* conditions, so `notify()` can wake the wrong group → stall.
- In prod, just use a `BlockingQueue`; hand-rolling is only to prove you understand it.

See [`ProducerConsumerWaitNotify.java`](./ProducerConsumerWaitNotify.java) and
[`ProducerConsumerBlockingQueue.java`](./ProducerConsumerBlockingQueue.java).
