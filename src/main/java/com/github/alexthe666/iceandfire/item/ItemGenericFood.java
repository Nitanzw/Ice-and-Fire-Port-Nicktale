package com.github.alexthe666.iceandfire.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ItemGenericFood extends Item {
    private final int healAmount;
    private final float saturation;

    public ItemGenericFood(int amount, float saturation, boolean isWolfFood, boolean eatFast, boolean alwaysEdible) {
        super(IafItemRegistry.itemProperties().food(createFood(amount, saturation, isWolfFood, eatFast, alwaysEdible, null), createConsumable(eatFast, null)));
        this.healAmount = amount;
        this.saturation = saturation;
    }

    public ItemGenericFood(int amount, float saturation, boolean isWolfFood, boolean eatFast, boolean alwaysEdible, int stackSize) {
        super(IafItemRegistry.itemProperties().food(createFood(amount, saturation, isWolfFood, eatFast, alwaysEdible, null), createConsumable(eatFast, null)).stacksTo(stackSize));
        this.healAmount = amount;
        this.saturation = saturation;
    }

    public static final FoodProperties createFood(int amount, float saturation, boolean isWolfFood, boolean eatFast, boolean alwaysEdible, MobEffectInstance potion) {
        FoodProperties.Builder builder = new FoodProperties.Builder();
        builder.nutrition(amount);
        builder.saturationModifier(saturation);
        if (alwaysEdible) {
            builder.alwaysEdible();
        }
        // Wolf-food (meat) status is expressed through the minecraft:meat item tag in 26.2; potion and fast-eating
        // behavior moved to the Consumable component (see createConsumable).
        return builder.build();
    }

    public static net.minecraft.world.item.component.Consumable createConsumable(boolean eatFast, MobEffectInstance potion) {
        net.minecraft.world.item.component.Consumable.Builder consumable = net.minecraft.world.item.component.Consumable.builder()
            .consumeSeconds(eatFast ? 0.8F : 1.6F);
        if (potion != null) {
            consumable.onConsume(new net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect(potion));
        }
        return consumable.build();
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level worldIn, @NotNull LivingEntity LivingEntity) {
        this.onFoodEaten(stack, worldIn, LivingEntity);
        return super.finishUsingItem(stack, worldIn, LivingEntity);
    }

    public void onFoodEaten(ItemStack stack, Level worldIn, LivingEntity livingEntity) {
    }
}
