package com.github.alexthe666.iceandfire.recipe;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.ForgeRegistries;
import net.neoforged.neoforge.registries.RegistryObject;

public class IafRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, IceAndFire.MODID);
    public static final RegistryObject<RecipeSerializer<?>> DRAGONFORGE_SERIALIZER = SERIALIZERS.register("dragonforge", DragonForgeRecipe.Serializer::new);

}
