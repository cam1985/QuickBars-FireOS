package dev.trooped.tvquickbars.platform

import android.content.Context
import android.provider.Settings
import android.view.WindowManager

/**
 * Narrow compatibility seam for OS/platform behavior that differs between upstream
 * Google/Android TV and Android-based Amazon Fire TV / Fire OS.
 *
 * Keep this object intentionally small: Fire-specific product features do not belong here.
 */
object PlatformCapabilities {
    const val FIRE_TV_FEATURE = "amazon.hardware.fire_tv"

    fun isFireTv(context: Context): Boolean =
        context.packageManager.hasSystemFeature(FIRE_TV_FEATURE)

    /**
     * QuickBars upstream uses TYPE_APPLICATION_OVERLAY. Fire TV does not support
     * ordinary third-party SYSTEM_ALERT_WINDOW use, so the Fire POC hosts the same
     * UI from the existing AccessibilityService as an accessibility overlay.
     */
    fun overlayWindowType(context: Context): Int =
        if (isFireTv(context)) {
            WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        }

    /**
     * TYPE_ACCESSIBILITY_OVERLAY is owned by the connected AccessibilityService and
     * does not use the separate SYSTEM_ALERT_WINDOW user grant.
     */
    fun canPresentOverlays(context: Context): Boolean =
        if (isFireTv(context)) true else Settings.canDrawOverlays(context)

    fun requiresSystemOverlayPermission(context: Context): Boolean = !isFireTv(context)
}
