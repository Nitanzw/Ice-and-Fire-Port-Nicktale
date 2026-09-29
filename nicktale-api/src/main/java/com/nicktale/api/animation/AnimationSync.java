// SPDX-License-Identifier: LGPL-3.0-or-later
package com.nicktale.api.animation;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

/** Server-to-client synchronization for the current clip and tick of an animated entity. */
public final class AnimationSync {
    private static final String PROTOCOL_VERSION = "1";

    private AnimationSync() {
    }

    public static void register(IEventBus modBus) {
        modBus.addListener(AnimationSync::registerPayloads);
    }

    private static void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(PROTOCOL_VERSION);
        registrar.playToClient(AnimationPayload.TYPE, AnimationPayload.STREAM_CODEC, AnimationPayload::handle);
    }

    /** Call from the entity's server-side setAnimation implementation after changing its clip. */
    public static void synchronize(Entity entity, IAnimatedEntity animated) {
        if (entity.level().isClientSide()) {
            return;
        }
        Animation[] animations = animated.getAnimations();
        Animation active = animated.getAnimation();
        int animationIndex = 0;
        if (active != null && active != IAnimatedEntity.NO_ANIMATION && animations != null) {
            for (int i = 0; i < animations.length; i++) {
                if (animations[i] == active) {
                    animationIndex = i;
                    break;
                }
            }
        }
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity,
                new AnimationPayload(entity.getId(), animationIndex, animated.getAnimationTick()));
    }

    public record AnimationPayload(int entityId, int animationIndex, int animationTick) implements CustomPacketPayload {
        public static final Type<AnimationPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath("nicktaleapi", "animation_sync"));
        public static final StreamCodec<RegistryFriendlyByteBuf, AnimationPayload> STREAM_CODEC = StreamCodec.of(
                (buffer, payload) -> {
                    buffer.writeVarInt(payload.entityId());
                    buffer.writeVarInt(payload.animationIndex());
                    buffer.writeVarInt(payload.animationTick());
                },
                buffer -> new AnimationPayload(buffer.readVarInt(), buffer.readVarInt(), buffer.readVarInt())
        );

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }

        private static void handle(AnimationPayload payload, IPayloadContext context) {
            context.enqueueWork(() -> apply(payload, context.player()));
        }

        private static void apply(AnimationPayload payload, Player player) {
            if (player == null || payload.animationIndex() < 0) {
                return;
            }
            Entity entity = player.level().getEntity(payload.entityId());
            if (!(entity instanceof IAnimatedEntity animated)) {
                return;
            }
            Animation[] animations = animated.getAnimations();
            if (animations == null || payload.animationIndex() >= animations.length) {
                return;
            }
            animated.setAnimation(animations[payload.animationIndex()]);
            animated.setAnimationTick(Math.max(0, payload.animationTick()));
        }
    }
}
