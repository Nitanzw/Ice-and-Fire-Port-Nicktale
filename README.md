# Ice and Fire Port Nicktale

**Unofficial** NeoForge port of [Ice and Fire](https://github.com/AlexModGuy/Ice_and_Fire) (dragons, hippogryphs, myrmex, sirens, hydras and more)
to Minecraft **26.2** and **26.3** (one source tree, one jar per Minecraft version), by Nicktale. It is **not affiliated with, endorsed by or supported by the original authors.**

> **Status: beta.** Builds for NeoForge 26.2 (26.2.0.88) and 26.3 (26.3.0.39-beta); both boot on dedicated servers with worldgen.
> Still needs hands-on play testing. See `CHANGELOG.md`, `docs/ESTADO_DEL_PROYECTO.md` and `docs/TEST_PLAN.md`.

## Credits
Ice and Fire is created by **Raptorfarian** and **Alexthe666** and their contributors. All original content, code and art belong to them and are used
under the terms of the license below. This port only adapts the code to NeoForge / Minecraft 26.x; it keeps the original copyright and history.

## License
[GNU LGPL-3.0](LICENSE), the same license as the original project. Files added by the port are released under the same license.
See [NOTICE.md](NOTICE.md) for the credits and the list of modifications. The LGPL builds on the GNU GPL-3.0, included as
[COPYING](COPYING). The three files are also included in every jar.

## Project layout
- `src/main`, `src/client` — the mod (client-only code lives in the `client` source set).
- `docs/ESTADO_DEL_PROYECTO.md` — current state and what remains; `docs/TEST_PLAN.md` — test plan.
- `tools/` — helper scripts used for the mass migration (import relocation, API renames, diagnostic-driven fixers).
- Depends on **Nicktale API** (own animation / model / respawn library written from scratch to replace Citadel; all rights reserved), kept in its own repository
  [Nitanzw/Nicktale-API](https://github.com/Nitanzw/Nicktale-API) and included here as the git submodule `nicktale-api/`.

## Building
Clone with submodules (the API lives in `nicktale-api/`), then build the API first (the mod depends on its jar). JDK 21+ to run Gradle
(the Java 25 toolchain is downloaded automatically):

```bash
git clone --recurse-submodules https://github.com/Nitanzw/Ice-and-Fire-Port-Nicktale.git
# already cloned?  git submodule update --init
./build_all.sh          # builds nicktale-api/ then the mod for 26.2 (jars in build/libs and nicktale-api/build/libs; both go in mods/)
# 26.3: ./gradlew build -Pminecraft_version=26.3 -Pneoforge_version=26.3.0.39-beta -Pnicktale_api_version=<api version>-neoforge-26.3
#       (gradle/mc-preprocess.gradle turns the sources into their 26.3 form)
```

This repository is public (LGPL-3.0). Nicktale API is a separate closed-source library (all rights reserved) by Nicktale; its
submodule needs access to its private repository. Players get both jars from CurseForge.
