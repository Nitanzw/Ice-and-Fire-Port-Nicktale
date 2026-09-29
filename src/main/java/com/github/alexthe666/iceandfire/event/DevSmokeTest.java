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
        if ("respawn".equals(System.getenv("IAF_WORLDGEN"))) {
            loads.add("execute in minecraft:overworld run forceload add 15952 -48 16048 48");
            commands.add("execute in minecraft:overworld run place feature iceandfire:fire_dragon_roost 16000 @Y@ 0");
            commands.add("#count after placing");
            for (int w = 0; w < 5; w++) {
                commands.add("#wait");
            }
            commands.add("execute in minecraft:overworld run kill @e[type=iceandfire:fire_dragon]");
            commands.add("#count after kill");
            for (int w = 0; w < 200; w++) {
                commands.add("#wait");
            }
            commands.add("#count after respawn window");
            commands = withLoads(loads, commands);
            return;
        }
        if ("retry".equals(System.getenv("IAF_WORLDGEN"))) {
            String[] names = {"minecraft:ore_iron", "silver_ore", "sapphire_ore"};
            int n = 0;
            for (String f : names) {
                for (int k = 0; k < 2; k++) {
                    int x = 12000 + 400 * n++;
                    loads.add("execute in minecraft:overworld run forceload add " + (x - 32) + " -32 " + (x + 32) + " 32");
                    String y = f.endsWith("_ore") || f.endsWith("ore_iron") ? "20" : "@Y@";
                    commands.add("execute in minecraft:overworld run place feature " + (f.contains(":") ? f : "iceandfire:" + f) + " " + x + " " + y + " 0");
                }
            }
            commands = withLoads(loads, commands);
            return;
        }
        int i = 0;
        for (String f : new String[]{"cyclops_cave", "fire_dragon_cave", "fire_dragon_roost", "fire_lily", "frost_lily", "hydra_cave", "ice_dragon_cave", "ice_dragon_roost", "lightning_dragon_cave", "lightning_dragon_roost", "lightning_lily", "myrmex_hive_desert", "myrmex_hive_jungle", "pixie_village", "sapphire_ore", "silver_ore", "siren_island", "spawn_death_worm", "spawn_dragon_skeleton_fire", "spawn_dragon_skeleton_ice", "spawn_dragon_skeleton_lightning", "spawn_hippocampus", "spawn_sea_serpent", "spawn_stymphalian_bird", "spawn_wandering_cyclops"}) {
            int x = 300 * ++i;
            loads.add("execute in minecraft:overworld run forceload add " + x + " 0");
            commands.add("execute in minecraft:overworld run place feature iceandfire:" + f + " " + x + " @Y@ 0");
        }
        for (int w = 0; w < 60; w++) {
            commands.add("#wait");
        }
        int sz = 0;
        for (String st : new String[]{"gorgon_temple", "graveyard", "mausoleum"}) {
            int z = 300 * sz++;
            loads.add("execute in minecraft:overworld run forceload add " + (8100 - 80) + " " + (z - 80) + " " + (8100 + 80) + " " + (z + 80));
            commands.add("execute in minecraft:overworld run place structure iceandfire:" + st + " 8100 @Y@ " + z);
        }
        java.util.List<String> retry = new java.util.ArrayList<>();
        for (String c : commands) {
            if (c.contains("place structure")) {
                retry.add(c);
            }
        }
        for (int w = 0; w < 60; w++) {
            commands.add("#wait");
        }
        commands.addAll(retry);
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
        if (cmd.startsWith("#count")) {
            int total = 0;
            for (var lvl : server.getAllLevels()) {
                for (var e : lvl.getAllEntities()) {
                    if (e instanceof com.github.alexthe666.iceandfire.entity.EntityDragonBase d && !d.isModelDead()) {
                        total++;
                    }
                }
            }
            IceAndFire.LOGGER.info("SMOKETEST dragons alive ({}): {}", cmd, total);
            return;
        }
        if (cmd.contains("@Y@")) {
            String[] tk = cmd.split(" ");
            int px = Integer.parseInt(tk[tk.length - 3]);
            int pz = Integer.parseInt(tk[tk.length - 1]);
            int py = server.overworld().getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.WORLD_SURFACE_WG, px, pz);
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
        int a = 0;
        java.util.Map<String, java.util.List<net.minecraft.world.item.ItemStack>> bySet = new java.util.LinkedHashMap<>();
        for (var item : BuiltInRegistries.ITEM) {
            if (item instanceof com.github.alexthe666.iceandfire.item.ItemModArmor armor) {
                bySet.computeIfAbsent(armor.getArmorMaterial().assetName(), k -> new java.util.ArrayList<>()).add(new net.minecraft.world.item.ItemStack(item));
            }
        }
        for (var entry : bySet.entrySet()) {
            try {
                var stand = new net.minecraft.world.entity.decoration.ArmorStand(level, player.getX() - 4.5 + (a % 7) * 1.5, player.getY(), player.getZ() + 5 + (a / 7) * 2);
                for (var stack : entry.getValue()) {
                    var slot = ((com.github.alexthe666.iceandfire.item.ItemModArmor) stack.getItem()).getArmorType().getSlot();
                    stand.setItemSlot(slot, stack);
                }
                level.addFreshEntity(stand);
            } catch (Throwable t) {
                IceAndFire.LOGGER.error("SMOKETEST armor stand failed: {}", entry.getKey(), t);
            }
            a++;
        }
        IceAndFire.LOGGER.info("SMOKETEST armor stands: {}", a);
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
        this.blockOrigin = bbase;
        this.gameplayAt = player.tickCount + 100;
    }

    private BlockPos blockOrigin;
    private int gameplayAt = -1;
    private boolean gameplayDone;

    /** Scripted gameplay: hit, feed and kill every mod mob, use every block-entity block (opens its menu). */
    @SubscribeEvent
    public void onGameplay(PlayerTickEvent.Post event) {
        if (gameplayDone || gameplayAt < 0 || !(event.getEntity() instanceof ServerPlayer player) || player.tickCount < gameplayAt) {
            return;
        }
        gameplayDone = true;
        var level = (net.minecraft.server.level.ServerLevel) player.level();
        java.util.List<net.minecraft.world.item.ItemStack> tools = new java.util.ArrayList<>();
        for (var item : new net.minecraft.world.item.Item[]{net.minecraft.world.item.Items.BEEF, net.minecraft.world.item.Items.BONE, net.minecraft.world.item.Items.COD, net.minecraft.world.item.Items.WHEAT, net.minecraft.world.item.Items.DIAMOND_SWORD, net.minecraft.world.item.Items.STICK}) {
            tools.add(new net.minecraft.world.item.ItemStack(item, 16));
        }
        for (var item : BuiltInRegistries.ITEM) {
            if (BuiltInRegistries.ITEM.getKey(item).getNamespace().equals(IceAndFire.MODID)) {
                tools.add(new net.minecraft.world.item.ItemStack(item, 1));
            }
        }
        int mobs = 0;
        int uses = 0;
        int fails = 0;
        java.util.List<net.minecraft.world.entity.LivingEntity> living = level.getEntitiesOfClass(net.minecraft.world.entity.LivingEntity.class, player.getBoundingBox().inflate(400), e -> e != player && BuiltInRegistries.ENTITY_TYPE.getKey(e.getType()).getNamespace().equals(IceAndFire.MODID));
        for (var mob : living) {
            mobs++;
            for (var stack : tools) {
                try {
                    player.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND, stack.copy());
                    player.interactOn(mob, net.minecraft.world.InteractionHand.MAIN_HAND, mob.position());
                    uses++;
                } catch (Throwable t) {
                    fails++;
                    IceAndFire.LOGGER.error("SMOKETEST interact failed: {} with {}", mob.getType(), stack, t);
                }
            }
            try {
                player.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND, new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.DIAMOND_SWORD));
                mob.hurt(level.damageSources().playerAttack(player), 4.0F);
                mob.hurt(level.damageSources().onFire(), 2.0F);
            } catch (Throwable t) {
                fails++;
                IceAndFire.LOGGER.error("SMOKETEST hurt failed: {}", mob.getType(), t);
            }
        }
        int blocks = 0;
        for (var pos : BlockPos.betweenClosed(blockOrigin, blockOrigin.offset(40, 3, 40))) {
            var state = level.getBlockState(pos);
            if (state.getBlock() instanceof EntityBlock && BuiltInRegistries.BLOCK.getKey(state.getBlock()).getNamespace().equals(IceAndFire.MODID)) {
                blocks++;
                try {
                    player.closeContainer();
                    state.useWithoutItem(level, player, new net.minecraft.world.phys.BlockHitResult(net.minecraft.world.phys.Vec3.atCenterOf(pos), net.minecraft.core.Direction.UP, pos.immutable(), false));
                    player.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND, new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.DIAMOND));
                    state.useItemOn(player.getMainHandItem(), level, player, net.minecraft.world.InteractionHand.MAIN_HAND, new net.minecraft.world.phys.BlockHitResult(net.minecraft.world.phys.Vec3.atCenterOf(pos), net.minecraft.core.Direction.UP, pos.immutable(), false));
                } catch (Throwable t) {
                    fails++;
                    IceAndFire.LOGGER.error("SMOKETEST block use failed: {}", state, t);
                }
            }
        }
        for (var mob : living) {
            try {
                mob.kill(level);
            } catch (Throwable t) {
                fails++;
                IceAndFire.LOGGER.error("SMOKETEST kill failed: {}", mob.getType(), t);
            }
        }
        IceAndFire.LOGGER.info("SMOKETEST gameplay done: {} mobs, {} interactions, {} blocks used, {} failures", mobs, uses, blocks, fails);
    }
}
