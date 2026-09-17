# Fire OS port development

## Requirements

- Git
- Bash
- JDK 17
- Android SDK matching upstream `compileSdk`
- Android platform tools (`adb`) for device testing
- Python 3 for maintenance tooling

Upstream currently uses AGP 8.13.1, Kotlin 2.2.20 and `compileSdk = 36`.

## Clone and configure upstream

```bash
git clone https://github.com/cam1985/QuickBars-FireOS.git
cd QuickBars-FireOS
git remote add upstream https://github.com/Trooped/QuickBars.git
git fetch upstream --tags
```

Confirm with:

```bash
git remote -v
bash scripts/check-upstream.sh
```

## Build

```bash
bash scripts/build-debug.sh
```

This builds and lints the source tree directly. The script prints the resulting APK path.

The GitHub Actions `Fire OS Build` workflow independently runs `:app:assembleDebug` and `:app:lintDebug` and uploads the debug APK as a workflow artifact.

## Install on a Fire TV development device

```bash
bash scripts/device/install-debug.sh 192.168.1.50
```

ADB debugging must be enabled and the development machine authorized on the Fire TV.

## Accessibility service

Use Fire TV's Accessibility settings normally when the installed Fire OS build exposes a usable service list. For POC hardware where Amazon's Accessibility UI cannot enable the service, the ADB helper can add the service without removing other enabled accessibility services:

```bash
bash scripts/device/enable-accessibility.sh 192.168.1.50
```

This helper is for development/testing. Store-release setup must follow the path accepted by Amazon certification and the target Fire OS version.

## Remote capture

```bash
bash scripts/device/collect-input-devices.sh 192.168.1.50
```

Raw captures are ignored by Git. Sanitize any hardware/user data before deliberately committing a fixture.

## Fire compatibility seam check after upstream merges

The small set of upstream call sites touched by this port is represented by an idempotent exact-pattern migration/drift-check script:

```bash
python3 scripts/apply_runtime_seams.py
```

If upstream has materially changed one of those locations, the script intentionally fails instead of guessing. Review the upstream change first, adapt the Fire compatibility seam manually, then update the script pattern and documentation together.

## Development discipline

When upstream changes a shared file:

1. preserve the new upstream behavior first;
2. re-apply the smallest Fire compatibility seam;
3. avoid copying an entire upstream class unless the platform genuinely requires it;
4. build + lint;
5. run the parity tests in `docs/TEST_PLAN.md`;
6. record Fire-specific changes in `CHANGELOG.md`.

Do not use this fork to develop unrelated features during the parity phase. Features should ideally be developed upstream/shared once collaboration is agreed.
