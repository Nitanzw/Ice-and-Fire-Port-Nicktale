package com.github.alexthe666.iceandfire.world;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MyrmexWorldData extends SavedData {
    private static final Codec<MyrmexWorldData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.optionalFieldOf("Tick", 0).forGetter(data -> data.tickCounter),
            MyrmexHive.CODEC.listOf().optionalFieldOf("Hives", List.of()).forGetter(data -> data.hiveList)
    ).apply(instance, MyrmexWorldData::new));
    private static final SavedDataType<MyrmexWorldData> TYPE = new SavedDataType<>(
            Identifier.fromNamespaceAndPath(IceAndFire.MODID, "iceandfire_myrmex"),
            MyrmexWorldData::new,
            CODEC
    );

    private final List<MyrmexHive> hiveList = new ArrayList<>();
    private Level world;
    private int tickCounter;

    public MyrmexWorldData() {
    }

    private MyrmexWorldData(int tickCounter, List<MyrmexHive> hives) {
        this.tickCounter = tickCounter;
        this.hiveList.addAll(hives);
    }

    public static MyrmexWorldData get(Level world) {
        if (world instanceof ServerLevel serverLevel) {
            MyrmexWorldData data = serverLevel.getDataStorage().computeIfAbsent(TYPE);
            data.setWorldsForAll(world);
            return data;
        }
        return new MyrmexWorldData();
    }

    public static void addHive(Level world, MyrmexHive hive) {
        MyrmexWorldData data = get(world);
        if (data != null) {
            hive.setWorld(world);
            data.hiveList.add(hive);
            data.setDirty();
        }
    }

    public void setWorldsForAll(Level worldIn) {
        this.world = worldIn;
        for (MyrmexHive hive : this.hiveList) {
            hive.setWorld(worldIn);
        }
    }

    public void tick() {
        ++this.tickCounter;
        for (MyrmexHive hive : this.hiveList) {
            hive.tick(this.tickCounter, world);
        }
        this.setDirty();
    }

    public List<MyrmexHive> getHivelist() {
        return this.hiveList;
    }

    public MyrmexHive getNearestHive(net.minecraft.core.BlockPos doorBlock, int radius) {
        MyrmexHive nearest = null;
        double bestDistance = Double.MAX_VALUE;
        for (MyrmexHive hive : this.hiveList) {
            double distance = hive.getCenter().distSqr(doorBlock);
            if (distance < bestDistance) {
                float reach = radius + hive.getVillageRadius();
                if (distance <= reach * reach) {
                    nearest = hive;
                    bestDistance = distance;
                }
            }
        }
        return nearest;
    }

    public void debug() {
        for (MyrmexHive hive : this.hiveList) {
            IceAndFire.LOGGER.warn(hive.toString());
        }
    }

    public MyrmexHive getHiveFromUUID(UUID id) {
        for (MyrmexHive hive : hiveList) {
            if (hive.hiveUUID != null && hive.hiveUUID.equals(id)) {
                return hive;
            }
        }
        return null;
    }
}
