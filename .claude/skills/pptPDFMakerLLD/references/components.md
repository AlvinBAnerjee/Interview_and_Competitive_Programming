# Component catalog — `scripts/slidelib.py`

Every helper in the library, grouped by purpose, with a minimal usage example.
Import everything with `from slidelib import *`. Coordinates for SVG helpers
are in viewBox units, which are 1:1 with CSS pixels when the diagram's `w`
matches the space it's actually rendered into (see "Diagram sizing" below —
this is the single most important rule for not squeezing/clipping content).

## Setup

```python
reset_counter()                              # call once, before building any slides
css = build_css(accent1, accent2, soft)       # from a THEMES entry, or custom hex
```

`THEMES` (top of `slidelib.py`) holds the named accent palettes — see SKILL.md
for the table. `build_css` bakes the *fixed* Obsidian Circuit shell (dark
background, dot-grid, card styling, code chrome, typography) plus the chosen
accent gradient into one `<style>` block.

## Slide shells

Every slide function returns an HTML string; append it to your `S` list (or
use the `add()` wrapper shown in SKILL.md, which calls `slide()` for you).

- **`slide(kicker, title, body_html, *, deck_label, footer_note="")`** — the
  standard content slide: small uppercase kicker, big title, gradient
  underline, your `body_html` filling the rest, footer with deck label / note
  / auto-incrementing page number.
