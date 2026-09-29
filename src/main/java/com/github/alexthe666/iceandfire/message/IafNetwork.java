package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

/** NeoForge 26.2 payload registration and distribution for Ice and Fire. */
public final class IafNetwork {
    private static final String PROTOCOL_VERSION = "1";

    private IafNetwork() {
    }

    public static void init(IEventBus modBus) {
        modBus.addListener(IafNetwork::registerPayloads);
    }

    private static void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(PROTOCOL_VERSION);

        registrar.playToClient(MessageDaytime.TYPE, MessageDaytime.STREAM_CODEC, MessageDaytime.Handler::handle);
        registrar.playToClient(MessageDeathWormHitbox.TYPE, MessageDeathWormHitbox.STREAM_CODEC, MessageDeathWormHitbox.Handler::handle);
        registrar.playToServer(MessageDragonControl.TYPE, MessageDragonControl.STREAM_CODEC, MessageDragonControl.Handler::handle);
        registrar.playToClient(MessageDragonSetBurnBlock.TYPE, MessageDragonSetBurnBlock.STREAM_CODEC, MessageDragonSetBurnBlock.Handler::handle);
        registrar.playBidirectional(MessageDragonSyncFire.TYPE, MessageDragonSyncFire.STREAM_CODEC, MessageDragonSyncFire.Handler::handle, MessageDragonSyncFire.Handler::handle);
        registrar.playBidirectional(MessageGetMyrmexHive.TYPE, MessageGetMyrmexHive.STREAM_CODEC, MessageGetMyrmexHive.Handler::handle, MessageGetMyrmexHive.Handler::handle);
        registrar.playToServer(MessageMyrmexSettings.TYPE, MessageMyrmexSettings.STREAM_CODEC, MessageMyrmexSettings.Handler::handle);
        registrar.playToServer(MessageHippogryphArmor.TYPE, MessageHippogryphArmor.STREAM_CODEC, MessageHippogryphArmor.Handler::handle);
        registrar.playToServer(MessageMultipartInteract.TYPE, MessageMultipartInteract.STREAM_CODEC, MessageMultipartInteract.Handler::handle);
        registrar.playToServer(MessagePlayerHitMultipart.TYPE, MessagePlayerHitMultipart.STREAM_CODEC, MessagePlayerHitMultipart.Handler::handle);
        registrar.playToClient(MessageSetMyrmexHiveNull.TYPE, MessageSetMyrmexHiveNull.STREAM_CODEC, MessageSetMyrmexHiveNull.Handler::handle);
        registrar.playToClient(MessageSirenSong.TYPE, MessageSirenSong.STREAM_CODEC, MessageSirenSong.Handler::handle);
        registrar.playToClient(MessageSpawnParticleAt.TYPE, MessageSpawnParticleAt.STREAM_CODEC, MessageSpawnParticleAt.Handler::handle);
        registrar.playBidirectional(MessageStartRidingMob.TYPE, MessageStartRidingMob.STREAM_CODEC, MessageStartRidingMob.Handler::handle, MessageStartRidingMob.Handler::handle);
        registrar.playToServer(MessageSwingArm.TYPE, MessageSwingArm.STREAM_CODEC, MessageSwingArm.Handler::handle);
        registrar.playToClient(MessageSyncPath.TYPE, MessageSyncPath.STREAM_CODEC, MessageSyncPath::handle);
        registrar.playToClient(MessageSyncPathReached.TYPE, MessageSyncPathReached.STREAM_CODEC, MessageSyncPathReached::handle);
        registrar.playToClient(MessageUpdateDragonforge.TYPE, MessageUpdateDragonforge.STREAM_CODEC, MessageUpdateDragonforge.Handler::handle);
        registrar.playBidirectional(MessageUpdateLectern.TYPE, MessageUpdateLectern.STREAM_CODEC, MessageUpdateLectern.Handler::handle, MessageUpdateLectern.Handler::handle);
        registrar.playToClient(MessageUpdatePixieHouse.TYPE, MessageUpdatePixieHouse.STREAM_CODEC, MessageUpdatePixieHouse.Handler::handle);
        registrar.playToClient(MessageUpdatePixieHouseModel.TYPE, MessageUpdatePixieHouseModel.STREAM_CODEC, MessageUpdatePixieHouseModel.Handler::handle);
        registrar.playToClient(MessageUpdatePixieJar.TYPE, MessageUpdatePixieJar.STREAM_CODEC, MessageUpdatePixieJar.Handler::handle);
        registrar.playToClient(MessageUpdatePodium.TYPE, MessageUpdatePodium.STREAM_CODEC, MessageUpdatePodium.Handler::handle);
    }

    public static void sendToServer(CustomPacketPayload payload) {
        ClientPacketDistributor.sendToServer(payload);
    }

    public static void sendToAll(CustomPacketPayload payload) {
        PacketDistributor.sendToAllPlayers(payload);
    }

    public static void sendToPlayer(ServerPlayer player, CustomPacketPayload payload) {
        PacketDistributor.sendToPlayer(player, payload);
    }
}
