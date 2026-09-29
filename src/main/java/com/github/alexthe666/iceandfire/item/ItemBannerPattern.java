package com.github.alexthe666.iceandfire.item;

import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BannerPattern;

/** Banner-pattern item backed by the vanilla 26.2 provides_banner_patterns component. */
public class ItemBannerPattern extends Item {
    public ItemBannerPattern(TagKey<BannerPattern> patternTag, Item.Properties properties) {
        super(properties.delayedComponent(DataComponents.PROVIDES_BANNER_PATTERNS, lookup ->
            lookup.lookupOrThrow(Registries.BANNER_PATTERN).get(patternTag).orElse(HolderSet.empty())));
    }
}
