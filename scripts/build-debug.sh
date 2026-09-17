#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT"
./gradlew --no-daemon :app:assembleDebug :app:lintDebug
find app/build/outputs/apk/debug -maxdepth 1 -type f -name '*.apk' -print
