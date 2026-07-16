# Elevator System  🛗  (Medium)

## Problem Statement
Design the control system for a building with multiple elevators. Handle external (floor)
and internal (cabin) requests, dispatch the best elevator, and move cars efficiently.

## Clarifying Questions to Ask
- Number of elevators and floors?
- Scheduling goal: minimize wait time, minimize travel, or SCAN/LOOK (elevator algorithm)?
- Capacity limits? Express/service elevators?
- Real-time simulation (cars move on their own thread) or step-driven?

## Functional Requirements
- **External request:** (floor, direction) from a hall button.
- **Internal request:** (destination floor) from inside a cabin.
- **Dispatcher** assigns a request to the best elevator.
- Each elevator serves requests in an efficient order (SCAN/LOOK).
- Track state: moving up/down, idle, doors open, current floor.

## Non-Functional Requirements
- Requests arrive **concurrently**; each elevator runs its own control loop.
- Pluggable dispatch/scheduling strategy.

## Core Entities
- `ElevatorSystem` (dispatcher), `Elevator` (car), `Request`, `Direction`, `ElevatorState`
- `DispatchStrategy` (nearest-car / least-load), `SchedulingStrategy` (SCAN/LOOK)

## APIs
```
void requestElevator(int floor, Direction dir)   // external
void pressFloor(int elevatorId, int destFloor)   // internal
```

## Design Patterns
- **State** — elevator states (Idle/MovingUp/MovingDown/DoorsOpen).
- **Strategy** — dispatch & scheduling.
- **Command** — a request as a queued command.

## Concurrency / Multithreading  🔴 HIGH — this is a producer/consumer design
- Requests are **produced** by many callers and **consumed** by elevator control loops →
  classic producer/consumer.
- Give each `Elevator` its own worker thread and a **`PriorityBlockingQueue`** of stops
  ordered by the SCAN/LOOK direction; the thread blocks when idle.
- The dispatcher must **atomically assign** a request to exactly one elevator (guard the
  choose-and-enqueue step).
- Use `ExecutorService` for the elevator threads, `Condition`/`BlockingQueue` to park idle
  cars, and `volatile`/atomics for current-floor visibility to the dispatcher.
- **Pitfalls:** a request assigned to two cars, or lost when a car goes idle; busy-wait
  loops (use blocking, not spin).
- **Test:** fire a burst of concurrent hall requests; assert every request is served
  exactly once and no elevator deadlocks/starves.

## Evaluation Metrics
- [ ] Correct external + internal request handling.
- [ ] Efficient per-car ordering (SCAN/LOOK), not naive FIFO.
- [ ] Dispatcher picks a sensible car and assigns it atomically.
- [ ] Concurrency: producer/consumer with blocking queues, no busy-wait, no lost requests.
- [ ] Strategies pluggable; states modeled cleanly.
- [ ] Simulation terminates with all requests served.
