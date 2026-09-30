# Plan de pruebas para agentes — Ice and Fire Port (NeoForge 26.2)

Documento para pasarle a una IA con agentes. **Objetivo: buscar bugs y verificar funciones, no arreglar nada.**
Al terminar, cada agente entrega un reporte en Markdown (formato al final) y la IA principal junta todo en
`docs/reports/REPORTE_FINAL.md`.

## 0. Reglas (léelas antes de empezar)

1. **No modifiques código del mod.** Sólo lee, ejecuta y reporta. Si algo falla, descríbelo; no lo arregles.
2. **No hagas commit ni push.** Los únicos archivos que puedes crear están en `docs/reports/`.
3. **No mates procesos Java en general** (`pkill java` rompe el entorno). Si hace falta cerrar el juego, usa
   `jps -l | grep -i devlaunch` y `kill -9 <pid>` sólo de ese proceso.
4. **Ahorra dinero:** trabaja por lotes, no leas logs enteros (pueden pesar GB). Usa siempre `grep`, `head`, `tail`,
   `sort | uniq -c`. Un agente por área (sección 3) y modelo barato para los agentes de búsqueda.
5. **No inventes resultados.** Si no pudiste verificar algo, escribe "NO VERIFICADO" y por qué.
6. Este entorno **no tiene pantalla ni herramientas de captura**. Lo puramente visual (cómo se ve un modelo) no se
   puede juzgar: márcalo como "REQUIERE OJO HUMANO" y pon la instrucción exacta para que una persona lo compruebe.

## 1. Proyecto

- Repo: `/home/user/Ice-and-Fire-Port-Nicktale`, rama `main`.
- Mod: port no oficial de Ice and Fire a **NeoForge 26.2 (MC 26.2)**. Necesita la **Nicktale API** (`nicktale-api/`, mismo repo).
- Java 25 (toolchain de Gradle), Gradle wrapper en la raíz. Código: `src/main` (común), `src/client` (cliente).
- Jars ya construidos en `dist/` (`iceandfire-nicktale-…jar` y `nicktaleapi-…jar`, van los dos en `mods/`).

## 2. Cómo ejecutar

| Qué | Comando (desde la raíz del repo) |
|---|---|
| Compilar todo | `./gradlew build -q` (la API se compila aparte: `cd nicktale-api && ./gradlew build -q`) |
| Servidor dedicado | `./gradlew runServer --console=plain` (buscar "Done (" en el log) |
| Cliente + mundo de prueba | `xvfb-run -a ./gradlew runClientTest -q` (usa `-Diaf.smoketest=true`, ver 2.1) |
| Generar datos | `./gradlew runServerData` y `./gradlew runClientData` |

Logs: servidor `runs/server/logs/latest.log`, cliente `runs/client/logs/latest.log`, crashes
`runs/client/crash-reports/` y `runs/server/crash-reports/`.
Ruido que se puede ignorar (es del entorno, no del mod): errores de Narrator/`flite`, OpenAL, `Advanced terminal features`.

### 2.1 Arnés de desarrollo (ya incluido en el código)

Clase `src/main/java/.../event/DevSmokeTest.java`. Sólo actúa si se activa:

- `-Diaf.smoketest=true` (cliente): abre el mundo `iaftest`, hace aparecer **todas las entidades del mod**,
  todos los bloques con tile entity y 35 armor stands con todas las armaduras; a los ~100 ticks ejecuta una
  **prueba de jugabilidad**: interactúa con cada mob usando muchos items, los golpea, los quema, los mata y usa cada
  bloque con tile entity. Termina con la línea `SMOKETEST gameplay done: N mobs, N interactions, N blocks used, N failures`.
  Preparación: `rm -rf runs/client/saves/iaftest && cp -r runs/server/wgworld runs/client/saves/iaftest` (o `runs/server/world`)
  y `runs/client/options.txt` con `onboardAccessibility:false`.
