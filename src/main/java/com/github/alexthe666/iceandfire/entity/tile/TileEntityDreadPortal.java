package com.github.alexthe666.iceandfire.entity.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;

public class TileEntityDreadPortal extends BlockEntity {
    private long age;
    private BlockPos exitPortal;
    private boolean exactTeleport;

    public TileEntityDreadPortal(BlockPos pos, BlockState state) {
        super(IafTileEntityRegistry.DREAD_PORTAL.get(), pos, state);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putLong("Age", this.age);

        if (this.exitPortal != null) {
            //   compound.setTag("ExitPortal", NBTUtil.createPosTag(this.exitPortal));
        }

        if (this.exactTeleport) {
            output.putBoolean("ExactTeleport", this.exactTeleport);
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.age = input.getLongOr("Age", 0L);

        if (input.child("ExitPortal").isPresent()) {
            this.exitPortal = BlockPos.ZERO;
        }

        this.exactTeleport = input.getBooleanOr("ExactTeleport", false);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, TileEntityDreadPortal dreadPortal) {
        ++dreadPortal.age;
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull net.minecraft.nbt.CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveCustomOnly(registries);
    }

    public boolean shouldRenderFace(Direction face) {
        return true;
    }
}
