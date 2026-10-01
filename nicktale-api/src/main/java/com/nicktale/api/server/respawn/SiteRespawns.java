package com.nicktale.api.server.respawn;

import com.nicktale.api.NicktaleApi;
import com.nicktale.api.server.worldgen.ForcedGeneration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * Respawning world-generated sites. A mod registers a site when its feature places an entity
 * ({@link #register}), tells the API when that entity is gone ({@link #markFreed}), and the API regenerates the
 * whole site, structure and entity, once the site's real-time delay has passed. Regeneration runs the same configured
 * feature again with {@link ForcedGeneration} on, so the feature should skip its chance and spacing checks then.
 */
public final class SiteRespawns {
    private static final Logger LOGGER = LoggerFactory.getLogger(NicktaleApi.MOD_ID);
    private static final int CHECK_INTERVAL_TICKS = 1200;

    private SiteRespawns() {
    }

    /**
     * Registers a site. Safe to call from worldgen threads.
     *
     * @param configuredFeatureId id of the configured feature that builds the site, e.g. {@code mymod:fire_dragon_cave}
     * @param dimension           dimension id, e.g. {@code minecraft:overworld}
     * @param origin              origin the feature was placed at (passed to the feature again on respawn)
     * @param entity              the entity the site spawned
     * @param delayMillis         real-time milliseconds between losing the entity and regenerating the site
     */
    public static void register(String configuredFeatureId, String dimension, BlockPos origin, UUID entity, long delayMillis) {
        SiteRespawnData.queue(configuredFeatureId, dimension, origin, entity, delayMillis);
    }

    /** The entity of a site died, was tamed, or was otherwise lost: start the site's respawn timer. */
    public static void markFreed(MinecraftServer server, UUID entity) {
        if (server == null) {
            return;
        }
        SiteRespawnData data = SiteRespawnData.get(server);
        data.drainPending(); // sites registered by worldgen threads may not be merged yet
        data.markFreed(entity).ifPresent(site -> LOGGER.info("Site {} at {} freed, respawning in {} ms", site.featureId(), site.origin(), site.delayMillis()));
    }

    /** Game-bus handler; registered by {@link NicktaleApi}. */
    public static final class Events {
        @SubscribeEvent
        public void onServerTick(ServerTickEvent.Post event) {
            MinecraftServer server = event.getServer();
            if (server.getTickCount() % CHECK_INTERVAL_TICKS != 0) {
                return;
            }
            SiteRespawnData data = SiteRespawnData.get(server);
            data.drainPending();
            for (SiteRespawnData.Site site : data.takeDue(System.currentTimeMillis())) {
                regenerate(server, site);
            }
        }

        private static void regenerate(MinecraftServer server, SiteRespawnData.Site site) {
            ServerLevel level = server.getLevel(ResourceKey.create(Registries.DIMENSION, Identifier.parse(site.dimension())));
            if (level == null) {
                return;
            }
            var feature = level.registryAccess().lookupOrThrow(Registries.CONFIGURED_FEATURE)
                    .get(ResourceKey.create(Registries.CONFIGURED_FEATURE, Identifier.parse(site.featureId())));
            if (feature.isEmpty()) {
                LOGGER.warn("Cannot respawn site, unknown feature {}", site.featureId());
                return;
            }
            ChunkPos chunk = ChunkPos.containing(site.origin());
            level.getChunk(chunk.x(), chunk.z(), ChunkStatus.FULL, true);
            ForcedGeneration.set(true);
            try {
                boolean placed = feature.get().value().place(level, level.getChunkSource().getGenerator(), RandomSource.create(), site.origin());
                LOGGER.info("Respawned site {} at {} -> {}", site.featureId(), site.origin(), placed);
            } catch (Exception e) {
                LOGGER.error("Failed to respawn site {} at {}", site.featureId(), site.origin(), e);
            } finally {
                ForcedGeneration.set(false);
            }
        }
    }
}
