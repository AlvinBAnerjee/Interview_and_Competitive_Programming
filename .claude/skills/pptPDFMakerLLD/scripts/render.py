"""
Renders slide HTML (built with slidelib.py) to a landscape PDF using headless
Chrome's native --print-to-pdf. This is the whole rendering pipeline: no
pptx/reportlab/weasyprint dependency, just Chrome, which is already on the
machine and renders the CSS/SVG exactly as designed.
"""
import subprocess
import os

_CANDIDATE_CHROME_PATHS = [
    os.environ.get("PPTPDFMAKER_CHROME", ""),
    "/Applications/Google Chrome.app/Contents/MacOS/Google Chrome",
    "/Applications/Chromium.app/Contents/MacOS/Chromium",
    "/usr/bin/google-chrome",
    "/usr/bin/chromium",
    "/usr/bin/chromium-browser",
]


def find_chrome():
    for path in _CANDIDATE_CHROME_PATHS:
        if path and os.path.exists(path):
            return path
    raise RuntimeError(
        "Could not find a Chrome/Chromium binary. Set PPTPDFMAKER_CHROME to its path."
    )


def html_doc(title, css, slides_html):
    return f"""<!doctype html>
<html><head><meta charset="utf-8"><title>{title}</title>
<style>{css}</style></head>
<body>
{''.join(slides_html)}
</body></html>"""


def write_and_pdf(html_str, out_html, out_pdf):
    """Writes the HTML to disk, then prints it to PDF via headless Chrome.
    Returns (ok: bool, stderr_tail: str)."""
    with open(out_html, "w") as f:
        f.write(html_str)
    chrome = find_chrome()
    cmd = [
        chrome, "--headless=new", "--disable-gpu", "--no-pdf-header-footer",
        f"--print-to-pdf={out_pdf}", "--print-to-pdf-no-header",
        "--no-sandbox", "--virtual-time-budget=10000",
        out_html,
    ]
    r = subprocess.run(cmd, capture_output=True, text=True, timeout=120)
    ok = os.path.exists(out_pdf) and os.path.getsize(out_pdf) > 1000
    return ok, (r.stderr[-2000:] if not ok else "")
