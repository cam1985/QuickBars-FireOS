# Proof-of-concept parity test plan

## Preflight — identify device and capture baseline

Before changing the Fire TV configuration, record the device/OS snapshot and input-device inventory:

```bash
bash scripts/device/adb-connect.sh <fire-tv-ip>
bash scripts/device/device-info.sh <fire-tv-ip>
bash scripts/device/collect-input-devices.sh <fire-tv-ip>
```

Record at minimum:

- Fire TV model/device name;
- Fire OS / Android release information exposed by the device;
- Android SDK level;
- `amazon.hardware.fire_tv` feature presence;
- installed QuickBars Fire OS package/version;
- enabled accessibility-service state;
- remote/input device identity and capabilities.

Use the same snapshot after significant Fire OS updates so regressions can be tied to a concrete device/build.

## Gate 1 — application lifecycle

- APK installs alongside official QuickBars.
- App launches with remote/D-pad only.
- Existing setup/onboarding screens remain functionally equivalent to upstream.
- Home Assistant pairing/credentials persist across restart.

Install either a locally built APK or a downloaded successful CI artifact:

```bash
bash scripts/device/install-debug.sh <fire-tv-ip> [/path/to/app-debug.apk]
```

## Gate 2 — accessibility service

- Service can be enabled from the normal Fire TV Accessibility UI where available.
- Development fallback can enable the service without deleting other enabled accessibility services.
- Service reconnects after app process restart.
- Key events continue after sleep/wake.
- Reboot auto-start behavior matches upstream setting.

Development fallback:

```bash
bash scripts/device/enable-accessibility.sh <fire-tv-ip>
```

## Gate 3 — QuickBar overlay

Test over:

- Amazon Home;
- Prime Video;
- Netflix;
- YouTube;
- a sideloaded application.

Verify:

- overlay appears;
- correct size/position;
- D-pad focus works;
- entity state updates work;
- entity actions reach Home Assistant;
- auto-close/auto-hide match upstream;
- existing single/double/long trigger behavior matches upstream.

## Gate 4 — camera PiP

- HTTP/MJPEG camera path.
- RTSP path.
- all upstream positions/sizes.
- auto-hide.
- toggle close.
- playback while another streaming app is foreground.

## Gate 5 — notifications

- basic notification;
- image notification;
- action buttons;
- sound;
- timeout/queue behavior.

## Gate 6 — remote input

For each tested remote capture:

- identify `InputDevice` as remote;
- record vendor/product IDs where exposed;
- DPAD/Back/Menu/media keys behave exactly as upstream;
- confirm key remains protected from remapping as upstream expects;
- profile selection never depends on runtime device ID;
- unknown remote falls back safely.

When debugging runtime behavior, capture the focused QuickBars logs with:

```bash
bash scripts/device/logcat.sh <fire-tv-ip>
```

## Gate 7 — Plus beta

- all Plus features work without purchase;
- no RevenueCat purchase flow is invoked;
- Amazon IAP is disabled in beta runtime;
- no Plus feature is unexpectedly hidden after app restart.

## Gate 8 — Amazon IAP (disabled production path)

Run separately using App Tester:

- product data returned for `plus_unlock`;
- purchase response processed;
- active entitlement restored with `getPurchaseUpdates(false)`;
- canceled receipt does not grant entitlement;
- production flag can select Amazon entitlement provider without feature-code changes.

## Minimum devices before beta release

- one Fire OS 7 device;
- one Fire OS 8 device.

Fire OS 14/16 should be added as physical/remote test hardware becomes available.
