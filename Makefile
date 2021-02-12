all: preinit createXD gradlew xd.apk

preinit:
	git submodule update --init --recursive
createXD:
	cd XD && ./release.sh || true
	cp XD/XD--*arm app/src/main/assets/XD
gradlew:
	./gradlew assembleDebug
xd.apk:
	cp app/build/outputs/apk/debug/app-debug.apk xd.apk


