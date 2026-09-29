package com.github.alexthe666.iceandfire.entity.tile;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityPixie;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.enums.EnumParticles;
import com.github.alexthe666.iceandfire.message.MessageUpdatePixieHouse;
import com.github.alexthe666.iceandfire.message.MessageUpdatePixieHouseModel;
import com.github.alexthe666.iceandfire.message.MessageUpdatePixieJar;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.UUIDUtil;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.UUID;

public class TileEntityJar extends BlockEntity {

    private static final float PARTICLE_WIDTH = 0.3F;
    private static final float PARTICLE_HEIGHT = 0.6F;
    public boolean hasPixie;
    public boolean prevHasProduced;
    public boolean hasProduced;
    public boolean tamedPixie;
    public UUID pixieOwnerUUID;
    public int pixieType;
    public int ticksExisted;
    public NonNullList<ItemStack> pixieItems = NonNullList.withSize(1, ItemStack.EMPTY);
    public float rotationYaw;
    public float prevRotationYaw;
    private final PixieJarInvWrapper itemHandler = new PixieJarInvWrapper(this);
    private final Random rand;

    public TileEntityJar(BlockPos pos, BlockState state) {
        super(IafTileEntityRegistry.PIXIE_JAR.get(), pos, state);
        this.rand = new Random();
        this.hasPixie = true;
    }

    public TileEntityJar(BlockPos pos, BlockState state, boolean empty) {
        super(IafTileEntityRegistry.PIXIE_JAR.get(), pos, state);
        this.rand = new Random();
        this.hasPixie = !empty;
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putBoolean("HasPixie", hasPixie);
        output.putInt("PixieType", pixieType);
        output.putBoolean("HasProduced", hasProduced);
        output.putBoolean("TamedPixie", tamedPixie);
        if (pixieOwnerUUID != null) {
            output.store("PixieOwnerUUID", UUIDUtil.CODEC, pixieOwnerUUID);
        }
        output.putInt("TicksExisted", ticksExisted);
        ContainerHelper.saveAllItems(output, this.pixieItems);
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        if (this.hasPixie) {
            this.releasePixie();
        }
        super.preRemoveSideEffects(pos, state);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        hasPixie = input.getBooleanOr("HasPixie", false);
        pixieType = input.getIntOr("PixieType", 0);
        hasProduced = input.getBooleanOr("HasProduced", false);
        ticksExisted = input.getIntOr("TicksExisted", 0);
        tamedPixie = input.getBooleanOr("TamedPixie", false);
        pixieOwnerUUID = input.read("PixieOwnerUUID", UUIDUtil.LENIENT_CODEC).orElse(null);
        if (pixieOwnerUUID == null) {
            input.getString("PixieOwnerUUID").ifPresent(ownerName -> {
                try {
                    if (this.level != null && this.level.getServer() != null) {
                        this.pixieOwnerUUID = net.minecraft.server.players.OldUsersConverter.convertMobOwnerIfNecessary(
                            this.level.getServer(), ownerName);
                    }
                } catch (Exception ignored) {
                }
            });
        }
        this.pixieItems = NonNullList.withSize(1, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(input, pixieItems);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, TileEntityJar entityJar) {
        entityJar.ticksExisted++;
        if (level.isClientSide() && entityJar.hasPixie) {
            IceAndFire.PROXY.spawnParticle(EnumParticles.If_Pixie,
                pos.getX() + 0.5F + (double) (entityJar.rand.nextFloat() * PARTICLE_WIDTH * 2F) - PARTICLE_WIDTH,
                pos.getY() + (double) (entityJar.rand.nextFloat() * PARTICLE_HEIGHT),
                pos.getZ() + 0.5F + (double) (entityJar.rand.nextFloat() * PARTICLE_WIDTH * 2F) - PARTICLE_WIDTH, EntityPixie.PARTICLE_RGB[entityJar.pixieType][0], EntityPixie.PARTICLE_RGB[entityJar.pixieType][1], EntityPixie.PARTICLE_RGB[entityJar.pixieType][2]);
        }
        if (entityJar.ticksExisted % 24000 == 0 && !entityJar.hasProduced && entityJar.hasPixie) {
            entityJar.hasProduced = true;
            if (!level.isClientSide()) {
                IceAndFire.sendMSGToAll(new MessageUpdatePixieJar(pos.asLong(), entityJar.hasProduced));
            }
        }
        if (entityJar.hasPixie && entityJar.hasProduced != entityJar.prevHasProduced && entityJar.ticksExisted > 5) {
            if (!level.isClientSide()) {
                IceAndFire.sendMSGToAll(new MessageUpdatePixieJar(pos.asLong(), entityJar.hasProduced));
            } else {
                level.playLocalSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5, IafSoundRegistry.PIXIE_HURT, SoundSource.BLOCKS, 1, 1, false);
            }
        }
        entityJar.prevRotationYaw = entityJar.rotationYaw;
        if (entityJar.rand.nextInt(30) == 0) {
            entityJar.rotationYaw = (entityJar.rand.nextFloat() * 360F) - 180F;
        }
        if (entityJar.hasPixie && entityJar.ticksExisted % 40 == 0 && entityJar.rand.nextInt(6) == 0 && level.isClientSide()) {
            level.playLocalSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5, IafSoundRegistry.PIXIE_IDLE, SoundSource.BLOCKS, 1, 1, false);
        }
        entityJar.prevHasProduced = entityJar.hasProduced;
    }

    public void releasePixie() {
        if (this.level == null || this.level.isClientSide()) {
            return;
        }
        EntityPixie pixie = new EntityPixie(IafEntityRegistry.PIXIE.get(), this.level);
        pixie.setPos(this.worldPosition.getX() + 0.5F, this.worldPosition.getY() + 1F, this.worldPosition.getZ() + 0.5F);
        pixie.setYRot(this.rand.nextInt(360));
        pixie.setItemInHand(InteractionHand.MAIN_HAND, pixieItems.get(0));
        pixie.setColor(this.pixieType);
        this.level.addFreshEntity(pixie);
        this.hasPixie = false;
        this.pixieType = 0;
        pixie.ticksUntilHouseAI = 500;
        pixie.setTame(this.tamedPixie, false);
        pixie.setOwnerReference(this.pixieOwnerUUID == null ? null : EntityReference.of(this.pixieOwnerUUID));

        IceAndFire.sendMSGToAll(new MessageUpdatePixieHouse(worldPosition.asLong(), false, 0));
    }

    @Override
    public @NotNull net.minecraft.nbt.CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveWithFullMetadata(registries);
    }

    public ResourceHandler<ItemResource> getItemHandler() {
        return this.itemHandler;
    }
}
