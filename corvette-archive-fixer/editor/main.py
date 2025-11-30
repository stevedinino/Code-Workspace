#!/usr/bin/env python3
"""
main.py - HTML cleanup pipeline for Corvette Archive
Includes: harvesting linked CSS, rewriting CSS urls, copying assets, injecting styles/layout.css
"""

import os
import json
import shutil
import urllib.parse
import re
import mimetypes
from bs4 import BeautifulSoup, Comment

# === Paths ===
BASE_DIR = os.path.dirname(__file__)                     # editor folder
ROOT_DIR = os.path.abspath(os.path.join(BASE_DIR, "..")) # project root

MAP_FILE = os.path.join(ROOT_DIR, "maps", "fs-map.json")
CONFIG_FILE = os.path.join(BASE_DIR, "cleanup-config.json")

OUTPUT_DIR = os.path.join(ROOT_DIR, "cleaned")
IMAGE_DIR = os.path.join(OUTPUT_DIR, "images")
FONT_DIR = os.path.join(OUTPUT_DIR, "fonts")
STYLE_DIR = os.path.join(OUTPUT_DIR, "styles")
LAYOUT_CSS = os.path.join(STYLE_DIR, "layout.css")

LOG_FILE = os.path.join(ROOT_DIR, "maps", "run.log")

# === Helpers ===

def load_json(path):
    with open(path, "r", encoding="utf-8") as f:
        return json.load(f)

def ensure_dirs():
    os.makedirs(OUTPUT_DIR, exist_ok=True)
    os.makedirs(IMAGE_DIR, exist_ok=True)
    os.makedirs(FONT_DIR, exist_ok=True)
    os.makedirs(STYLE_DIR, exist_ok=True)
    if not os.path.exists(LAYOUT_CSS):
        with open(LAYOUT_CSS, "w", encoding="utf-8") as f:
            f.write("/* Captured layout styles */\n\n")

def log_action(log_f, message):
    print(message)
    log_f.write(message + "\n")

def capture_style(css_text):
    if css_text and css_text.strip():
        with open(LAYOUT_CSS, "a", encoding="utf-8") as f:
            f.write(css_text.strip() + "\n\n")

def capture_class_styles(classes):
    if not classes:
        return
    with open(LAYOUT_CSS, "a", encoding="utf-8") as f:
        for cls in classes:
            safe = cls.strip().replace(" ", ".")
            if safe:
                f.write(f".{safe} {{ /* captured class */ }}\n")
        f.write("\n")

def slug_from_entry(html_entry):
    rel = html_entry.get("path", "")
    folder = os.path.basename(os.path.dirname(rel))
    if not folder:
        folder = os.path.splitext(os.path.basename(rel))[0]
    return folder

def output_path_for_entry(html_entry):
    slug = slug_from_entry(html_entry)
    return os.path.join(OUTPUT_DIR, f"{slug}.html")

# === Image resolution helpers ===

def safe_get_images_root(map_data):
    """
    Infer a likely images root directory using map_data["root"] and images mapping.
    """
    archive_root = map_data.get("root") or ROOT_DIR
    images = (map_data or {}).get("images", {}) or {}

    # explicit keys
    for key in ("root", "base", "path"):
        root = images.get(key)
        if root:
            candidate = root if os.path.isabs(root) else os.path.normpath(os.path.join(archive_root, root))
            if os.path.isdir(candidate):
                return candidate

    # derive from files mapping
    try:
        file_map = images.get("files", {}) or {}
        for val in file_map.values():
            if isinstance(val, list) and val:
                first = val[0]
                p = first.get("path") if isinstance(first, dict) else None
            elif isinstance(val, str):
                p = val
            else:
                p = None
            if p:
                candidate = p if os.path.isabs(p) else os.path.normpath(os.path.join(archive_root, p))
                if os.path.isdir(os.path.dirname(candidate)):
                    return os.path.dirname(candidate)
    except Exception:
        pass

    # common fallbacks under archive root
    for c in [os.path.join(archive_root, "wp-content", "uploads"),
              os.path.join(archive_root, "uploads"),
              os.path.join(archive_root, "images")]:
        if os.path.isdir(c):
            return c
    return None

