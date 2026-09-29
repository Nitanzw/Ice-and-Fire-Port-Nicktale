package com.github.alexthe666.iceandfire.util;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

/** Enchantments are data-driven holders in 26.2; resolves a key against a registry access. */
public final class EnchantUtil {
    private EnchantUtil() {
    }

    public static int getLevel(HolderLookup.Provider registries, ResourceKey<Enchantment> key, ItemStack stack) {
        return registries.lookupOrThrow(Registries.ENCHANTMENT).get(key)
            .map(holder -> EnchantmentHelper.getItemEnchantmentLevel(holder, stack)).orElse(0);
    }
}
