# mapper/main.py
import argparse
import json
import os
from fs_map import build_map

def main():
    parser = argparse.ArgumentParser(description="Build filesystem map for archived site")
    parser.add_argument("root", help="Root directory of the HTTrack archive")
    parser.add_argument("-o", "--output", default="maps/fs-map.json", help="Output JSON path")
    args = parser.parse_args()

    root = os.path.abspath(args.root)
    data = build_map(root=root)

    os.makedirs(os.path.dirname(args.output), exist_ok=True)
    with open(args.output, "w", encoding="utf-8") as f:
        json.dump(data, f, ensure_ascii=False, indent=2)

    print(f"Map written: {args.output}")

if __name__ == "__main__":
    main()