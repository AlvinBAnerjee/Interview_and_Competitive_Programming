---
name: pptPDFMakerLLD
description: Build a landscape (16:9) slide-deck-style PDF from a folder of LLD/DSA/concurrency/design-pattern notes in this repo — the "Obsidian Circuit" dark tech-deck design, hand-drawn vector diagrams (UML class diagrams, box-and-arrow concept diagrams), syntax-highlighted code panels, and a multi-class code grid for small classes. Trigger this whenever the user asks to turn a topic folder into a PDF deck, slides, or presentation for revision — including when they just type "pptPDFMakerLLD", or ask things like "make me a PDF slide deck of X", "create a landscape PDF for revising Y", "same as the design patterns/concurrency deck but for Z", "build slides for the <folder> notes". Prefer this over a generic PDF (reportlab/weasyprint) or a .pptx whenever the request matches this deck style — this skill exists specifically because that's what was hand-tuned and approved.
---

# pptPDFMakerLLD

Turns a folder of Java/LLD/DSA notes into a landscape (1280×720, 16:9) slide-deck
PDF: dark tech-deck design, custom SVG diagrams, syntax-highlighted code. This is
the exact pipeline used to build `Java_Concurrency_Fundamentals.pdf`,
`Java_Concurrency_Solved_Problems.pdf`, and `Java_Design_Patterns.pdf` — proven
across ~140 slides, several rounds of user feedback, and a real bug hunt. Don't
rebuild the pipeline from scratch; the hard parts are already solved in
`scripts/slidelib.py` and `scripts/render.py` — import them.

## Before anything else: two questions

### 1. Where to save the PDF (ask once, then remember)

Check memory first for a reference entry describing where this skill's output
goes. If one exists, use it silently — don't re-ask.

If none exists, this is the first run: ask the user where generated PDFs should
be saved by default, recommending `src/Practice/` (the location used for every
deck built so far in this repo) as the default option, but let them name any
other folder. Once they answer, **save it as a reference memory** (see the
memory instructions in your system prompt) so every future invocation of this
skill skips the question. Only ask again if the user later says "actually, put
these somewhere else" — then update the memory.

### 2. Which theme (ask every time)

