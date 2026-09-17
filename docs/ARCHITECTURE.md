# Architecture

## Objective

Mirror upstream QuickBars functionality on Android-based Fire OS while minimizing divergence from `Trooped/QuickBars`.

## Compatibility seams

### PlatformCapabilities

`PlatformCapabilities` owns operating-system differences that should not leak throughout QuickBars:

- Fire TV detection;
- overlay window type;
- whether a separate draw-over-other-apps permission is required.

On Fire OS, QuickBars uses the existing `AccessibilityService` as the owner of `TYPE_ACCESSIBILITY_OVERLAY` windows. On non-Fire Android, the provider retains upstream `TYPE_APPLICATION_OVERLAY` behavior.

### FireRemoteProfileRegistry

Remote events remain ordinary QuickBars `KeyEvent` handling. Before upstream's existing confirm-key and trigger-key logic runs, the event may be normalized through the Fire remote profile registry.

Profiles are selected from stable `InputDevice` characteristics when available:

- `vendorId`;
- `productId`;
- input sources;
- keyboard type;
- name/descriptor as secondary hints.

Runtime Android device IDs are never used as persistent remote identities.

### Entitlements

Beta builds expose Plus as enabled without invoking a purchase flow. Amazon purchasing code exists behind a disabled build flag so it can be tested separately before the paywall is enabled.

The eventual production Amazon product is a one-time Appstore entitlement corresponding to upstream's `plus_unlock` product.

## What stays upstream-owned

The following remain upstream code and behavior wherever technically possible:

- entity models and Home Assistant WebSocket API;
- UI/screens/layout/style;
- QuickBar management;
- trigger-key gestures and timing;
- camera stream behavior;
- notifications;
- backup/import format;
- persistence;
- user-facing feature definitions;
- Plus feature boundaries.

## Vega OS

Vega OS is not Android and cannot run this Android application. It is deliberately excluded rather than hidden behind conditional code in this project.
