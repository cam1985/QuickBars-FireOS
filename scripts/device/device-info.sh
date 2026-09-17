#!/usr/bin/env bash
set -euo pipefail

[[ $# -eq 1 ]] || { echo "Usage: $0 <fire-tv-ip>" >&2; exit 2; }

SERIAL="$1:5555"
DEBUG_PACKAGE="io.github.cam1985.quickbars.fireos.debug"
RELEASE_PACKAGE="io.github.cam1985.quickbars.fireos"
SERVICE_CLASS="dev.trooped.tvquickbars.services.QuickBarService"

adb connect "$SERIAL" >/dev/null

prop() {
  local key="$1"
  local value
  value="$(adb -s "$SERIAL" shell getprop "$key" 2>/dev/null | tr -d '\r')"
  printf '%-32s %s\n' "$key" "${value:-<unset>}"
}

feature_present() {
  local feature="$1"
  if adb -s "$SERIAL" shell pm list features 2>/dev/null | tr -d '\r' | grep -Fxq "feature:$feature"; then
    echo "yes"
  else
    echo "no"
  fi
}

echo "QuickBars Fire OS compatibility snapshot"
echo "========================================"
prop ro.product.manufacturer
prop ro.product.brand
prop ro.product.model
prop ro.product.device
prop ro.product.name
prop ro.build.version.release
prop ro.build.version.sdk
prop ro.build.version.incremental
prop ro.build.display.id
prop ro.build.fingerprint

echo
echo "Features"
echo "--------"
printf '%-32s %s\n' "amazon.hardware.fire_tv" "$(feature_present amazon.hardware.fire_tv)"
printf '%-32s %s\n' "android.software.leanback" "$(feature_present android.software.leanback)"

echo
echo "QuickBars packages"
echo "------------------"
for package_id in "$DEBUG_PACKAGE" "$RELEASE_PACKAGE"; do
  if adb -s "$SERIAL" shell pm path "$package_id" 2>/dev/null | grep -q '^package:'; then
    echo "$package_id: installed"
    adb -s "$SERIAL" shell dumpsys package "$package_id" 2>/dev/null \
      | tr -d '\r' \
      | grep -E 'versionName=|versionCode=' \
      | sed 's/^/  /' \
      | head -n 4 || true
  else
    echo "$package_id: not installed"
  fi
done

echo
echo "Accessibility"
echo "-------------"
ACCESSIBILITY_ENABLED="$(adb -s "$SERIAL" shell settings get secure accessibility_enabled 2>/dev/null | tr -d '\r')"
ENABLED_SERVICES="$(adb -s "$SERIAL" shell settings get secure enabled_accessibility_services 2>/dev/null | tr -d '\r')"
echo "accessibility_enabled=${ACCESSIBILITY_ENABLED:-<unavailable>}"
echo "enabled_accessibility_services=${ENABLED_SERVICES:-<none>}"

for package_id in "$DEBUG_PACKAGE" "$RELEASE_PACKAGE"; do
  component="$package_id/$SERVICE_CLASS"
  case ":$ENABLED_SERVICES:" in
    *":$component:"*) echo "$component: enabled" ;;
    *) echo "$component: not enabled" ;;
  esac
done
