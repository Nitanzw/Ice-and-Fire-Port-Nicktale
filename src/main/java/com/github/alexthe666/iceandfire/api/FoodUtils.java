package com.github.alexthe666.iceandfire.api;

import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class FoodUtils {

    public static int getFoodPoints(Entity entity) {
        int foodPoints = Math.round(entity.getBbWidth() * entity.getBbHeight() * 10);
        if (entity instanceof AgeableMob) {
            return foodPoints;
        }
        if (entity instanceof Player) {
            return 15;
        }
        return 0;
    }

    public static int getFoodPoints(ItemStack item, boolean meatOnly, boolean includeFish) {
        if (item != null && !item.isEmpty() && item.get(DataComponents.FOOD) != null) {
            int food = item.get(DataComponents.FOOD).nutrition() * 10;
            if (!meatOnly) {
                return food;
            } else if (item.is(ItemTags.MEAT)) {
                return food;
            } else if (includeFish && item.is(ItemTags.FISHES)) {
                return food;
            }
        }
        return 0;
    }
}
