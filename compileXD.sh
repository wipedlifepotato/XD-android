#!/bin/bash
cd XD
GIT_VERSION="" GOOS=linux GOARCH=arm GOARM=5 make clean build && mv XD XD-$version-linux-arm && gpg --sign --detach XD-$version-linux-arm
cd ..
cp XD/XD--*arm app/src/main/assets/XD