- **`title_slide(deck_label, kicker, title_html, subtitle, meta_items, accent_name)`**
  — the deck's opening slide. `title_html` can include `<br>` and
  `<span class="grad">word</span>` to color one word/phrase with the accent
  (don't use CSS gradient-text — see pitfalls.md). `meta_items` is a list of
  3 short strings shown as a dotted row (e.g. `["13 patterns", "UML notation
  primer", "Full class bodies"]`).
- **`section_slide(index_str, title, blurb_html, deck_label)`** — a divider
  slide: big number, title, one sentence of blurb. Use between sub-topics.
- **`closing_slide(deck_label, title, lines)`** — final slide: a title plus a
  bulleted `lines` list (each a short HTML string, can include `<b>`/`<code
  class="inline">`).

## Layout

- **`two_col(left_html, right_html, *, ratio="1fr 1fr", gap="28px")`** — side
  by side. Use for bullets+code, table+callout, etc.
- **`three_col(a, b, c, *, gap="24px")`** — three even columns.
- **`stack(*parts, gap="20px")`** — vertical stack where the **last** part
  grows to fill remaining height (everything before it sizes to its own
  content). This is the standard way to put a diagram above a code block or a
  stat row, or a table below a diagram.

## Text content

- **`bullets(items, *, tight=False, icon="chev")`** — a bulleted list.
  `items` can be plain strings, or `(head, sub)` tuples for a bold headline +
  dim sub-line per bullet (used constantly for "concept: one-line
  explanation" lists).
- **`stat_row(items, *, cols=None)`** — a horizontal row of compact
  head/sub cards. `items` is a list of `(head, sub)` tuples. Use this
  **instead of** `bullets()` right above/below a full-width diagram — it's
  short enough to leave room for the diagram, where a vertical bullet list
  would eat the space the diagram needs.
- **`pill(text, kind="default")`** — a small rounded badge. `kind="accent"`
  fills it with the gradient.
- **`kv_table(rows, headers=None, *, col_class="")`** — a clean table, no
  gridlines except a header rule and row separators. `rows` is a list of
  lists of HTML strings (use `<code class='inline'>x</code>` for inline code,
  `<b>` for emphasis).
- **`callout(text, kind="tip")`** — a left-accent-bar note box. `kind` ∈
  `tip` (key insight, accent-colored), `warn` (orange), `trap` (red, labeled
  "CLASSIC TRAP" — **never** label anything "interview trap" or similar;
  keep framing neutral), `fix` (green).

## Code

- **`code_block(code, *, lang_label="java", filename="")`** — a full-size
  syntax-highlighted code panel with a little traffic-light header bar and
  filename. Use for one substantial snippet per slide (or two, side by side
  via `two_col`).
- **`code_columns(code, *, filename="", split_ratio=0.5)`** — splits one
  long snippet's *lines* left-column-continuing-into-right-column
  (newspaper style), inside one bordered card. Use when a single file is too
  tall for one column but you don't want to break it into separate classes.
- **`mini_code(code, *, filename="")`** — a small code card sized for
  `code_grid()` — smaller font, wrapping enabled (long lines wrap instead of
  clipping). Never use `mini_code` alone at full slide size; it's designed
  for the grid.
- **`code_grid(cards, *, cols=3, gap="14px")`** — lays out several
  `mini_code()` cards in a grid. **This is how a design-pattern's 4–6 small
  classes fit on one slide.** Rows size to content automatically (fixed as of
  this skill's bundled `slidelib.py` — see pitfalls.md if you ever see a card
  cut off mid-method again).

```python
grid = code_grid([
    mini_code(code_a, filename="A.java"),
    mini_code(code_b, filename="B.java"),
    mini_code(code_c, filename="C.java"),
], cols=3)
add("PATTERN 01 · CODE", "Three Small Classes", grid, note="...")
```

**Keep snippets compact.** No blank line between one-line methods
(`{ body; }` on one line beats spreading it across three with a blank line
before/after). This isn't just cosmetic — cards with excess vertical
whitespace are more likely to end up taller than the grid row can comfortably
hold. If a class has a real Client/`main()` driver in the source, include it
as its own card, wrapped as an actual `public class X { public static void
main(String[] args) { ... } }` — not as bare loose statements.

## Diagrams

- **`diagram(svg_inner, *, w=None, h=None, vb=None, extra_class="")`** —
  wraps raw SVG fragments (built from the primitives below) in a container
  that centers and scales them. **Diagram sizing rule:** pass `w=FULLW`
  (1144) for any diagram that's the main/only content of its slide, or that
  sits in a `stack()` above other content — this makes 1 viewBox unit = 1 CSS
  pixel, so text renders at the size you designed it at. If you instead let a
  diagram get squeezed into a narrow column (e.g. one side of a `two_col`),
  everything inside it — including font sizes — shrinks with it, which is
  almost never what you want. Prefer full-width diagrams stacked above/below
  other content over side-by-side diagram+text layouts.

### Generic box/arrow primitives (concept diagrams)

- **`svg_box(x, y, w, h, title, sub="", *, accent=False, ...)`** — a rounded
  rect with a centered title and optional sub-line.
- **`svg_panel(x, y, w, h, *, accent=False, dashed=False)`** — a plain rect
  with no built-in text, for when you need to place several `svg_text()`
  calls inside it yourself (e.g. a two-part "compare A vs B" panel). Prefer
  this over cramming extra `svg_text` on top of a `svg_box`'s own title/sub —
  they will overlap (see pitfalls.md → "text-over-box overlap").
- **`svg_arrow(x1, y1, x2, y2, *, color="#7d8aa0", accent=False, dashed=False, curve=0)`**
  — a straight or curved arrow with a filled triangle head at `(x2, y2)`.
- **`svg_text(x, y, text, *, size=13, color="#c3ccda", weight=400, anchor="start", italic=False)`**
- **`svg_lane_label(x, y, text)`** — small bold label for a swimlane
  (used in timeline-style diagrams, e.g. "THREAD A" / "THREAD B").
- **`svg_pill(x, y, text, *, color="var(--accent1)")`** — a small filled
  badge inside an SVG diagram.

### UML primitives (class diagrams — design patterns, any OOP structure)

- **`svg_uml_box(x, y, w, name, *, stereotype="", methods=None, accent=False)`**
  — returns `(svg_fragment, height)`. A proper UML class box: optional
  `«stereotype»`, bold name, divider, left-aligned method signatures. **Always
  capture the returned height** — you need it to position boxes below/beside
  it and to compute edge endpoints precisely:
  ```python
  box, h = svg_uml_box(60, 30, 280, "Context", methods=["-state: State", "+request()"])
  ```
- **`uml_edge(x1, y1, x2, y2, kind, *, label="", color="#6b7280")`** — draws
  one UML relationship. `kind` ∈ `realize` (dashed, hollow triangle — "A
  implements B"), `inherit` (solid, hollow triangle — "A extends B"), `assoc`
  (solid, open arrow — "A uses a field of type B"), `aggregate` (solid,
  **hollow** diamond — "A has-a B, B can outlive A"), `compose` (solid,
  **filled** diamond — "A owns B, B dies with A"), `depend` (dashed, open
  arrow — "A temporarily uses B: a param, return type, or local").
  **The marker always renders at `(x2, y2)`** — the second point is always
  the "parent" / "interface" / "owner" / "whole", regardless of which kind
  you pick. Getting this backwards is the single most common bug in this
  skill's diagrams — see pitfalls.md → "UML relationship direction" and
  double-check the *English* sentence the edge is supposed to represent
  before trusting the coordinates.
- **`uml_legend_row(x, y, kind, label, meaning, *, w=110)`** — one row of a
  notation-primer legend (a sample edge + its name + a one-line meaning). Used
  to build the "six arrows" legend slide in the Design Patterns deck; reuse
  directly if a new deck's diagrams use the same UML notation.

### Self-referencing relationships (Singleton, Chain-of-Responsibility "next")

There's no built-in helper for a box pointing at *itself* — hand-draw a small
curved `<path>` from one side of the box back to another, plus a marker shape
that matches the relationship kind (an open-arrow polyline for a plain
association, a small diamond polygon touching the box edge for a
self-aggregation). Never reuse the `realize`/`inherit` hollow-triangle shape
for a self-loop unless the relationship really is inheritance — see
pitfalls.md → "self-loop marker shape" for the exact polygon coordinates that
worked.

## Syntax highlighting

`highlight_java(code)` is called internally by `code_block`/`code_columns`/
`mini_code` — you never call it directly. It's a single-pass regex tokenizer
(comments, strings, chars, annotations, keywords, numbers, method calls,
capitalized-identifier "types"), so it needs **raw, unescaped** Java source —
see pitfalls.md → "double-escaping" for what goes wrong if you HTML-escape
text yourself before passing it to any `svg_text`/`svg_box`/code function.
