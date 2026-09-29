package com.github.alexthe666.iceandfire.item;

import com.nicktale.api.server.item.CustomArmorMaterial;
import com.github.alexthe666.iceandfire.enums.EnumSeaSerpent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.equipment.ArmorType;

import java.util.function.Consumer;

public class ItemSeaSerpentArmor extends ItemModArmor {
    public final EnumSeaSerpent armor_type;

    public ItemSeaSerpentArmor(EnumSeaSerpent armorType, CustomArmorMaterial material, ArmorType type) {
        super(material, type);
        this.armor_type = armorType;
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity, EquipmentSlot slot) {
        if (!(entity instanceof Player player)) {
            return;
        }
        if (slot != EquipmentSlot.HEAD && slot != EquipmentSlot.CHEST
            && slot != EquipmentSlot.LEGS && slot != EquipmentSlot.FEET) {
            return;
        }
        player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 50, 0, false, false));
        if (player.isInWaterOrRain()) {
            int pieces = countSeaSerpentArmor(player);
            player.addEffect(new MobEffectInstance(MobEffects.STRENGTH, 50, pieces - 1, false, false));
        }
    }

    private static int countSeaSerpentArmor(Player player) {
        int pieces = 0;
        for (EquipmentSlot slot : new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
            if (player.getItemBySlot(slot).getItem() instanceof ItemSeaSerpentArmor) {
                pieces++;
            }
        }
        return pieces;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display,
                                Consumer<Component> tooltip, TooltipFlag flag) {
        tooltip.accept(Component.translatable("sea_serpent." + armor_type.resourceName).withStyle(armor_type.color));
        tooltip.accept(Component.translatable("item.iceandfire.sea_serpent_armor.desc_0").withStyle(ChatFormatting.GRAY));
        tooltip.accept(Component.translatable("item.iceandfire.sea_serpent_armor.desc_1").withStyle(ChatFormatting.GRAY));
    }
}