def resolve_image_source(src, images_root, map_data):
    """
    Build candidate absolute file paths for an image src using:
    - map_data['images']['files'] (string or list-of-objects with 'path')
    - map_data['root'] for absolute URL/relative resolution
    - common fallbacks by basename
    """
    if not src:
        return []
    archive_root = map_data.get("root") or ROOT_DIR

    # sanitize src and protocol-relative
    src_clean = src.split("?", 1)[0].split("#", 1)[0].strip()
    if src_clean.startswith("//"):
        src_clean = "http:" + src_clean

    candidates = []

    # prefer explicit mappings
    try:
        images_map = (map_data or {}).get("images", {}) or {}
        file_map = images_map.get("files", {}) or {}
        for key in (src_clean, os.path.basename(src_clean)):
            mapped = file_map.get(key)
            if mapped:
                if isinstance(mapped, str):
                    p = mapped if os.path.isabs(mapped) else os.path.normpath(os.path.join(archive_root, mapped))
                    candidates.append(p)
                elif isinstance(mapped, list):
                    for entry in mapped:
                        p = entry.get("path") if isinstance(entry, dict) else (entry if isinstance(entry, str) else None)
                        if p:
                            p = p if os.path.isabs(p) else os.path.normpath(os.path.join(archive_root, p))
                            candidates.append(p)
                if candidates:
                    break
    except Exception:
        pass

    # resolve URL or relative path under archive root
    if src_clean.startswith(("http://", "https://")):
        parsed = urllib.parse.urlparse(src_clean)
        path_part = parsed.path.lstrip("/")
        candidates.append(os.path.normpath(os.path.join(archive_root, path_part)))
    else:
        rel = src_clean.lstrip("./")
        candidates.append(os.path.normpath(os.path.join(archive_root, rel)))

    # basename fallbacks
    basename = os.path.basename(src_clean)
    candidates.extend([
        os.path.normpath(os.path.join(archive_root, "wp-content", "uploads", basename)),
        os.path.normpath(os.path.join(archive_root, "uploads", basename)),
        os.path.normpath(os.path.join(archive_root, "images", basename)),
        os.path.normpath(os.path.join(images_root or "", basename))
    ])

    # dedupe preserving order
    out, seen = [], set()
    for c in candidates:
        if not c:
            continue
        n = os.path.normpath(c)
        if n not in seen:
            seen.add(n)
            out.append(n)
    return out
# === CSS harvesting and asset helpers ===

def rewrite_css_urls(css_text, archive_root, map_data, log_f):
    """
    Rewrite url(...) values to point to cleaned/images or cleaned/fonts.
    Copy assets when found under archive_root.
    """
    def repl(m):
        raw = m.group(1).strip().strip('\'"')
        clean = raw.split("?", 1)[0].split("#", 1)[0]
        # ignore data: URLs
        if clean.startswith("data:"):
            return f"url({raw})"
        filename = os.path.basename(clean)
        ext = os.path.splitext(filename)[1].lower()
        # font extensions
        if ext in (".woff", ".woff2", ".ttf", ".otf", ".eot"):
            dest_sub = "fonts"
            dest_dir = FONT_DIR
            dest_path = os.path.join(dest_dir, filename)
            # try to copy from archive
            copied = copy_asset_if_exists(clean, archive_root, dest_path, map_data, log_f)
            return f"url(../fonts/{filename})" if copied else f"url(fonts/{filename})"
        else:
            dest_sub = "images"
            dest_dir = IMAGE_DIR
            dest_path = os.path.join(dest_dir, filename)
            copied = copy_asset_if_exists(clean, archive_root, dest_path, map_data, log_f)
            return f"url(../images/{filename})" if copied else f"url(images/{filename})"

    return re.sub(r"url\(([^)]+)\)", repl, css_text, flags=re.IGNORECASE)

