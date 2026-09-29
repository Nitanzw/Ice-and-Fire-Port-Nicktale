package com.nicktale.api.server.item;

import java.util.EnumMap;
import java.util.Map;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAssets;

/**
 * Mutable armor-material description. {@code damageReduction} is indexed boots, leggings, chestplate, helmet.
 * Subclasses may override the getters; {@link #toArmorMaterial()} snapshots them into a vanilla record.
 */
public class CustomArmorMaterial {
    private final String name;
    private final int durabilityFactor;
    private final int[] damageReduction;
    private final int enchantmentValue;
    private final Holder<SoundEvent> equipSound;
    private final float toughness;
    private final float knockbackResistance;
    private Ingredient repairIngredient = Ingredient.of();

    public CustomArmorMaterial(String name, int durabilityFactor, int[] damageReduction, int enchantmentValue,
                               Holder<SoundEvent> equipSound, float toughness, float knockbackResistance) {
        this.name = name;
        this.durabilityFactor = durabilityFactor;
        this.damageReduction = damageReduction;
        this.enchantmentValue = enchantmentValue;
        this.equipSound = equipSound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
    }

    public String getName() { return name; }
    public int getEnchantmentValue() { return enchantmentValue; }
    public Holder<SoundEvent> getEquipSound() { return equipSound; }
    public float getToughness() { return toughness; }
    public float getKnockbackResistance() { return knockbackResistance; }

    public int getDurabilityForType(ArmorType type) {
        return type.getDurability(durabilityFactor);
    }

    public int getDefenseForType(ArmorType type) {
        int index = switch (type) {
            case BOOTS -> 0;
            case LEGGINGS -> 1;
            case CHESTPLATE, BODY -> 2;
            case HELMET -> 3;
        };
        return index < damageReduction.length ? damageReduction[index] : 0;
    }

    public Ingredient getRepairIngredient() { return repairIngredient; }
    public void setRepairMaterial(Ingredient ingredient) { this.repairIngredient = ingredient; }

    /** Vanilla record view; the real repair set is applied separately as a delayed REPAIRABLE component. */
    public ArmorMaterial toArmorMaterial() {
        Map<ArmorType, Integer> defense = new EnumMap<>(ArmorType.class);
        int maxDurability = 0;
        for (ArmorType type : ArmorType.values()) {
            defense.put(type, getDefenseForType(type));
            maxDurability = Math.max(maxDurability, getDurabilityForType(type));
        }
        ResourceKey<net.minecraft.world.item.equipment.EquipmentAsset> asset =
            ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.parse("iceandfire:" + name.toLowerCase(java.util.Locale.ROOT)));
        return new ArmorMaterial(Math.max(1, maxDurability / 16), defense, enchantmentValue, equipSound, getToughness(),
            knockbackResistance, ItemTags.REPAIRS_IRON_ARMOR, asset);
    }
}
