package com.github.alexthe666.iceandfire.datagen.tags;

import net.minecraft.world.entity.EntityTypes;
import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;

public class IafEntityTags extends EntityTypeTagsProvider {
    public static TagKey<EntityType<?>> IMMUNE_TO_GORGON_STONE = createKey("immune_to_gorgon_stone");

    public IafEntityTags(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider, IceAndFire.MODID);
    }

    @Override
    protected RegistryTagAppender<EntityType<?>> tag(TagKey<EntityType<?>> tag) {
        return RegistryTagAppender.wrap(super.tag(tag), entityType -> BuiltInRegistries.ENTITY_TYPE.getResourceKey(entityType).orElseThrow());
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(IMMUNE_TO_GORGON_STONE)
                .addTag(Tags.EntityTypes.BOSSES)
                .add(EntityTypes.WARDEN);
    }

    private static TagKey<EntityType<?>> createKey(final String name) {
        return TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(IceAndFire.MODID, name));
    }
}
