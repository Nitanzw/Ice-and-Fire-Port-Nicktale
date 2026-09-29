package com.github.alexthe666.iceandfire.datagen;

import com.github.alexthe666.iceandfire.datagen.tags.BannerPatternTagGenerator;
import com.github.alexthe666.iceandfire.datagen.tags.IafBlockTags;
import com.github.alexthe666.iceandfire.datagen.tags.IafEntityTags;
import com.github.alexthe666.iceandfire.datagen.tags.IafItemTags;
import com.github.alexthe666.iceandfire.datagen.tags.POITagGenerator;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.metadata.PackMetadataGenerator;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

public final class DataGenerators {
    private DataGenerators() {
    }

    public static void gatherData(GatherDataEvent event) {
        if (event instanceof GatherDataEvent.Server serverEvent) {
            PackOutput output = serverEvent.getGenerator().getPackOutput();
            CompletableFuture<HolderLookup.Provider> provider = serverEvent.getLookupProvider();
            RegistryDataGenerator datapackProvider = new RegistryDataGenerator(output, provider);
            CompletableFuture<HolderLookup.Provider> lookupProvider = datapackProvider.getRegistryProvider();

            serverEvent.addProvider(datapackProvider);
            serverEvent.addProvider(new BannerPatternTagGenerator(output, provider));
            serverEvent.addProvider(new POITagGenerator(output, provider));
            serverEvent.addProvider(PackMetadataGenerator.forFeaturePack(
                    output,
                    net.minecraft.network.chat.Component.literal("Resources for Ice and Fire")));
            serverEvent.addProvider(new IafBiomeTagGenerator(output, lookupProvider));
            BlockTagsProvider blockTags = new IafBlockTags(output, provider);
            serverEvent.addProvider(blockTags);
            serverEvent.addProvider(new IafItemTags(output, provider, blockTags.contentsGetter()));
            serverEvent.addProvider(new IafEntityTags(output, provider));
            serverEvent.addProvider(new IafRecipes.Runner(output, lookupProvider));
        }
    }
}
