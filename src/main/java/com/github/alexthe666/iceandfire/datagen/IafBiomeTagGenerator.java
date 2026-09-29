package com.github.alexthe666.iceandfire.datagen;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

import java.util.concurrent.CompletableFuture;

public class IafBiomeTagGenerator extends BiomeTagsProvider {
    public static final TagKey<Biome> HAS_GORGON_TEMPLE = TagKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(IceAndFire.MODID, "has_structure/gorgon_temple"));
    public static final TagKey<Biome> HAS_MAUSOLEUM = TagKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(IceAndFire.MODID, "has_structure/mausoleum"));
    public static final TagKey<Biome> HAS_GRAVEYARD = TagKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(IceAndFire.MODID, "has_structure/graveyard"));


    public IafBiomeTagGenerator(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pProvider) {
        super(pOutput, pProvider, IceAndFire.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(HAS_GRAVEYARD).addTag(BiomeTags.IS_OVERWORLD);
        tag(HAS_MAUSOLEUM).addTag(BiomeTags.IS_OVERWORLD);
        tag(HAS_GORGON_TEMPLE).addTag(BiomeTags.IS_OVERWORLD);
    }

    @Override
    public String getName() {
        return "Ice and Fire Biome Tags";
    }
}
