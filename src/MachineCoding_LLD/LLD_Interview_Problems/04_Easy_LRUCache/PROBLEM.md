# LRU Cache  🧠  (Easy — but a concurrency favorite)

## Problem Statement
Design a fixed-capacity cache with O(1) `get` and `put` that evicts the **least recently
used** entry when full. Then make it **thread-safe**.

## Clarifying Questions to Ask
- Capacity fixed at construction?
- Eviction policy strictly LRU, or should design allow LFU/FIFO later?
- Single-threaded or must it be concurrent? (Interviewers almost always escalate to this.)
- TTL/expiry needed?

## Functional Requirements
- `get(key)` → value or miss; marks key as most-recently-used.
- `put(key, value)` → insert/update; evict LRU if over capacity.
- O(1) average for both operations.
- (Extension) pluggable eviction policy.

## Non-Functional Requirements
- **Thread-safe** `get`/`put` under high contention.
- Extensible eviction policy (Strategy).

## Core Entities
- `LRUCache<K,V>` backed by `HashMap<K, Node>` + **doubly linked list**.
- `Node` (key, value, prev, next); `EvictionPolicy` (optional strategy).

## APIs
```
V    get(K key)
void put(K key, V value)
```

## Design Patterns
- Primarily a **data-structure design** problem (HashMap + DLL).
- **Strategy** — swap eviction policy (LRU/LFU/FIFO).

## Concurrency / Multithreading  🔴 HIGH — this is the whole point in senior interviews
The hard part: `get` **mutates** the list (moves node to front), so even reads are writes.
- **Naïve:** wrap both `get` and `put` in one `synchronized`/`ReentrantLock`. Correct but
  serializes all access — discuss the throughput cost.
- **Read/write split fails here** because `get` mutates recency; a `ReadWriteLock` gives
  little unless you separate "value read" from "recency update".
- **Better approaches to discuss:**
  - **Sharded/striped LRU**: partition by `hash(key) % N`, each shard its own lock →
    contention drops ~N×. This is what real caches (Guava, Caffeine) do.
  - `ConcurrentHashMap` for storage + amortized/approximate recency (Caffeine uses ring
    buffers + a maintenance thread instead of strict LRU on the hot path).
  - CAS-based lock-free DLL is possible but usually out of scope — mention it.
- **Pitfalls to call out:** lost updates on eviction, dangling links if list ops aren't
  atomic with map ops, reader-vs-evictor races.
- **Test:** many threads hammering `get`/`put` on a small-capacity cache; assert size ≤
  capacity always and no `NullPointerException`/corrupted links. Use `CountDownLatch` to
  maximize contention.

## Evaluation Metrics
- [ ] True O(1) get/put via HashMap + doubly linked list.
- [ ] Correct LRU eviction order.
- [ ] **Thread-safety**: correct under concurrent load, with a chosen locking granularity.
- [ ] Candidate can articulate lock-granularity trade-offs (global vs striped).
- [ ] Eviction policy swappable (Strategy) for bonus.
- [ ] Stress test demonstrates no corruption.
