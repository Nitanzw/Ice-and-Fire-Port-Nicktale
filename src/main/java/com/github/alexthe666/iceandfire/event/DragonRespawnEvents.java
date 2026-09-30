package com.github.alexthe666.iceandfire.event;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.util.WorldUtil;
import com.github.alexthe666.iceandfire.world.DragonRespawnData;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.AnimalTameEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

/** Regenerates dragon caves and roosts (structure and dragon) twelve real hours after their dragon is gone. */
public class DragonRespawnEvents {
    private static final int CHECK_INTERVAL_TICKS = 1200;

    private static void markFreed(EntityDragonBase dragon) {
        DragonRespawnData data = DragonRespawnData.get(dragon.level().getServer());
        data.drainPending(); // sites registered by worldgen threads may not be merged yet
        IceAndFire.LOGGER.info("Dragon {} ({}) is gone, checking its site", dragon.getUUID(), dragon.getType());
        data.markFreed(dragon.getUUID());
    }

    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof EntityDragonBase dragon && !dragon.level().isClientSide()) {
            markFreed(dragon);
        }
    }

    @SubscribeEvent
    public void onTame(AnimalTameEvent event) {
        if (event.getAnimal() instanceof EntityDragonBase dragon && !dragon.level().isClientSide()) {
            markFreed(dragon);
        }
    }

    @SubscribeEvent
    public void onServerTick(ServerTickEvent.Post event) {
        var server = event.getServer();
        if (server.getTickCount() % CHECK_INTERVAL_TICKS != 0) {
            return;
        }
        DragonRespawnData data = DragonRespawnData.get(server);
        data.drainPending();
        for (DragonRespawnData.Site site : data.takeDue(System.currentTimeMillis(), DragonRespawnData.RESPAWN_MILLIS)) {
            regenerate(server, site);
        }
    }

    private static void regenerate(net.minecraft.server.MinecraftServer server, DragonRespawnData.Site site) {
        ServerLevel level = server.getLevel(ResourceKey.create(Registries.DIMENSION, Identifier.parse(site.dimension())));
        if (level == null) {
            return;
        }
        var feature = level.registryAccess().lookupOrThrow(Registries.CONFIGURED_FEATURE)
                .get(ResourceKey.create(Registries.CONFIGURED_FEATURE, Identifier.parse(site.featureId())));
        if (feature.isEmpty()) {
            IceAndFire.LOGGER.warn("Cannot respawn dragon site, unknown feature {}", site.featureId());
            return;
        }
        ChunkPos chunk = ChunkPos.containing(site.origin());
        level.getChunk(chunk.x(), chunk.z(), ChunkStatus.FULL, true);
        WorldUtil.setForceGeneration(true);
        try {
            boolean placed = feature.get().value().place(level, level.getChunkSource().getGenerator(), RandomSource.create(), site.origin());
            IceAndFire.LOGGER.info("Respawned dragon site {} at {} -> {}", site.featureId(), site.origin(), placed);
        } catch (Exception e) {
            IceAndFire.LOGGER.error("Failed to respawn dragon site {} at {}", site.featureId(), site.origin(), e);
        } finally {
            WorldUtil.setForceGeneration(false);
        }
    }
}
