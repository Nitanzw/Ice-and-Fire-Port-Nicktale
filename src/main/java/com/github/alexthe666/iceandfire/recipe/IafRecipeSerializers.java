package com.github.alexthe666.iceandfire.recipe;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class IafRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
        DeferredRegister.create(Registries.RECIPE_SERIALIZER, IceAndFire.MODID);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<DragonForgeRecipe>> DRAGONFORGE_SERIALIZER =
        SERIALIZERS.register("dragonforge", () -> DragonForgeRecipe.Serializer.INSTANCE);

    private IafRecipeSerializers() {}
}
