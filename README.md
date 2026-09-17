# QuickBars for Fire OS

> [!IMPORTANT]
> **Unofficial Fire OS port / proof of concept.** This repository is a community-maintained port of [Trooped/QuickBars](https://github.com/Trooped/QuickBars) for Android-based Amazon Fire TV / Fire OS devices. It is **not the official QuickBars repository**, is not currently endorsed or maintained by the upstream author, and is not affiliated with Amazon.
>
> The official QuickBars project, releases, Google/Android TV support and documentation remain at [Trooped/QuickBars](https://github.com/Trooped/QuickBars) and [quickbars.app](https://quickbars.app/).

This fork exists to make the existing QuickBars experience work on **Amazon Fire OS** while keeping the Fire-specific changes as small and upstream-friendly as possible. The objective during proof-of-concept and beta development is **feature parity with upstream QuickBars — not a separate feature set**.

## Status

**Stage:** proof of concept / early beta development  
**Upstream baseline:** `Trooped/QuickBars` @ `1478854aa5fa89831e9e7a1b4f8af4e61f031c74`  
**Maintainer of this port:** Cam Ashton (`cam1985`)  
**License:** GNU GPL v3, inherited from QuickBars

The first technical milestone is to prove the existing QuickBar, camera overlay, notifications and remote-key handling on Fire OS using the existing QuickBars AccessibilityService without relying on Fire TV's unsupported third-party `SYSTEM_ALERT_WINDOW` path.

## Port principles

- **Upstream is the product source of truth.** UI, Home Assistant behavior, entities, QuickBars, trigger gestures, camera behavior, notifications, persistence and feature boundaries should follow upstream.
- **Fire OS compatibility only.** Fire-specific code should solve platform differences, not introduce new user-facing features during the POC.
- **Mergeable upstream history.** This repository is a real GitHub fork. Fire OS work is committed directly on top of upstream history so future upstream updates can be merged normally.
- **Small compatibility seams.** Platform overlays, remote input and store purchasing are isolated instead of duplicating whole upstream classes.
- **No beta paywall.** All Plus functionality remains unlocked during beta. Amazon Appstore purchasing is being implemented behind disabled flags for later store release.
- **No privileged/root-only dependency for the Appstore build.** Normal Android/Fire OS APIs are preferred so the eventual package has a realistic certification path.

## Initial Fire OS compatibility work

The current development work introduces:

1. Fire TV detection using Amazon's documented `amazon.hardware.fire_tv` system feature.
2. `TYPE_ACCESSIBILITY_OVERLAY` for Fire OS, hosted by QuickBars' existing AccessibilityService, instead of `TYPE_APPLICATION_OVERLAY` + `SYSTEM_ALERT_WINDOW`.
3. A Fire remote profile layer based on Android `InputDevice` capabilities and stable vendor/product information where available. Existing QuickBars learn-a-key behavior remains the fallback.
4. Amazon Appstore SDK scaffolding for the existing one-time `plus_unlock` entitlement.
5. Beta purchasing guards so RevenueCat/Google purchase flows cannot be launched on the Fire beta.
6. A distinct application ID (`io.github.cam1985.quickbars.fireos`) so this port cannot be confused with or overwrite the official QuickBars package.

## Platform scope

The first supported target is **Android-based Fire OS 7 and newer**, matching upstream QuickBars' current API 28 minimum. Fire OS 6 is a later compatibility investigation because supporting it would require lowering the upstream minimum SDK.

**Vega OS is not supported by this Android project.** Vega is a different operating system and requires a separate application implementation.

## Development

```bash
# clone this fork
git clone https://github.com/cam1985/QuickBars-FireOS.git
cd QuickBars-FireOS

# add the official project as upstream
git remote add upstream https://github.com/Trooped/QuickBars.git
git fetch upstream --tags

# check development prerequisites
./scripts/doctor.sh

# build and lint the Fire OS debug APK
./scripts/build-debug.sh
```

Fire TV ADB helpers are under `scripts/device/`.

See:

- [`docs/DEVELOPMENT.md`](docs/DEVELOPMENT.md)
- [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md)
- [`docs/FIRE_OS_COMPATIBILITY.md`](docs/FIRE_OS_COMPATIBILITY.md)
- [`docs/REMOTE_PROFILES.md`](docs/REMOTE_PROFILES.md)
- [`docs/IAP.md`](docs/IAP.md)
- [`docs/TEST_PLAN.md`](docs/TEST_PLAN.md)
- [`docs/RESEARCH.md`](docs/RESEARCH.md)
- [`UPSTREAM.md`](UPSTREAM.md)
- [`CHANGELOG.md`](CHANGELOG.md)

## Upstream documentation and credit

The upstream `CONTRIBUTING.md`, issue templates, project conventions and GPLv3 `LICENSE` are retained in this fork unless Fire OS development specifically requires an additive change.

QuickBars was created by **Omri Peretz / Trooped**. Please use the official project for Google/Android TV releases and upstream support.

See [`NOTICE.md`](NOTICE.md) for attribution and project-status details.
