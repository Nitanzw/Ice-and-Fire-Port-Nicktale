package com.github.alexthe666.iceandfire.message;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.util.ISyncMount;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;


public class MessageStartRidingMob implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<MessageStartRidingMob> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(IceAndFire.MODID, "start_riding_mob"));
    public static final StreamCodec<RegistryFriendlyByteBuf, MessageStartRidingMob> STREAM_CODEC = StreamCodec.of((buffer, message) -> MessageStartRidingMob.write(message, buffer), MessageStartRidingMob::read);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }


    public int dragonId;
    public boolean ride;
    public boolean baby;

    public MessageStartRidingMob(int dragonId, boolean ride, boolean baby) {
        this.dragonId = dragonId;
        this.ride = ride;
        this.baby = baby;
    }

    public MessageStartRidingMob() {
    }

    public static MessageStartRidingMob read(RegistryFriendlyByteBuf buf) {
        return new MessageStartRidingMob(buf.readInt(), buf.readBoolean(), buf.readBoolean());
    }

    public static void write(MessageStartRidingMob message, RegistryFriendlyByteBuf buf) {
        buf.writeInt(message.dragonId);
        buf.writeBoolean(message.ride);
        buf.writeBoolean(message.baby);
    }

    public static class Handler {
        public Handler() {
        }

        public static void handle(final MessageStartRidingMob message, final IPayloadContext context) {


            context.enqueueWork(() -> {
                Player player = context.player();

                if (player != null) {
                    Entity entity = player.level().getEntity(message.dragonId);

                    if (entity instanceof ISyncMount && entity instanceof TamableAnimal tamable) {
                        if (tamable.isOwnedBy(player) && tamable.distanceTo(player) < 14) {
                            if (message.ride) {
                                if (message.baby) {
                                    tamable.startRiding(player, true, false);
                                } else {
                                    player.startRiding(tamable, true, false);
                                }
                            } else {
                                if (message.baby) {
                                    tamable.stopRiding();
                                } else {
                                    player.stopRiding();
                                }
                            }
                        }
                    }
                }
            });


        }
    }
}
