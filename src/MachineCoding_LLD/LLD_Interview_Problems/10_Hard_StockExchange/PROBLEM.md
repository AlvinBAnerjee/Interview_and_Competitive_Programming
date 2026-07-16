# Stock Exchange / Order Matching Engine  📈  (Hard)

## Problem Statement
Design the core of a stock exchange: accept buy/sell orders for instruments, maintain an
**order book** per symbol, and **match** orders by price-time priority, generating trades.
Support limit and market orders, and cancellations.

## Clarifying Questions to Ask
- Order types: limit, market, IOC/FOK, stop? Start with limit + market.
- Matching rule: **price-time priority** (FIFO within a price level)?
- Partial fills allowed? Cancellations/modifications?
- Latency requirements (this is the classic low-latency system)?
- One matching thread per symbol, or shared?

## Functional Requirements
- Place a **limit** order (buy/sell, price, qty) → rest in book or match.
- Place a **market** order → match against best available prices.
- **Match** by price-time priority; support **partial fills**; emit `Trade`s.
- **Cancel** a resting order.
- Query order book (best bid/ask, depth).

## Non-Functional Requirements
- Deterministic matching; correct price-time priority.
- **Very low latency / high throughput** — concurrency model is central.
- Order book operations O(log n) or better for best price.

## Core Entities
- `Order` (id, side, type, price, qty, timestamp), `Trade`, `OrderBook` (per symbol)
- Bids/asks as **price levels** (`TreeMap<price, FIFO queue>`), `MatchingEngine`
- `MatchingStrategy` (price-time), `OrderType` handling

## APIs
```
List<Trade> placeOrder(Order order)
boolean     cancelOrder(String orderId)
OrderBookSnapshot getBook(String symbol)
```

## Design Patterns
- **Strategy** — matching algorithm / order-type handling.
- **Observer** — publish trades & book updates to subscribers.
- **Command** — order actions (place/cancel/modify) on a queue.

## Concurrency / Multithreading  🔴 CRITICAL — the defining challenge
- **Single-writer per order book** is the industry pattern: serialize all mutations to one
  symbol's book through **one matching thread** fed by a queue → no locks on book internals,
  deterministic, cache-friendly.
- Inbound orders (from many client threads) are **produced** onto a queue; the matcher is
  the sole **consumer**. Use a `BlockingQueue`, or discuss a **ring buffer / LMAX Disruptor**
  for ultra-low latency (mechanical sympathy, no GC churn, batching).
- Shard by symbol → independent single-threaded engines run in parallel across symbols.
- Publish trades asynchronously (Observer) on a separate thread so matching isn't blocked.
- **Pitfalls to discuss:** lock contention on a shared book (why the single-writer model
  wins), ordering/fairness (price-time must be exact), backpressure when the queue fills,
  false sharing / GC pauses at the tail of the latency distribution.
- **Test:** flood the engine with concurrent buy/sell orders → order book stays consistent,
  fills obey price-time priority, total bought qty == total sold qty across trades.

## Evaluation Metrics
- [ ] Correct **price-time priority** matching with partial fills.
- [ ] Limit + market orders and cancellation handled correctly.
- [ ] Efficient book (best bid/ask in O(1)/O(log n); `TreeMap` of price levels).
- [ ] **Single-writer / queue-based** concurrency model articulated and justified.
- [ ] Conservation invariant holds (bought qty == sold qty) under concurrent load.
- [ ] Candidate discusses latency: Disruptor/ring buffer, sharding by symbol, async publish.
