package com.github.alexthe666.iceandfire.entity.tile;

import com.github.alexthe666.iceandfire.entity.EntityGhost;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.ContainerUser;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

public class TileEntityGhostChest extends ChestBlockEntity {

    public TileEntityGhostChest(BlockPos pos, BlockState state) {
        super(IafTileEntityRegistry.GHOST_CHEST.get(), pos, state);
    }

    @Override
    public void startOpen(ContainerUser user) {
        super.startOpen(user);
        if (this.level != null && this.level.getDifficulty() != Difficulty.PEACEFUL) {
            EntityGhost ghost = IafEntityRegistry.GHOST.get().create(level);
            if (ghost != null && this.level instanceof ServerLevel serverLevel) {
                ghost.setPos(this.worldPosition.getX() + 0.5F, this.worldPosition.getY() + 0.5F, this.worldPosition.getZ() + 0.5F);
                ghost.setYRot(ThreadLocalRandom.current().nextFloat() * 360F);
                ghost.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(this.worldPosition), EntitySpawnReason.SPAWNER, null);
                if (user.getLivingEntity() instanceof Player player && !player.isCreative()) {
                    ghost.setTarget(player);
                }
                ghost.setPersistenceRequired();
                serverLevel.addFreshEntity(ghost);
                ghost.setAnimation(EntityGhost.ANIMATION_SCARE);
                ghost.setFromChest(true);
            }
        }
    }

    @Override
    protected void signalOpenCount(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, int p_155336_, int p_155337_) {
        super.signalOpenCount(level, pos, state, p_155336_, p_155337_);
        level.updateNeighborsAt(pos.below(), state.getBlock());
    }
}
