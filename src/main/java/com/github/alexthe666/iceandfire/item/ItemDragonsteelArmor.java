package com.github.alexthe666.iceandfire.item;

import com.nicktale.api.server.item.CustomArmorMaterial;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.equipment.ArmorType;

import java.util.function.Consumer;

/** Dragonsteel armor uses the material's 26.2 equipment and attribute components. */
public class ItemDragonsteelArmor extends ItemModArmor implements IProtectAgainstDragonItem {
    /** Kept in the constructor for the original registry call shape. */
    public ItemDragonsteelArmor(CustomArmorMaterial material, int renderIndex, ArmorType type) {
        super(material, type);
    }

    public int getDefense() {
        return material.getDefenseForType(armorType);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display,
                                Consumer<Component> tooltip, TooltipFlag flag) {
        tooltip.accept(Component.translatable("item.dragonscales_armor.desc").withStyle(ChatFormatting.GRAY));
    }
}
