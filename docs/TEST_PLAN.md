# Proof-of-concept parity test plan

## Gate 1 — application lifecycle

- APK installs alongside official QuickBars.
- App launches with remote/D-pad only.
- Existing setup/onboarding screens remain functionally equivalent to upstream.
- Home Assistant pairing/credentials persist across restart.

## Gate 2 — accessibility service

- Service can be enabled.
- Service reconnects after app process restart.
- Key events continue after sleep/wake.
- Reboot auto-start behavior matches upstream setting.

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
