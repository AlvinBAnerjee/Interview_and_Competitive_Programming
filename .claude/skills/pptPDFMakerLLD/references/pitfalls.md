# Pitfalls — every real bug found across the three production decks

Each entry: what it looked like, why it happened, how it was fixed. Read this
before the QA pass, and whenever a rendered slide looks subtly wrong and the
cause isn't obvious. Most of these are *silent* — the Python script runs with
no exceptions, the PDF builds successfully, and the bug only shows up as a
visual defect you have to actually look at a rendered page to catch. That's
why the QA pass in SKILL.md is not optional.

## 1. `SyntaxError: f-string expression part cannot include a backslash`

**Symptom**: script crashes immediately with this exact error, pointing at a
line inside a big `f"""..."""` SVG-building string.

**Cause**: Python (pre-3.12) forbids a backslash inside the `{...}`
expression part of an f-string. This bites you specifically when a Python
string literal *inside* one of those `{...}` expressions contains an
apostrophe you tried to escape with `\'`, e.g.:

```python
f"""{svg_text(10, 20, 'it\'s broken', ...)}"""   # SyntaxError
```

**Fix**: switch that inner literal to double quotes instead of escaping the
apostrophe — no backslash needed:

```python
f"""{svg_text(10, 20, "it's fine", ...)}"""
```

Do a quick `grep -n "\\\\'" your_script.py` before running; any hit inside an
f-string expression is this bug waiting to happen.

## 2. Doubled braces `{{ }}` where they don't belong

**Symptom**: rendered Java code shows literal `{{` and `}}` instead of `{`
and `}` (e.g. `synchronized(lock){{ x = 5; }}` printed verbatim).

