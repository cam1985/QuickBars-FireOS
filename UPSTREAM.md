# Upstream synchronization policy

## Source of truth

Official QuickBars:

```text
https://github.com/Trooped/QuickBars.git
```

This repository is a GitHub fork and the Fire OS work should remain a narrow compatibility layer on top of that history.

## Recommended remotes

```text
origin    https://github.com/cam1985/QuickBars-FireOS.git
upstream  https://github.com/Trooped/QuickBars.git
```

Configure once:

```bash
git remote add upstream https://github.com/Trooped/QuickBars.git
git fetch upstream --tags
```

## Updating from upstream

Use a dedicated sync branch rather than copying files from GitHub:

```bash
git fetch upstream --tags
git switch -c sync/upstream-YYYYMMDD
git merge --no-ff upstream/main
```

Resolve conflicts by keeping the upstream implementation and then restoring only the Fire OS compatibility behavior. Run build/lint and the parity test matrix before merging the sync branch back into `main`.

After a validated upstream merge:

- update `.upstream-commit` to the upstream commit that was merged;
- record any Fire-specific adaptation in `CHANGELOG.md`;
- do not duplicate ordinary upstream release notes in the Fire changelog.

## Conflict preference

Prefer, in order:

1. upstream implementation unchanged;
2. a small shared abstraction with identical upstream behavior;
3. a Fire-only implementation behind a narrow interface;
4. a documented conditional when neither of the above is practical.

Avoid long-lived copies of complete upstream classes.

## Potential upstream contributions

Generic seams may eventually be suitable to offer upstream if the maintainer is interested, for example:

- overlay-window provider abstraction;
- store-neutral entitlement provider;
- input-device normalization hook.

Amazon-specific code should remain in this fork/source set unless collaboration explicitly changes that arrangement.
