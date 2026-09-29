// SPDX-License-Identifier: LGPL-3.0-or-later
package com.nicktale.api.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;

/** Small 26.2 stream helpers for values commonly used by mod payloads. */
public final class PacketBufferUtils {
    private PacketBufferUtils() {
    }

    public static void writeBlockPos(RegistryFriendlyByteBuf buffer, BlockPos pos) {
        BlockPos.STREAM_CODEC.encode(buffer, Objects.requireNonNull(pos, "pos"));
    }

    public static BlockPos readBlockPos(RegistryFriendlyByteBuf buffer) {
        return BlockPos.STREAM_CODEC.decode(buffer);
    }

    public static void writeIdentifier(RegistryFriendlyByteBuf buffer, Identifier identifier) {
        Identifier.STREAM_CODEC.encode(buffer, Objects.requireNonNull(identifier, "identifier"));
    }

    public static Identifier readIdentifier(RegistryFriendlyByteBuf buffer) {
        return Identifier.STREAM_CODEC.decode(buffer);
    }

    public static void writeItemStack(RegistryFriendlyByteBuf buffer, ItemStack stack) {
        ItemStack.STREAM_CODEC.encode(buffer, Objects.requireNonNull(stack, "stack"));
    }

    public static ItemStack readItemStack(RegistryFriendlyByteBuf buffer) {
        return ItemStack.STREAM_CODEC.decode(buffer);
    }

    public static void writeVec3(RegistryFriendlyByteBuf buffer, Vec3 vector) {
        buffer.writeDouble(vector.x);
        buffer.writeDouble(vector.y);
        buffer.writeDouble(vector.z);
    }

    public static Vec3 readVec3(RegistryFriendlyByteBuf buffer) {
        return new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
    }

    public static <E extends Enum<E>> void writeEnum(RegistryFriendlyByteBuf buffer, E value) {
        buffer.writeVarInt(Objects.requireNonNull(value, "value").ordinal());
    }

    public static <E extends Enum<E>> E readEnum(RegistryFriendlyByteBuf buffer, Class<E> enumClass) {
        E[] all = Objects.requireNonNull(enumClass, "enumClass").getEnumConstants();
        int ordinal = buffer.readVarInt();
        if (ordinal < 0 || ordinal >= all.length) {
            throw new IllegalArgumentException("Invalid enum ordinal " + ordinal);
        }
        return all[ordinal];
    }
}
