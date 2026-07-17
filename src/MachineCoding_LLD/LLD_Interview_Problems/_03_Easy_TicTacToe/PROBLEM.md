# Tic-Tac-Toe  ❌⭕  (Easy)

## Problem Statement
Design a generic Tic-Tac-Toe game for two players on an N×N board with win/draw
detection. Make the board size and win condition configurable.

## Clarifying Questions to Ask
- Fixed 3×3 or generic N×N (and K-in-a-row to win)?
- Two humans, or human vs bot?
- CLI-driven or library API?

## Functional Requirements
- Create a game with 2 players and board size N.
- Players alternate placing marks.
- Detect **win** (row / column / diagonal) and **draw**.
- Reject invalid moves (occupied cell, out of turn, out of bounds).

## Non-Functional Requirements
- Efficient win check — ideally O(1) per move using running counters, not O(N²) scan.
- Extensible to new player types (bot strategies).

## Core Entities
- `Game`, `Board`, `Cell`, `Player` (Human/Bot), `Symbol` (X/O)
- `WinningStrategy` (rows/cols/diagonals), `PlayerStrategy` (move selection)

## APIs
```
GameStatus makeMove(Player p, int row, int col)   // IN_PROGRESS / WIN / DRAW
```

## Design Patterns
- **Strategy** — winning-check strategy, bot move strategy.
- **Factory** — player/board creation.
- (Optional) **Observer** — notify UI on state change.

## Concurrency / Multithreading  🟢 Low
Turn-based and single-game → **no real concurrency**. Only relevant if you host **many
games on a server**: then each `Game` is independent, so shard by game id and lock
per-game (or make each game single-threaded via an actor/queue). Don't over-engineer.
> Use this to show clean OOP and an **O(1) win check** with per-line counters.

## Evaluation Metrics
- [ ] Generic N×N with K-in-a-row (not hard-coded 3×3).
- [ ] O(1) incremental win detection (row/col/diag counters).
- [ ] Invalid moves rejected with clear errors.
- [ ] Bot vs human swappable via strategy.
- [ ] Clean, testable game-state API.
