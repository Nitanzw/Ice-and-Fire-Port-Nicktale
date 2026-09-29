package com.github.alexthe666.iceandfire.client.render.tile;

import com.github.alexthe666.iceandfire.entity.tile.TileEntityGhostChest;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.renderer.blockentity.state.ChestRenderState;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.properties.ChestType;

public class RenderGhostChest extends ChestRenderer<TileEntityGhostChest> {

    private static final Identifier ATLAS = Sheets.CHEST_SHEET;
    private static final SpriteId GHOST_CHEST = new SpriteId(ATLAS, Identifier.parse("iceandfire:entity/chest/ghost_chest"));
    private static final SpriteId GHOST_CHEST_LEFT = new SpriteId(ATLAS, Identifier.parse("iceandfire:entity/chest/ghost_chest_left"));
    private static final SpriteId GHOST_CHEST_RIGHT = new SpriteId(ATLAS, Identifier.parse("iceandfire:entity/chest/ghost_chest_right"));

    public RenderGhostChest(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected SpriteId getCustomSprite(TileEntityGhostChest tileEntity, ChestRenderState state) {
        ChestType type = state.type;
        if (type == ChestType.LEFT) {
            return GHOST_CHEST_LEFT;
        } else if (type == ChestType.RIGHT) {
            return GHOST_CHEST_RIGHT;
        }
        return GHOST_CHEST;
    }

}