**Cause**: `{{`/`}}` is the escape sequence for a literal brace **in the
literal text portion of an f-string** (used constantly and correctly inside
`build_css`'s big CSS f-string). It is **not** needed, and is wrong, inside a
Python string literal that's itself sitting inside an f-string's `{...}`
expression — that string is ordinary Python source, and a single `{` there is
just a character. Mixing these two contexts up is an easy copy-paste mistake
when you're writing lots of `svg_box(...)` calls with Java snippets as
arguments inside a big diagram f-string.

**Fix**: inside any string literal that's an *argument* to a function call
(not the f-string's own literal text), use single braces for actual Java/CSS
braces. Grep for `{{` in your generator script; every hit should be inside
`build_css`'s CSS block — anywhere else, it's almost certainly this bug.

## 3. Double-escaped HTML entities (`Future&lt;V&gt;` instead of `Future<V>`)

**Symptom**: a class name with generics (`Future<V>`, `CF<name>`,
`Deque<Command>`) renders showing the literal text `&lt;`/`&gt;` instead of
angle brackets.

**Cause**: `svg_text`/`svg_box`/`mini_code`/`code_block` all call `esc()`
(HTML-escape) on your text internally. If you pre-escape the text yourself
before passing it in (typing `'Future&lt;V&gt;'` because you were thinking in
HTML), it gets escaped a second time, turning `&lt;` into `&amp;lt;`, which
the browser then displays as the literal string `&lt;`.

**Fix**: always pass raw, unescaped text — `'Future<V>'`, not
`'Future&lt;V&gt;'`. Same rule for `&` in "A & B" style labels: write `&` (or
just avoid it and use "and"), never `&amp;`, when passing to any of these
helpers. (Exception: raw HTML you insert directly into a slide body — kicker
strings, `kv_table` cells, `bullets()` — is **not** auto-escaped, so `&amp;`
*is* correct there. The rule only applies to arguments passed to the SVG/code
helpers, which do their own escaping.)

## 4. Gradient text renders a visible bounding box in Chrome's PDF export

**Symptom**: a heading styled with `background: linear-gradient(...)` +
`background-clip: text` + `color: transparent` (the standard CSS trick for
multi-color text) shows a faint rectangular border around the text in the
printed PDF, even though it looks fine in a normal browser tab.

**Cause**: a Chrome headless `--print-to-pdf` quirk with this specific CSS
combination — reproducible in isolation, unrelated to anything else on the
page.

**Fix**: don't use gradient text at all. `build_css` already sets this up
correctly: title/section/closing headings are solid white
(`color: #f5f8fc`), with a `.grad` span class that colors just one word/phrase
in the solid accent color (`color: var(--accent1)`) instead of a gradient.
Use `<span class="grad">word</span>` for emphasis, never a CSS gradient on
text.

## 5. A diagram box positioned past the viewBox's right edge gets clipped

**Symptom**: a box (or its label) is cut off at the slide's right edge, as if
sliced by an invisible wall — usually only noticed by an experienced eye
scanning a contact sheet, since the cut edge can look almost intentional at
low resolution.

**Cause**: hand-computed x/width coordinates that sum past the diagram's
declared `w` (which should be `FULLW = 1144` for a full-width diagram). Easy
to introduce when copy-pasting box layout from a similar diagram and shifting
positions without rechecking the rightmost box's `x + w`.

**Fix**: before trusting any new diagram, sanity check every
`svg_uml_box`/`svg_box` call's `x + w <= FULLW` (and `x >= 0`). This is
mechanical enough to script:

```python
import re
FULLW = 1144
for m in re.finditer(r'svg_(?:uml_)?box\(\s*([0-9]+)\s*,\s*[0-9]+\s*,\s*([0-9]+)', open("your_script.py").read()):
    x, w = int(m.group(1)), int(m.group(2))
    if x + w > FULLW or x < 0:
        print("OUT OF BOUNDS:", m.group(0))
```

Run something like this across the whole generator script before the visual
QA pass — it catches this exact bug in seconds instead of relying on
eyeballing every diagram.

## 6. Explanatory note text overlapping the boxes above it

**Symptom**: a caption/explanation line under a diagram visually collides
with — runs straight through — a box or arrow that's supposed to be above it.

**Cause**: the note's y-coordinate was hardcoded (e.g. always `y=335`)
instead of computed from the actual height of whatever box sits above it. This
works fine when every diagram on a deck happens to have similarly-sized boxes
in that position, and then breaks the moment one diagram has taller boxes
there (e.g. a box with 3 methods instead of 1, or two boxes stacked instead of
side-by-side).

**Fix**: compute the note's y from the real geometry, using the height that
`svg_uml_box`/`svg_box` returns:

```python
box, box_h = svg_uml_box(x, y, w, "Name", methods=[...])
note_y = y + box_h + 35   # not a hardcoded constant
```

When two boxes are stacked (one below the other, as in a self-aggregation
example or a "Director drives Builder" layout), the note goes below **both**,
computed from the second box's bottom, not a guess.

## 7. UML aggregation/composition diamond on the wrong end

**Symptom**: a diagram visually asserts the *opposite* ownership from what
the code actually does — e.g. showing "Observer owns Subject" when the real
relationship is "Subject holds a list of Observers," or "RealSubject controls
Proxy" when it's the other way around. Easy to miss on a quick look because
the diagram still "looks reasonable" — both ends are legitimate class names,
just swapped.

**Cause**: `uml_edge(x1, y1, x2, y2, kind, ...)` always renders the marker
(triangle/diamond/arrowhead) at `(x2, y2)` — the **second** point. For
`aggregate`/`compose`, the second point must be the "whole"/"owner" class.
It's easy to pass the coordinates in the order the boxes happen to sit in your
layout code (left box first, right box second) rather than the order the
*relationship* requires, especially when copy-pasting a similar edge from
another diagram where the owner happened to be on the other side.

**Real example of the bug** (from the Observer diagram): the source relation
is `Subject o-- Observer` (Subject is the owner). The buggy code was:

```python
# WRONG — puts the diamond at Observer, implying Observer owns Subject
e3o = uml_edge(subject_x, subject_y, observer_x, observer_y, "aggregate", label="notifies *")
```

The fix swaps which box comes first vs. second:

```python
# RIGHT — diamond lands at Subject, the actual owner
e3o = uml_edge(observer_x, observer_y, subject_x, subject_y, "aggregate", label="notifies *")
```

**How to check**: for every `aggregate`/`compose` edge in a new diagram,
write out the English sentence first ("X owns/has-a Y"), identify which
variable is X, and confirm X is the **second** argument pair to `uml_edge`.
Do this explicitly for each one — don't trust a visual skim, since a
plausible-looking wrong diagram is exactly what slips through one. This bug
was found in 6 separate edges across 3 different patterns in one deck despite
an earlier visual pass having "looked fine."

## 8. Self-loop relationship using the wrong marker shape

**Symptom**: a self-referencing relationship (Singleton holding its own
instance, a Handler pointing at "next" Handler) is drawn with a hollow
triangle — which the deck's own notation legend defines as *realization/
inheritance* — even though the actual relationship is a plain association or
a self-aggregation.

**Cause**: there's no built-in self-loop helper (`uml_edge` assumes two
distinct boxes), so self-loops get hand-drawn with a `<path>` + a manually
built marker polygon. It's easy to copy the hollow-triangle shape from a
nearby `realize` edge as a starting point and forget to change it to match
the *actual* relationship kind.

**Fix**: match the marker to the relationship exactly like `uml_edge` does —
an **open arrow** (two-line polyline, no fill) for a plain association, a
small **diamond polygon** touching the box edge for a self-aggregation. Never
leave a closed/hollow triangle on a self-loop unless the relationship really
is inheritance.

## 9. Multi-class code grid silently clips a card's bottom content

**Symptom**: a `mini_code()` card inside `code_grid()` cuts off mid-method —
e.g. showing `notifyObservers() { ... }` but not the `setPrice(...)` method
that comes right after it in the source, even though the source string
clearly contains it, and even though there's visible empty space in an
*adjacent* card in the same row.

**Cause**: this was a real CSS bug in `.code-grid`'s row-sizing (no
`grid-auto-rows`/`align-content` specified, so rows could be sized smaller
than their tallest card's actual content when the grid had multiple rows and
total content was close to the available slide height). **This is fixed** in
the bundled `slidelib.py` (`grid-auto-rows: min-content; align-content:
start;`) — but if you ever see this symptom again (e.g. after copying
`slidelib.py` into a fresh location and accidentally reverting the fix, or if
Chrome's grid implementation changes behavior in a future version), that CSS
rule is where to look first.

**Additional lesson, independent of the CSS fix**: keep `mini_code()`
snippets **compact** regardless — no blank line between one-line methods,
combine short setter-and-call bodies onto one line (`{ this.x = x;
somethingElse(); }`) rather than three lines with blank padding. This isn't
just cosmetic: less total height per card means more margin before any
future row-sizing edge case bites again, and it reads better in a small card
regardless.

## 10. A real `Client`/`main()` class from the source silently dropped

**Symptom**: the deck shows every supporting class of a pattern/example
except the one that actually drives it — no `main()`, no wiring code — even
though the source repo has a `Client.java` (or similarly named) file with
exactly that.

**Cause**: when trimming a source file down to fit a slide, the "boring"
driver class is the easiest one to leave out, especially if its logic feels
self-evident from the other classes. But it's often the class a reader most
wants to see — it's the one that shows *how the pieces actually get wired
together and run*, which the other classes (interfaces, individual
implementations) don't show in isolation.

**Fix**: if the source folder has a driver/`Client`/`main()` file, include it
as its own card — and make sure it's wrapped as an actual compilable class
(`public class Client { public static void main(String[] args) { ... } }`),
not bare loose statements lifted out of context. Loose statements read as an
unfinished snippet; a real class with a real `main()` reads as something you
could paste and run.

## 11. Renumbering when the user reorders content

**Symptom** (not a bug, but a real request that came up): the user asked for
certain sub-topics to be moved to the front of the deck, and for the
numbering shown on every slide (`PATTERN 03 · …`, section index, roadmap
table `#` column) to match the **new** order (1, 2, 3…) rather than whatever
number the sub-topic had in the source folder.

**How this was handled**: a single incrementing counter
(`next_num()`/`reset_counter()` in `slidelib.py`, driven by a small local
counter dict in the generator script) called exactly once per sub-topic, in
the order you add them to the deck — never hardcode the source folder's own
numbering into a kicker string. This makes reordering trivial: move the block
of Python code that builds that sub-topic's slides, and the numbering follows
automatically.

## 12. QA methodology that actually catches this class of bug

None of the above are Python exceptions — every one of them is a script that
runs cleanly and produces a PDF that *looks* plausible at a glance. The
process that actually caught them:

1. Render the whole deck, then build a **low-resolution contact sheet**
   (PyMuPDF `Matrix(0.3, 0.3)` or similar + Pillow tiling) so every slide is
   visible at once across a few images. This is fast and cheap, and catches
   gross problems immediately.
2. **Actually look at every chunk** — don't skip this because the build
   succeeded. A script with no exceptions is not evidence the output is
   correct.
3. For anything that looks even slightly off, zoom into that specific page at
   full resolution (`Matrix(1.4, 1.4)`+) before deciding whether it's fine.
4. For diagrams with relationship edges specifically, do the explicit
   "owner" re-derivation described in #7 above — a quick visual pass is not
   sufficient for this one, because a wrong-but-plausible diagram doesn't
   look wrong.
5. After any shared-library fix (like #9's CSS fix), **re-scan the whole
   deck**, not just the slide that surfaced the bug — a shared-component bug
   fix can affect every other slide using that component, for better (bug
   fixed everywhere) or worse (a fix that resolves one case can behave
   differently under another case's specific content).
