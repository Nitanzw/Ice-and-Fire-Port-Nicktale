package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.entity.util.EntityDataIO;
import com.github.alexthe666.iceandfire.entity.util.IDreadMob;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.equine.SkeletonHorse;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class EntityDreadHorse extends SkeletonHorse implements IDreadMob {

    protected static final EntityDataAccessor<Optional<UUID>> COMMANDER_UNIQUE_ID = SynchedEntityData.defineId(EntityDreadHorse.class, EntityDataSerializers.OPTIONAL_UUID);

    public EntityDreadHorse(EntityType type, Level worldIn) {
        super(type, worldIn);
    }


    public static AttributeSupplier.Builder bakeAttributes() {
        return createBaseHorseAttributes()
            //HEALTH
            .add(Attributes.MAX_HEALTH, 25.0D)
            //SPEED
            .add(Attributes.MOVEMENT_SPEED, 0.3D)
            //ARMOR
            .add(Attributes.ARMOR, 4.0D);
    }


    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(COMMANDER_UNIQUE_ID, Optional.empty());
    }

    @Override
    public void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        CompoundTag compound = new CompoundTag();

        if (this.getCommanderId() != null) {
            compound.putUUID("CommanderUUID", this.getCommanderId());
        }

        output.store(compound);
    }

    @Override
    public void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        CompoundTag compound = EntityDataIO.readLegacyFields(input);

        UUID uuid;
        if (compound.hasUUID("CommanderUUID")) {
            uuid = compound.getUUID("CommanderUUID");
        } else {
            String s = compound.getString("CommanderUUID");
            uuid = OldUsersConverter.convertMobOwnerIfNecessary(this.getServer(), s);
        }

        if (uuid != null) {
            try {
                this.setCommanderId(uuid);
            } catch (Throwable throwable) {
            }
        }


    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor worldIn, @NotNull DifficultyInstance difficultyIn, @NotNull EntitySpawnReason reason, @Nullable SpawnGroupData spawnDataIn) {
        SpawnGroupData data = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn);
        this.setAge(24000);
        return data;
    }

    @Override
    public boolean isAlliedTo(@NotNull Entity entityIn) {
        return entityIn instanceof IDreadMob || super.isAlliedTo(entityIn);
    }

    @Nullable
    public UUID getCommanderId() {
        return this.entityData.get(COMMANDER_UNIQUE_ID).orElse(null);
    }

    public void setCommanderId(@Nullable UUID uuid) {
        this.entityData.set(COMMANDER_UNIQUE_ID, Optional.ofNullable(uuid));
    }

    @Override
    public Entity getCommander() {
        try {
            UUID uuid = this.getCommanderId();
            return uuid == null ? null : this.level().getPlayerByUUID(uuid);
        } catch (IllegalArgumentException var2) {
            return null;
        }
    }

}
