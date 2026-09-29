package com.nicktale.api.server.item;

import net.minecraft.world.item.crafting.Ingredient;

/** Mutable tool-material description (Minecraft 26.2 replaced Tier with an immutable record). */
public class CustomToolMaterial {
    private final String name;
    private final int level;
    private final int uses;
    private final float speed;
    private final float attackDamageBonus;
    private final int enchantmentValue;
    private Ingredient repairIngredient = Ingredient.of();

    public CustomToolMaterial(String name, int level, int uses, float speed, float attackDamageBonus, int enchantmentValue) {
        this.name = name;
        this.level = level;
        this.uses = uses;
        this.speed = speed;
        this.attackDamageBonus = attackDamageBonus;
        this.enchantmentValue = enchantmentValue;
    }

    public String getName() { return name; }
    public int getLevel() { return level; }
    public int getUses() { return uses; }
    public float getSpeed() { return speed; }
    public float getAttackDamageBonus() { return attackDamageBonus; }
    public int getEnchantmentValue() { return enchantmentValue; }

    public Ingredient getRepairIngredient() { return repairIngredient; }
    public void setRepairMaterial(Ingredient ingredient) { this.repairIngredient = ingredient; }
}
