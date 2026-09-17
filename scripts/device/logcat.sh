#!/usr/bin/env bash
set -euo pipefail
[[ $# -eq 1 ]] || { echo "Usage: $0 <fire-tv-ip>" >&2; exit 2; }
SERIAL="$1:5555"; adb connect "$SERIAL" >/dev/null
adb -s "$SERIAL" logcat -v threadtime QuickBarService:D CameraPIP:D AmazonEntitlement:D '*:S'
