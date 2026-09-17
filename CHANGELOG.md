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
- Fire OS architecture, compatibility, remote, IAP, research and parity-test documentation.
- CI build/lint and static repository checks.

### Changed
- Fire port package ID is `io.github.cam1985.quickbars.fireos` so it cannot overwrite or masquerade as the official QuickBars package.
- Plus remains unlocked for all beta builds.
- Google/RevenueCat purchasing initialization and purchase surfaces are disabled on the Fire OS beta path.
- QuickBar, camera PiP and notification overlay checks/window types are routed through the Fire OS platform capability layer.
- Remote keys can be normalized through the Fire remote profile registry before existing QuickBars gesture handling.

### Security
- Amazon authentication keys, receipt-verification secrets, signing keystores/passwords and local device captures are excluded from version control.
