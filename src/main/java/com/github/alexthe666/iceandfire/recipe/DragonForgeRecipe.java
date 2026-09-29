package com.github.alexthe666.iceandfire.recipe;

import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;

public final class DragonForgeRecipe implements Recipe<DragonForgeRecipe.Input> {
    private final Recipe.CommonInfo commonInfo;
    private final Ingredient input;
    private final Ingredient blood;
    private final ItemStackTemplate result;
    private final String dragonType;
    private final int cookTime;

    public DragonForgeRecipe(Recipe.CommonInfo commonInfo, Ingredient input, Ingredient blood, ItemStackTemplate result,
                             String dragonType, int cookTime) {
        this.commonInfo = commonInfo;
        this.input = input;
        this.blood = blood;
        this.result = result;
        this.dragonType = dragonType;
        this.cookTime = cookTime;
    }

    public Ingredient getInput() {
        return this.input;
    }

    public Ingredient getBlood() {
        return this.blood;
    }

    public int getCookTime() {
        return this.cookTime;
    }

    public String getDragonType() {
        return this.dragonType;
    }

    public ItemStack getResultItem() {
        return this.result.create();
    }

    @Override
    public boolean matches(Input input, Level level) {
        return this.input.test(input.getItem(0)) && this.blood.test(input.getItem(1))
            && this.dragonType.equals(input.dragonType());
    }

    public boolean isValidInput(ItemStack stack) {
        return this.input.test(stack);
    }

    public boolean isValidBlood(ItemStack stack) {
        return this.blood.test(stack);
    }

    @Override
    public ItemStack assemble(Input input) {
        return this.result.create();
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public boolean showNotification() {
        return this.commonInfo.showNotification();
    }

    @Override
    public String group() {
        return "";
    }

    @Override
    public RecipeSerializer<DragonForgeRecipe> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<DragonForgeRecipe> getType() {
        return IafRecipeRegistry.DRAGON_FORGE_TYPE.get();
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    public record Input(ItemStack input, ItemStack blood, String dragonType) implements RecipeInput {
        @Override
        public ItemStack getItem(int index) {
            return switch (index) {
                case 0 -> this.input;
                case 1 -> this.blood;
                default -> throw new IndexOutOfBoundsException("Dragon forge input slot: " + index);
            };
        }

        @Override
        public int size() {
            return 2;
        }
    }

    public static final class Serializer {
        private static final MapCodec<DragonForgeRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Recipe.CommonInfo.MAP_CODEC.forGetter(recipe -> recipe.commonInfo),
            Ingredient.CODEC.fieldOf("input").forGetter(recipe -> recipe.input),
            Ingredient.CODEC.fieldOf("blood").forGetter(recipe -> recipe.blood),
            ItemStackTemplate.CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
            Codec.STRING.fieldOf("dragon_type").forGetter(recipe -> recipe.dragonType),
            Codec.INT.optionalFieldOf("cook_time", 100).forGetter(recipe -> recipe.cookTime)
        ).apply(instance, DragonForgeRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, DragonForgeRecipe> STREAM_CODEC = StreamCodec.composite(
            Recipe.CommonInfo.STREAM_CODEC, recipe -> recipe.commonInfo,
            Ingredient.CONTENTS_STREAM_CODEC, recipe -> recipe.input,
            Ingredient.CONTENTS_STREAM_CODEC, recipe -> recipe.blood,
            ItemStackTemplate.STREAM_CODEC, recipe -> recipe.result,
            ByteBufCodecs.STRING_UTF8, recipe -> recipe.dragonType,
            ByteBufCodecs.VAR_INT, recipe -> recipe.cookTime,
            DragonForgeRecipe::new
        );

        public static final RecipeSerializer<DragonForgeRecipe> INSTANCE = new RecipeSerializer<>(CODEC, STREAM_CODEC);

        private Serializer() {}
    }
}
