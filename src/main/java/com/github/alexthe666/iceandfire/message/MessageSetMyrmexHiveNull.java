package com.github.alexthe666.iceandfire.message;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.world.entity.player.Player;


public class MessageSetMyrmexHiveNull implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<MessageSetMyrmexHiveNull> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(IceAndFire.MODID, "set_myrmex_hive_null"));
    public static final StreamCodec<RegistryFriendlyByteBuf, MessageSetMyrmexHiveNull> STREAM_CODEC = StreamCodec.of((buffer, message) -> MessageSetMyrmexHiveNull.write(message, buffer), MessageSetMyrmexHiveNull::read);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }


    public MessageSetMyrmexHiveNull() {
    }

    public static MessageSetMyrmexHiveNull read(RegistryFriendlyByteBuf buf) {
        return new MessageSetMyrmexHiveNull();
    }

    public static void write(MessageSetMyrmexHiveNull message, RegistryFriendlyByteBuf buf) {
    }

    public static class Handler {
        public Handler() {
        }

        public static void handle(MessageSetMyrmexHiveNull message, IPayloadContext context) {
            context.enqueueWork(() -> IceAndFire.PROXY.setReferencedHive(null));
        }
    }
}
