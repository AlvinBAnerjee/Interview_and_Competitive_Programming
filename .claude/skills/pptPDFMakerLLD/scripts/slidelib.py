"""
Shared library for building landscape (1280x720, 16:9) slide-deck HTML that
renders 1:1 to PDF via headless Chrome (--print-to-pdf with @page size in px).

Design system: "Obsidian Circuit" — a dark tech-deck shell (near-black background,
dot-grid texture, card panels, terminal-style code blocks, hand-drawn UML line art)
that stays fixed across decks. Only the accent gradient (two colors + a soft glow
tint) changes between decks — see THEMES below for the named palette options.
"""
import html
import re

W, H = 1280, 720

# ---------------------------------------------------------------------------
# Named accent palettes for the Obsidian Circuit shell.
# Each deck picks ONE of these (or a user-supplied hex pair) and passes it to
# build_css(accent1, accent2, soft). `soft` is accent1 at ~11% alpha, used for
# the background glow and highlighted UML boxes.
# ---------------------------------------------------------------------------
THEMES = {
    "Cyan Violet":    {"accent1": "#22d3ee", "accent2": "#a78bfa", "soft": "rgba(34,211,238,0.11)"},
    "Amber Rose":     {"accent1": "#fbbf24", "accent2": "#fb7185", "soft": "rgba(251,191,36,0.11)"},
    "Emerald Sky":    {"accent1": "#34d399", "accent2": "#38bdf8", "soft": "rgba(52,211,153,0.11)"},
    "Violet Magenta": {"accent1": "#a78bfa", "accent2": "#f472b6", "soft": "rgba(167,139,250,0.11)"},
    "Blue Orange":    {"accent1": "#38bdf8", "accent2": "#fb923c", "soft": "rgba(56,189,248,0.11)"},
}

# ---------------------------------------------------------------------------
# Java syntax highlighter (self-contained, no CDN dependency)
# ---------------------------------------------------------------------------

_KEYWORDS = (
    "public private protected static final class interface enum extends "
    "implements new return if else while for do switch case default break "
    "continue try catch finally throw throws synchronized volatile transient "
    "void int long double float boolean char byte short this super null true "
    "false package import abstract instanceof var record"
).split()

_TOKEN_RE = re.compile(
    r"(?P<comment>//[^\n]*|/\*.*?\*/)"
    r"|(?P<string>\"(?:\\.|[^\"\\])*\")"
    r"|(?P<char>'(?:\\.|[^'\\])')"
    r"|(?P<annotation>@[A-Za-z_][A-Za-z0-9_]*)"
    r"|(?P<keyword>\b(?:" + "|".join(_KEYWORDS) + r")\b)"
    r"|(?P<number>\b\d[\d_]*(?:\.\d+)?[fFdDlL]?\b)"
    r"|(?P<call>\b[a-zA-Z_][A-Za-z0-9_]*(?=\s*\())"
    r"|(?P<type>\b[A-Z][A-Za-z0-9_]*\b)",
    re.DOTALL,
)

def highlight_java(code: str) -> str:
    code = code.rstrip("\n")

    def repl(m):
        kind = m.lastgroup
        text = html.escape(m.group(0))
        return f'<span class="tok-{kind}">{text}</span>'

    # Escape first is wrong (would break regex on raw code); instead run regex
    # on raw code and escape only inside repl / default segments.
    out = []
    last = 0
    for m in _TOKEN_RE.finditer(code):
        if m.start() > last:
            out.append(html.escape(code[last:m.start()]))
        out.append(repl(m))
        last = m.end()
    out.append(html.escape(code[last:]))
    return "".join(out)


def code_block(code: str, *, lang_label: str = "java", filename: str = "") -> str:
    body = highlight_java(code)
    fname = f'<span class="code-fname">{html.escape(filename)}</span>' if filename else ""
    return f"""<div class="code-card">
  <div class="code-topbar">
    <span class="dot d1"></span><span class="dot d2"></span><span class="dot d3"></span>
    <span class="code-lang">{lang_label}</span>{fname}
  </div>
  <pre class="code-pre"><code>{body}</code></pre>
</div>"""


