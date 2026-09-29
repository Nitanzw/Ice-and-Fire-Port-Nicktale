package com.github.alexthe666.iceandfire.item;

import com.nicktale.api.server.item.CustomToolMaterial;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

/** Custom tool-material values replacing Forge's tier sorting registry on Minecraft 26.2. */
public final class DragonSteelTier {
    public static final TagKey<Block> DRAGONSTEEL_TIER_TAG = BlockTags.create(Identifier.parse("iceandfire:needs_dragonsteel"));

    public static final CustomToolMaterial DRAGONSTEEL_TIER_FIRE = createMaterial("dragonsteel_tier_fire");
    public static final CustomToolMaterial DRAGONSTEEL_TIER_ICE = createMaterial("dragonsteel_tier_ice");
    public static final CustomToolMaterial DRAGONSTEEL_TIER_LIGHTNING = createMaterial("dragonsteel_tier_lightning");
    // FIXME: Retained legacy name until callers are ported to the material abstraction.
    public static final CustomToolMaterial DRAGONSTEEL_TIER_DREAD_QUEEN = createMaterial("dragonsteel_tier_dread_queen");

    private DragonSteelTier() {
    }

    private static CustomToolMaterial createMaterial(String name) {
        // These are the values of the former ForgeTier used by every dragonsteel variant.
        return new CustomToolMaterial(name, 4, 8000, 10.0F, 21.0F, 10);
    }
}
