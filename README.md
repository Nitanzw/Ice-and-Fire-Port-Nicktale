# Ice and Fire Port Nicktale

**Unofficial** NeoForge port of [Ice and Fire](https://github.com/AlexModGuy/Ice_and_Fire) (dragons, hippogryphs, myrmex, sirens, hydras and more)
to Minecraft **26.2** (26.3 planned, as a dual build), by Nicktale. It is **not affiliated with, endorsed by or supported by the original authors.**

> **Status: work in progress — not playable yet.** The code base was migrated from the Forge 1.20.1 branch; the migration is still being
> finished (see `docs/PROGRESO.md`). No release jars exist.

## Credits
Ice and Fire is created by **Raptorfarian** and **Alexthe666** and their contributors. All original content, code and art belong to them and are used
under the terms of the license below. This port only adapts the code to NeoForge / Minecraft 26.x; it keeps the original copyright and history.

## License
[GNU LGPL-3.0](LICENSE), the same license as the original project. Files added by the port are released under the same license.

## Project layout
- `src/main`, `src/client` — the mod (client-only code lives in the `client` source set).
- `docs/ESTADO_DEL_PROYECTO.md` — current state and what remains.
- `tools/` — helper scripts used for the mass migration (import relocation, API renames, diagnostic-driven fixers).
- Depends on **Nicktale API** (own animation / model library that replaces Citadel), developed alongside.

## Building
Nicktale API is included in `nicktale-api/` and must be built first (the mod depends on its jar). JDK 21+ to run Gradle
(the Java 25 toolchain is downloaded automatically):

```bash
./build_all.sh          # builds nicktale-api/ then runs compileJava on the mod
```

`build.gradle` keeps `-Xmaxerrs 500`; while migrating, raise it (and `org.gradle.jvmargs=-Xmx8G`) locally to see every error.