def copy_asset_if_exists(asset_href, archive_root, dest_path, map_data, log_f):
    """
    Try to locate asset_href under archive_root or via map_data images mapping.
    If found, copy to dest_path and return True. Otherwise return False.
    """
    # candidate resolution: absolute path under archive_root, or mapped via images.files
    candidates = []
    href_clean = asset_href.strip()
    if href_clean.startswith(("http://", "https://", "//")):
        parsed = urllib.parse.urlparse(href_clean)
        path_part = parsed.path.lstrip("/")
        candidates.append(os.path.normpath(os.path.join(archive_root, path_part)))
    else:
        rel = href_clean.lstrip("./")
        candidates.append(os.path.normpath(os.path.join(archive_root, rel)))

    # check map_data images.files for basename
    try:
        images_map = (map_data or {}).get("images", {}) or {}
        file_map = images_map.get("files", {}) or {}
        basename = os.path.basename(href_clean)
        mapped = file_map.get(basename)
        if mapped:
            if isinstance(mapped, str):
                p = mapped if os.path.isabs(mapped) else os.path.normpath(os.path.join(archive_root, mapped))
                candidates.insert(0, p)
            elif isinstance(mapped, list):
                for entry in mapped:
                    p = entry.get("path") if isinstance(entry, dict) else (entry if isinstance(entry, str) else None)
                    if p:
                        p = p if os.path.isabs(p) else os.path.normpath(os.path.join(archive_root, p))
                        candidates.insert(0, p)
    except Exception:
        pass

    # try candidates
    for c in candidates:
        if not c:
            continue
        if os.path.isfile(c):
            try:
                os.makedirs(os.path.dirname(dest_path), exist_ok=True)
                shutil.copyfile(c, dest_path)
                log_action(log_f, f"Copied asset {c} -> {dest_path}")
                return True
            except Exception as e:
                log_action(log_f, f"Error copying asset {c}: {e}")
                return False
    # not found
    log_action(log_f, f"Asset not found for CSS url: {asset_href}; tried: {candidates}")
    return False

def harvest_linked_stylesheets(soup, map_data, config, log_f):
    """
    Append contents of local linked stylesheets to styles/layout.css,
    rewrite their asset URLs, copy assets when possible, and remove the original <link> tags.
    """
    if not config.get("harvest_linked_css", True):
        return False

    archive_root = map_data.get("root") or ROOT_DIR
    modified = False

    for link in list(soup.find_all("link", rel="stylesheet")):
        href = (link.get("href") or "").strip()
        if not href:
            continue

        # skip remote CSS unless it maps to local path
        if href.startswith(("http://", "https://", "//")):
            parsed = urllib.parse.urlparse(href)
            path_part = parsed.path.lstrip("/")
            candidate = os.path.normpath(os.path.join(archive_root, path_part))
            if not os.path.isfile(candidate):
                log_action(log_f, f"Skipping remote stylesheet not found locally: {href}")
                continue
            picked = candidate
        else:
            # resolve relative to archive_root
            candidate = os.path.normpath(os.path.join(archive_root, href.lstrip("./")))
            if os.path.isfile(candidate):
                picked = candidate
            else:
                # try common theme and upload locations
                alt_candidates = [
                    os.path.normpath(os.path.join(archive_root, href.lstrip("/"))),
                    os.path.normpath(os.path.join(archive_root, "wp-content", href.lstrip("/"))),
                    os.path.normpath(os.path.join(archive_root, "wp-content", "themes", href.lstrip("/"))),
                    os.path.normpath(os.path.join(archive_root, "wp-content", "uploads", href.lstrip("/")))
                ]
                picked = None
                for p in alt_candidates:
                    if os.path.isfile(p):
                        picked = p
                        break
                if not picked:
                    log_action(log_f, f"Stylesheet not found locally: {href}; tried: {alt_candidates}")
                    continue

        try:
            with open(picked, "r", encoding="utf-8", errors="ignore") as cssf:
                css_text = cssf.read()
            css_text = rewrite_css_urls(css_text, archive_root, map_data, log_f)
            capture_style(f"/* harvested: {href} -> {picked} */\n{css_text}")
            log_action(log_f, f"Harvested stylesheet: {href} ({picked})")
            link.decompose()
            modified = True
        except Exception as e:
            log_action(log_f, f"Error harvesting stylesheet {href}: {e}")

    return modified

