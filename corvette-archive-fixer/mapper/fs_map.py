# mapper/fs_map.py
import os
import mimetypes
from datetime import datetime, timezone

IMAGE_EXTS = {".png", ".jpg", ".jpeg", ".gif", ".webp", ".bmp", ".tif", ".tiff", ".svg"}
HTML_EXTS = {".html", ".htm"}

def relpath(root, full):
    """Normalize relative paths with forward slashes."""
    return os.path.relpath(full, root).replace("\\", "/")

def build_map(root: str) -> dict:
    stats = {"files_total": 0, "html_total": 0, "images_total": 0, "others_total": 0}
    html = []
    images_by_filename = {}
    images_by_path = []
    other = []

    for dirpath, _, files in os.walk(root):
        for name in files:
            full = os.path.join(dirpath, name)
            ext = os.path.splitext(name)[1].lower()
            try:
                size = os.path.getsize(full)
            except OSError:
                continue

            stats["files_total"] += 1
            path_rel = relpath(root, full)

            if ext in HTML_EXTS:
                stats["html_total"] += 1
                html.append({"path": path_rel, "size": size})
            elif ext in IMAGE_EXTS:
                stats["images_total"] += 1
                ctype, _ = mimetypes.guess_type(full)
                entry = {
                    "path": path_rel,
                    "size": size,
                    "ext": ext.lstrip("."),
                    "content_type": ctype or "application/octet-stream"
                }
                images_by_path.append(entry)
                images_by_filename.setdefault(name, []).append({"path": path_rel, "size": size})
            else:
                stats["others_total"] += 1
                other.append({"path": path_rel, "size": size})

    # Sort for determinism
    html.sort(key=lambda x: x["path"])
    images_by_path.sort(key=lambda x: x["path"])
    other.sort(key=lambda x: x["path"])
    for fname in images_by_filename:
        images_by_filename[fname].sort(key=lambda x: x["path"])

    return {
        "root": root.replace("\\", "/"),
        "generated_at": datetime.now(timezone.utc).isoformat(),
        "stats": stats,
        "html": html,
        "images": {
            "by_filename": images_by_filename,
            "by_path": images_by_path
        },
        "other": other
    }