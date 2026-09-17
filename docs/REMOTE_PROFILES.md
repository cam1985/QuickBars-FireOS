# Fire remote profiles

## Amazon-supported identification

Fire TV uses Android's standard `InputDevice` API. Amazon documents a remote as an input device with `SOURCE_DPAD` and `KEYBOARD_TYPE_NON_ALPHABETIC`.

The runtime `deviceId` is arbitrary and must not be used as a persistent controller identity.

## Profile matching

The Fire port matches profiles using, in order:

1. vendor ID + product ID when known;
2. Fire-remote capability test (`SOURCE_DPAD` + non-alphabetic keyboard);
3. generic/unmodified event behavior.

Known Amazon vendor/product IDs can be added only after they are captured and verified. Profiles must not guess a marketing model from an unverified ID.

## Why profiles exist

Profiles are an internal compatibility layer, not a new QuickBars feature. They allow hardware-specific normalization while preserving upstream trigger configuration and gesture behavior.

Current profiles deliberately normalize to upstream behavior because Amazon documents the normal Fire TV Remote and Voice Remote controls as behaviorally identical except for the microphone/Alexa button.

## Capturing a test remote

With ADB connected:

```bash
./scripts/device/collect-input-devices.sh <device-ip>
```

The capture contains `dumpsys input`, selected system properties and key-layout information available to the shell. Raw captures are ignored by Git; sanitize them before deliberately committing a fixture.

## Rules

- Never persist Android runtime device IDs.
- Never depend on a branded app button reaching normal application key handling.
- System-owned Power/Volume/Alexa/Home behavior must not be bypassed with privileged/root-only mechanisms in the Appstore build.
- Preserve QuickBars' learn-a-key behavior as the ultimate fallback.