- Variable `IAF_WORLDGEN` (servidor): fuerza las probabilidades de generación y ejecuta comandos `place`.
  - `IAF_WORLDGEN=1`: coloca todas las features y estructuras del mod y para el servidor (`SMOKETEST worldgen done`).
  - `IAF_WORLDGEN=retry`: repite las features que suelen fallar en varias posiciones.
  - `IAF_WORLDGEN=respawn` + `-Diaf.dragonRespawnMillis=20000`: prueba el respawn de dragones (ver 3.6).
  - Ajustes locales del servidor de pruebas: `pause-when-empty-seconds=-1`, `max-tick-time=-1` en `runs/server/server.properties`.
- Al lanzar procesos largos, hazlo **desacoplado** (`setsid nohup … &`) y espera con un bucle `until grep -q "…" log; do sleep 10; done`.
  No encadenes `sleep` largos.

## 3. Áreas de prueba (un agente por área)

Para cada punto: ejecuta, busca evidencia en logs, y clasifica **OK / FALLA / NO VERIFICADO**.

### 3.1 Arranque y datos
- Servidor y cliente llegan a "Done"/menú sin `ERROR` propios del mod (`grep -E "ERROR|Exception" | grep -v "Narrator|OpenAL|flite"`).
- Ningún `Couldn't parse data file`, `Couldn't load tag`, `Missing following references`, `Unknown registry key`.
- Los datapacks cargan: recetas, advancements, loot tables, tags (`iceandfire:*`), villager trades.
- Recursos: nada de `Missing block model`, `Missing textures`, `Failed to load model` (excepto `fossil:` que es de otro mod).

### 3.2 Entidades y jugabilidad automatizada
- Correr el arnés del cliente (2.1). Esperado: 0 crashes, `0 failures`.
- Listar por entidad cualquier excepción (`grep -B1 -A6 "SMOKETEST .* failed"`).
- Revisar el log tras la corrida por `ERROR`, `Exception`, `Failed to handle packet`, `Ticking entity`.

### 3.3 Generación de mundo
- Correr `IAF_WORLDGEN=1` y `IAF_WORLDGEN=retry`. Registrar cuáles features/estructuras se colocan y cuáles no.
- Esperado: sin excepciones. Fallos de "Failed to place feature" son normales si el terreno/bioma no encaja
  (las flores necesitan arena/hielo/tierra; las menas necesitan piedra). Distingue **fallo legítimo** de **bug**:
  para una mena que falle, compárala con una mena vanilla (`minecraft:ore_iron`) en la misma posición.
- Pendiente conocido: menas `silver_ore` y `sapphire_ore` fallaron a y=20 en la prueba; **no se sabe si es bug**. Investígalo.
- Estructuras: `gorgon_temple`, `graveyard`, `mausoleum` (necesitan carga de área ancha; ver el arnés).
- No debe haber spam de logs de worldgen (`unsafe terrain read`, `far chunk`): contar líneas, esperado 0.

### 3.4 Items, bloques y GUIs
- Todos los items tienen definición en `assets/iceandfire/items/*.json` y su modelo existe (script: por cada `items/X.json`
  comprobar que el modelo referenciado existe en `models/`).
- Propiedades de item: arco (`dragonbone_bow`), cuerno de dragón, cristales de invocación, tridente (`tide_trident`).
- Cada bloque con tile entity abre su menú/GUI sin excepción (lo cubre el arnés; revisa la cifra de bloques usados).
- Cofre maldito (`ghost_chest`) y portal dread (`dread_portal`) deben tener icono: REQUIERE OJO HUMANO.

### 3.5 Armaduras
- Cada `ItemModArmor` tiene JSON en `assets/iceandfire/equipment/<asset>.json` y texturas en
  `textures/entity/equipment/humanoid[_leggings]/`. Verificar que existen todas.
- Los modelos propios se conectan en `client/model/armor/IafArmorModels.java`: comprobar que cada set con modelo propio
  (plata, cobre, dragonsteel×3, trol×3, serpiente marina×7, escamas de dragón×12, gusano×3) está mapeado.
- Render: los 35 armor stands del arnés no deben producir excepción. Cómo se ven: REQUIERE OJO HUMANO.

