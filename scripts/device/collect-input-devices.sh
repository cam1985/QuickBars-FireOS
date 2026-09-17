#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
[[ $# -eq 1 ]] || { echo "Usage: $0 <fire-tv-ip>" >&2; exit 2; }
SERIAL="$1:5555"; STAMP="$(date -u +%Y%m%dT%H%M%SZ)"; OUT="$ROOT/dev/captures/fire-input-$STAMP.txt"
mkdir -p "$(dirname "$OUT")"
adb connect "$SERIAL" >/dev/null
{
 echo '# QuickBars Fire OS input capture'; echo "# UTC: $STAMP"; echo
 echo '## model / OS'; adb -s "$SERIAL" shell getprop ro.product.model; adb -s "$SERIAL" shell getprop ro.build.version.release; adb -s "$SERIAL" shell getprop ro.build.version.sdk
 echo; echo '## dumpsys input'; adb -s "$SERIAL" shell dumpsys input
 echo; echo '## getevent capabilities'; adb -s "$SERIAL" shell 'getevent -lp 2>/dev/null || true'
} > "$OUT"
echo "Capture written to: $OUT"
