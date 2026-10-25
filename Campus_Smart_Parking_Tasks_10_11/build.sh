#!/bin/sh
set -eu
mkdir -p build
find src -name '*.java' > build/sources.list
javac -d build @build/sources.list
