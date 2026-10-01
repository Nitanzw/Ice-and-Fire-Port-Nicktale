package com.nicktale.api.server.entity;

import com.mojang.serialization.MapCodec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;

/** Helpers for reading the existing flat entity-save keys through Minecraft's ValueInput API. */
public final class EntityDataIO {
    private EntityDataIO() {
    }

    public static CompoundTag readLegacyFields(ValueInput input) {
        return input.read(MapCodec.assumeMapUnsafe(CompoundTag.CODEC)).orElseGet(CompoundTag::new);
    }

    /** Full entity save (without id) as a CompoundTag. */
    public static CompoundTag saveWithoutId(Entity entity) {
        TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, entity.registryAccess());
        entity.saveWithoutId(output);
        return output.buildResult();
    }

    /** Only the entity-specific additional save data as a CompoundTag. */
    public static CompoundTag saveAdditional(Entity entity, java.util.function.Consumer<net.minecraft.world.level.storage.ValueOutput> writer) {
        TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, entity.registryAccess());
        writer.accept(output);
        return output.buildResult();
    }

    public static ValueInput input(HolderLookup.Provider registries, CompoundTag tag) {
        return TagValueInput.create(ProblemReporter.DISCARDING, registries, tag);
    }

    public static ValueInput input(Entity entity, CompoundTag tag) {
        return input(entity.registryAccess(), tag);
    }
}
