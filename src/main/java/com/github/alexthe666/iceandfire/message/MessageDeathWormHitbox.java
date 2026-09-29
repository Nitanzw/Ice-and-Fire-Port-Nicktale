package com.github.alexthe666.iceandfire.message;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;



public class MessageDeathWormHitbox implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<MessageDeathWormHitbox> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(IceAndFire.MODID, "death_worm_hitbox"));
    public static final StreamCodec<RegistryFriendlyByteBuf, MessageDeathWormHitbox> STREAM_CODEC = StreamCodec.of((buffer, message) -> MessageDeathWormHitbox.write(message, buffer), MessageDeathWormHitbox::read);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }


    public int deathWormId;
    public float scale;

    public MessageDeathWormHitbox(int deathWormId, float scale) {
        this.deathWormId = deathWormId;
        this.scale = scale;
    }

    public MessageDeathWormHitbox() {
    }

    public static MessageDeathWormHitbox read(RegistryFriendlyByteBuf buf) {
        return new MessageDeathWormHitbox(buf.readInt(), buf.readFloat());
    }

    public static void write(MessageDeathWormHitbox message, RegistryFriendlyByteBuf buf) {
        buf.writeInt(message.deathWormId);
        buf.writeFloat(message.scale);
    }

    public static class Handler {
        public Handler() {
        }

        public static void handle(final MessageDeathWormHitbox message, final IPayloadContext context) {


            context.enqueueWork(() -> {
                Player player = context.player();

                if (player != null) {
                    Entity entity = player.level().getEntity(message.deathWormId);

                    if (entity instanceof EntityDeathWorm deathWorm) {
                        deathWorm.initSegments(message.scale);
                    }
                }
            });


        }
    }
}
