#!/usr/bin/env bash
set -euo pipefail
UPSTREAM_URL="${UPSTREAM_URL:-https://github.com/Trooped/QuickBars.git}"
if ! git remote get-url upstream >/dev/null 2>&1; then git remote add upstream "$UPSTREAM_URL"; fi
git fetch upstream --tags
printf 'Current fork:  '; git rev-parse --short HEAD
printf 'Upstream main: '; git rev-parse --short upstream/main
echo
echo 'Review before merging:'
git log --oneline --decorate HEAD..upstream/main || true
