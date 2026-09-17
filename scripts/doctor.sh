#!/usr/bin/env bash
set -euo pipefail
fail=0
for cmd in git java adb; do
  if command -v "$cmd" >/dev/null 2>&1; then printf '%-8s %s\n' "$cmd" "$(command -v "$cmd")"; else printf '%-8s MISSING\n' "$cmd"; fail=1; fi
done
[[ -x ./gradlew ]] || { echo 'gradlew   MISSING/NOT EXECUTABLE'; fail=1; }
java -version 2>&1 | head -n 1 || true
adb version 2>/dev/null | head -n 1 || true
exit "$fail"
