# QuickBars for Fire OS

[![Fire OS Build](https://github.com/cam1985/QuickBars-FireOS/actions/workflows/fireos-build.yml/badge.svg)](https://github.com/cam1985/QuickBars-FireOS/actions/workflows/fireos-build.yml)

> [!IMPORTANT]
> **Unofficial Fire OS port / proof of concept.** This repository is a community-maintained port of [Trooped/QuickBars](https://github.com/Trooped/QuickBars) for Android-based Amazon Fire TV / Fire OS devices. It is **not the official QuickBars repository**, is not currently endorsed or maintained by the upstream author, and is not affiliated with Amazon.
>
> The official QuickBars project, releases, Google/Android TV support and documentation remain at [Trooped/QuickBars](https://github.com/Trooped/QuickBars) and [quickbars.app](https://quickbars.app/).

This fork exists to make the existing QuickBars experience work on **Amazon Fire OS** while keeping Fire-specific changes as small and upstream-friendly as possible. During proof-of-concept and beta development the objective is **feature parity with upstream QuickBars — not a separate feature set**.

## Status

**Stage:** proof of concept / early beta development  
**Upstream baseline:** `Trooped/QuickBars` @ `1478854aa5fa89831e9e7a1b4f8af4e61f031c74`  
**Maintainer of this port:** Cam Ashton (`cam1985`)  
**License:** GNU GPL v3, inherited from QuickBars

**Current engineering status:** the Fire OS compatibility layer is wired into the existing QuickBar, camera PiP, rich-notification and remote-key paths. The Fire OS debug application now builds and passes Android lint in GitHub Actions. **Physical Fire TV validation is still pending**, so this repository should not yet be treated as a production-ready or generally supported Fire TV release.

The next proof-of-concept gate is real-device testing on Fire OS 8, followed by Fire OS 7: enable the existing QuickBars AccessibilityService, verify QuickBars can display over other applications using `TYPE_ACCESSIBILITY_OVERLAY`, and verify Alexa/Fire remote key events continue through the upstream QuickBars trigger engine.

## Port principles

- **Upstream is the product source of truth.** UI, Home Assistant behavior, entities, QuickBars, trigger gestures, camera behavior, notifications, persistence and feature boundaries should follow upstream.
- **Fire OS compatibility only.** Fire-specific code should solve platform differences, not introduce new user-facing features during the parity phase.
- **Mergeable upstream history.** This repository is a real GitHub fork. Fire OS work is committed directly on top of upstream history so future upstream updates can be merged normally.
- **Small compatibility seams.** Platform overlays, remote input and store purchasing are isolated instead of duplicating whole upstream classes.
- **No beta paywall.** All Plus functionality remains unlocked during beta. Amazon Appstore purchasing is implemented behind disabled beta flags for later store release/testing.
- **No privileged/root-only dependency for the Appstore build.** Normal Android/Fire OS APIs are preferred so the eventual package has a realistic certification path.

## Fire OS compatibility work

The port currently includes:

1. Fire TV detection using Amazon's documented `amazon.hardware.fire_tv` system feature.
2. `TYPE_ACCESSIBILITY_OVERLAY` on Fire OS, hosted by QuickBars' existing AccessibilityService, instead of upstream's `TYPE_APPLICATION_OVERLAY` + `SYSTEM_ALERT_WINDOW` path.
3. The existing QuickBar, camera PiP and rich-notification overlay call sites routed through the shared platform capability layer.
4. A Fire remote profile layer based on Android `InputDevice` capabilities and stable vendor/product information where available. Existing QuickBars learn-a-key behavior remains the fallback.
5. Remote events normalized before entering the existing QuickBars single/double/long-press trigger logic.
6. Amazon Appstore SDK scaffolding for the existing one-time `plus_unlock` entitlement.
7. Beta purchasing guards so RevenueCat/Google purchase flows cannot control Plus status on the Fire beta.
8. A distinct application ID (`io.github.cam1985.quickbars.fireos`) so this port can coexist with and cannot overwrite the official QuickBars package.
9. CI that builds the debug APK and runs Android lint on every change to `main` and on pull requests.

## Platform scope

The first target is **Android-based Fire OS 7 and newer**, matching upstream QuickBars' current API 28 minimum. Fire OS 6 is a later compatibility investigation because supporting it would require lowering the upstream minimum SDK and auditing dependencies.

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
bash scripts/doctor.sh

# build and lint the Fire OS debug APK
bash scripts/build-debug.sh
```

Fire TV ADB helpers are under `scripts/device/`. They are development/test tooling and are not required by the production application.

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
