package com.github.alexthe666.iceandfire.client.render.tile;

import com.github.alexthe666.iceandfire.client.model.ModelDragonEgg;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityEggInIce;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class RenderEggInIce<T extends TileEntityEggInIce> implements BlockEntityRenderer<T, TileRenderState> {

    private final ModelDragonEgg model = new ModelDragonEgg();

    public RenderEggInIce(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public @NotNull TileRenderState createRenderState() {
        return new TileRenderState();
    }

    @Override
    public void extractRenderState(@NotNull T egg, @NotNull TileRenderState state, float partialTick, @NotNull Vec3 cameraPos, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(egg, state, partialTick, cameraPos, breakProgress);
        state.tile = egg;
        state.partialTick = partialTick;
    }

    @Override
    public void submit(@NotNull TileRenderState state, @NotNull PoseStack matrixStackIn, @NotNull SubmitNodeCollector collector, @NotNull CameraRenderState camera) {
        TileEntityEggInIce egg = (TileEntityEggInIce) state.tile;
        if (egg.type != null) {
            PoseStates.Egg eggState = new PoseStates.Egg();
            eggState.pose = () -> model.renderFrozen(egg);
            matrixStackIn.pushPose();
            matrixStackIn.translate(0.5, -0.8F, 0.5F);
            collector.submitModel(model, eggState, matrixStackIn, RenderPodium.getEggTexture(egg.type), state.lightCoords, OverlayTexture.NO_OVERLAY, -1, null, 0, null);
            matrixStackIn.popPose();
        }
    }

}