def inject_layout_stylesheet(soup, config, log_f):
    if not config.get("inject_layout_css", True):
        return False
    head = soup.find("head")
    if not head:
        head = soup.new_tag("head")
        html = soup.find("html")
        if html:
            html.insert(0, head)
        else:
            soup.insert(0, head)
    # avoid duplicate link
    existing = head.find("link", rel="stylesheet", href="styles/layout.css")
    if existing:
        return False
    link_tag = soup.new_tag("link", rel="stylesheet", href="styles/layout.css")
    head.append(link_tag)
    log_action(log_f, "Injected stylesheet link to styles/layout.css")
    return True
# === Cleanup functions (with class-preservation tweaks) ===

def remove_dead_links(soup, config, log_f):
    if not config.get("remove_dead_links", False):
        return False
    modified = False
    for a in list(soup.find_all("a")):
        href = (a.get("href") or "").lower()
        if "404" in href or "deadlink" in href:
            a.replace_with(a.get_text() or "")
            log_action(log_f, f"Removed dead link: {href}")
            modified = True
    return modified

def remove_wp_styles(soup, config, log_f):
    if not config.get("remove_wp_styles", False):
        return False
    modified = False
    for style in list(soup.find_all("style")):
        css = style.string or ""
        capture_style(css)
        style.decompose()
        log_action(log_f, "Removed inline <style>")
        modified = True
    return modified

def strip_article_taxonomy_classes(soup, config, log_f):
    """
    Preserve structural/layout classes while removing taxonomy/meta classes.
    """
    if not config.get("strip_article_classes", False):
        return False
    modified = False
    keep_prefixes = ("site-", "menu", "nav", "entry-", "container", "row", "col-", "content", "header", "footer", "site", "main", "widget")
    drop_prefixes = ("category-", "tag-", "postid-", "type-", "format-", "cat-", "tag-")
    for article in list(soup.find_all("article")):
        classes = article.get("class") or []
        if classes:
            kept = [c for c in classes if any(c.startswith(p) for p in keep_prefixes) or not any(c.startswith(dp) for dp in drop_prefixes)]
            dropped = [c for c in classes if c not in kept]
            if dropped:
                capture_class_styles(dropped)
                if kept:
                    article["class"] = kept
                else:
                    article.attrs.pop("class", None)
                log_action(log_f, f"Stripped taxonomy classes, kept structural: kept={kept} dropped={dropped}")
                modified = True
    return modified

# Reuse other cleanup functions from previous version (remove_nav_containers, remove_footer, etc.)
def remove_oembed_links(soup, config, log_f):
    if not config.get("remove_oembed_links", False):
        return False
    modified = False
    for link in list(soup.find_all("link", rel="alternate")):
        if "oembed" in (link.get("type") or ""):
            href = link.get("href") or ""
            link.decompose()
            log_action(log_f, f"Removed oEmbed link: {href}")
            modified = True
    return modified

def remove_dns_prefetch_links(soup, config, log_f):
    if not config.get("remove_dns_prefetch", False):
        return False
    modified = False
    for link in list(soup.find_all("link", rel="dns-prefetch")):
        href = link.get("href") or ""
        link.decompose()
        log_action(log_f, f"Removed dns-prefetch link: {href}")
        modified = True
    return modified

def remove_nav_containers(soup, config, log_f):
    if not config.get("remove_nav", False):
        return False
    modified = False
    for nav in list(soup.find_all("nav")):
        nav.decompose()
        log_action(log_f, "Removed <nav> container")
        modified = True
    return modified

def remove_footer(soup, config, log_f):
    if not config.get("remove_footer", False):
        return False
    modified = False
    for footer in list(soup.find_all("footer")):
        footer.decompose()
        log_action(log_f, "Removed <footer>")
        modified = True
    return modified