### 3.6 Respawn de dragones (feature nueva)
- Regla: cuando el dragón de una cueva o roost muere o se doma, a las **12 horas reales** se regenera el sitio
  (estructura + dragón). Código: `world/DragonRespawnData.java`, `event/DragonRespawnEvents.java`.
- Prueba: `IAF_WORLDGEN=respawn` con `-Diaf.dragonRespawnMillis=20000` (la comprobación corre cada 1200 ticks ≈ 60 s).
  Esperado en el log: `SMOKETEST dragons alive` sube tras colocar, baja tras matar, **vuelve a subir** y aparece
  `Respawned dragon site …`. **Estado actual: la primera prueba NO respawneó** (se corrigió una condición de carrera
  registrando sitios antes de marcarlos; hay que repetir la prueba y confirmar).
- Probar también: dragón domado (evento `AnimalTameEvent`), reinicio del servidor entre la muerte y el respawn
  (los datos deben persistir en el mundo), y que un dragón vivo no se duplique.

### 3.7 Balance y reglas pedidas
- Dragones vuelan al 50 %: `EntityDragonBase.DRAGON_FLIGHT_SPEED_SCALE = 0.5`. Comprobar que se aplica en vuelo autónomo
  (`IafDragonFlightManager.speedMod`) y montado (`EntityDragonBase.travel`); los amphithere NO deben cambiar.
- Dragones domesticados nunca rompen bloques: `DragonUtils.canGrief`. Comprobar que ninguna otra ruta de rotura
  (`breakBlocks`, `isBlockExplicitlyPassable`, fuego del aliento) los deja destruir bloques.

### 3.8 Mixins
- Declarados en `META-INF/neoforge.mods.toml` (`[[mixins]]`) y `iceandfire.mixins.json`. Confirmar que ninguno falla al aplicarse
  (`grep -i "mixin" log | grep -i "error|critical|fail"`) y que `EntityCollideMixin` (colisiones del deathworm) y
  `WorldGenRegionMixin` (silencia logs de worldgen) hacen su trabajo.

### 3.9 Revisión de código (sólo lectura)
- Buscar en `src/` restos de API antigua o `TODO/FIXME` peligrosos; rutas de datos con nombres antiguos
  (`recipes`, `loot_tables`, `structures`, `tags/items|blocks|entity_types`), tags `forge:` en vez de `c:`,
  eventos de entidad que colisionen con ids especiales del cliente vanilla (21, 35, 63).

## 4. Problemas conocidos (no los reportes como nuevos, pero confirma su estado)

- Render simplificado: portal dread, fantasma y grietas de hielo.
- El "susto" del fantasma/sirena es un destello de pantalla, no el modelo original.
- `firedragon_swimming.tbl` / `firedragon_swim5.tbl` no existen (sólo es un aviso).
- Alas del dragón: se corrigió que TabulaModel descartaba cajas planas (membranas). **No se ha visto a la vista.**
- No se ha jugado nada a mano: combate real, cría, forja completa, estructuras sobre terreno normal.

## 5. Formato del reporte (cada agente, en `docs/reports/<area>.md`)

```markdown
# Reporte: <área>
Fecha/hora, commit probado (`git rev-parse --short HEAD`), comandos ejecutados.

## Resumen
OK: N · FALLA: N · NO VERIFICADO: N · REQUIERE OJO HUMANO: N

## Resultados
| # | Prueba | Estado | Evidencia (línea de log / archivo:línea) |
|---|--------|--------|-------------------------------------------|

## Bugs encontrados
### BUG-<n>: título corto
- Severidad: crash / funcional / visual / menor
- Cómo reproducir (comandos exactos)
- Evidencia (máx. 15 líneas de log o traza)
- Causa probable (archivo:línea) — sólo hipótesis, sin tocar código

## No verificado y por qué

## Para revisión humana
Lista de cosas visuales con instrucciones concretas (dónde mirar, qué debería verse).
```

La IA principal consolida en `docs/reports/REPORTE_FINAL.md`: tabla global de estado, lista de bugs ordenada por
severidad (crashes primero), y una lista corta de qué debe revisar una persona.
