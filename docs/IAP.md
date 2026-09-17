# Amazon Appstore purchasing

## Goal

Mirror upstream QuickBars' one-time Plus unlock on the Amazon Appstore without changing which features are Free vs Plus.

Upstream logical product:

```text
plus_unlock
```

Amazon production mapping:

```text
Product type: ENTITLEMENT
Logical entitlement: plus
SKU: plus_unlock (unless the Appstore listing requires a different mapped SKU)
```

## SDK

The POC pins:

```gradle
implementation("com.amazon.device:amazon-appstore-sdk:3.0.9")
```

Do not use a `3.+` wildcard in production builds.

## Beta policy

During POC/beta:

```text
FIRE_BETA_UNLOCK_PLUS = true
AMAZON_IAP_ENABLED    = false
```

All existing Plus functionality is available. No purchase is required and Amazon IAP responses cannot disable Plus. Google/RevenueCat purchase initialization and purchase surfaces are disabled on the Fire beta path.

The IAP implementation is developed/tested separately and enabled only after:

- App Tester succeeds;
- restore/getPurchaseUpdates succeeds;
- cancellation/revocation handling is validated;
- Live App Testing succeeds;
- the package/signing/Appstore ownership decision is finalized.

## Required Amazon configuration

For API 30+ package visibility, the Fire manifest queries:

- `com.amazon.sdktestclient`
- `com.amazon.venezia`

It also registers Amazon's `com.amazon.device.iap.ResponseReceiver` using the documented notification permission/action.

Do not commit:

- `AppstoreAuthenticationKey.pem`;
- `api_key.txt`;
- RVS shared secrets;
- signing keystores/passwords.

## Receipt validation

Before production monetization, entitlement activation should be hardened with Amazon's Receipt Verification Service (RVS), especially to handle canceled/refunded receipts correctly.

## Other upstream purchase products

QuickBars also exposes the existing support/donation products from Settings:

- `donation_coffee`
- `donation_cookie`
- `donation_cake`

Before Appstore release these must be mapped to Amazon consumable products so the Fire edition mirrors the official app rather than silently removing its support-purchase UI. This is billing parity, not a new feature.
