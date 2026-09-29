package com.github.alexthe666.iceandfire.client.render.tile;

import com.github.alexthe666.iceandfire.block.BlockPixieHouse;
import com.github.alexthe666.iceandfire.client.model.ModelPixie;
import com.github.alexthe666.iceandfire.client.model.ModelPixieHouse;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityPixieHouse;
import com.mojang.math.Axis;
import net.minecraft.core.Direction;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class RenderPixieHouse<T extends TileEntityPixieHouse> implements BlockEntityRenderer<T, RenderPixieHouse.HouseRenderState> {

    private static final ModelPixieHouse MODEL = new ModelPixieHouse();
    private final ModelPixie modelPixie = new ModelPixie();
    private static final RenderType TEXTURE_0 = RenderTypes.entityCutout(Identifier.parse("iceandfire:textures/models/pixie/house/pixie_house_0.png"), false);
    private static final RenderType TEXTURE_1 = RenderTypes.entityCutout(Identifier.parse("iceandfire:textures/models/pixie/house/pixie_house_1.png"), false);
    private static final RenderType TEXTURE_2 = RenderTypes.entityCutout(Identifier.parse("iceandfire:textures/models/pixie/house/pixie_house_2.png"), false);
    private static final RenderType TEXTURE_3 = RenderTypes.entityCutout(Identifier.parse("iceandfire:textures/models/pixie/house/pixie_house_3.png"), false);
    private static final RenderType TEXTURE_4 = RenderTypes.entityCutout(Identifier.parse("iceandfire:textures/models/pixie/house/pixie_house_4.png"), false);
    private static final RenderType TEXTURE_5 = RenderTypes.entityCutout(Identifier.parse("iceandfire:textures/models/pixie/house/pixie_house_5.png"), false);

    public RenderPixieHouse(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public @NotNull HouseRenderState createRenderState() {
        return new HouseRenderState();
    }

    @Override
    public void extractRenderState(@NotNull T house, @NotNull HouseRenderState state, float partialTick, @NotNull Vec3 cameraPos, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(house, state, partialTick, cameraPos, breakProgress);
        state.tile = house;
        state.partialTick = partialTick;
        state.valid = false;
        if (house.getLevel() != null && house.getLevel().getBlockState(house.getBlockPos()).getBlock() instanceof BlockPixieHouse) {
            var blockState = house.getLevel().getBlockState(house.getBlockPos());
            // With render-optimizing mods this can run before the block exists; bail out quietly.
            if (!blockState.hasProperty(BlockPixieHouse.FACING)) {
                return;
            }
            state.meta = TileEntityPixieHouse.getHouseTypeFromBlock(blockState.getBlock());
            Direction facing = blockState.getValue(BlockPixieHouse.FACING);
            state.rotation = facing == Direction.NORTH ? 180 : facing == Direction.EAST ? -90 : facing == Direction.WEST ? 90 : 0;
            state.valid = true;
        }
    }

    /** Renders the house model for an item (no tile entity) with the meta of the given block item. */
    public static void submitItemHouse(BlockItem item, PoseStack matrixStackIn, SubmitNodeCollector collector, int light, int overlay) {
        int meta = TileEntityPixieHouse.getHouseTypeFromBlock(item.getBlock());
        matrixStackIn.pushPose();
        matrixStackIn.translate(0.5F, 1.501F, 0.5F);
        matrixStackIn.mulPose(Axis.XP.rotationDegrees(180.0F));
        collector.submitModel(MODEL, new PoseStates.Generic(MODEL::resetToDefaultPose), matrixStackIn, typeForMeta(meta), light, overlay, -1, null, 0, null);
        matrixStackIn.popPose();
    }

    private static RenderType typeForMeta(int meta) {
        switch (meta) {
            case 1:
                return TEXTURE_1;
            case 2:
                return TEXTURE_2;
            case 3:
                return TEXTURE_3;
            case 4:
                return TEXTURE_4;
            case 5:
                return TEXTURE_5;
            default:
                return TEXTURE_0;
        }
    }

    @Override
    public void submit(@NotNull HouseRenderState state, @NotNull PoseStack matrixStackIn, @NotNull SubmitNodeCollector collector, @NotNull CameraRenderState camera) {
        if (!state.valid) {
            return;
        }
        TileEntityPixieHouse entity = (TileEntityPixieHouse) state.tile;
        int light = state.lightCoords;
        matrixStackIn.pushPose();
        matrixStackIn.translate(0.5F, 1.501F, 0.5F);
        matrixStackIn.mulPose(Axis.XP.rotationDegrees(180.0F));
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(state.rotation));
        if (entity.hasPixie) {
            matrixStackIn.pushPose();
            matrixStackIn.translate(0F, 0.95F, 0F);
            matrixStackIn.scale(0.55F, 0.55F, 0.55F);
            RenderType type;
            RenderType type2;
            switch (entity.pixieType) {
                default:
                    type = RenderJar.TEXTURE_0;
                    type2 = RenderJar.TEXTURE_0_GLO;
                    break;
                case 1:
                    type = RenderJar.TEXTURE_1;
                    type2 = RenderJar.TEXTURE_1_GLO;
                    break;
                case 2:
                    type = RenderJar.TEXTURE_2;
                    type2 = RenderJar.TEXTURE_2_GLO;
                    break;
                case 3:
                    type = RenderJar.TEXTURE_3;
                    type2 = RenderJar.TEXTURE_3_GLO;
                    break;
                case 4:
                    type = RenderJar.TEXTURE_4;
                    type2 = RenderJar.TEXTURE_4_GLO;
                    break;
                case 5:
                    type = RenderJar.TEXTURE_5;
                    type2 = RenderJar.TEXTURE_5_GLO;
                    break;
            }
            PoseStates.Pixie pixieState = new PoseStates.Pixie();
            pixieState.pose = () -> modelPixie.animateInHouse(entity);
            collector.submitModel(modelPixie, pixieState, matrixStackIn, type, light, OverlayTexture.NO_OVERLAY, -1, null, 0, null);
            collector.submitModel(modelPixie, pixieState, matrixStackIn, type2, light, OverlayTexture.NO_OVERLAY, -1, null, 0, null);
            matrixStackIn.popPose();
        }
        collector.submitModel(MODEL, new PoseStates.Generic(MODEL::resetToDefaultPose), matrixStackIn, typeForMeta(state.meta), light, OverlayTexture.NO_OVERLAY, -1, null, 0, null);
        matrixStackIn.popPose();
    }

    public static class HouseRenderState extends TileRenderState {
        public boolean valid;
        public int meta;
        public int rotation;
    }
}
