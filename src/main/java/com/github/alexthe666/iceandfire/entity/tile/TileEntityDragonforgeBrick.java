package com.github.alexthe666.iceandfire.entity.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public final class TileEntityDragonforgeBrick extends BlockEntity {
    public TileEntityDragonforgeBrick(BlockPos pos, BlockState state) {
        super(IafTileEntityRegistry.DRAGONFORGE_BRICK.get(), pos, state);
    }
}
