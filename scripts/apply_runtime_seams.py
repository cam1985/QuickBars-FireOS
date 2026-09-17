#!/usr/bin/env python3
"""Wire the Fire OS compatibility seams into the upstream QuickBars sources.

This script is deliberately narrow and idempotent. It only changes the platform-specific
call sites needed by the Fire OS port. If upstream changes a touched code pattern, the
script fails loudly instead of guessing.
"""
from __future__ import annotations

from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]


def replace_once(path: Path, old: str, new: str, label: str) -> None:
    text = path.read_text(encoding="utf-8")
    if new in text:
        return
    count = text.count(old)
    if count != 1:
        raise RuntimeError(f"{label}: expected exactly one upstream match in {path}, found {count}")
    path.write_text(text.replace(old, new, 1), encoding="utf-8")


def ensure_import(path: Path, import_line: str, after: str) -> None:
    text = path.read_text(encoding="utf-8")
    if import_line in text:
        return
    marker = after + "\n"
    if marker not in text:
        raise RuntimeError(f"Import marker changed in {path}: {after}")
    path.write_text(text.replace(marker, marker + import_line + "\n", 1), encoding="utf-8")


def main() -> None:
    # Shared permission helper: Fire OS accessibility overlays do not need the separate
    # SYSTEM_ALERT_WINDOW grant used by upstream Android/Google TV.
    permission_utils = ROOT / "app/src/main/java/dev/trooped/tvquickbars/utils/PermissionUtils.kt"
    ensure_import(
        permission_utils,
        "import dev.trooped.tvquickbars.platform.PlatformCapabilities",
        "import dev.trooped.tvquickbars.services.QuickBarService",
    )
    replace_once(
        permission_utils,
        "        return Settings.canDrawOverlays(context)",
        "        return PlatformCapabilities.canPresentOverlays(context)",
        "PermissionUtils overlay check",
    )

    # Settings status should use the same compatibility decision as runtime overlays.
    settings_fragment = ROOT / "app/src/main/java/dev/trooped/tvquickbars/ui/SettingsFragment.kt"
    replace_once(
        settings_fragment,
        "            Settings.canDrawOverlays(requireContext())",
        "            dev.trooped.tvquickbars.platform.PlatformCapabilities.canPresentOverlays(requireContext())",
        "SettingsFragment overlay status",
    )

    # Camera PiP remains the upstream UI/behaviour; only its permission check and window
    # type are selected through the platform seam.
    camera = ROOT / "app/src/main/java/dev/trooped/tvquickbars/camera/CameraPipController.kt"
    ensure_import(
        camera,
        "import dev.trooped.tvquickbars.platform.PlatformCapabilities",
        "import dev.trooped.tvquickbars.persistence.SecurePrefsManager",
    )
    replace_once(
        camera,
        "        if (!Settings.canDrawOverlays(context)) return@runOnMain",
        "        if (!PlatformCapabilities.canPresentOverlays(context)) return@runOnMain",
        "Camera PiP overlay permission",
    )
    replace_once(
        camera,
        "            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,",
        "            PlatformCapabilities.overlayWindowType(context),",
        "Camera PiP window type",
    )

    # Rich notifications use the same AccessibilityService-owned overlay type on Fire OS.
    notifications = ROOT / "app/src/main/java/dev/trooped/tvquickbars/notification/NotificationController.kt"
    ensure_import(
        notifications,
        "import dev.trooped.tvquickbars.platform.PlatformCapabilities",
        "import dev.trooped.tvquickbars.R",
    )
    replace_once(
        notifications,
        "        if (!android.provider.Settings.canDrawOverlays(context)) return",
        "        if (!PlatformCapabilities.canPresentOverlays(context)) return",
        "Notification overlay permission",
    )
    replace_once(
        notifications,
        "        val type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY",
        "        val type = PlatformCapabilities.overlayWindowType(context)",
        "Notification window type",
    )

    # Main QuickBar overlay and remote events. Existing trigger-key assignment and
    # single/double/long-press logic remains untouched after normalization.
    service = ROOT / "app/src/main/java/dev/trooped/tvquickbars/services/QuickBarService.kt"
    ensure_import(
        service,
        "import dev.trooped.tvquickbars.platform.PlatformCapabilities",
        "import dev.trooped.tvquickbars.notification.NotificationSpec",
    )
    ensure_import(
        service,
        "import dev.trooped.tvquickbars.platform.FireRemoteProfileRegistry",
        "import dev.trooped.tvquickbars.platform.PlatformCapabilities",
    )
    replace_once(
        service,
        "        if (!Settings.canDrawOverlays(this)) {",
        "        if (!PlatformCapabilities.canPresentOverlays(this)) {",
        "QuickBar overlay permission",
    )
    replace_once(
        service,
        "        val layoutFlag = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY",
        "        val layoutFlag = PlatformCapabilities.overlayWindowType(this)",
        "QuickBar window type",
    )
    replace_once(
        service,
        "        val keyCodeRaw = event?.keyCode ?: return super.onKeyEvent(event)\n        val keyCode = normalizeConfirm(keyCodeRaw)",
        "        val keyCodeRaw = event?.keyCode ?: return super.onKeyEvent(event)\n        val keyCode = normalizeConfirm(FireRemoteProfileRegistry.normalizeKey(event))",
        "Fire remote normalization hook",
    )

    print("Fire OS runtime compatibility seams are applied.")


if __name__ == "__main__":
    main()
