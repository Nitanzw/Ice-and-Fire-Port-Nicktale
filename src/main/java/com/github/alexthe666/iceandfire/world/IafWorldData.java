package com.github.alexthe666.iceandfire.world;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.world.gen.TypedFeature;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class IafWorldData extends SavedData {
    public enum FeatureType {
        SURFACE,
        UNDERGROUND,
        OCEAN
    }

    private record GeneratedFeature(String id, BlockPos position, FeatureType type) {
        private static final Codec<GeneratedFeature> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf("id").forGetter(GeneratedFeature::id),
                BlockPos.CODEC.fieldOf("position").forGetter(GeneratedFeature::position),
                Codec.STRING.xmap(FeatureType::valueOf, FeatureType::name).fieldOf("type").forGetter(GeneratedFeature::type)
        ).apply(instance, GeneratedFeature::new));
    }

    private static final Codec<IafWorldData> CODEC = GeneratedFeature.CODEC.listOf().fieldOf("generated").codec()
            .xmap(IafWorldData::new, IafWorldData::toGeneratedFeatures);
    private static final SavedDataType<IafWorldData> TYPE = new SavedDataType<>(
            Identifier.fromNamespaceAndPath(IceAndFire.MODID, IceAndFire.MODID + "_general"),
            IafWorldData::new,
            CODEC
    );
    private final EnumMap<FeatureType, ArrayList<Map.Entry<String, BlockPos>>> generated = new EnumMap<>(FeatureType.class);

    public IafWorldData() {
        for (FeatureType type : FeatureType.values()) {
            generated.put(type, new ArrayList<>());
        }
    }

    private IafWorldData(List<GeneratedFeature> entries) {
        this();
        for (GeneratedFeature entry : entries) {
            this.generated.get(entry.type()).add(Map.entry(entry.id(), entry.position()));
        }
    }

    public static IafWorldData get(Level level) {
        if (level instanceof ServerLevel serverLevel) {
            return serverLevel.getDataStorage().computeIfAbsent(TYPE);
        }
        return null;
    }

    public boolean check(TypedFeature feature, BlockPos position, String id) {
        return check(feature.getFeatureType(), position, id);
    }

    public boolean check(FeatureType type, BlockPos position, String id) {
        ArrayList<Map.Entry<String, BlockPos>> entries = generated.get(type);
        entries.removeIf(entry -> entry.getKey().equals(id));

        boolean canGenerate = true;
        for (Map.Entry<String, BlockPos> entry : entries) {
            if (position.distSqr(entry.getValue()) <= IafConfig.dangerousWorldGenSeparationLimit * IafConfig.dangerousWorldGenSeparationLimit) {
                canGenerate = false;
                break;
            }
        }

        if (entries.size() > 5_000) {
            IceAndFire.LOGGER.debug("Too many BlockPos entries for feature type {} tracked, removing oldest ones", type);
            entries.subList(0, 1_000).clear();
            entries.trimToSize();
        }

        entries.add(Map.entry(id, position));
        setDirty();
        return canGenerate;
    }

    private List<GeneratedFeature> toGeneratedFeatures() {
        List<GeneratedFeature> entries = new ArrayList<>();
        for (Map.Entry<FeatureType, ArrayList<Map.Entry<String, BlockPos>>> featureEntries : generated.entrySet()) {
            for (Map.Entry<String, BlockPos> entry : featureEntries.getValue()) {
                entries.add(new GeneratedFeature(entry.getKey(), entry.getValue(), featureEntries.getKey()));
            }
        }
        return entries;
    }
}
