package com.github.alexthe666.iceandfire.event;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

/** Development aid: with {@code -Diaf.smoketest=true} spawns every mod entity and block-entity block near the player. */
public class DevSmokeTest {
    private boolean done;

    @SubscribeEvent
    public void onTick(PlayerTickEvent.Post event) {
        if (done || !(event.getEntity() instanceof ServerPlayer player) || player.tickCount < 100) {
            return;
        }
        done = true;
        player.setGameMode(GameType.CREATIVE);
        var level = player.level();
        BlockPos base = player.blockPosition().offset(6, 0, 0);
        int i = 0;
        for (EntityType<?> type : BuiltInRegistries.ENTITY_TYPE) {
            if (!BuiltInRegistries.ENTITY_TYPE.getKey(type).getNamespace().equals(IceAndFire.MODID)) {
                continue;
            }
            try {
                Entity e = type.create(level, EntitySpawnReason.COMMAND);
                if (e != null) {
                    e.setPos(base.getX() + (i % 8) * 6, base.getY() + 2, base.getZ() + (i / 8) * 6);
                    level.addFreshEntity(e);
                }
            } catch (Throwable t) {
                IceAndFire.LOGGER.error("SMOKETEST entity spawn failed: {}", type, t);
            }
            i++;
        }
        int j = 0;
        BlockPos bbase = player.blockPosition().offset(-6, 0, -30);
        for (var block : BuiltInRegistries.BLOCK) {
            if (!BuiltInRegistries.BLOCK.getKey(block).getNamespace().equals(IceAndFire.MODID) || !(block instanceof EntityBlock)) {
                continue;
            }
            try {
                BlockState state = block.defaultBlockState();
                level.setBlock(bbase.offset((j % 10) * 3, 1, (j / 10) * 3), state, 3);
            } catch (Throwable t) {
                IceAndFire.LOGGER.error("SMOKETEST block failed: {}", block, t);
            }
            j++;
        }
        IceAndFire.LOGGER.info("SMOKETEST spawned {} entities, {} block entities", i, j);
    }
}
