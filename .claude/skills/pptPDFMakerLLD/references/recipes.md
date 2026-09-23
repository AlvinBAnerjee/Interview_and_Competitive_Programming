# Slide-structure recipes

Three shapes, each proven across a real deck. Pick whichever matches the new
topic's own shape (a linear list of concepts? a list of solved problems each
with an approach + implementation? a catalog of reusable structures each with
a canonical form + a concrete example?) and adapt it — don't design from a
blank page every time.

## Recipe A — "Fundamentals" (linear concept list)

Used for: **Java Concurrency Fundamentals** (40 slides, 14 lessons).

Fits topics that are a **sequence of individual concepts** building on each
other (a language feature, a primitive, an API) where each concept needs a
mental-model explanation *and* real code.

```
Title
Section: Orientation
  Concept slide (table or two_col bullets+diagram) — the big-picture framing
  Concept slide — the core problem/tension the whole topic exists to solve
Section: <first cluster of concepts>
  For EACH concept:
    Concept slide — stack(diagram(w=FULLW), stat_row(...))  — the mental model
    Code slide    — stack(diagram or code_block, code_block(...))  — the real implementation
Section: <next cluster>
  ...
Section: Reference
  Cheat-sheet slides (kv_table) — decision guides, comparison tables
Closing — checklist + teaser for what's next (if this is part of a series)
```

**Key lesson from this deck**: the first version shipped with diagrams but no
code for most concepts, because "the concept is illustrated" felt sufficient
while building it. It wasn't — the user explicitly asked for real code on
*every* concept afterward ("you have not shown any code whatsoever"). If a
topic has real source code available for a concept, show it. A diagram alone
is not a substitute for the actual implementation when the deck's purpose is
revision.

## Recipe B — "Solved Problems" (diagram → code, per problem)

Used for: **Java Concurrency Solved Problems** (33 slides, 10 problems).

Fits topics that are a **list of discrete problems**, each with one
recognizable approach.

```
Title
Roadmap — table: # / problem name / difficulty / core skill tested
For EACH problem:
  Section divider — problem statement in one sentence
  Diagram slide — the approach, as a labeled box/arrow diagram (the
                  mechanism: what waits on what, what signals what)
  Code slide    — the real implementation (code_block or two_col of two
                  variants if the source offers e.g. a "from scratch" and a
                  "production" version)
Closing — the pattern behind the patterns (what recurs across problems)
```

Two slides per problem is the default; go to three only if the problem
genuinely has two code variants worth showing separately (e.g. a hand-rolled
`wait`/`notify` version *and* the idiomatic library version).

## Recipe C — "Design Patterns" (generic shape → concrete example → code)

Used for: **Java Design Patterns** (58 slides, 13 patterns + 2 orientation
slides).

Fits topics that are a **catalog of reusable structures**, each with (a) a
canonical/abstract form and (b) one or more concrete instances in the
codebase. Not just GoF patterns — this shape also fits things like "HTTP
status code families", "sorting algorithm categories", or any topic where
there's a generic template *and* worked examples of it.

```
Title
Orientation slide(s) — what is a <thing>, and the N categories it falls into
  (a 3-column "type card" grid works well here — see components.md's kv_table
  and the type-card CSS classes already in the shared stylesheet)
Notation primer — IF the diagrams use a notation with its own vocabulary
  (UML arrows, state-machine notation, etc.), a legend slide explaining every
  symbol ONCE up front pays for itself across every later diagram.
Roadmap — table of every item, numbered in deck order
For EACH item:
  Section divider — one-sentence intent
  Generic diagram  — the canonical/abstract shape (interface + roles), pulled
                      directly from the source's own diagram spec if one
                      exists (e.g. a Mermaid classDiagram block) rather than
                      invented from scratch
  Example diagram  — the same shape with THIS repo's actual class names
  Code slide       — code_grid() with every small class from the source,
                      compacted (see components.md), including the real
                      Client/main() if the source has one
Closing — how to read the notation/structure in general, applied to any new
  instance the reader encounters later
```

**Key lessons from this deck** (worth re-reading before building a new
catalog-style deck):
- Read the source's own diagram spec (Mermaid, PlantUML, whatever) as ground
  truth for relationships — don't eyeball class names and guess whether it's
  aggregation or composition.
- Every `aggregate`/`compose` edge needs its direction verified against the
  actual "owns" relationship, not just visually eyeballed once — see
  pitfalls.md.
- When the user asks for "one more" of something repeated (e.g. one more
  concrete subclass shown in full), add exactly one, not all of them — the
  whole point of picking a representative example is to keep the slide
  readable; showing every near-duplicate defeats it.
