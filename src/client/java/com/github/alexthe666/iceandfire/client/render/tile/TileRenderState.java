package com.github.alexthe666.iceandfire.client.render.tile;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.level.block.entity.BlockEntity;

/** Block-entity render state that carries the tile itself (client thread only) plus a resolved item, if any. */
public class TileRenderState extends BlockEntityRenderState {
    public BlockEntity tile;
    public float partialTick;
    public final ItemStackRenderState item = new ItemStackRenderState();
}
