#!/bin/sh
set -eu
sh build.sh
java -cp build parking.test.ParkingTests
