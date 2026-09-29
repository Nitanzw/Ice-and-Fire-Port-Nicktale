/*
 * Ice and Fire NeoForge port
 * SPDX-License-Identifier: LGPL-3.0-or-later
 */
package com.github.alexthe666.iceandfire.entity.util;

import com.mojang.serialization.MapCodec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.ValueInput;

/** Helpers for reading the existing flat entity-save keys through Minecraft's ValueInput API. */
public final class EntityDataIO {
    private EntityDataIO() {
    }

    public static CompoundTag readLegacyFields(ValueInput input) {
        return input.read(MapCodec.assumeMapUnsafe(CompoundTag.CODEC)).orElseGet(CompoundTag::new);
    }
}
