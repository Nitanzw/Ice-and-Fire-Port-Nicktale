package com.github.alexthe666.iceandfire.recipe;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class IafRecipeRegistry {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPE =
        DeferredRegister.create(Registries.RECIPE_TYPE, IceAndFire.MODID);
    public static final DeferredHolder<RecipeType<?>, RecipeType<DragonForgeRecipe>> DRAGON_FORGE_TYPE =
        RECIPE_TYPE.register("dragonforge", () -> RecipeType.simple(Identifier.fromNamespaceAndPath(IceAndFire.MODID, "dragonforge")));

    private IafRecipeRegistry() {}
}
