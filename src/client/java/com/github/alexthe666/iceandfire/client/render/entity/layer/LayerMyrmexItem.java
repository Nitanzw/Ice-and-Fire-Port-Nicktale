package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.iceandfire.client.model.ModelMyrmexBase;
import com.github.alexthe666.iceandfire.client.model.MyrmexRenderState;
import com.github.alexthe666.iceandfire.client.render.entity.RenderMyrmexBase;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.jetbrains.annotations.NotNull;

public class LayerMyrmexItem extends RenderLayer<MyrmexRenderState, EntityModel<MyrmexRenderState>> {

    protected final RenderMyrmexBase livingEntityRenderer;

    public LayerMyrmexItem(RenderMyrmexBase livingEntityRendererIn) {
        super(livingEntityRendererIn);
        this.livingEntityRenderer = livingEntityRendererIn;
    }

    @Override
    public void submit(@NotNull PoseStack poseStack, @NotNull SubmitNodeCollector collector, int light, @NotNull MyrmexRenderState state, float yRot, float xRot) {
        if (state.heldItem.isEmpty() || !(this.getParentModel() instanceof ModelMyrmexBase<?> myrmexModel)) {
            return;
        }
        poseStack.pushPose();
        myrmexModel.postRenderArm(0, poseStack);
        poseStack.translate(0F, 0.3F, -1.6F);
        if (state.holdsBlockItem) {
            poseStack.translate(0F, 0, 0.2F);
        } else {
            poseStack.translate(0F, 0.2F, 0.3F);
        }
        poseStack.mulPose(Axis.XP.rotationDegrees(160.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        state.heldItem.submit(poseStack, collector, light, OverlayTexture.NO_OVERLAY, state.outlineColor);
        poseStack.popPose();
    }
}
