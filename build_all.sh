#!/usr/bin/env bash
# Builds Nicktale API first (the mod depends on its jar), then compiles the mod.
# Needs a JDK 21+ to run Gradle; the Java 25 toolchain is downloaded automatically.
set -e
git submodule update --init nicktale-api
(cd nicktale-api && chmod +x gradlew && ./gradlew build --console=plain)
chmod +x gradlew
# -Xmaxerrs in build.gradle is 500; raise it with an init script or edit the file to see every error while migrating.
./gradlew build --console=plain "$@"
