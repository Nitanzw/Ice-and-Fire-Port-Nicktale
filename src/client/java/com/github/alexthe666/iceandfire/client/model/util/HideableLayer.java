package com.github.alexthe666.iceandfire.client.model.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import org.jetbrains.annotations.NotNull;

public class HideableLayer<S extends EntityRenderState, M extends EntityModel<S>, C extends RenderLayer<S, M>> extends RenderLayer<S, M> {

    public boolean hidden;
    C layerRenderer;

    public HideableLayer(C layerRenderer, RenderLayerParent<S, M> entityRendererIn) {
        super(entityRendererIn);
        hidden = false;
        this.layerRenderer = layerRenderer;
    }

    @Override
    public void submit(@NotNull PoseStack poseStack, @NotNull SubmitNodeCollector collector, int light, @NotNull S state, float yRot, float xRot) {
        if (!hidden)
            layerRenderer.submit(poseStack, collector, light, state, yRot, xRot);
    }
}
