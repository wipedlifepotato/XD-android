all: preinit createXD gradlew xd.apk

preinit:
	git submodule update --init --recursive
createXD:
	./compileXD.sh || true
xd.apk:
	cp app/build/outputs/apk/debug/app-debug.apk xd.apk


