package com.github.alexthe666.iceandfire.item;

import com.nicktale.api.server.item.CustomArmorMaterial;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.equipment.ArmorType;

import java.util.Calendar;
import java.util.Date;
import java.util.function.Consumer;

/** Armor items in 26.2 carry their equip and attribute behavior in item data components. */
public class ItemModArmor extends Item {
    protected final CustomArmorMaterial material;
    protected final ArmorType armorType;

    public ItemModArmor(CustomArmorMaterial material, ArmorType armorType) {
        super(ItemProperties.armor(material, armorType));
        this.material = material;
        this.armorType = armorType;
    }

    public CustomArmorMaterial getArmorMaterial() {
        return material;
    }

    public ArmorType getArmorType() {
        return armorType;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display,
                                Consumer<Component> tooltip, TooltipFlag flag) {
        if (this == IafItemRegistry.EARPLUGS.get()) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            if (calendar.get(Calendar.MONTH) + 1 == 4 && calendar.get(Calendar.DATE) == 1) {
                tooltip.accept(Component.translatable("item.iceandfire.air_pods.desc").withStyle(ChatFormatting.GREEN));
            }
        }
        super.appendHoverText(stack, context, display, tooltip, flag);
    }
}
