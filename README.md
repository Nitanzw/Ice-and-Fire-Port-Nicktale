# Ice and Fire Port Nicktale

**Unofficial** NeoForge port of [Ice and Fire](https://github.com/AlexModGuy/Ice_and_Fire) (dragons, hippogryphs, myrmex, sirens, hydras and more)
to Minecraft **26.2** (26.3 planned, as a dual build), by Nicktale. It is **not affiliated with, endorsed by or supported by the original authors.**

> **Status: compiles and boots on NeoForge 26.2** (server and client smoke tests pass); it still needs hands-on play testing and visual QA.
> See `docs/ESTADO_DEL_PROYECTO.md` (current state) and `docs/TEST_PLAN.md` (test plan). No release jars are published yet.

## Credits
Ice and Fire is created by **Raptorfarian** and **Alexthe666** and their contributors. All original content, code and art belong to them and are used
under the terms of the license below. This port only adapts the code to NeoForge / Minecraft 26.x; it keeps the original copyright and history.

## License
[GNU LGPL-3.0](LICENSE), the same license as the original project. Files added by the port are released under the same license.

## Project layout
- `src/main`, `src/client` — the mod (client-only code lives in the `client` source set).
- `docs/ESTADO_DEL_PROYECTO.md` — current state and what remains; `docs/TEST_PLAN.md` — test plan.
- `tools/` — helper scripts used for the mass migration (import relocation, API renames, diagnostic-driven fixers).
- Depends on **Nicktale API** (own animation / model / respawn library that replaces Citadel), kept in its own repository
  [Nitanzw/Nicktale-API](https://github.com/Nitanzw/Nicktale-API) and included here as the git submodule `nicktale-api/`.

## Building
Clone with submodules (the API lives in `nicktale-api/`), then build the API first (the mod depends on its jar). JDK 21+ to run Gradle
(the Java 25 toolchain is downloaded automatically):

```bash
git clone --recurse-submodules https://github.com/Nitanzw/Ice-and-Fire-Port-Nicktale.git
# already cloned?  git submodule update --init
./build_all.sh          # builds nicktale-api/ then the mod (jars in build/libs and nicktale-api/build/libs; both go in mods/)
```

Both repositories are private: the clone needs a GitHub login with access to both.