def code_columns(code: str, *, filename: str = "", split_ratio: float = 0.5) -> str:
    """Split a longer snippet's lines left-column -> right-column (newspaper style)."""
    lines = code.rstrip("\n").split("\n")
    n = len(lines)
    cut = max(1, int(n * split_ratio))
    left = "\n".join(lines[:cut])
    right = "\n".join(lines[cut:])
    fname = f'<span class="code-fname">{html.escape(filename)}</span>' if filename else ""
    return f"""<div class="code-card code-card-split">
  <div class="code-topbar">
    <span class="dot d1"></span><span class="dot d2"></span><span class="dot d3"></span>
    <span class="code-lang">java</span>{fname}
  </div>
  <div class="code-split-body">
    <pre class="code-pre code-pre-col"><code>{highlight_java(left)}</code></pre>
    <div class="code-split-divider"></div>
    <pre class="code-pre code-pre-col"><code>{highlight_java(right)}</code></pre>
  </div>
</div>"""


# ---------------------------------------------------------------------------
# Generic components
# ---------------------------------------------------------------------------

def esc(s: str) -> str:
    return html.escape(s)


def bullets(items, *, tight=False, icon="chev"):
    cls = "bullets tight" if tight else "bullets"
    lis = []
    for it in items:
        if isinstance(it, tuple):
            head, sub = it
            lis.append(f'<li><span class="b-head">{head}</span><span class="b-sub">{sub}</span></li>')
        else:
            lis.append(f"<li>{it}</li>")
    return f'<ul class="{cls} icon-{icon}">' + "".join(lis) + "</ul>"


def pill(text, kind="default"):
    return f'<span class="pill pill-{kind}">{text}</span>'


def kv_table(rows, headers=None, *, col_class=""):
    thead = ""
    if headers:
        thead = "<thead><tr>" + "".join(f"<th>{h}</th>" for h in headers) + "</tr></thead>"
    trs = []
    for row in rows:
        tds = "".join(f"<td>{c}</td>" for c in row)
        trs.append(f"<tr>{tds}</tr>")
    return f'<table class="kv-table {col_class}">{thead}<tbody>' + "".join(trs) + "</tbody></table>"


def two_col(left_html, right_html, *, ratio="1fr 1fr", gap="28px"):
    return (f'<div class="two-col" style="grid-template-columns:{ratio};gap:{gap};">'
            f'<div class="col col-left">{left_html}</div>'
            f'<div class="col col-right">{right_html}</div></div>')


def three_col(a, b, c, *, gap="24px"):
    return (f'<div class="three-col" style="gap:{gap};">'
            f'<div class="col">{a}</div><div class="col">{b}</div><div class="col">{c}</div></div>')


def stat_row(items, *, cols=None):
    """items: list of (head, sub) short strings, laid out in a compact horizontal row."""
    n = cols or len(items)
    cells = "".join(
        f'<div class="stat-cell"><div class="stat-head">{h}</div><div class="stat-sub">{s}</div></div>'
        for h, s in items
    )
    return f'<div class="stat-row" style="grid-template-columns:repeat({n},1fr);">{cells}</div>'


def stack(*parts, gap="20px"):
    inner = "".join(f'<div class="stack-item">{p}</div>' for p in parts)
    return f'<div class="stack" style="gap:{gap};">{inner}</div>'


def callout(text, kind="tip"):
    labels = {"tip": "KEY INSIGHT", "warn": "WATCH OUT", "trap": "CLASSIC TRAP", "fix": "THE FIX"}
    return f'<div class="callout callout-{kind}"><span class="callout-label">{labels.get(kind,kind.upper())}</span><div class="callout-body">{text}</div></div>'


_ARROW_DEFS = """<defs>
  <marker id="arrow" viewBox="0 0 10 10" refX="8" refY="5" markerWidth="7" markerHeight="7" orient="auto-start-reverse">
    <path d="M0,0 L10,5 L0,10 z" fill="#7d8aa0"/>
  </marker>
  <marker id="arrowA" viewBox="0 0 10 10" refX="8" refY="5" markerWidth="7" markerHeight="7" orient="auto-start-reverse">
    <path d="M0,0 L10,5 L0,10 z" fill="var(--accent1)"/>
  </marker>
</defs>"""

def diagram(svg_inner, *, vb=None, extra_class="", w=None, h=None):
    w = w or (W - 2*68)
    h = h or 300
    vb = vb or f"0 0 {w} {h}"
    return f'<div class="diagram {extra_class}"><svg viewBox="{vb}" xmlns="http://www.w3.org/2000/svg">{_ARROW_DEFS}{svg_inner}</svg></div>'


# ---- SVG primitives (compose diagrams quickly, consistent look) ----

