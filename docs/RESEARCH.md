# Fire OS research references and decisions

Research checked for the initial POC baseline on 2026-09-17.

## Amazon Fire TV device detection

Amazon documents `amazon.hardware.fire_tv` as the system feature for identifying Fire TV devices and recommends feature detection over manufacturer-only checks.

- https://developer.amazon.com/docs/device-specs/identify-fire-tv-devices.html

## Remote/controller identification

Amazon documents Android `InputDevice` as the supported controller API. Runtime device IDs are arbitrary. A Fire TV Remote/Voice Remote can be identified from `SOURCE_DPAD` together with `KEYBOARD_TYPE_NON_ALPHABETIC`.

- https://developer.amazon.com/docs/fire-tv/identify-controllers.html
- https://developer.amazon.com/docs/fire-tv/controller-behavior-guidelines.html
- https://developer.amazon.com/docs/fire-tv/remote-input.html

Amazon's guidance says normal Fire TV Remote and Voice Remote behavior is identical apart from the microphone button. Marketing-generation detection is therefore not required for ordinary controls; verified vendor/product IDs can be used for quirks when evidence shows a hardware-specific difference.

## Overlays

Amazon developer guidance/support does not provide an Amazon equivalent to ordinary third-party `SYSTEM_ALERT_WINDOW` behavior on Fire TV. QuickBars already runs an Android `AccessibilityService`, so the POC uses Android's `TYPE_ACCESSIBILITY_OVERLAY` as its Fire compatibility mechanism.

- Android window type: https://developer.android.com/reference/android/view/WindowManager.LayoutParams
- Amazon discussion/reference: https://community.amazondeveloper.com/t/firetv-overlay-permissions/6053

This is an engineering POC decision, not a claim that Amazon documents `TYPE_ACCESSIBILITY_OVERLAY` as a direct supported replacement for `SYSTEM_ALERT_WINDOW`.

## Amazon purchasing

Amazon's current Appstore SDK supplies Fire OS IAP. The fixed version used by this POC is 3.0.9 (documented 2026-05-20).

- https://developer.amazon.com/docs/appstore-sdk/appstore-sdk-overview.html
- https://developer.amazon.com/docs/appstore-sdk/integrate-appstore-sdk.html
- https://developer.amazon.com/docs/in-app-purchasing/migrate-google-billing-appstore-sdk-iap.html

For API 30+ Amazon documents package queries for App Tester (`com.amazon.sdktestclient`) and the Amazon Appstore (`com.amazon.venezia`), plus registration of `com.amazon.device.iap.ResponseReceiver`.
