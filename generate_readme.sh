#!/usr/bin/env bash

set -e

if [ -z "$VERSION" ]; then
  VERSION=$(grep version pom.xml | head -n 1 | grep -Po '\>.*\<' | sed 's/[<>]//g')
fi

sed "s/@@VERSION@@/$VERSION/g" README.template.md >README.md

cp README.md jrecordbind/src/site/markdown/index.md
