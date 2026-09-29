// SPDX-License-Identifier: LGPL-3.0-or-later
package com.github.alexthe666.iceandfire.item;

import com.nicktale.api.server.item.CustomArmorMaterial;
import com.nicktale.api.server.item.CustomToolMaterial;
import com.github.alexthe666.iceandfire.IafConfig;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.enchantment.Repairable;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.level.block.Block;
import com.github.alexthe666.iceandfire.util.IafEntityUtil;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

/** Builders for item properties which need the Ice and Fire materials and data-driven components. */
public final class ItemProperties {
    private ItemProperties() {
    }

    /**
     * Applies the repair item as a delayed item component. Material repair ingredients are populated after the item
     * registry has constructed its entries, so eagerly copying the Ingredient would lose the custom repair set.
     */
    public static Item.Properties withRepairIngredient(Item.Properties properties, Supplier<Ingredient> ingredient) {
        return properties.delayedComponent(net.minecraft.core.component.DataComponents.REPAIRABLE, lookup -> {
            Ingredient repair;
            net.minecraft.core.HolderLookup.Provider previous = IafEntityUtil.LOOKUP.get();
            IafEntityUtil.LOOKUP.set(lookup);
            try {
                repair = ingredient.get();
            } finally {
                IafEntityUtil.LOOKUP.set(previous);
            }
            return new Repairable(repair == null ? net.minecraft.core.HolderSet.empty() : repair.getValues());
        });
    }

    public static Item.Properties withRepairIngredient(Item.Properties properties, Ingredient ingredient) {
        return withRepairIngredient(properties, () -> ingredient);
    }

    /** Use the active deferred-register properties for legacy constructor-only registrations. */
    public static Item.Properties withRepairIngredient(Ingredient ingredient) {
        return withRepairIngredient(IafItemRegistry.itemProperties(), ingredient);
    }

    public static Item.Properties armor(CustomArmorMaterial material, ArmorType type) {
        return withRepairIngredient(IafItemRegistry.itemProperties().humanoidArmor(material.toArmorMaterial(), type), material::getRepairIngredient);
    }

    public static Item.Properties sword(CustomToolMaterial material, float attackDamage, float attackSpeed) {
        float dragonsteelDamage = (float) IafConfig.dragonsteelBaseDamage - 1.0F;
        Item.Properties properties = IafItemRegistry.itemProperties().sword(toVanillaToolMaterial(material),
            attackDamageBaseline(material, attackDamage, dragonsteelDamage), attackSpeed);
        return withRepairIngredient(withMaterialDurability(material, properties), material::getRepairIngredient);
    }

    public static Item.Properties pickaxe(CustomToolMaterial material, float attackDamage, float attackSpeed) {
        Item.Properties properties = IafItemRegistry.itemProperties().pickaxe(toVanillaToolMaterial(material),
            attackDamageBaseline(material, attackDamage, (float) IafConfig.dragonsteelBaseDamage), attackSpeed);
        return withRepairIngredient(withMaterialDurability(material, properties), material::getRepairIngredient);
    }

    public static Item.Properties axe(CustomToolMaterial material, float attackDamage, float attackSpeed) {
        Item.Properties properties = IafItemRegistry.itemProperties().axe(toVanillaToolMaterial(material),
            attackDamageBaseline(material, attackDamage, (float) IafConfig.dragonsteelBaseDamage + 4.0F), attackSpeed);
        return withRepairIngredient(withMaterialDurability(material, properties), material::getRepairIngredient);
    }

    public static Item.Properties hoe(CustomToolMaterial material, float attackDamage, float attackSpeed) {
        Item.Properties properties = IafItemRegistry.itemProperties().hoe(toVanillaToolMaterial(material),
            attackDamageBaseline(material, attackDamage, 1.0F), attackSpeed);
        return withRepairIngredient(withMaterialDurability(material, properties), material::getRepairIngredient);
    }

    public static Item.Properties shovel(CustomToolMaterial material, float attackDamage, float attackSpeed) {
        Item.Properties properties = IafItemRegistry.itemProperties().shovel(toVanillaToolMaterial(material),
            attackDamageBaseline(material, attackDamage, (float) IafConfig.dragonsteelBaseDamage + 0.5F), attackSpeed);
        return withRepairIngredient(withMaterialDurability(material, properties), material::getRepairIngredient);
    }

    private static float attackDamageBaseline(CustomToolMaterial material, float defaultBaseline, float dragonsteelDamage) {
        return isDragonsteel(material) ? dragonsteelDamage - material.getAttackDamageBonus() : defaultBaseline;
    }

    private static Item.Properties withMaterialDurability(CustomToolMaterial material, Item.Properties properties) {
        return isDragonsteel(material) ? properties.durability(IafConfig.dragonsteelBaseDurability) : properties;
    }

    private static boolean isDragonsteel(CustomToolMaterial material) {
        return material == DragonSteelTier.DRAGONSTEEL_TIER_FIRE
            || material == DragonSteelTier.DRAGONSTEEL_TIER_ICE
            || material == DragonSteelTier.DRAGONSTEEL_TIER_LIGHTNING
            || material == DragonSteelTier.DRAGONSTEEL_TIER_DREAD_QUEEN;
    }

    /**
     * 26.2 replaced Tier with a value record. The custom repair Ingredient is installed independently as a delayed
     * REPAIRABLE component; the record uses a vanilla placeholder because its repair set is represented by a tag,
     * while our custom repair Ingredient is only available after registry setup.
     */
    public static ToolMaterial toVanillaToolMaterial(CustomToolMaterial material) {
        return new ToolMaterial(incorrectBlocksFor(material), material.getUses(), material.getSpeed(),
            material.getAttackDamageBonus(), Math.max(1, material.getEnchantmentValue()), ItemTags.REPAIRS_IRON_ARMOR);
    }

    private static TagKey<Block> incorrectBlocksForLevel(int level) {
        return switch (level) {
            case 0 -> BlockTags.INCORRECT_FOR_WOODEN_TOOL;
            case 1 -> BlockTags.INCORRECT_FOR_STONE_TOOL;
            case 2 -> BlockTags.INCORRECT_FOR_IRON_TOOL;
            case 3 -> BlockTags.INCORRECT_FOR_DIAMOND_TOOL;
            default -> BlockTags.INCORRECT_FOR_NETHERITE_TOOL;
        };
    }

    private static TagKey<Block> incorrectBlocksFor(CustomToolMaterial material) {
        if (material == DragonSteelTier.DRAGONSTEEL_TIER_FIRE
            || material == DragonSteelTier.DRAGONSTEEL_TIER_ICE
            || material == DragonSteelTier.DRAGONSTEEL_TIER_LIGHTNING
            || material == DragonSteelTier.DRAGONSTEEL_TIER_DREAD_QUEEN) {
            return DragonSteelTier.DRAGONSTEEL_TIER_TAG;
        }
        return incorrectBlocksForLevel(material.getLevel());
    }
}
