package com.github.alexthe666.iceandfire.misc;

import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Optional;
import java.util.UUID;

/**
 * Synched entity data serializers that vanilla no longer provides. The instances are created eagerly (entities need them in
 * static initializers) and registered afterwards through {@link #SERIALIZERS}, which the mod constructor must attach to the mod bus.
 */
public final class IafDataSerializers {
    public static final DeferredRegister<EntityDataSerializer<?>> SERIALIZERS =
            DeferredRegister.create(NeoForgeRegistries.ENTITY_DATA_SERIALIZERS, "iceandfire");

    public static final EntityDataSerializer<Optional<UUID>> OPTIONAL_UUID =
            EntityDataSerializer.forValueType(ByteBufCodecs.optional(UUIDUtil.STREAM_CODEC));

    public static final EntityDataSerializer<CompoundTag> COMPOUND_TAG =
            EntityDataSerializer.forValueType(ByteBufCodecs.COMPOUND_TAG);

    static {
        SERIALIZERS.register("optional_uuid", () -> OPTIONAL_UUID);
        SERIALIZERS.register("compound_tag", () -> COMPOUND_TAG);
    }

    private IafDataSerializers() {
    }
}
