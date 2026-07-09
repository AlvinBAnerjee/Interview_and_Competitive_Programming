# Interview and Competitive Programming

A curated collection of Java solutions covering data structures, algorithms, system design, multi-threading, and company-tagged interview problems. Built up over years of practice for coding interviews and competitive programming contests.

---

## Table of Contents

- [Overview](#overview)
- [Repository Structure](#repository-structure)
- [Topics Covered](#topics-covered)
  - [Core DSA](#core-dsa)
  - [Advanced Topics](#advanced-topics)
  - [System-Level and Applied Topics](#system-level-and-applied-topics)
  - [Company-Tagged Questions](#company-tagged-questions)
- [Getting Started](#getting-started)
- [How to Run a Solution](#how-to-run-a-solution)
- [Naming Convention](#naming-convention)
- [Useful References](#useful-references)
- [Contributing](#contributing)
- [License](#license)

---

## Overview

This repository is organized as an IntelliJ IDEA Java project. Each top-level folder inside [`src/`](src) covers a single topic area, and files inside are numbered roughly in the order they should be studied. The problems range from warm-up questions on bit manipulation to advanced dynamic programming, graph algorithms, and segment trees.

The content is drawn from:
- [GeeksforGeeks DSA Sheet](https://www.geeksforgeeks.org/dsa-tutorial-learn-data-structures-and-algorithms/)
- [LeetCode](https://leetcode.com/)
- [Striver's SDE Sheet](https://takeuforward.org/interviews/strivers-sde-sheet-top-coding-interview-problems/)
- [CodeChef](https://www.codechef.com/) and [Codeforces](https://codeforces.com/) contests
- Real interview questions asked at the companies listed under [`X_CompanyQuestions/`](src/X_CompanyQuestions).

---

## Repository Structure

```
Interview_and_Competitive_Programming/
├── src/
│   ├── _0_NumberTheory/          Modular arithmetic, GCD, sieve, primality
│   ├── _1_BitMagic/              Bitwise tricks and manipulation
│   ├── _2_Recursion/             Foundational recursion problems
│   ├── _3_Arrays/                Array patterns, prefix sums, sliding window
│   ├── _4_Searching/             Binary search and its variants
│   ├── _5_Strings/               Pattern matching, KMP, Rabin-Karp
│   ├── _6_Sorting/               Comparison sorts, non-comparison sorts, cheat sheets
│   ├── _7_Matrix/                2D grid problems
│   ├── _8_Hashing/               Hash maps, hash sets, frequency problems
│   ├── _9_LinkedList/            Singly and doubly linked list problems
│   ├── _10_Stack/                Monotonic stacks, expression evaluation
│   ├── _11_Queue/                Queue variants, deque tricks
│   ├── _12_Tree/                 Binary trees, traversals, LCA, views
│   ├── _13_BST/                  Binary search tree operations
│   ├── _14_Heaps/                Priority queues, top-K, k-way merge
│   ├── _15_Graphs/               BFS, DFS, MST, shortest paths, SCC
│   ├── _16_Greedy/               Activity selection, interval problems
│   ├── _18_DP/                   Classic dynamic programming problems
│   ├── _19_Trie/                 Prefix tree problems
│   ├── _20_SegmentTree_BinaryIndexedTree/   Range queries, Fenwick tree
│   ├── _21_Disjoint_sets/        Union-Find with path compression
│   ├── _22_BackTracking/         Subsets, permutations, combination sum
│   ├── DesignPatterns/           Gang-of-Four patterns implemented in Java
│   ├── MultiThreading/           Concurrency primitives, classic problems
│   ├── CNS/                      Cryptography and Network Security exercises
│   ├── SystemLab/                Operating systems lab assignments (schedulers, IPC)
│   └── X_CompanyQuestions/       Real interview problems grouped by company
└── README.md
```

---

## Topics Covered

### Core DSA

| # | Topic | Highlights |
|---|-------|------------|
| 0 | [Number Theory](src/_0_NumberTheory) | Modular exponentiation, sieve of Eratosthenes |
| 1 | [Bit Magic](src/_1_BitMagic) | Count set bits, power of two, XOR tricks |
| 2 | [Recursion](src/_2_Recursion) | Tower of Hanoi, subsets, print pattern |
| 3 | [Arrays](src/_3_Arrays) | Kadane, prefix sums, majority element, sliding window |
| 4 | [Searching](src/_4_Searching) | Binary search on answer, rotated arrays |
| 5 | [Strings](src/_5_Strings) | KMP, Rabin-Karp, anagram search, longest substring |
| 6 | [Sorting](src/_6_Sorting) | Quick, merge, radix; `TreeSet` and `TreeMap` cheat sheet |
| 7 | [Matrix](src/_7_Matrix) | Rotate, spiral, boolean matrix |
| 8 | [Hashing](src/_8_Hashing) | Longest consecutive subsequence, subarray sums |
| 9 | [Linked List](src/_9_LinkedList) | Reverse, detect loop, merge sorted lists |

### Advanced Topics

| # | Topic | Highlights |
|---|-------|------------|
| 10 | [Stack](src/_10_Stack) | Next greater element, largest rectangle in histogram |
| 11 | [Queue](src/_11_Queue) | Sliding window maximum, LRU cache |
| 12 | [Tree](src/_12_Tree) | Traversals, serialize/deserialize, burn tree from node |
| 13 | [BST](src/_13_BST) | Floor, ceiling, k-th smallest, self-balancing intuition |
| 14 | [Heaps](src/_14_Heaps) | K-th largest, median of a stream, merge k sorted lists |
| 15 | [Graphs](src/_15_Graphs) | Prim, Dijkstra, Bellman-Ford, Kosaraju, bridges, articulation points |
| 16 | [Greedy](src/_16_Greedy) | Activity selection, merge intervals, non-overlapping intervals |
| 18 | [Dynamic Programming](src/_18_DP) | LCS, matrix chain, egg drop, subset sum, optimal strategy |
| 19 | [Trie](src/_19_Trie) | Insert, search, prefix count |
| 20 | [Segment Tree / BIT](src/_20_SegmentTree_BinaryIndexedTree) | Range sum, range min, Fenwick tree |
| 21 | [Disjoint Sets](src/_21_Disjoint_sets) | Union-Find with rank and path compression |
| 22 | [Backtracking](src/_22_BackTracking) | Subsets I/II, permutations I/II, combination sum I/II |

### System-Level and Applied Topics

- [Design Patterns](src/DesignPatterns) — Factory, Singleton, Adapter, Decorator, Builder, Chain of Responsibility, Facade, Proxy.
- [Multi-Threading](src/MultiThreading) — Producer-Consumer, Reader-Writer, coffee shop simulation, and shared-state examples.
- [CNS](src/CNS) — Cryptography and Network Security lab exercises.
- [System Lab](src/SystemLab) — Operating system assignments including FIFO, LRU, and LFU scheduling and shared-queue problems.

### Company-Tagged Questions

Real interview problems grouped by company under [`src/X_CompanyQuestions/`](src/X_CompanyQuestions):

| Company | Folder |
|---------|--------|
| Amazon | [Amazon](src/X_CompanyQuestions/Amazon) |
| BNY Mellon | [BNY](src/X_CompanyQuestions/BNY) |
| Chalo | [Chalo](src/X_CompanyQuestions/Chalo) |
| ColorToken | [ColorToken](src/X_CompanyQuestions/ColorToken) |
| Confluent | [Confluent](src/X_CompanyQuestions/Confluent) |
| Flipkart | [flipkart](src/X_CompanyQuestions/flipkart) |
| Intel | [Intel](src/X_CompanyQuestions/Intel) |
| MathWorks | [MathWorks](src/X_CompanyQuestions/MathWorks) |
| Microsoft | [Microsoft](src/X_CompanyQuestions/Microsoft) |
| Navi | [Navi](src/X_CompanyQuestions/Navi) |
| Oracle | [Oracle](src/X_CompanyQuestions/Oracle) |
| RazorPay | [RazorPay](src/X_CompanyQuestions/RazorPay) |
| SAP Labs | [SAPLabs](src/X_CompanyQuestions/SAPLabs) |
| Thoughtspot | [Thoughtspot](src/X_CompanyQuestions/Thoughtspot) |
| Uber | [Uber](src/X_CompanyQuestions/Uber) |
| UI (frontend rounds) | [UI](src/X_CompanyQuestions/UI) |

---

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 11 or higher — [download](https://adoptium.net/)
- Any IDE (IntelliJ IDEA is recommended since the project ships with `.idea/` configuration)
- Optional: `git` for cloning the repository

### Clone the Repository

```bash
git clone https://github.com/<your-username>/Interview_and_Competitive_Programming.git
cd Interview_and_Competitive_Programming
```

### Open in IntelliJ IDEA

1. Launch IntelliJ IDEA.
2. Choose **File → Open** and select the project root.
3. IntelliJ will detect the `.iml` file and configure the project automatically.

---

## How to Run a Solution

Each Java file contains a self-contained `main` method with sample inputs. To run one from the command line:

```bash
# Compile
javac -d out src/_3_Arrays/_1_Second_Largest_Element.java

# Run (use the fully-qualified name if the file declares a package)
java -cp out _1_Second_Largest_Element
```

From IntelliJ, right-click the file and choose **Run**.

---

## Naming Convention

- Folders are prefixed with an underscore and a number (`_3_Arrays`, `_18_DP`) to keep them in a fixed learning order in file explorers.
- Files inside each folder follow the same pattern (`_1_Second_Largest_Element.java`) so that scrolling through the folder mirrors the study sequence.
- Company folders use the company name directly (`Amazon`, `Microsoft`, `flipkart`).

---

## Useful References

- [Striver's A-Z DSA Sheet](https://takeuforward.org/strivers-a2z-dsa-course/strivers-a2z-dsa-course-sheet-2/)
- [Neetcode 150](https://neetcode.io/practice)
- [CP Handbook by Antti Laaksonen](https://cses.fi/book/book.pdf)
- [CSES Problem Set](https://cses.fi/problemset/)
- [Codeforces Round Archive](https://codeforces.com/contests)
- [LeetCode Explore Cards](https://leetcode.com/explore/)
- [Java `Collections` Framework Overview](https://docs.oracle.com/javase/8/docs/technotes/guides/collections/overview.html)

---

## Contributing

This is primarily a personal study repository, but pull requests are welcome for:

- Cleaner or more idiomatic solutions
- Additional test cases
- Fixes for typos or off-by-one bugs
- New problems that fit an existing topic folder

Please keep the file-numbering convention intact when adding new problems.

---

## License

Released under the [MIT License](https://opensource.org/licenses/MIT). Solutions may be freely used for study and interview preparation.
