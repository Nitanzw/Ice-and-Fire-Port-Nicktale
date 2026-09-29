package com.github.alexthe666.iceandfire.world;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class IafMobSpawnBiomeModifier implements BiomeModifier {
    private static final DeferredHolder<MapCodec<? extends BiomeModifier>, MapCodec<IafMobSpawnBiomeModifier>> SERIALIZER = DeferredHolder.create(NeoForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, Identifier.fromNamespaceAndPath(IceAndFire.MODID, "iaf_mob_spawns"));

    public IafMobSpawnBiomeModifier() {
    }

    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase == Phase.ADD) {
            IafEntityRegistry.addSpawners(biome, builder);
        }
    }


    public MapCodec<? extends BiomeModifier> codec() {
        return SERIALIZER.get();
    }

    public static MapCodec<IafMobSpawnBiomeModifier> makeCodec() {
        return MapCodec.unit(IafMobSpawnBiomeModifier::new);
    }
}