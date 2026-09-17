# Fire OS compatibility strategy

## Initial target

The upstream QuickBars minimum SDK is Android API 28, which aligns with Fire OS 7. The POC therefore targets Android-based Fire TV devices on Fire OS 7 and newer without lowering upstream's minimum SDK.

Primary target families:

- Fire OS 7 / Android 9 (API 28)
- Fire OS 8 / Android 10-11 era Fire TV platform levels
- Fire OS 14 / Android 14 era Fire TV platform
- Fire OS 16 when supported by upstream's target/compile SDK

Fire OS 6 is a later compatibility investigation because it would require lowering upstream's current minimum SDK. Vega OS is not supported by this Android project.

## Fire TV detection

Use Amazon's documented Fire TV system feature:

```kotlin
context.packageManager.hasSystemFeature("amazon.hardware.fire_tv")
```

Do not depend on `Build.MANUFACTURER == "Amazon"`; third-party manufacturers ship Fire TV products.

## Overlay strategy

Amazon does not support ordinary third-party use of `SYSTEM_ALERT_WINDOW` on Fire TV and does not document an Amazon-specific replacement API.

The POC therefore tests the Android framework's accessibility overlay window type from QuickBars' existing `AccessibilityService`:

```kotlin
WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY
```

This is a POC compatibility mechanism, not an assertion that Amazon documents it as a direct replacement for `SYSTEM_ALERT_WINDOW`.

Success criteria:

- QuickBar visible over Amazon Home and third-party streaming apps;
- D-pad focus/interaction preserved;
- camera PiP surface visible;
- notification surfaces visible;
- no `SYSTEM_ALERT_WINDOW` grant required;
- accessibility service remains stable across sleep/wake/reboot.
