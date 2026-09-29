package com.github.alexthe666.iceandfire.item;

import com.nicktale.api.server.item.CustomToolMaterial;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

public class ItemModPickaxe extends Item implements DragonSteelOverrides<ItemModPickaxe> {
    private final CustomToolMaterial toolMaterial;

    public ItemModPickaxe(CustomToolMaterial toolMaterial) {
        super(ItemProperties.pickaxe(toolMaterial, 1.0F, -2.8F));
        this.toolMaterial = toolMaterial;
    }

    @Override
    public CustomToolMaterial getToolMaterial() {
        return toolMaterial;
    }

    @Override
    public float getBaseAttackDamage() {
        return 1.0F;
    }

    @Override
    public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        applyMaterialHit(this, stack, target, attacker);
        super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display,
                                Consumer<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, display, tooltip, flag);
        appendMaterialTooltip(toolMaterial, stack, context, display, tooltip, flag);
    }
}
