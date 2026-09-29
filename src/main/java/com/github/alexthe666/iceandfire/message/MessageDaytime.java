package com.github.alexthe666.iceandfire.message;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;


public class MessageDaytime implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<MessageDaytime> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(IceAndFire.MODID, "daytime"));
    public static final StreamCodec<RegistryFriendlyByteBuf, MessageDaytime> STREAM_CODEC = StreamCodec.of((buffer, message) -> MessageDaytime.write(message, buffer), MessageDaytime::read);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }


    public int dragonId;
    public boolean isDay;

    public MessageDaytime(int dragonId, boolean isDay) {
        this.dragonId = dragonId;
        this.isDay = isDay;
    }

    public MessageDaytime() {
    }

    public static MessageDaytime read(RegistryFriendlyByteBuf buf) {
        return new MessageDaytime(buf.readInt(), buf.readBoolean());
    }

    public static void write(MessageDaytime message, RegistryFriendlyByteBuf buf) {
        buf.writeInt(message.dragonId);
        buf.writeBoolean(message.isDay);
    }

    public static class Handler {
        public Handler() {
        }

        public static void handle(final MessageDaytime message, final IPayloadContext context) {


            context.enqueueWork(() -> {
                Player player = context.player();

                if (player != null) {
                    Entity entity = player.level().getEntity(message.dragonId);

                    if (entity instanceof EntityDragonBase dragon) {
                        dragon.isDaytime = message.isDay;
                    }
                }
            });


        }
    }
}
