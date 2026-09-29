package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import com.github.alexthe666.iceandfire.event.ServerEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;


public class MessageSwingArm implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<MessageSwingArm> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(IceAndFire.MODID, "swing_arm"));
    public static final StreamCodec<RegistryFriendlyByteBuf, MessageSwingArm> STREAM_CODEC = StreamCodec.of((buffer, message) -> MessageSwingArm.write(message, buffer), MessageSwingArm::read);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }


    public MessageSwingArm() {

    }

    public static class Handler {
        public Handler() {
        }

        public static void handle(MessageSwingArm message, IPayloadContext context) {

            Player player = context.player();
            if (player != null) {
                ServerEvents.onLeftClick(player, player.getItemInHand(InteractionHand.MAIN_HAND));
            }
        }
    }


    public static MessageSwingArm read(RegistryFriendlyByteBuf buf) {
        return new MessageSwingArm();
    }

    public static void write(MessageSwingArm message, RegistryFriendlyByteBuf buf) {
    }

}
