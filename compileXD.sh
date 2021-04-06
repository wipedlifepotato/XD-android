#!/bin/bash
GIT_VERSION="" GOOS=linux GOARCH=arm GOARM=5 cd XD && make clean build && mv XD XD-$version-linux-arm
cd ..
mkdir -p app/src/main/assets/
cp XD/XD--*arm app/src/main/assets/XD
./gradlew assembleDebug
