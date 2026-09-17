#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"

[[ $# -ge 1 && $# -le 2 ]] || {
  echo "Usage: $0 <fire-tv-ip> [apk-path]" >&2
  exit 2
}

SERIAL="$1:5555"
adb connect "$SERIAL" >/dev/null

if [[ $# -eq 2 ]]; then
  APK="$2"
else
  APK="$(find "$ROOT/app/build/outputs/apk/debug" -maxdepth 1 -type f -name '*.apk' | head -n1)"
fi

[[ -n "${APK:-}" && -f "$APK" ]] || {
  echo "No debug APK found." >&2
  echo "Build one with 'bash scripts/build-debug.sh' or pass an APK path as the second argument." >&2
  exit 2
}

echo "Installing: $APK"
adb -s "$SERIAL" install -r "$APK"
