#!/usr/bin/env bash
set -euo pipefail
[[ $# -eq 1 ]] || { echo "Usage: $0 <fire-tv-ip>" >&2; exit 2; }
adb connect "$1:5555"
adb -s "$1:5555" shell getprop ro.product.model
adb -s "$1:5555" shell getprop ro.build.version.release
adb -s "$1:5555" shell getprop ro.build.version.sdk
