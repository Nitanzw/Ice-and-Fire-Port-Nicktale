package com.github.alexthe666.iceandfire.world;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;
import net.neoforged.neoforge.registries.ForgeRegistries;
import net.neoforged.neoforge.registries.RegistryObject;

import java.util.HashMap;

public class IafFeatureBiomeModifier implements BiomeModifier {
    private static final RegistryObject<Codec<? extends BiomeModifier>> SERIALIZER = RegistryObject.create(Identifier.fromNamespaceAndPath(IceAndFire.MODID, "iaf_features"), ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, IceAndFire.MODID);
    private final HolderSet<PlacedFeature> features;
    public final HashMap<String, Holder<PlacedFeature>> featureMap = new HashMap<>();
    public IafFeatureBiomeModifier(HolderSet<PlacedFeature> features) {
        this.features = features;
        this.features.forEach(feature -> featureMap.put(feature.unwrapKey().get().location().toString(), feature));
    }

    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase == Phase.ADD) {
            IafWorldRegistry.addFeatures(biome, featureMap, builder);
        }
    }

    public Codec<? extends BiomeModifier> codec() {
        return SERIALIZER.get();
    }

    public static Codec<IafFeatureBiomeModifier> makeCodec() {
        return RecordCodecBuilder.create(config -> config.group(PlacedFeature.LIST_CODEC.fieldOf("features").forGetter((otherConfig) -> otherConfig.features)).apply(config, IafFeatureBiomeModifier::new));
    }
}