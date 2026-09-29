package com.github.alexthe666.iceandfire.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class ItemHydraHeart extends Item {

    public ItemHydraHeart() {
        super(IafItemRegistry.itemProperties()/*.tab(IceAndFire.TAB_ITEMS)*/.stacksTo(1));
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull net.minecraft.server.level.ServerLevel world, @NotNull Entity entity, EquipmentSlot slot) {
        if (entity instanceof Player player && isInHotbar(player, stack)) {
            double healthPercentage = player.getHealth() / Math.max(1, player.getMaxHealth());
            if (healthPercentage < 1.0D) {
                int level = 0;
                if (healthPercentage < 0.25D) {
                    level = 3;
                } else if (healthPercentage < 0.5D) {
                    level = 2;
                } else if (healthPercentage < 0.75D) {
                    level = 1;
                }
                //Consider using EffectInstance.combine
                if (!player.hasEffect(MobEffects.REGENERATION) || player.getEffect(MobEffects.REGENERATION).getAmplifier() < level)
                    player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, level, true, false));
            }
            //In hotbar
        }
    }

    private static boolean isInHotbar(Player player, ItemStack stack) {
        for (int slot = 0; slot < 9; slot++) {
            if (player.getInventory().getItem(slot) == stack) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, net.minecraft.world.item.component.TooltipDisplay display, java.util.function.Consumer<Component> tooltip, TooltipFlag flagIn) {
        tooltip.accept(Component.translatable("item.iceandfire.legendary_weapon.desc").withStyle(ChatFormatting.GRAY));
        tooltip.accept(Component.translatable("item.iceandfire.hydra_heart.desc_0").withStyle(ChatFormatting.GRAY));
        tooltip.accept(Component.translatable("item.iceandfire.hydra_heart.desc_1").withStyle(ChatFormatting.GRAY));
    }
}
