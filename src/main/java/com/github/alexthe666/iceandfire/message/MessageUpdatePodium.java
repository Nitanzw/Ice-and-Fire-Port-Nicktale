package com.github.alexthe666.iceandfire.message;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityPodium;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;


public class MessageUpdatePodium implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<MessageUpdatePodium> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(IceAndFire.MODID, "update_podium"));
    public static final StreamCodec<RegistryFriendlyByteBuf, MessageUpdatePodium> STREAM_CODEC = StreamCodec.of((buffer, message) -> MessageUpdatePodium.write(message, buffer), MessageUpdatePodium::read);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }


    public long blockPos;
    public ItemStack heldStack;

    public MessageUpdatePodium(long blockPos, ItemStack heldStack) {
        this.blockPos = blockPos;
        this.heldStack = heldStack;

    }

    public MessageUpdatePodium() {
    }

    public static MessageUpdatePodium read(FriendlyByteBuf buf) {
        return new MessageUpdatePodium(buf.readLong(), ItemStack.STREAM_CODEC.decode((RegistryFriendlyByteBuf) buf));
    }

    public static void write(MessageUpdatePodium message, FriendlyByteBuf buf) {
        buf.writeLong(message.blockPos);
        ItemStack.STREAM_CODEC.encode((RegistryFriendlyByteBuf) buf, message.heldStack);
    }

    public static class Handler {
        public Handler() {
        }

        public static void handle(final MessageUpdatePodium message, final IPayloadContext context) {


            context.enqueueWork(() -> {
                Player player = context.player();

                if (player != null) {
                    BlockPos pos = BlockPos.of(message.blockPos);

                    if (player.level().getBlockEntity(pos) instanceof TileEntityPodium podium) {
                        podium.setItem(0, message.heldStack);
                    }
                }
            });


        }
    }
}
