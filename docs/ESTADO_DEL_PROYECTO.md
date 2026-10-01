# Estado del proyecto — Ice and Fire Port (NeoForge 26.2)

## Dónde está todo

- **Repositorio:** https://github.com/Nitanzw/Ice-and-Fire-Port-Nicktale
- **Rama con TODO el trabajo:** `main` (no está en `main`).
  `main` sigue en el commit viejo `46abb1d`. Para ver lo nuevo hay que abrir esa rama en GitHub
  (selector de ramas) o hacer un merge/PR hacia `main` (pendiente de tu autorización: no creo PRs ni empujo a `main` sin que lo pidas).
- **Jars para probar:** carpeta `dist/` del repo (ignorada por git) y los que te mandé por el chat:
  `iceandfire-nicktale-0.1.0-neoforge-26.2.jar` + `nicktaleapi-0.1.0-neoforge-26.2.jar` (van **los dos** en `mods/`).
  Los jars de `dist/` hay que regenerarlos con `./gradlew build` si cambias algo.
- **Nicktale API:** repo propio https://github.com/Nitanzw/Nicktale-API (privado), incluido aquí como submódulo en `nicktale-api/`.
- **Documentos:** `docs/AGENT_TEST_PLAN.md` (plan para que otras IAs testeen), este archivo, `docs/reports/` (para reportes de QA).

## Qué se hizo (resumen por bloques)

### 1. Port a NeoForge 26.2 (base)
- Compila (`compileJava` y `compileClientJava` sin errores) y arranca en servidor y cliente.
- Renderizado reescrito al sistema nuevo (render states, `submit*`, `SpecialModelRenderer`, `BlockEntityRenderer`).
- Registros de datos (banner patterns, damage types, trades), loot/processors por `MapCodec`, bloques/items con id previo.
- Tags `forge:` → `c:`, carpetas de datos renombradas (`structure`, `tags/item|block|entity_type`), JSON de recetas, loot,
  advancements y villager trades pasados al formato 26.2. **Los tags de entidad estaban mal ubicados y rompían la IA de los mobs.**
- Definiciones de items (`assets/iceandfire/items/*.json`), equipamiento de armaduras, post-efecto de la sirena,
  propiedades de item (arco, cuerno, cristales, tridente), iconos del cofre maldito y del portal.
- Mixins declarados (antes nunca se aplicaban): colisiones del deathworm y silenciado de logs de worldgen.
- Bugs de runtime corregidos: atributo `tempt_range`, `SWIMMING` del dragón, animaciones nulas, texturas dinámicas
  (`registerAndLoad`), entidad fantasma de las estatuas, ids de evento del Hydra (colisión con id 63), membranas de
  alas (cajas planas descartadas en TabulaModel), y más.

### 2. Funciones pedidas por ti
- Dragones vuelan al **50 %** de velocidad (`EntityDragonBase.DRAGON_FLIGHT_SPEED_SCALE`).
- Dragones **domados nunca rompen bloques** (`DragonUtils.canGrief`).
- **Respawn de dragones cada 12 h reales:** cuevas y roosts regeneran estructura + dragón cuando el dragón muere, se doma o
  es eliminado. Probado con retraso corto (20 s), incluso con reinicio del servidor. No probado con las 12 h reales.
- **Armaduras:** cada pieza usa su modelo propio y muestra sólo su parte (casco/peto/pantalones/botas).

### 3. Movido a la Nicktale API (código nuevo y reutilizable)
En `nicktale-api/src/main/java/com/nicktale/api/`:
- `server/respawn/` — sistema genérico de sitios que reaparecen (`SiteRespawns`, `SiteRespawnData`).
- `server/worldgen/ForcedGeneration` — interruptor para saltar azar y distancias al regenerar.
- `server/entity/EntityDataIO` — utilidades de guardado/lectura de entidades.
- `client/model/armor/` — `ArmorModelBase` y `ArmorModels` (registro de armaduras con modelo propio por pieza).
- `client/item/StackSpecialModel` — render especial de items que ve el `ItemStack`.
- `client/gui/ScareOverlay` — destello de susto a pantalla completa (fantasma/sirena).
- `client/dev/AutoJoinWorld` — abre un mundo solo al arrancar el cliente (`-Dnicktale.autojoin=<mundo>`), para pruebas automáticas.
- Ya estaban en la API: animación, `TabulaModel`/`AdvancedEntityModel`, materiales de armas/armaduras, colisiones custom.

### 4. Pruebas automatizadas (arnés `DevSmokeTest` en el mod)
- Servidor: arranca limpio; generación forzada de todas las features y estructuras; menas naturales.
- Cliente (Xvfb): mundo con las 58 entidades, 41 bloques con tile entity y 35 armor stands con todas las armaduras;
  prueba de jugabilidad guionizada (~21.000 interacciones: usar items, golpear, quemar, matar, usar bloques) → **0 fallos, sin crashes**.
- Respawn: ciclo completo colocar → matar → regenerar, y persistencia tras reiniciar el servidor.

## Pendiente / sin verificar (necesita ojos humanos o tiempo real)
- **Aspecto visual:** alas del dragón, armaduras (por piezas), iconos del cofre maldito y del portal.
- **Jugarlo a mano:** combate real, cría, forja, estructuras en terreno normal.
- **Respawn con las 12 h reales** (sólo se probó con 20 s).
- Render simplificado: portal dread, fantasma, grietas de hielo (funcionan; no son idénticos al original).
- Mundo: 7 features (cueva del cíclope, cueva de dragón de fuego, flores, etc.) dependen de terreno/bioma; sin evidencia de bug.
- **Decidir la licencia** de la API y del código nuevo (ver abajo). La API ya es un repo aparte.

## Sobre separar la API y la licencia (decisión tuya)
- La API ya está en su propio repo (privado): Nitanzw/Nicktale-API.
- Hoy la API declara `LGPL-3.0-or-later` (`nicktale-api/gradle.properties`, `mod_license`). **LGPL permite que cualquiera use,
  modifique y redistribuya** el código (incluso comercialmente) con ciertas condiciones, así que no sirve para cobrar.
  Si quieres que se pague por usarlo, hay que cambiar la licencia de la API (y del código nuevo del port) a una propietaria
  ("todos los derechos reservados" o licencia comercial) **antes** de publicarlo.
- Ojo: sólo se puede relicenciar lo que es realmente tuyo. Las partes derivadas del mod original (o de Citadel) siguen
  sujetas a sus licencias. Conviene revisar el texto exacto de la licencia de Ice and Fire y, si vas a vender, consultarlo con
  un abogado. Esto no es asesoría legal.
- Un repo privado + licencia propietaria protege más que un repo público con cualquier licencia, pero el mod compilado
  siempre se puede descompilar: lo que protege de verdad es la licencia y el control de distribución.
