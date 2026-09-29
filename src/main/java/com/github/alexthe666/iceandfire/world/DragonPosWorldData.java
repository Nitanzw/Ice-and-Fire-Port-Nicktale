package com.github.alexthe666.iceandfire.world;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DragonPosWorldData extends SavedData {
    private record DragonPosition(UUID id, int x, int y, int z) {
        private static final Codec<DragonPosition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                UUIDUtil.CODEC.fieldOf("DragonUUID").forGetter(DragonPosition::id),
                Codec.INT.fieldOf("DragonPosX").forGetter(DragonPosition::x),
                Codec.INT.fieldOf("DragonPosY").forGetter(DragonPosition::y),
                Codec.INT.fieldOf("DragonPosZ").forGetter(DragonPosition::z)
        ).apply(instance, DragonPosition::new));

        private DragonPosition(UUID id, BlockPos position) {
            this(id, position.getX(), position.getY(), position.getZ());
        }

        private BlockPos position() {
            return new BlockPos(x, y, z);
        }
    }

    private static final Codec<DragonPosWorldData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.optionalFieldOf("Tick", 0).forGetter(data -> data.tickCounter),
            DragonPosition.CODEC.listOf().optionalFieldOf("DragonMap", List.of()).forGetter(DragonPosWorldData::toDragonPositions)
    ).apply(instance, DragonPosWorldData::new));
    private static final SavedDataType<DragonPosWorldData> TYPE = new SavedDataType<>(
            Identifier.fromNamespaceAndPath(IceAndFire.MODID, "iceandfire_dragon_positions"),
            DragonPosWorldData::new,
            CODEC
    );

    protected final Map<UUID, BlockPos> lastDragonPositions = new HashMap<>();
    private int tickCounter;

    public DragonPosWorldData() {
    }

    private DragonPosWorldData(int tickCounter, List<DragonPosition> positions) {
        this.tickCounter = tickCounter;
        for (DragonPosition position : positions) {
            this.lastDragonPositions.put(position.id(), position.position());
        }
    }

    public static DragonPosWorldData get(Level world) {
        if (world instanceof ServerLevel serverLevel) {
            return serverLevel.getDataStorage().computeIfAbsent(TYPE);
        }
        return null;
    }

    public void addDragon(UUID uuid, BlockPos pos) {
        lastDragonPositions.put(uuid, pos);
        setDirty();
    }

    public void removeDragon(UUID uuid) {
        if (lastDragonPositions.remove(uuid) != null) {
            setDirty();
        }
    }

    public BlockPos getDragonPos(UUID uuid) {
        return lastDragonPositions.get(uuid);
    }

    public void debug() {
        IceAndFire.LOGGER.warn(lastDragonPositions.toString());
    }

    public void tick() {
        ++this.tickCounter;
    }

    private List<DragonPosition> toDragonPositions() {
        List<DragonPosition> positions = new ArrayList<>();
        for (Map.Entry<UUID, BlockPos> entry : lastDragonPositions.entrySet()) {
            positions.add(new DragonPosition(entry.getKey(), entry.getValue()));
        }
        return positions;
    }
}
