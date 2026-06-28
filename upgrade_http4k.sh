#!/bin/bash
set -e

NEW_VERSION=$1

sed -i.bak -E "s|^http4k = \".*\"|http4k = \"$NEW_VERSION\"|" gradle/libs.versions.toml
rm -f gradle/libs.versions.toml.bak
