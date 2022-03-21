#!/bin/bash
set -e

NEW_VERSION=$1

cat gradle.properties | grep -v "http4kVersion" > out.txt
echo "http4kVersion=$NEW_VERSION" >> out.txt
mv out.txt gradle.properties
