package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.iceandfire.client.model.IafRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;

/** Base for Ice and Fire layers: draws the parent model again with another texture or tint. */
public abstract class IafRenderLayer<S extends IafRenderState, M extends EntityModel<S>> extends RenderLayer<S, M> {

    protected IafRenderLayer(RenderLayerParent<S, M> renderer) {
        super(renderer);
    }

    @SuppressWarnings("unchecked")
    protected static <E extends Entity> E entityOf(IafRenderState state) {
        return (E) state.entity;
    }

    protected void submitParentModel(SubmitNodeCollector collector, PoseStack poseStack, int lightCoords, S state, RenderType renderType) {
        submitParentModel(collector, poseStack, lightCoords, state, renderType, -1);
    }

    protected void submitParentModel(SubmitNodeCollector collector, PoseStack poseStack, int lightCoords, S state, RenderType renderType, int color) {
        collector.submitModel(getParentModel(), state, poseStack, renderType, lightCoords,
                LivingEntityRenderer.getOverlayCoords(state, 0.0F), color, null, state.outlineColor, null);
    }

    protected void submitParentModel(SubmitNodeCollector collector, PoseStack poseStack, int lightCoords, S state, Identifier texture) {
        submitParentModel(collector, poseStack, lightCoords, state, RenderTypes.entityCutout(texture), -1);
    }

    /** Full-bright overlay such as eyes. */
    protected void submitGlowing(SubmitNodeCollector collector, PoseStack poseStack, S state, Identifier texture) {
        collector.submitModel(getParentModel(), state, poseStack, RenderTypes.eyes(texture), 15728880,
                LivingEntityRenderer.getOverlayCoords(state, 0.0F), -1, null, state.outlineColor, null);
    }
}
