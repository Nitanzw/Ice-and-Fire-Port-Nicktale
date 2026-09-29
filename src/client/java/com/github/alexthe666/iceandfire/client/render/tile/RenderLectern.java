package com.github.alexthe666.iceandfire.client.render.tile;

import com.github.alexthe666.iceandfire.block.BlockLectern;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityLectern;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.book.BookModel;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class RenderLectern<T extends TileEntityLectern> implements BlockEntityRenderer<T, RenderLectern.LecternRenderState> {

    private static final RenderType ENCHANTMENT_TABLE_BOOK_TEXTURE = RenderTypes.entityCutout(Identifier.parse("iceandfire:textures/models/lectern_book.png"));
    private final BookModel bookModel;

    public RenderLectern(BlockEntityRendererProvider.Context context) {
        this.bookModel = new BookModel(context.bakeLayer(ModelLayers.BOOK));
    }

    @Override
    public @NotNull LecternRenderState createRenderState() {
        return new LecternRenderState();
    }

    @Override
    public void extractRenderState(@NotNull T lectern, @NotNull LecternRenderState state, float partialTick, @NotNull Vec3 cameraPos, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(lectern, state, partialTick, cameraPos, breakProgress);
        state.rotation = this.getRotation(lectern);
        float f4 = lectern.pageFlipPrev + (lectern.pageFlip - lectern.pageFlipPrev) * partialTick + 0.25F;
        float f5 = lectern.pageFlipPrev + (lectern.pageFlip - lectern.pageFlipPrev) * partialTick + 0.75F;
        f4 = (f4 - Mth.floor(f4)) * 1.6F - 0.3F;
        f5 = (f5 - Mth.floor(f5)) * 1.6F - 0.3F;
        state.pageFlip1 = Mth.clamp(f4, 0.0F, 1.0F);
        state.pageFlip2 = Mth.clamp(f5, 0.0F, 1.0F);
    }

    @Override
    public void submit(@NotNull LecternRenderState state, @NotNull PoseStack matrixStackIn, @NotNull SubmitNodeCollector collector, @NotNull CameraRenderState camera) {
        matrixStackIn.pushPose();
        matrixStackIn.translate(0.5F, 1.1F, 0.5F);
        matrixStackIn.scale(0.8F, 0.8F, 0.8F);
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(state.rotation));
        matrixStackIn.mulPose(Axis.XP.rotationDegrees(112.0F));
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(90.0F));
        float openness = 1.29F;
        collector.submitModel(this.bookModel, new BookModel.State(openness, state.pageFlip1, state.pageFlip2), matrixStackIn,
            ENCHANTMENT_TABLE_BOOK_TEXTURE, state.lightCoords, OverlayTexture.NO_OVERLAY, -1, null, 0, null);
        matrixStackIn.popPose();
    }

    private float getRotation(TileEntityLectern lectern) {
        switch (lectern.getBlockState().getValue(BlockLectern.FACING)) {
            default:
                return 180;
            case EAST:
                return 90;
            case WEST:
                return -90;
            case SOUTH:
                return 0;
        }
    }

    public static class LecternRenderState extends net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState {
        public float rotation;
        public float pageFlip1;
        public float pageFlip2;
    }
}
