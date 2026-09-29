package com.github.alexthe666.iceandfire.message;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import com.github.alexthe666.iceandfire.world.MyrmexWorldData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;


public class MessageGetMyrmexHive implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<MessageGetMyrmexHive> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(IceAndFire.MODID, "get_myrmex_hive"));
    public static final StreamCodec<RegistryFriendlyByteBuf, MessageGetMyrmexHive> STREAM_CODEC = StreamCodec.of((buffer, message) -> MessageGetMyrmexHive.write(message, buffer), MessageGetMyrmexHive::read);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }


    public CompoundTag hive;

    public MessageGetMyrmexHive(CompoundTag hive) {
        this.hive = hive;
    }

    public MessageGetMyrmexHive() {
    }

    public static MessageGetMyrmexHive read(RegistryFriendlyByteBuf buf) {
        return new MessageGetMyrmexHive(buf.readNbt());
    }

    public static void write(MessageGetMyrmexHive message, RegistryFriendlyByteBuf buf) {
        buf.writeNbt(message.hive);
    }

    public static class Handler {
        public Handler() {
        }

        public static void handle(final MessageGetMyrmexHive message, final IPayloadContext context) {


            context.enqueueWork(() -> {
                Player player = context.player();

                MyrmexHive serverHive = MyrmexHive.fromNBT(message.hive);
                CompoundTag tag = new CompoundTag();
                serverHive.writeVillageDataToNBT(tag);
                serverHive.readVillageDataFromNBT(tag);
                IceAndFire.PROXY.setReferencedHive(serverHive);

                if (player != null) {
                    MyrmexHive realHive = MyrmexWorldData.get(player.level()).getHiveFromUUID(serverHive.hiveUUID);
                    realHive.readVillageDataFromNBT(serverHive.toNBT());
                }
            });


        }
    }
}