def remove_tagline(soup, config, log_f):
    if not config.get("remove_tagline", False):
        return False
    modified = False
    for h2 in list(soup.find_all("h2")):
        classes = h2.get("class") or []
        if any("tagline" in c.lower() for c in classes):
            h2.decompose()
            log_action(log_f, "Removed tagline <h2>")
            modified = True
    return modified

def remove_comments(soup, config, log_f):
    if not config.get("remove_comments", False):
        return False
    modified = False
    for comment in soup.find_all(string=lambda text: isinstance(text, Comment)):
        comment.extract()
        log_action(log_f, "Removed HTML comment")
        modified = True
    return modified

def remove_ld_json(soup, config, log_f):
    if not config.get("remove_ld_json", False):
        return False
    modified = False
    for script in list(soup.find_all("script", type="application/ld+json")):
        script.decompose()
        log_action(log_f, "Removed <script type='application/ld+json'>")
        modified = True
    return modified

def clean_meta(soup, config, log_f):
    if not config.get("clean_meta", False):
        return False
    modified = False
    for meta in list(soup.find_all("meta")):
        name = (meta.get("name") or "").lower()
        if name == "generator":
            meta.decompose()
            log_action(log_f, "Removed meta[name='generator']")
            modified = True
    return modified

# normalize_images_in_html reused from previous version (keeps map_data root usage)
def normalize_images_in_html(soup, html_entry, config, map_data, log_f):
    if not config.get("normalize_images", False):
        return False
    modified = False
    images_root = safe_get_images_root(map_data)
    attrs = ["src", "data-src", "data-original", "data-lazy", "data-srcset", "srcset"]

    for img in list(soup.find_all("img")):
        original_src, attr_used = None, None
        for a in attrs:
            val = img.get(a)
            if val:
                original_src = val
                attr_used = a
                break
        if not original_src:
            continue

        if attr_used and attr_used.endswith("srcset"):
            first = original_src.split(",")[0].strip().split(" ")[0]
            original_src = first

        filename = os.path.basename(original_src.split("?", 1)[0].split("#", 1)[0])
        if not filename:
            log_action(log_f, f"Skipping image with no filename: {original_src}")
            continue

        new_src = f"images/{filename}"
        candidates = resolve_image_source(original_src, images_root, map_data)
        dest = os.path.join(IMAGE_DIR, filename)
        copied = False

        for candidate in candidates:
            try:
                if os.path.isfile(candidate):
                    os.makedirs(os.path.dirname(dest), exist_ok=True)
                    shutil.copyfile(candidate, dest)
                    copied = True
                    log_action(log_f, f"Copied image from {candidate} -> {dest}")
                    break
            except Exception as e:
                log_action(log_f, f"Error copying {candidate}: {e}")

        if not copied:
            if os.path.isfile(dest):
                log_action(log_f, f"Image already present in cleaned/images: {dest} (skipped copy)")
            else:
                log_action(log_f, f"LOGIC FAIL: Could not resolve image {original_src}; candidates tried: {candidates}")

        img["src"] = new_src
        for a in ["data-src", "data-original", "data-lazy", "data-srcset", "srcset"]:
            if a in img.attrs:
                img.attrs.pop(a, None)

        log_action(log_f, f"Normalized image: {original_src} -> {new_src} (copied={copied})")
        modified = True

    return modified

def remove_plugin_scripts(soup, config, log_f):
    if not config.get("remove_plugin_scripts", False):
        return False
    modified = False
    plugin_keywords = [
        "google-analytics-for-wordpress",
        "monsterinsights",
        "modernizr",
        "jquery",
        "wp-emoji-release",
        "googletagmanager",
        "__gatracker",
        "mi_version",
        "ga-disable",
        "gtag("
    ]
    for script in list(soup.find_all("script")):
        src = (script.get("src") or "").lower()
        text = (script.string or script.get_text() or "") or ""
        text_l = text.lower()
        if any(k in src for k in plugin_keywords) or any(k in text_l for k in plugin_keywords):
            script.decompose()
            log_action(log_f, f"Removed plugin/analytics script: {src or '[inline]'}")
            modified = True
    return modified

