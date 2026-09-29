package com.github.alexthe666.iceandfire.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

/** Kept for item registry compatibility; 26.2 custom item models are supplied by model assets. */
public class BlockItemWithRender extends BlockItem {
    public BlockItemWithRender(Block block, Item.Properties properties) {
        super(block, properties);
    }
}
