package com.github.alexthe666.iceandfire.entity.tile;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.EntityDragonEgg;
import com.github.alexthe666.iceandfire.entity.EntityIceDragon;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.enums.EnumDragonEgg;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class TileEntityEggInIce extends BlockEntity {
    public EnumDragonEgg type;
    public int age;
    public int ticksExisted;
    @Nullable
    public UUID ownerUUID;
    // boolean to prevent time in a bottle shenanigans
    private boolean spawned;

    public TileEntityEggInIce(BlockPos pos, BlockState state) {
        super(IafTileEntityRegistry.EGG_IN_ICE.get(), pos, state);
    }

    public static void tickEgg(Level level, BlockPos pos, BlockState state, TileEntityEggInIce entityEggInIce) {
        entityEggInIce.age++;
        if (entityEggInIce.age >= IafConfig.dragonEggTime && entityEggInIce.type != null && !entityEggInIce.spawned) {
            if (!level.isClientSide()) {
                EntityIceDragon dragon = new EntityIceDragon(level);
                dragon.setPos(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
                dragon.setVariant(entityEggInIce.type.ordinal() - 4);
                dragon.setGender(ThreadLocalRandom.current().nextBoolean());
                dragon.setTame(true, false);
                dragon.setHunger(50);
                dragon.setOwnerReference(entityEggInIce.ownerUUID == null ? null : EntityReference.of(entityEggInIce.ownerUUID));
                level.addFreshEntity(dragon);
                entityEggInIce.spawned = true;
                level.destroyBlock(pos, false);
                level.setBlockAndUpdate(pos, Blocks.WATER.defaultBlockState());
            }

        }
        entityEggInIce.ticksExisted++;
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("Color", type == null ? 0 : type.ordinal());
        output.putInt("Age", age);
        output.putBoolean("Spawned", spawned);
        if (ownerUUID == null) {
            output.putString("OwnerUUID", "");
        } else {
            output.store("OwnerUUID", UUIDUtil.CODEC, ownerUUID);
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        int color = Mth.clamp(input.getIntOr("Color", 0), 0, EnumDragonEgg.values().length - 1);
        type = EnumDragonEgg.values()[color];
        age = input.getIntOr("Age", 0);
        spawned = input.getBooleanOr("Spawned", false);
        ownerUUID = input.read("OwnerUUID", UUIDUtil.LENIENT_CODEC).orElse(null);
        if (ownerUUID == null) {
            input.getString("OwnerUUID").ifPresent(ownerName -> {
                try {
                    if (this.level != null && this.level.getServer() != null) {
                        this.ownerUUID = OldUsersConverter.convertMobOwnerIfNecessary(this.level.getServer(), ownerName);
                    }
                } catch (Exception ignored) {
                }
            });
        }
    }

    @Override
    public @NotNull net.minecraft.nbt.CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveCustomOnly(registries);
    }

    @Override
    @Nullable
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void spawnEgg() {
        if (type != null) {
            EntityDragonEgg egg = new EntityDragonEgg(IafEntityRegistry.DRAGON_EGG.get(), level);
            egg.setEggType(type);
            egg.setPos(worldPosition.getX() + 0.5, worldPosition.getY() + 1, worldPosition.getZ() + 0.5);
            egg.setOwnerId(this.ownerUUID);
            if (!level.isClientSide()) {
                level.addFreshEntity(egg);
            }
        }
    }
}
