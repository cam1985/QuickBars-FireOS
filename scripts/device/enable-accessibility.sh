#!/usr/bin/env bash
set -euo pipefail

[[ $# -ge 1 && $# -le 2 ]] || {
  echo "Usage: $0 <fire-tv-ip> [package-id]" >&2
  echo "Default package-id: io.github.cam1985.quickbars.fireos.debug" >&2
  exit 2
}

SERIAL="$1:5555"
PACKAGE_ID="${2:-io.github.cam1985.quickbars.fireos.debug}"
SERVICE_CLASS="dev.trooped.tvquickbars.services.QuickBarService"
COMPONENT="$PACKAGE_ID/$SERVICE_CLASS"

adb connect "$SERIAL" >/dev/null

if ! adb -s "$SERIAL" shell pm path "$PACKAGE_ID" | grep -q '^package:'; then
  echo "Package '$PACKAGE_ID' is not installed on $SERIAL." >&2
  echo "Install the debug APK first, or pass the installed package ID as the second argument." >&2
  exit 1
fi

CURRENT="$(adb -s "$SERIAL" shell settings get secure enabled_accessibility_services 2>/dev/null | tr -d '\r')"
if [[ "$CURRENT" == "null" ]]; then
  CURRENT=""
fi

case ":$CURRENT:" in
  *":$COMPONENT:"*)
    UPDATED="$CURRENT"
    echo "QuickBars accessibility service is already present in enabled_accessibility_services."
    ;;
  *)
    if [[ -n "$CURRENT" ]]; then
      UPDATED="$CURRENT:$COMPONENT"
    else
      UPDATED="$COMPONENT"
    fi
    echo "Adding QuickBars while preserving existing accessibility services."
    ;;
esac

adb -s "$SERIAL" shell settings put secure enabled_accessibility_services "$UPDATED"
adb -s "$SERIAL" shell settings put secure accessibility_enabled 1

VERIFIED="$(adb -s "$SERIAL" shell settings get secure enabled_accessibility_services 2>/dev/null | tr -d '\r')"
case ":$VERIFIED:" in
  *":$COMPONENT:"*)
    echo "Enabled accessibility service: $COMPONENT"
    ;;
  *)
    echo "Fire OS did not retain the accessibility service setting." >&2
    echo "Current enabled_accessibility_services: $VERIFIED" >&2
    echo "Try enabling QuickBars from Fire TV Accessibility settings. If the UI is unavailable, capture the device/Fire OS details for compatibility investigation." >&2
    exit 1
    ;;
esac

adb -s "$SERIAL" shell settings get secure accessibility_enabled | tr -d '\r' | sed 's/^/accessibility_enabled=/'