def svg_box(x, y, w, h, title, sub="", *, accent=False, fill=None, stroke=None,
            title_size=14.5, sub_size=12, rx=10, title_color=None, sub_color="#93a1b5",
            dashed=False):
    fill = fill or ("rgba(34,211,238,0.10)" if accent else "rgba(255,255,255,0.045)")
    stroke = stroke or ("var(--accent1)" if accent else "rgba(255,255,255,0.16)")
    title_color = title_color or ("var(--accent1)" if accent else "#eef2f8")
    dash = ' stroke-dasharray="5,4"' if dashed else ""
    ty = y + h/2 - (6 if sub else -4)
    sub_html = f'<text x="{x+w/2}" y="{ty+20}" text-anchor="middle" font-size="{sub_size}" fill="{sub_color}">{esc(sub)}</text>' if sub else ""
    return f"""<g>
  <rect x="{x}" y="{y}" width="{w}" height="{h}" rx="{rx}" fill="{fill}" stroke="{stroke}" stroke-width="1.4"{dash}/>
  <text x="{x+w/2}" y="{ty}" text-anchor="middle" font-size="{title_size}" font-weight="700" fill="{title_color}">{esc(title)}</text>
  {sub_html}
</g>"""


def svg_panel(x, y, w, h, *, accent=False, fill=None, stroke=None, rx=12, dashed=False):
    fill = fill or ("rgba(34,211,238,0.08)" if accent else "rgba(255,255,255,0.04)")
    stroke = stroke or ("var(--accent1)" if accent else "rgba(255,255,255,0.14)")
    dash = ' stroke-dasharray="5,4"' if dashed else ""
    return f'<rect x="{x}" y="{y}" width="{w}" height="{h}" rx="{rx}" fill="{fill}" stroke="{stroke}" stroke-width="1.4"{dash}/>'


def svg_arrow(x1, y1, x2, y2, *, color="#7d8aa0", accent=False, dashed=False, width=1.8, curve=0):
    marker = "url(#arrowA)" if accent else "url(#arrow)"
    color = "var(--accent1)" if accent else color
    dash = ' stroke-dasharray="5,4"' if dashed else ""
    if curve:
        mx, my = (x1+x2)/2, (y1+y2)/2 - curve
        d = f"M{x1},{y1} Q{mx},{my} {x2},{y2}"
        return f'<path d="{d}" fill="none" stroke="{color}" stroke-width="{width}"{dash} marker-end="{marker}"/>'
    return f'<line x1="{x1}" y1="{y1}" x2="{x2}" y2="{y2}" stroke="{color}" stroke-width="{width}"{dash} marker-end="{marker}"/>'


def svg_text(x, y, text, *, size=13, color="#c3ccda", weight=400, anchor="start", italic=False, family=None):
    style = f' font-style="italic"' if italic else ""
    fam = f' font-family="{family}"' if family else ""
    return f'<text x="{x}" y="{y}" text-anchor="{anchor}" font-size="{size}" font-weight="{weight}" fill="{color}"{style}{fam}>{esc(text)}</text>'


def svg_pill(x, y, text, *, color="var(--accent1)", fg="#0a0d12", size=11.5, pad=8, h=20):
    w = pad*2 + len(text) * (size*0.62)
    return f"""<g>
  <rect x="{x-w/2}" y="{y-h/2}" width="{w}" height="{h}" rx="{h/2}" fill="{color}"/>
  <text x="{x}" y="{y+4}" text-anchor="middle" font-size="{size}" font-weight="800" fill="{fg}">{esc(text)}</text>
</g>"""


def svg_lane_label(x, y, text, *, color="#93a1b5"):
    return svg_text(x, y, text, size=12.5, color=color, weight=700)


# ---------------------------------------------------------------------------
# UML class-diagram primitives (for design-pattern decks)
# ---------------------------------------------------------------------------

def svg_uml_box(x, y, w, name, *, stereotype="", methods=None, accent=False, name_size=15):
    """Returns (svg_fragment, height). Draws a UML class box: optional
    <<stereotype>>, bold name, divider, then left-aligned method signatures."""
    methods = methods or []
    stroke = "var(--accent1)" if accent else "#6b7280"
    fill = "rgba(52,211,153,0.08)" if accent else "rgba(255,255,255,0.035)"
    name_color = "var(--accent1)" if accent else "#eef2f8"

    ty = y + 22
    svg = ""
    if stereotype:
        svg += f'<text x="{x+w/2}" y="{ty}" text-anchor="middle" font-size="10.5" font-style="italic" fill="#93a1b5">&#171;{esc(stereotype)}&#187;</text>'
        ty += 17
    svg += f'<text x="{x+w/2}" y="{ty+3}" text-anchor="middle" font-size="{name_size}" font-weight="800" fill="{name_color}">{esc(name)}</text>'
    content_bottom = ty + 14

    if methods:
        line_y = content_bottom + 8
        svg += f'<line x1="{x+10}" y1="{line_y}" x2="{x+w-10}" y2="{line_y}" stroke="rgba(255,255,255,0.16)"/>'
        my = line_y + 17
        for m in methods:
            svg += f'<text x="{x+12}" y="{my}" font-size="11" font-family="SF Mono, Menlo, monospace" fill="#9aa5b8">{esc(m)}</text>'
            my += 16
        content_bottom = my - 16 + 10
    else:
        content_bottom += 12

    h = content_bottom - y
    box = f'<rect x="{x}" y="{y}" width="{w}" height="{h}" rx="7" fill="{fill}" stroke="{stroke}" stroke-width="1.4"/>' + svg
    return box, h


