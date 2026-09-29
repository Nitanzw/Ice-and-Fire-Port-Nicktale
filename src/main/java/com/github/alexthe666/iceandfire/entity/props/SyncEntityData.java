/*
 * Ice and Fire NeoForge port
 * SPDX-License-Identifier: LGPL-3.0-or-later
 */
package com.github.alexthe666.iceandfire.entity.props;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/** Server-to-client update for custom state attached to a living entity. */
public record SyncEntityData(int entityId, CompoundTag tag) implements CustomPacketPayload {
    public static final Type<SyncEntityData> TYPE = new Type<>(Identifier.fromNamespaceAndPath(IceAndFire.MODID, "sync_entity_data"));
    public static final StreamCodec<RegistryFriendlyByteBuf, CompoundTag> TAG_CODEC =
        ByteBufCodecs.fromCodecWithRegistries(CompoundTag.CODEC);
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncEntityData> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.VAR_INT, SyncEntityData::entityId,
        TAG_CODEC, SyncEntityData::tag,
        SyncEntityData::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SyncEntityData message, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            if (player != null) {
                Entity entity = player.level().getEntity(message.entityId());
                if (entity != null) {
                    EntityDataProvider.getCapability(entity).ifPresent(data -> data.deserialize(message.tag()));
                }
            }
        });
    }
}