def handle_anchors_and_links(soup, config, log_f):
    if not config.get("handle_anchors", False):
        return False
    modified = False

    blocked_domains = config.get("blocked_domains", [
        "willcoxcorvette.com",
        "checkout",
        "customer/account",
        "cart",
        "paypal.com"
    ])
    archive_root = config.get("archive_root_domain", "").lower()

    for a in list(soup.find_all("a")):
        href = (a.get("href") or "").strip()
        if not href:
            continue
        href_l = href.lower()

        if any(domain in href_l for domain in blocked_domains):
            a.replace_with(a.get_text() or "")
            log_action(log_f, f"Removed external/store link: {href}")
            modified = True
            continue

        if href.startswith(("http://", "https://")):
            if archive_root and archive_root in href_l:
                try:
                    path = href.split(archive_root, 1)[-1].lstrip("/")
                    if path.endswith("/") or path == "":
                        slug = os.path.basename(path.rstrip("/")) or "index"
                    else:
                        slug = os.path.splitext(os.path.basename(path.rstrip("/")))[0]
                    new_href = f"{slug}.html"
                except Exception:
                    new_href = href
                a["href"] = new_href
                log_action(log_f, f"Rewrote internal absolute link: {href} -> {new_href}")
                modified = True
            else:
                if config.get("neutralize_external_links", True):
                    a.replace_with(a.get_text() or "")
                    log_action(log_f, f"Neutralized external link: {href}")
                    modified = True
        else:
            if href.startswith("../") or href.startswith("./") or href.endswith("index.html"):
                try:
                    cleaned = href.split("?")[0].rstrip("/")
                    base = os.path.basename(cleaned)
                    slug = os.path.splitext(base)[0] or cleaned.replace("/", "_")
                    new_href = f"{slug}.html"
                except Exception:
                    new_href = href
                a["href"] = new_href
                log_action(log_f, f"Normalized relative link: {href} -> {new_href}")
                modified = True

    return modified

def remove_empty_menus(soup, config, log_f):
    if not config.get("remove_empty_menus", False):
        return False
    modified = False
    for ul in list(soup.find_all("ul")):
        classes = ul.get("class") or []
        if any("menu" in c.lower() for c in classes) and not ul.find("li"):
            ul.decompose()
            log_action(log_f, "Removed empty menu <ul>")
            modified = True
    return modified

def remove_legacy_forms(soup, config, log_f):
    if not config.get("remove_legacy_forms", False):
        return False
    modified = False
    for form in list(soup.find_all("form")):
        action = (form.get("action") or "").lower()
        classes = " ".join(form.get("class") or []).lower()
        if ("search" in action) or ("search" in classes) or form.find("input", {"type": "search"}) or form.find("input", {"name": "s"}):
            form.decompose()
            log_action(log_f, f"Removed search/legacy form: action={action} class={classes}")
            modified = True
            continue
        if "checkout" in action or "customer" in action or "cart" in action:
            form.decompose()
            log_action(log_f, f"Removed store form: action={action}")
            modified = True
    return modified

def remove_copyright_paragraphs(soup, config, log_f):
    if not config.get("remove_copyright", False):
        return False
    modified = False
    for p in list(soup.find_all("p")):
        text = (p.get_text() or "").lower()
        if "copyright" in text or "all rights reserved" in text:
            p.decompose()
            log_action(log_f, "Removed copyright paragraph")
            modified = True
    return modified

def remove_hypercache_text_nodes(soup, config, log_f):
    if not config.get("remove_hypercache_text", False):
        return False
    modified = False
    for el in list(soup.find_all(string=True)):
        txt = (el or "").strip().lower()
        if "hyper cache" in txt or "cached page" in txt:
            el.extract()
            log_action(log_f, "Removed hypercache text node")
            modified = True
    return modified

