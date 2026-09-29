package com.github.alexthe666.iceandfire.world;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;
import net.neoforged.neoforge.registries.ForgeRegistries;
import net.neoforged.neoforge.registries.RegistryObject;

public class IafMobSpawnBiomeModifier implements BiomeModifier {
    private static final RegistryObject<Codec<? extends BiomeModifier>> SERIALIZER = RegistryObject.create(Identifier.fromNamespaceAndPath(IceAndFire.MODID, "iaf_mob_spawns"), ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, IceAndFire.MODID);

    public IafMobSpawnBiomeModifier() {
    }

    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase == Phase.ADD) {
            IafEntityRegistry.addSpawners(biome, builder);
        }
    }


    public Codec<? extends BiomeModifier> codec() {
        return SERIALIZER.get();
    }

    public static Codec<IafMobSpawnBiomeModifier> makeCodec() {
        return Codec.unit(IafMobSpawnBiomeModifier::new);
    }
}