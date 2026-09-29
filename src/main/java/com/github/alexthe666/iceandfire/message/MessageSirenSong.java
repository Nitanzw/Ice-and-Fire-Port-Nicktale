package com.github.alexthe666.iceandfire.message;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntitySiren;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;


public class MessageSirenSong implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<MessageSirenSong> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(IceAndFire.MODID, "siren_song"));
    public static final StreamCodec<RegistryFriendlyByteBuf, MessageSirenSong> STREAM_CODEC = StreamCodec.of((buffer, message) -> MessageSirenSong.write(message, buffer), MessageSirenSong::read);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }


    public int sirenId;
    public boolean isSinging;

    public MessageSirenSong(int sirenId, boolean isSinging) {
        this.sirenId = sirenId;
        this.isSinging = isSinging;
    }

    public MessageSirenSong() {
    }

    public static MessageSirenSong read(RegistryFriendlyByteBuf buf) {
        return new MessageSirenSong(buf.readInt(), buf.readBoolean());
    }

    public static void write(MessageSirenSong message, RegistryFriendlyByteBuf buf) {
        buf.writeInt(message.sirenId);
        buf.writeBoolean(message.isSinging);
    }


    public static class Handler {
        public Handler() {
        }

        public static void handle(final MessageSirenSong message, final IPayloadContext context) {


            context.enqueueWork(() -> {
                Player player = context.player();

                if (player != null) {
                    Entity entity = player.level().getEntity(message.sirenId);

                    if (entity instanceof EntitySiren siren) {
                        siren.setSinging(message.isSinging);
                    }
                }
            });


        }
    }

}
