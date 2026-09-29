package com.github.alexthe666.iceandfire.event;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

/** Development aid: with {@code -Diaf.smoketest=true} spawns every mod entity and block-entity block near the player. */
public class DevSmokeTest {
    private boolean done;
    private java.util.ArrayDeque<String> commands;
    private int wait;

    /** With {@code IAF_WORLDGEN} set, places every mod feature and structure through commands, then stops the server. */
    @SubscribeEvent
    public void onServerStarted(ServerStartedEvent event) {
        if (System.getenv("IAF_WORLDGEN") == null) {
            return;
        }
        commands = new java.util.ArrayDeque<>();
        java.util.List<String> loads = new java.util.ArrayList<>();
        int i = 0;
        for (String f : new String[]{"cyclops_cave", "fire_dragon_cave", "fire_dragon_roost", "fire_lily", "frost_lily", "hydra_cave", "ice_dragon_cave", "ice_dragon_roost", "lightning_dragon_cave", "lightning_dragon_roost", "lightning_lily", "myrmex_hive_desert", "myrmex_hive_jungle", "pixie_village", "sapphire_ore", "silver_ore", "siren_island", "spawn_death_worm", "spawn_dragon_skeleton_fire", "spawn_dragon_skeleton_ice", "spawn_dragon_skeleton_lightning", "spawn_hippocampus", "spawn_sea_serpent", "spawn_stymphalian_bird", "spawn_wandering_cyclops"}) {
            int x = 300 * ++i;
            loads.add("execute in minecraft:overworld run forceload add " + x + " 0");
            commands.add("execute in minecraft:overworld run place feature iceandfire:" + f + " " + x + " @Y@ 0");
        }
        for (String st : new String[]{"gorgon_temple", "graveyard", "mausoleum"}) {
            int x = 300 * ++i;
            loads.add("execute in minecraft:overworld run forceload add " + x + " 0");
            commands.add("execute in minecraft:overworld run place structure iceandfire:" + st + " " + x + " @Y@ 0");
        }
        commands = withLoads(loads, commands);
    }

    private static java.util.ArrayDeque<String> withLoads(java.util.List<String> loads, java.util.ArrayDeque<String> rest) {
        java.util.ArrayDeque<String> all = new java.util.ArrayDeque<>(loads);
        for (int k = 0; k < 40; k++) {
            all.add("#wait");
        }
        all.addAll(rest);
        return all;
    }

    @SubscribeEvent
    public void onServerTick(ServerTickEvent.Post event) {
        if (commands == null || ++wait % 20 != 0) {
            return;
        }
        var server = event.getServer();
        if (commands.isEmpty()) {
            IceAndFire.LOGGER.info("SMOKETEST worldgen done");
            server.halt(false);
            return;
        }
        String cmd = commands.poll();
        if (cmd.equals("#wait")) {
            return;
        }
        if (cmd.contains("@Y@")) {
            int px = Integer.parseInt(cmd.split(" ")[cmd.split(" ").length - 3]);
            int py = server.overworld().getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.WORLD_SURFACE_WG, px, 0);
            cmd = cmd.replace("@Y@", String.valueOf(py));
        }
        IceAndFire.LOGGER.info("SMOKETEST cmd [{}]", cmd);
        server.getCommands().performPrefixedCommand(server.createCommandSourceStack(), cmd);
    }

    @SubscribeEvent
    public void onTick(PlayerTickEvent.Post event) {
        if (done || !(event.getEntity() instanceof ServerPlayer player) || player.tickCount < 100) {
            return;
        }
        done = true;
        player.setGameMode(GameType.CREATIVE);
        var level = player.level();
        BlockPos base = player.blockPosition().offset(6, 0, 0);
        int i = 0;
        for (EntityType<?> type : BuiltInRegistries.ENTITY_TYPE) {
            if (!BuiltInRegistries.ENTITY_TYPE.getKey(type).getNamespace().equals(IceAndFire.MODID)) {
                continue;
            }
            try {
                Entity e = type.create(level, EntitySpawnReason.COMMAND);
                if (e != null) {
                    e.setPos(base.getX() + (i % 8) * 6, base.getY() + 2, base.getZ() + (i / 8) * 6);
                    level.addFreshEntity(e);
                }
            } catch (Throwable t) {
                IceAndFire.LOGGER.error("SMOKETEST entity spawn failed: {}", type, t);
            }
            i++;
        }
        int j = 0;
        BlockPos bbase = player.blockPosition().offset(-6, 0, -30);
        for (var block : BuiltInRegistries.BLOCK) {
            if (!BuiltInRegistries.BLOCK.getKey(block).getNamespace().equals(IceAndFire.MODID) || !(block instanceof EntityBlock)) {
                continue;
            }
            try {
                BlockState state = block.defaultBlockState();
                level.setBlock(bbase.offset((j % 10) * 3, 1, (j / 10) * 3), state, 3);
            } catch (Throwable t) {
                IceAndFire.LOGGER.error("SMOKETEST block failed: {}", block, t);
            }
            j++;
        }
        IceAndFire.LOGGER.info("SMOKETEST spawned {} entities, {} block entities", i, j);
    }
}
