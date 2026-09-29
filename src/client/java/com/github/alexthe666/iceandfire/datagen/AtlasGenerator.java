package com.github.alexthe666.iceandfire.datagen;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.data.AtlasIds;
import net.minecraft.core.HolderLookup;
import net.minecraft.client.renderer.texture.atlas.sources.SingleFile;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.data.SpriteSourceProvider;

import java.util.concurrent.CompletableFuture;
import java.util.Optional;

public class AtlasGenerator extends SpriteSourceProvider {
    private static final Identifier GHOST_CHEST_LOCATION = Identifier.fromNamespaceAndPath(IceAndFire.MODID, "models/ghost/ghost_chest");
    private static final Identifier GHOST_CHEST_LEFT_LOCATION = Identifier.fromNamespaceAndPath(IceAndFire.MODID, "models/ghost/ghost_chest_left");
    private static final Identifier GHOST_CHEST_RIGHT_LOCATION = Identifier.fromNamespaceAndPath(IceAndFire.MODID, "models/ghost/ghost_chest_right");

    public AtlasGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, IceAndFire.MODID);
    }
    @Override
    protected void gather() {
        this.atlas(AtlasIds.CHESTS).addSource(new SingleFile(GHOST_CHEST_LOCATION, Optional.empty()));
        this.atlas(AtlasIds.CHESTS).addSource(new SingleFile(GHOST_CHEST_LEFT_LOCATION, Optional.empty()));
        this.atlas(AtlasIds.CHESTS).addSource(new SingleFile(GHOST_CHEST_RIGHT_LOCATION, Optional.empty()));



    }
}
