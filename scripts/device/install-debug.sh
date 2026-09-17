#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
[[ $# -eq 1 ]] || { echo "Usage: $0 <fire-tv-ip>" >&2; exit 2; }
SERIAL="$1:5555"
adb connect "$SERIAL" >/dev/null
APK="$(find "$ROOT/app/build/outputs/apk/debug" -maxdepth 1 -type f -name '*.apk' | head -n1)"
[[ -n "$APK" ]] || { echo 'No debug APK found; run ./scripts/build-debug.sh first.' >&2; exit 2; }
adb -s "$SERIAL" install -r "$APK"