The visual *shell* — background, cards, code-block chrome, typography, diagram
line style — is fixed and called **Obsidian Circuit**. It doesn't change. What
changes per deck is the **accent palette** (two gradient colors + a soft glow
tint). Ask the user which one to use for this deck, every time, via a question
with these options (pull the exact hex values from `THEMES` at the top of
`scripts/slidelib.py` — don't hand-type them):

| Name | Accent 1 → Accent 2 | Used so far |
|------|---------------------|-------------|
| Cyan Violet | `#22d3ee` → `#a78bfa` | Concurrency Fundamentals |
| Amber Rose | `#fbbf24` → `#fb7185` | Concurrency Solved Problems |
| Emerald Sky | `#34d399` → `#38bdf8` | Design Patterns |
| Violet Magenta | `#a78bfa` → `#f472b6` | not yet used |
| Blue Orange | `#38bdf8` → `#fb923c` | not yet used |

Offer "something else" too — if the user gives their own two colors, add them
to `THEMES` in `scripts/slidelib.py` under a name they pick, so it's available
next time. If they say "surprise me" or "whatever," prefer one of the unused
ones so decks in the same repo stay visually distinguishable from each other.

## The workflow

### Step 1 — Gather the source content

Find the target folder and read *everything* relevant before writing a single
slide: `README.md`, `PROBLEM.md`/`SOLUTION.md` files, and every source file.
**Specifically look for existing Mermaid diagrams** (often in
`assets/*-source.md` or inline in READMEs, as in the DesignPatterns folder) —
these already encode the exact class names, methods, and UML relationships
(`<|..`, `<|--`, `o--`, `*--`, `..>` , `-->`). Read them as ground truth for the
diagrams you'll draw; don't invent a shape when one is already specified.

For a large folder, delegate the reading to an Explore agent if it's more than
a handful of files — but you (not the subagent) should do the slide *design*,
since that requires judgment about what's essential vs. what to trim.

### Step 2 — Plan the slide structure

Every deck built so far follows the same skeleton; reuse it:

1. **Title slide** — deck name, a 3-item meta row, one subtitle sentence.
2. *(Optional, for a deck introducing a whole subject)* **1–2 orientation
   slides** — "what is X", "the N categories/types", or a notation primer (the
   Design Patterns deck's UML legend is a good template if the topic uses
   diagrams with a notation of its own).
3. **Roadmap slide** — a table of every sub-topic covered, in the order the
   deck presents them.
4. **Per sub-topic**, in a repeating pattern (pick the recipe that fits — see
   `references/recipes.md` for all three in full):
   - **Concurrency-fundamentals recipe**: one slide per concept, diagram+code
     interleaved or on the same slide via `stack()`.
   - **Solved-problems recipe**: a `section_slide` divider, then one diagram
     slide (the approach), then one code slide (the implementation).
   - **Design-patterns recipe**: a `section_slide` divider, then a *generic*
     UML diagram slide, an *example* UML diagram slide, then a *code* slide
     using `code_grid()` to fit every small class on one slide.
5. **Reference/cheat-sheet slides** *(optional)* — decision-guide tables,
   comparison tables (`kv_table`).
6. **Closing slide** — a punchy "how to read/apply this" checklist, 6–9 lines.

Number sub-topics **sequentially in the order they appear in the deck** (01,
02, 03…) — not by whatever numbering the source folder uses internally. If the
user asks to prioritize some sub-topics first, put those first and renumber
around them (this exact request happened before — see `references/pitfalls.md`
→ "Renumbering").

### Step 3 — Write the generator script

Write a plain Python script (put it in your scratchpad directory, not the
repo) that:

```python
import sys
sys.path.insert(0, "<absolute path to this skill's scripts/ folder>")
from slidelib import *
from render import html_doc, write_and_pdf

reset_counter()
theme = THEMES["Cyan Violet"]  # whatever the user picked
css = build_css(theme["accent1"], theme["accent2"], theme["soft"])
DECK = "YOUR DECK LABEL"   # shown bottom-left on every slide footer
FULLW = 1144               # standard content width inside the 1280px slide

S = []  # collect every slide's HTML here, in order

def add(kicker, title, body, note=""):
    S.append(slide(kicker, title, body, deck_label=DECK, footer_note=note))

# ... build every slide with add()/S.append() using the component catalog
# in references/components.md ...

doc = html_doc("Deck Title", css, S)
ok, err = write_and_pdf(doc, "/path/to/scratch/deck.html", "/path/to/scratch/deck.pdf")
print("OK" if ok else "FAIL", err)
```

Read `references/components.md` for the full function catalog (every
`slidelib.py` helper with a usage example) before writing slide bodies from
scratch — almost everything you need (title/section/closing slides, two/three
-column layouts, stat rows, callouts, code blocks, the multi-class code grid,
and the UML diagram primitives) already exists.

### Step 4 — Render

Run the script. It calls Chrome headless via `render.py`'s `write_and_pdf()` —
no other rendering path is needed or should be used (don't reach for
reportlab, weasyprint, or a pptx library; they can't produce this design).

### Step 5 — QA pass (never skip this)

This is the step that caught every real bug across all three decks. Do it in
this order:

1. **Build a contact sheet.** Use PyMuPDF (`fitz`) to render every page at low
   resolution (`Matrix(0.3, 0.3)` or similar) and tile them into a grid with
   Pillow, split into 3–4 chunks if the deck is long. This is far faster than
   opening 40+ full-res images, and catches gross problems (empty slides,
   wildly misaligned boxes, missing content) at a glance.
2. **Read every chunk.** Actually look at each one — don't skip this because
   the generator "ran without errors." A script with zero Python exceptions
   can still render visually broken output (clipped text, wrong UML arrow
   direction, overlapping elements) that only a visual pass catches.
3. **Zoom into anything suspicious** at full resolution (`Matrix(1.4, 1.4)` or
   higher) before deciding it's fine or broken.
4. **If diagrams show relationships** (UML `aggregate`/`compose`/`realize`
   edges), explicitly re-derive which box should be the "owner" for every such
   edge and check the diamond/triangle actually landed there — this exact
   class of bug (diamond on the wrong end) slipped through visual review once
   already; see `references/pitfalls.md` → "UML relationship direction."
5. Read `references/pitfalls.md` in full before this pass if you haven't
   already this session — it's the list of every bug actually found in
   production decks, with the fix for each, so you can check for them
   proactively instead of rediscovering them.

### Step 6 — Fix, rebuild, re-check

Loop steps 4→5 until the contact sheet looks clean. Don't rationalize away
something that looks off in a thumbnail — zoom in and confirm.

### Step 7 — Deliver

Copy the finished PDF to the remembered save location (Step 0), and send it to
the user with `SendUserFile`. Mention the page count and, briefly, what's
covered — don't just say "done."

## Quick facts

- Page size: 1280×720px (16:9), set via `@page { size: 1280px 720px; }` — this
  is what makes the PDF a real landscape slide deck rather than a portrait
  document with wide pages.
- Content width convention: work within `FULLW = 1144` (leaves the slide's own
  padding on each side) for anything you position by hand (diagram viewBox
  widths, custom coordinates).
- Syntax highlighting is hand-rolled (regex-based, in `slidelib.py`) — no CDN,
  no network dependency, works offline in headless Chrome.
- Never use Google Fonts or any network resource in the HTML — Chrome headless
  in this sandbox has no guaranteed network access. Stick to the system font
  stack already baked into `build_css()`.

## Reference files

- `references/components.md` — every `slidelib.py` function, what it's for,
  and a copy-pasteable usage example. Read this before building slide bodies.
- `references/recipes.md` — the three proven slide-structure templates
  (fundamentals / solved-problems / design-patterns) in full, so you can pick
  the closest match for a new topic and adapt it instead of designing from a
  blank page.
- `references/pitfalls.md` — every real bug found across the three production
  decks, with symptom → cause → fix. Read this before the QA pass (Step 5) and
  whenever something looks subtly wrong and you can't tell why.
