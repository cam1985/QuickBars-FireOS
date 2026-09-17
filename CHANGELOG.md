# Changelog

All notable **Fire OS port-specific** changes are documented here. Ordinary QuickBars changes remain documented by the upstream project and are not duplicated unless an upstream change requires Fire-specific adaptation.

The format follows [Keep a Changelog](https://keepachangelog.com/en/1.1.0/).

## [Unreleased]

### Added
- Fire OS compatibility development fork based directly on `Trooped/QuickBars` history.
- Fire TV platform detection through `amazon.hardware.fire_tv`.
- Platform capability abstraction for overlay behavior.
- Fire OS accessibility-overlay path using `WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY`.
- Fire remote identity/profile model using Android `InputDevice` capabilities and stable vendor/product metadata when available.
- Amazon Appstore SDK 3.0.9 integration scaffold for the existing one-time Plus entitlement.
- Store-neutral Fire entitlement provider seam and beta-unlocked provider.
- Amazon IAP manifest requirements for App Tester/Appstore package visibility and `ResponseReceiver`.
- Development scripts for build/lint, upstream checks and ADB Fire TV testing.
- Deterministic runtime-seam migration/drift-check script for replaying the small Fire-specific source changes after upstream merges.
- Fire OS architecture, compatibility, remote, IAP, research and parity-test documentation.
- GitHub Actions validation for shell/Python tooling, Android debug APK compilation and Android lint.
- First successfully compiled/linted Fire OS proof-of-concept debug APK from the fork on 2026-09-17.

### Changed
- Fire port package ID is `io.github.cam1985.quickbars.fireos` so it cannot overwrite or masquerade as the official QuickBars package.
- Plus remains unlocked for all beta builds.
- Google/RevenueCat purchasing initialization and purchase surfaces are disabled on the Fire OS beta path.
- QuickBar, camera PiP and notification overlay permission/window-type decisions are routed through the Fire OS platform capability layer.
- Remote keys are normalized through the Fire remote profile registry before entering the existing QuickBars gesture handling.
- Fire build CI now uses the hosted Android SDK directly, pins API/build-tools requirements, separates compilation from lint, caches Gradle dependencies and applies a build timeout.

### Fixed
- GitHub Actions no longer depends on the retired Android SDK `tools` package requested by `android-actions/setup-android@v3`.
- GitHub Actions normalizes the upstream Gradle wrapper execute permission on hosted Linux runners before building.

### Security
- Amazon authentication keys, receipt-verification secrets, signing keystores/passwords and local device captures are excluded from version control.

### Validation
- `:app:assembleDebug` passes in GitHub Actions.
- `:app:lintDebug` passes in GitHub Actions.
- Physical Fire TV validation remains outstanding; no production-support claim is made yet.
