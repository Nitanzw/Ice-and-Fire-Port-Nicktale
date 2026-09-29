/*
 * Ice and Fire NeoForge port
 * SPDX-License-Identifier: LGPL-3.0-or-later
 */
package com.github.alexthe666.iceandfire.entity.props;

import net.neoforged.neoforge.event.tick.EntityTickEvent;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.message.IafNetwork;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

/** Registration and synchronization for Ice and Fire entity data and entity inventories. */
public final class CapabilityHandler {
    public static final Identifier ENTITY_DATA = Identifier.fromNamespaceAndPath(IceAndFire.MODID, "entity_data");

    private CapabilityHandler() {
    }

    /** Register mod-bus entry points and runtime entity/player listeners. */
    public static void init(IEventBus modBus) {
        modBus.addListener(CapabilityHandler::registerCapabilities);
        modBus.addListener(CapabilityHandler::registerPayloads);
        NeoForge.EVENT_BUS.addListener(CapabilityHandler::handleInitialSync);
        NeoForge.EVENT_BUS.addListener(CapabilityHandler::onPlayerStartTracking);
        NeoForge.EVENT_BUS.addListener(CapabilityHandler::tickData);
    }

    private static void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(SyncEntityData.TYPE, SyncEntityData.STREAM_CODEC, SyncEntityData::handle);
    }

    private static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerEntity(Capabilities.Item.ENTITY, IafEntityRegistry.FIRE_DRAGON.get(),
            (dragon, context) -> dragon.getDragonItemHandler());
        event.registerEntity(Capabilities.Item.ENTITY, IafEntityRegistry.ICE_DRAGON.get(),
            (dragon, context) -> dragon.getDragonItemHandler());
        event.registerEntity(Capabilities.Item.ENTITY, IafEntityRegistry.LIGHTNING_DRAGON.get(),
            (dragon, context) -> dragon.getDragonItemHandler());
        event.registerEntity(Capabilities.Item.ENTITY, IafEntityRegistry.HIPPOCAMPUS.get(),
            (hippocampus, context) -> hippocampus.getItemHandler());
    }

    public static void handleInitialSync(EntityJoinLevelEvent event) {
        syncEntityData(event.getEntity());
    }

    public static void onPlayerStartTracking(PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof LivingEntity target && event.getEntity() instanceof ServerPlayer serverPlayer) {
            EntityDataProvider.getCapability(target).ifPresent(data ->
                IafNetwork.sendToPlayer(serverPlayer, new SyncEntityData(target.getId(), data.serialize())));
        }
    }

    public static void tickData(EntityTickEvent.Pre event) {
        if (!(event.getEntity() instanceof net.minecraft.world.entity.LivingEntity iafLiving)) {
            return;
        }
        EntityDataProvider.getCapability(iafLiving).ifPresent(data -> data.tick(iafLiving));
    }

    public static void syncEntityData(Entity entity) {
        if (entity.level().isClientSide() || !(entity instanceof LivingEntity)) {
            return;
        }

        EntityDataProvider.getCapability(entity).ifPresent(data -> {
            SyncEntityData payload = new SyncEntityData(entity.getId(), data.serialize());
            if (entity instanceof ServerPlayer) {
                PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, payload);
            } else {
                PacketDistributor.sendToPlayersTrackingEntity(entity, payload);
            }
        });
    }

    public static @Nullable Player getLocalPlayer() {
        return IceAndFire.PROXY.getClientSidePlayer();
    }
}
