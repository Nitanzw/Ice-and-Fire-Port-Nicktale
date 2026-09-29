package com.github.alexthe666.iceandfire.datagen;

import com.github.alexthe666.iceandfire.datagen.tags.*;
import net.minecraft.DetectedVersion;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.metadata.PackMetadataGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;


public class DataGenerators {

    public static void gatherData(GatherDataEvent event) {
        PackOutput output = event.getGenerator().getPackOutput();
        CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();
        if (event instanceof GatherDataEvent.Server) {
            event.addProvider(new PackMetadataGenerator(output)
                    .add(PackMetadataSection.SERVER_TYPE, new PackMetadataSection(
                            Component.literal("Resources for Ice and Fire"),
                            DetectedVersion.BUILT_IN.packVersion(PackType.SERVER_DATA).minorRange())));
            DatapackBuiltinEntriesProvider datapackProvider = new RegistryDataGenerator(output, provider);
            CompletableFuture<HolderLookup.Provider> lookupProvider = datapackProvider.getRegistryProvider();
            event.addProvider(datapackProvider);
            event.addProvider(new BannerPatternTagGenerator(output, provider));
            event.addProvider(new POITagGenerator(output, provider));
            event.addProvider(new IafBiomeTagGenerator(output, lookupProvider));
            BlockTagsProvider blockTags = new IafBlockTags(output, provider);
            event.addProvider(blockTags);
            event.addProvider(new IafItemTags(output, provider, blockTags.contentsGetter()));
            event.addProvider(new IafEntityTags(output, provider));
            event.addProvider(new IafRecipes.Runner(output, lookupProvider));
        }

        if (event instanceof GatherDataEvent.Client) {
            event.addProvider(new AtlasGenerator(output, provider));
        }

    }
}
