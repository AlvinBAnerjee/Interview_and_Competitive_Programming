# Multithreaded Web Crawler  (Medium)

## Problem
Given a `startUrl` and an `HtmlParser` with `List<String> getUrls(String url)` (a blocking network
call), crawl **every** URL reachable from `startUrl` that lives on the **same hostname**, using
multiple threads. Return the set of URLs visited. Never visit the same URL twice.
(LeetCode 1242 "Web Crawler Multithreaded".)

Example: from `http://news.yahoo.com/news` you may follow `http://news.yahoo.com/*` but **not**
`http://news.google.com/...` (different host).

## Why it's asked
Unlike the turn-taking puzzles (FooBar, H2O), this is **real-world concurrency**: a thread pool,
a shared concurrent set for dedup, and *recursively spawned* work whose size isn't known up front.
The hard parts are two:
1. **Dedup without double-work** under a race — two threads discover the same link at once.
2. **Termination** — you can't just `shutdown()` after the first task, because tasks spawn tasks.
   How do you know the whole graph is drained?

## Design
- **`ConcurrentHashMap.newKeySet()`** for `visited`. The trick: `visited.add(url)` is atomic and
  returns `true` only for the thread that *first* inserts it → that thread (and only it) schedules
  the crawl. No separate "check then add" race.
- **A fixed thread pool** (`ExecutorService`) runs crawl tasks; each task submits child tasks.
- **An `AtomicInteger pending`** counts outstanding tasks. Increment **before** submitting a task,
  decrement in the task's `finally`. Because a parent increments for each child *before* it
  decrements itself, `pending == 0` can only happen when the entire graph is done. That's the
  termination signal — the main thread `wait()`s until then, then shuts the pool down.

## Key points to say
- `visited.add(x)` returning a boolean **is** the compare-and-set — it replaces a `contains()`+`add()`
  race with one atomic op.
- The pending-counter invariant: *increment before submit, inside the parent, so the count never
  falsely hits zero while children are still queued.*
- **Trap to avoid:** recursively `future.get()`-ing children on a *bounded* pool deadlocks —
  parents hold worker threads while waiting for children that can't get scheduled. The
  fire-and-forget + counter pattern sidesteps it.
- Extract the host once from `startUrl`; compare with `startsWith("http://" + host)` (or parse it).

See [`WebCrawler.java`](./WebCrawler.java).