def uml_edge(x1, y1, x2, y2, kind, *, label="", color="#6b7280", lw=1.5):
    """Draws a UML relationship edge from (x1,y1) to (x2,y2). The marker
    (triangle/diamond/arrowhead) always renders at the (x2,y2) end — pass
    points in the semantically correct order (child->parent, part->whole,
    user->used)."""
    import math
    dashed = kind in ("realize", "depend")
    dx, dy = x2 - x1, y2 - y1
    dist = max(1.0, math.hypot(dx, dy))
    ux, uy = dx / dist, dy / dist
    angle = math.degrees(math.atan2(dy, dx))

    trim = 0
    marker = ""
    if kind in ("realize", "inherit"):
        s = 15
        trim = s
        marker = (f'<g transform="translate({x2},{y2}) rotate({angle})">'
                  f'<polygon points="0,0 -{s*1.7},{s*0.62} -{s*1.7},{-s*0.62}" '
                  f'fill="#0a0d12" stroke="{color}" stroke-width="1.5"/></g>')
    elif kind in ("aggregate", "compose"):
        s = 8
        trim = s * 2.4
        dfill = "#0a0d12" if kind == "aggregate" else color
        marker = (f'<g transform="translate({x2},{y2}) rotate({angle})">'
                  f'<polygon points="0,0 -{s*1.2},{s} -{s*2.4},0 -{s*1.2},{-s}" '
                  f'fill="{dfill}" stroke="{color}" stroke-width="1.5"/></g>')
    elif kind in ("depend", "assoc"):
        s = 9
        trim = s
        marker = (f'<g transform="translate({x2},{y2}) rotate({angle})">'
                  f'<polyline points="-{s},{s*0.65} 0,0 -{s},{-s*0.65}" '
                  f'fill="none" stroke="{color}" stroke-width="1.6"/></g>')

    ex, ey = x2 - ux * trim, y2 - uy * trim
    dash = ' stroke-dasharray="6,4"' if dashed else ""
    line = f'<line x1="{x1}" y1="{y1}" x2="{ex}" y2="{ey}" stroke="{color}" stroke-width="{lw}"{dash}/>'

    lbl = ""
    if label:
        mx, my = (x1 + x2) / 2, (y1 + y2) / 2
        lw_px = len(label) * 6.3 + 10
        lbl = (f'<rect x="{mx-lw_px/2}" y="{my-10}" width="{lw_px}" height="16" rx="3" fill="#0a0d12"/>'
               f'<text x="{mx}" y="{my+2.5}" text-anchor="middle" font-size="10.5" fill="#93a1b5" font-style="italic">{esc(label)}</text>')
    return line + marker + lbl


def uml_legend_row(x, y, kind, label, meaning, *, w=120):
    """One row of the UML relationship legend: a short sample edge + label + meaning."""
    y0 = y + 14
    edge = uml_edge(x, y0, x + w, y0, kind)
    return (edge +
            svg_text(x, y - 6, label, size=12, color="#eef2f8", weight=800) +
            svg_text(x + w + 20, y0 + 4, meaning, size=12, color="#93a1b5"))


# ---------------------------------------------------------------------------
# Multi-class code grid (fit several small pattern classes on one slide)
# ---------------------------------------------------------------------------

def mini_code(code: str, *, filename: str = "") -> str:
    body = highlight_java(code)
    fname = f'<span class="mini-fname">{esc(filename)}</span>' if filename else ""
    return f"""<div class="mini-code">
  <div class="mini-code-top">{fname}</div>
  <pre class="mini-code-pre"><code>{body}</code></pre>
</div>"""


def code_grid(cards, *, cols=3, gap="14px"):
    inner = "".join(f'<div class="grid-cell">{c}</div>' for c in cards)
    return f'<div class="code-grid" style="grid-template-columns:repeat({cols},1fr);gap:{gap};">{inner}</div>'


