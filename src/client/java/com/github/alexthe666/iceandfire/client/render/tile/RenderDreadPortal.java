package com.github.alexthe666.iceandfire.client.render.tile;

import com.github.alexthe666.iceandfire.entity.tile.TileEntityDreadPortal;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

/**
 * Draws the portal cube with vanilla's end-portal render type: the original two-texture shader needs a custom
 * pipeline in 26.2, so the dread textures below are kept for a future custom pipeline.
 */
public class RenderDreadPortal<T extends TileEntityDreadPortal> implements BlockEntityRenderer<T, TileRenderState> {
    public static final Identifier DREAD_PORTAL_BACKGROUND = Identifier.parse("iceandfire:textures/environment/dread_portal_background.png");
    public static final Identifier DREAD_PORTAL = Identifier.parse("iceandfire:textures/environment/dread_portal.png");

    public RenderDreadPortal(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public @NotNull TileRenderState createRenderState() {
        return new TileRenderState();
    }

    @Override
    public void extractRenderState(@NotNull T tile, @NotNull TileRenderState state, float partialTick, @NotNull Vec3 cameraPos, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(tile, state, partialTick, cameraPos, breakProgress);
        state.tile = tile;
        state.partialTick = partialTick;
    }

    @Override
    public void submit(@NotNull TileRenderState state, @NotNull PoseStack matrixStackIn, @NotNull SubmitNodeCollector collector, @NotNull CameraRenderState camera) {
        TileEntityDreadPortal tile = (TileEntityDreadPortal) state.tile;
        collector.submitCustomGeometry(matrixStackIn, this.renderType(), (pose, consumer) -> this.renderCube(tile, pose, consumer));
    }

    private void renderCube(TileEntityDreadPortal tileEntityIn, PoseStack.Pose pose, com.mojang.blaze3d.vertex.VertexConsumer consumer) {
        float f = 1.0F;
        float f1 = 1.0F;
        this.renderFace(tileEntityIn, pose, consumer, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, Direction.SOUTH);
        this.renderFace(tileEntityIn, pose, consumer, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, Direction.NORTH);
        this.renderFace(tileEntityIn, pose, consumer, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, Direction.EAST);
        this.renderFace(tileEntityIn, pose, consumer, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, Direction.WEST);
        this.renderFace(tileEntityIn, pose, consumer, 0.0F, 1.0F, f, f, 0.0F, 0.0F, 1.0F, 1.0F, Direction.DOWN);
        this.renderFace(tileEntityIn, pose, consumer, 0.0F, 1.0F, f1, f1, 1.0F, 1.0F, 0.0F, 0.0F, Direction.UP);
    }

    private void renderFace(TileEntityDreadPortal tile, PoseStack.Pose pose, com.mojang.blaze3d.vertex.VertexConsumer consumer, float x0, float x1, float y0, float y1, float z0, float z1, float z2, float z3, Direction direction) {
        if (tile.shouldRenderFace(direction)) {
            consumer.addVertex(pose, x0, y0, z0);
            consumer.addVertex(pose, x1, y0, z1);
            consumer.addVertex(pose, x1, y1, z2);
            consumer.addVertex(pose, x0, y1, z3);
        }
    }

    protected RenderType renderType() {
        return RenderTypes.endPortal();
    }

}