def remove_empty_paragraphs(soup, config, log_f):
    if not config.get("remove_empty_paragraphs", False):
        return False
    modified = False
    for p in list(soup.find_all("p")):
        text = (p.get_text(strip=True) or "")
        if text == "":
            p.decompose()
            log_action(log_f, "Removed empty <p>")
            modified = True
    return modified

def remove_html_prefix_attr(soup, config, log_f):
    if not config.get("remove_html_prefix_attr", False):
        return False
    modified = False
    html_tag = soup.find("html")
    if html_tag and "prefix" in html_tag.attrs:
        html_tag.attrs.pop("prefix", None)
        log_action(log_f, "Removed html[prefix] attribute")
        modified = True
    return modified

# === Orchestration ===

def rewrite_html(root, map_data, config):
    ensure_dirs()
    with open(LOG_FILE, "w", encoding="utf-8") as log_f:
        html_entries = map_data.get("html", [])
        total = len(html_entries)
        log_action(log_f, f"Processing {total} HTML files")

        for html_entry in html_entries:
            html_rel = html_entry.get("path")
            html_path = os.path.join(root, html_rel)
            if not os.path.exists(html_path):
                log_action(log_f, f"Source HTML missing: {html_path}")
                continue

            try:
                with open(html_path, "r", encoding="utf-8", errors="ignore") as fh:
                    content = fh.read()
            except Exception as e:
                log_action(log_f, f"Failed to read HTML: {html_path} ({e})")
                continue

            soup = BeautifulSoup(content, "html.parser")
            modified_any = False

            steps = [
                remove_dead_links,
                remove_oembed_links,
                remove_dns_prefetch_links,
                remove_wp_styles,
                harvest_linked_stylesheets,
                inject_layout_stylesheet,
                remove_nav_containers,
                remove_footer,
                remove_tagline,
                strip_article_taxonomy_classes,
                remove_comments,
                remove_ld_json,
                clean_meta,
                lambda s, c, l: normalize_images_in_html(s, html_entry, c, map_data, l),
                remove_plugin_scripts,
                handle_anchors_and_links,
                remove_empty_menus,
                remove_legacy_forms,
                remove_copyright_paragraphs,
                remove_hypercache_text_nodes,
                remove_empty_paragraphs,
                remove_html_prefix_attr
            ]

            for step in steps:
                try:
                    # some steps expect (soup, config, log_f) others (soup, map_data, config, log_f)
                    changed = False
                    # harvest_linked_stylesheets signature: (soup, map_data, config, log_f)
                    if step is harvest_linked_stylesheets:
                        changed = step(soup, map_data, config, log_f)
                    elif step is inject_layout_stylesheet:
                        changed = step(soup, config, log_f)
                    elif step is normalize_images_in_html:
                        changed = step(soup, html_entry, config, map_data, log_f)
                    else:
                        changed = step(soup, config, log_f)
                except Exception as e:
                    step_name = getattr(step, "__name__", repr(step))
                    log_action(log_f, f"Error running step {step_name}: {e}")
                    changed = False
                if changed:
                    modified_any = True

            out_path = output_path_for_entry(html_entry)
            try:
                with open(out_path, "w", encoding="utf-8") as out_f:
                    out_f.write(str(soup))
                log_action(log_f, f"Wrote cleaned file: {out_path}")
            except Exception as e:
                log_action(log_f, f"Failed to write cleaned file: {out_path} ({e})")

# === Main ===

def main():
    if not os.path.exists(MAP_FILE):
        print(f"Map file missing: {MAP_FILE}")
        return
    if not os.path.exists(CONFIG_FILE):
        print(f"Config file missing: {CONFIG_FILE}")
        return

    map_data = load_json(MAP_FILE)
    config = load_json(CONFIG_FILE)
    root = map_data.get("root", "")

    if not root or not os.path.isdir(root):
        print(f"Archive root not found or invalid: {root}")
        return

    rewrite_html(root, map_data, config)
    print("Run complete. Inspect maps/run.log for details.")

if __name__ == "__main__":
    main()