# ---------------------------------------------------------------------------
# Slide shell
# ---------------------------------------------------------------------------

_slide_counter = {"n": 0}

def reset_counter():
    _slide_counter["n"] = 0


def slide(kicker, title, body_html, *, deck_label="", total=None, footer_note="",
          bare=False, header=True):
    _slide_counter["n"] += 1
    n = _slide_counter["n"]
    header_html = ""
    if header:
        header_html = f"""<div class="s-header">
      <div class="s-kicker">{kicker}</div>
      <h1 class="s-title">{title}</h1>
      <div class="s-rule"></div>
    </div>"""
    footer_html = f"""<div class="s-footer">
      <span class="f-left">{deck_label}</span>
      <span class="f-mid">{esc(footer_note)}</span>
      <span class="f-right">{n:02d}{f' / {total:02d}' if total else ''}</span>
    </div>"""
    bare_cls = " bare" if bare else ""
    return f"""<section class="slide{bare_cls}">
  {header_html}
  <div class="s-body">{body_html}</div>
  {footer_html}
  <div class="s-bg"></div>
</section>"""


def title_slide(deck_label, kicker, title, subtitle, meta_items, accent_name):
    metas = "".join(f'<div class="tm-item"><span class="tm-dot"></span>{m}</div>' for m in meta_items)
    return f"""<section class="slide title-slide">
  <div class="s-bg"></div>
  <div class="title-glow"></div>
  <div class="title-wrap">
    <div class="title-kicker">{kicker}</div>
    <h1 class="title-main">{title}</h1>
    <div class="title-sub">{subtitle}</div>
    <div class="title-meta">{metas}</div>
  </div>
  <div class="title-footer">{deck_label}</div>
</section>"""


def section_slide(index_str, title, blurb, deck_label):
    return f"""<section class="slide section-slide">
  <div class="s-bg"></div>
  <div class="section-wrap">
    <div class="section-index">{index_str}</div>
    <h1 class="section-title">{title}</h1>
    <div class="section-blurb">{blurb}</div>
  </div>
  <div class="s-footer"><span class="f-left">{deck_label}</span><span class="f-mid"></span><span class="f-right"></span></div>
</section>"""


def closing_slide(deck_label, title, lines):
    items = "".join(f'<li>{l}</li>' for l in lines)
    return f"""<section class="slide closing-slide">
  <div class="s-bg"></div>
  <div class="closing-wrap">
    <h1 class="closing-title">{title}</h1>
    <ul class="closing-list">{items}</ul>
  </div>
  <div class="s-footer"><span class="f-left">{deck_label}</span><span class="f-mid"></span><span class="f-right"></span></div>
</section>"""


# ---------------------------------------------------------------------------
# CSS
# ---------------------------------------------------------------------------

