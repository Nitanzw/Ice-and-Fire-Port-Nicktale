package com.github.alexthe666.iceandfire.client.render.tile;

import com.github.alexthe666.iceandfire.entity.tile.TileEntityDreadSpawner;
import net.minecraft.client.renderer.blockentity.SpawnerRenderer;
import net.minecraft.client.renderer.blockentity.state.SpawnerRenderState;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BaseSpawner;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class RenderDreadSpawner<T extends TileEntityDreadSpawner> implements BlockEntityRenderer<T, SpawnerRenderState> {

    private final EntityRenderDispatcher entityRenderer;

    public RenderDreadSpawner(BlockEntityRendererProvider.Context context) {
        this.entityRenderer = context.entityRenderer();
    }

    @Override
    public @NotNull SpawnerRenderState createRenderState() {
        return new SpawnerRenderState();
    }

    @Override
    public void extractRenderState(@NotNull T tile, @NotNull SpawnerRenderState state, float partialTick, @NotNull Vec3 cameraPos, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(tile, state, partialTick, cameraPos, breakProgress);
        if (tile.getLevel() == null) {
            return;
        }
        BaseSpawner spawner = tile.getSpawner();
        Entity entity = spawner.getOrCreateDisplayEntity(tile.getLevel(), tile.getBlockPos());
        if (entity != null) {
            state.displayEntity = this.entityRenderer.extractEntity(entity, partialTick);
            state.spin = (float) Mth.lerp(partialTick, spawner.getOSpin(), spawner.getSpin()) * 10.0F;
            float scale = 0.53125F;
            float largest = Math.max(entity.getBbWidth(), entity.getBbHeight());
            if ((double) largest > 1.0D) {
                scale /= largest;
            }
            state.scale = scale;
        }
    }

    @Override
    public void submit(@NotNull SpawnerRenderState state, @NotNull PoseStack matrixStackIn, @NotNull SubmitNodeCollector collector, @NotNull CameraRenderState camera) {
        if (state.displayEntity != null) {
            matrixStackIn.pushPose();
            matrixStackIn.translate(0.5D, 0.0D, 0.5D);
            SpawnerRenderer.submitEntityInSpawner(matrixStackIn, collector, state.displayEntity, this.entityRenderer, state.spin, state.scale, camera);
            matrixStackIn.popPose();
        }
    }
}
