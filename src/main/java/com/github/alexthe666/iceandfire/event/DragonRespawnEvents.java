package com.github.alexthe666.iceandfire.event;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.nicktale.api.server.respawn.SiteRespawns;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.AnimalTameEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

/**
 * Dragon caves and roosts come back (structure and dragon) twelve real hours after their dragon dies or is tamed.
 * The respawn machinery itself lives in the Nicktale API ({@link SiteRespawns}); this class only reports dragons.
 */
public class DragonRespawnEvents {
    /** Real-time delay before an emptied dragon site regenerates. */
    public static final long RESPAWN_MILLIS = Long.getLong("iaf.dragonRespawnMillis", 12L * 60L * 60L * 1000L);

    /** Called by the cave and roost features after they spawn their dragon. */
    public static void registerSite(Level level, EntityDragonBase dragon, String featureSuffix, BlockPos origin) {
        String feature = BuiltInRegistries.ENTITY_TYPE.getKey(dragon.getType()).withSuffix(featureSuffix).toString();
        SiteRespawns.register(feature, level.dimension().identifier().toString(), origin, dragon.getUUID(), RESPAWN_MILLIS);
    }

    public static void markFreed(EntityDragonBase dragon) {
        if (dragon.level().getServer() != null) {
            IceAndFire.LOGGER.info("Dragon {} ({}) is gone, checking its site", dragon.getUUID(), dragon.getType());
            SiteRespawns.markFreed(dragon.level().getServer(), dragon.getUUID());
        }
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
}