def build_css(accent1, accent2, accent_soft):
    return f"""
@page {{ size: {W}px {H}px; margin: 0; }}
* {{ box-sizing: border-box; }}
html, body {{ margin:0; padding:0; background:#0a0d12; }}
body {{
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", "Helvetica Neue", Arial, sans-serif;
  color: #e7ecf3;
}}

:root {{
  --accent1: {accent1};
  --accent2: {accent2};
  --accent-soft: {accent_soft};
  --bg0: #0a0d12;
  --bg1: #0e1319;
  --panel: rgba(255,255,255,0.045);
  --panel-border: rgba(255,255,255,0.09);
  --text-dim: #93a1b5;
  --text-mid: #c3ccda;
}}

.slide {{
  position: relative;
  width: {W}px;
  height: {H}px;
  overflow: hidden;
  page-break-after: always;
  background: var(--bg0);
  padding: 46px 68px 40px 68px;
  display: flex;
  flex-direction: column;
}}
.slide:last-child {{ page-break-after: auto; }}

.s-bg {{
  position: absolute; inset: 0; z-index: 0; pointer-events: none;
  background-image:
    radial-gradient(circle at 88% -8%, {accent_soft} 0%, transparent 45%),
    radial-gradient(circle at -6% 108%, {accent_soft} 0%, transparent 40%),
    linear-gradient(180deg, #0a0d12 0%, #0b0f15 100%);
}}
.s-bg::after {{
  content:"";
  position:absolute; inset:0;
  background-image: radial-gradient(rgba(255,255,255,0.05) 1px, transparent 1px);
  background-size: 26px 26px;
  opacity: 0.35;
  mask-image: radial-gradient(ellipse 90% 80% at 50% 40%, #000 40%, transparent 85%);
}}

.s-header, .s-body, .s-footer {{ position: relative; z-index: 1; }}

.s-header {{ margin-bottom: 22px; flex-shrink: 0; }}
.s-kicker {{
  font-size: 13px; font-weight: 700; letter-spacing: 2.6px; text-transform: uppercase;
  color: var(--accent1);
  margin-bottom: 8px;
}}
.s-title {{
  font-size: 34px; font-weight: 800; margin: 0; letter-spacing: -0.2px;
  color: #f5f8fc;
}}
.s-rule {{
  height: 4px; width: 64px; margin-top: 14px; border-radius: 4px;
  background: linear-gradient(90deg, var(--accent1), var(--accent2));
}}

.s-body {{ flex: 1; min-height: 0; }}

.s-footer {{
  flex-shrink: 0; margin-top: 16px; padding-top: 12px;
  border-top: 1px solid rgba(255,255,255,0.07);
  display: flex; justify-content: space-between; align-items: center;
  font-size: 12px; color: var(--text-dim); letter-spacing: 0.4px;
}}
.f-left {{ font-weight: 700; color: var(--text-mid); text-transform: uppercase; letter-spacing: 1.6px; font-size: 11px;}}
.f-mid {{ color: var(--text-dim); font-style: italic; }}
.f-right {{ font-variant-numeric: tabular-nums; }}

/* ---------------- Title slide ---------------- */
.title-slide {{ align-items: center; justify-content: center; padding: 0; }}
.title-glow {{
  position:absolute; z-index:0; inset:0;
  background: radial-gradient(650px 420px at 50% 38%, {accent_soft}, transparent 70%);
}}
.title-wrap {{ position:relative; z-index:1; text-align:center; max-width: 980px; }}
.title-kicker {{
  font-size: 15px; font-weight: 700; letter-spacing: 6px; text-transform: uppercase;
  color: var(--accent1); margin-bottom: 26px;
}}
.title-main {{
  font-size: 68px; font-weight: 800; margin: 0 0 22px 0; line-height: 1.08;
  letter-spacing: -1.5px; color: #f5f8fc;
}}
.title-main .grad {{ color: var(--accent1); }}
.title-sub {{ font-size: 22px; color: var(--text-mid); font-weight: 400; margin-bottom: 40px; line-height:1.5;}}
.title-meta {{ display:flex; gap:28px; justify-content:center; flex-wrap: wrap; }}
.tm-item {{ font-size: 14px; color: var(--text-dim); display:flex; align-items:center; gap:8px; font-weight:600; letter-spacing:0.3px;}}
.tm-dot {{ width:7px; height:7px; border-radius:50%; background: linear-gradient(135deg,var(--accent1),var(--accent2)); display:inline-block;}}
.title-footer {{ position:absolute; bottom: 34px; z-index:1; font-size:12px; letter-spacing:3px; color: var(--text-dim); text-transform:uppercase; font-weight:700;}}

/* ---------------- Section break slide ---------------- */
.section-slide {{ align-items:flex-start; justify-content:center; padding: 0 90px; }}
.section-wrap {{ position:relative; z-index:1; max-width: 900px; }}
.section-index {{
  font-size: 110px; font-weight: 800; line-height:1;
  color: var(--accent1); opacity: 0.9;
  margin-bottom: 6px; letter-spacing: -3px;
}}
.section-title {{ font-size: 46px; font-weight: 800; color:#f5f8fc; margin: 0 0 18px 0; letter-spacing:-0.5px;}}
.section-blurb {{ font-size: 19px; color: var(--text-mid); line-height:1.6; max-width: 760px;}}

/* ---------------- Closing slide ---------------- */
.closing-slide {{ align-items:center; justify-content:center; padding: 0 100px; text-align:left; }}
.closing-wrap {{ max-width: 920px; position:relative; z-index:1;}}
.closing-title {{
  font-size: 42px; font-weight: 800; margin: 0 0 26px 0; color:#f5f8fc;
}}
.closing-title .grad {{ color: var(--accent1); }}
.closing-list {{ list-style:none; margin:0; padding:0; display:flex; flex-direction:column; gap:14px;}}
.closing-list li {{
  font-size: 18px; color: var(--text-mid); padding-left: 30px; position:relative; line-height:1.5;
}}
.closing-list li::before {{
  content:"›"; position:absolute; left:0; top:-2px; color: var(--accent1); font-size:24px; font-weight:800;
}}

/* ---------------- Bullets ---------------- */
.bullets {{ list-style:none; margin:0; padding:0; display:flex; flex-direction:column; gap:14px; }}
.bullets.tight {{ gap: 9px; }}
.bullets li {{
  position:relative; padding-left: 26px; font-size: 16.5px; line-height:1.5; color: var(--text-mid);
}}
.bullets.icon-chev li::before {{
  content:""; position:absolute; left:2px; top:8px; width:9px; height:9px;
  background: linear-gradient(135deg,var(--accent1),var(--accent2));
  clip-path: polygon(0 0, 100% 50%, 0 100%);
}}
.bullets li .b-head {{ display:block; font-weight:700; color:#eef2f8; font-size:16.5px; margin-bottom:2px;}}
.bullets li .b-sub {{ display:block; color: var(--text-dim); font-size:14.5px; }}
.bullets b, .bullets strong {{ color:#fff; }}
code.inline, .s-body code.inline {{
  font-family: "SF Mono", "Menlo", "Consolas", monospace; background: rgba(255,255,255,0.08);
  padding: 1px 6px; border-radius:5px; font-size:0.9em; color: #ffd88a;
}}

/* ---------------- Cards / panels ---------------- */
.panel {{
  background: var(--panel); border:1px solid var(--panel-border); border-radius: 14px;
  padding: 20px 22px;
}}
.panel-title {{
  font-size: 12.5px; font-weight: 800; letter-spacing: 1.6px; text-transform:uppercase;
  color: var(--accent1); margin-bottom: 10px;
}}

/* ---------------- Layout helpers ---------------- */
.two-col {{ display:grid; height:100%; align-items: start; }}
.three-col {{ display:grid; grid-template-columns: 1fr 1fr 1fr; height:100%; }}
.col {{ min-width:0; }}

.stack {{ display:flex; flex-direction:column; height:100%; min-height:0; }}
.stack-item {{ flex-shrink:0; }}
.stack-item:last-child {{ flex:1; min-height:0; }}

.stat-row {{ display:grid; gap:20px; }}
.stat-cell {{
  padding: 13px 16px; border-radius: 12px; background: rgba(255,255,255,0.035);
  border: 1px solid rgba(255,255,255,0.08); border-left: 3px solid var(--accent1);
}}
.stat-head {{ font-size: 15px; font-weight: 800; color:#eef2f8; margin-bottom: 3px; }}
.stat-sub {{ font-size: 12.5px; color: var(--text-dim); line-height:1.4; }}

/* ---------------- Pills ---------------- */
.pill {{
  display:inline-block; padding: 3px 11px; border-radius: 999px; font-size: 12px; font-weight:700;
  letter-spacing: 0.4px; margin: 2px 6px 2px 0; border:1px solid rgba(255,255,255,0.14);
  color: var(--text-mid); background: rgba(255,255,255,0.05);
}}
.pill-accent {{ color:#0a0d12; background: linear-gradient(120deg,var(--accent1),var(--accent2)); border:none; }}

/* ---------------- Callouts ---------------- */
.callout {{
  border-radius: 12px; padding: 14px 18px; border-left: 4px solid var(--accent1);
  background: rgba(255,255,255,0.045);
  display:flex; flex-direction:column; gap:4px;
}}
.callout-label {{ font-size:11px; font-weight:800; letter-spacing:1.6px; color: var(--accent1); }}
.callout-body {{ font-size: 15px; color: var(--text-mid); line-height:1.5; }}
.callout-warn {{ border-left-color:#fb923c; }}
.callout-warn .callout-label {{ color:#fb923c; }}
.callout-trap {{ border-left-color:#f87171; }}
.callout-trap .callout-label {{ color:#f87171; }}
.callout-fix {{ border-left-color:#34d399; }}
.callout-fix .callout-label {{ color:#34d399; }}

/* ---------------- Tables ---------------- */
.kv-table {{ width:100%; border-collapse: collapse; font-size: 14.5px; }}
.kv-table th {{
  text-align:left; font-size: 11.5px; letter-spacing:1.2px; text-transform:uppercase;
  color: var(--accent1); padding: 0 14px 10px 0; border-bottom: 1px solid rgba(255,255,255,0.14);
  font-weight: 800;
}}
.kv-table td {{
  padding: 10px 14px 10px 0; border-bottom: 1px solid rgba(255,255,255,0.06);
  color: var(--text-mid); vertical-align: top; line-height:1.4;
}}
.kv-table tr:last-child td {{ border-bottom:none; }}
.kv-table td:first-child, .kv-table th:first-child {{ color:#eef2f8; font-weight:700; padding-left:0; }}

/* ---------------- Code blocks ---------------- */
.code-card {{
  background: #0d1117; border: 1px solid rgba(255,255,255,0.09); border-radius: 12px;
  overflow: hidden; height:100%; display:flex; flex-direction:column;
  box-shadow: 0 14px 30px rgba(0,0,0,0.35);
}}
.code-topbar {{
  display:flex; align-items:center; gap:7px; padding: 9px 14px;
  background: rgba(255,255,255,0.035); border-bottom: 1px solid rgba(255,255,255,0.07);
  flex-shrink:0;
}}
.dot {{ width:9px; height:9px; border-radius:50%; }}
.d1 {{ background:#ff5f56; }} .d2 {{ background:#ffbd2e; }} .d3 {{ background:#27c93f; }}
.code-lang {{ margin-left:8px; font-size:11px; color:#7a8699; font-weight:700; letter-spacing:1px; text-transform:uppercase;}}
.code-fname {{ margin-left:auto; font-size:11.5px; color:#5b6577; font-family:"SF Mono",Menlo,monospace;}}
.code-pre {{
  margin:0; padding: 14px 18px; overflow:hidden; flex:1;
  font-family: "SF Mono", "Menlo", "Consolas", monospace;
  font-size: 13.3px; line-height: 1.58; color:#c9d1d9; white-space:pre;
}}
.code-card-split .code-split-body {{ display:flex; flex:1; min-height:0; }}
.code-pre-col {{ flex:1; width:50%; }}
.code-split-divider {{ width:1px; background: rgba(255,255,255,0.09); margin: 10px 0; flex-shrink:0;}}

.tok-comment {{ color:#6b7280; font-style: italic; }}
.tok-string {{ color:#a5d6a7; }}
.tok-char {{ color:#a5d6a7; }}
.tok-annotation {{ color:#f6ad55; }}
.tok-keyword {{ color:#ff7ab6; font-weight:600;}}
.tok-number {{ color:#f6ad55; }}
.tok-call {{ color:#82d4ff; }}
.tok-type {{ color:#82e6d4; }}

/* ---------------- Pattern-type cards ---------------- */
.type-grid {{ display:grid; grid-template-columns:1fr 1fr 1fr; gap:22px; height:100%; }}
.type-card {{
  background: var(--panel); border:1px solid var(--panel-border); border-radius:14px;
  padding:22px 22px 18px 22px; display:flex; flex-direction:column;
  border-top:3px solid var(--accent1);
}}
.type-label {{ font-size:20px; font-weight:800; color:#f5f8fc; margin-bottom:10px; }}
.type-blurb {{ font-size:14px; color:var(--text-mid); line-height:1.5; margin-bottom:16px; }}
.type-list {{ list-style:none; margin:0 0 16px 0; padding:0; display:flex; flex-direction:column; gap:9px; flex:1; }}
.type-list li {{
  font-size:14.5px; color:#eef2f8; font-weight:700; padding:8px 12px; border-radius:8px;
  background: rgba(255,255,255,0.04); border:1px solid rgba(255,255,255,0.07);
}}
.type-other {{ font-size:12px; color:var(--text-dim); line-height:1.5; padding-top:12px; border-top:1px solid rgba(255,255,255,0.08); }}

/* ---------------- Multi-class code grid ---------------- */
.code-grid {{ display:grid; height:100%; min-height:0; grid-auto-rows:min-content; align-content:start; }}
.grid-cell {{ min-height:0; min-width:0; }}
.mini-code {{
  background:#0d1117; border:1px solid rgba(255,255,255,0.09); border-radius:10px;
  height:100%; display:flex; flex-direction:column; overflow:hidden;
  box-shadow: 0 8px 18px rgba(0,0,0,0.28);
}}
.mini-code-top {{
  padding:6px 10px; background:rgba(255,255,255,0.035); border-bottom:1px solid rgba(255,255,255,0.07);
  flex-shrink:0;
}}
.mini-fname {{ font-size:10.5px; color:#5b6577; font-family:"SF Mono",Menlo,monospace; }}
.mini-code-pre {{
  margin:0; padding:9px 11px; overflow:hidden; flex:1;
  font-family:"SF Mono","Menlo",monospace; font-size:10.6px; line-height:1.44; color:#c9d1d9;
  white-space:pre-wrap; word-break:break-word;
}}

/* ---------------- Diagram ---------------- */
.diagram {{ width:100%; height:100%; display:flex; align-items:center; justify-content:center; }}
.diagram svg {{ width:100%; height:100%; }}

/* svg text defaults */
svg text {{ font-family: -apple-system, "Segoe UI", sans-serif; }}
"""
