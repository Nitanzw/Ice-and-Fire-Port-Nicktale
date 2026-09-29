package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.iceandfire.client.model.DragonRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.nicktale.api.client.model.AdvancedModelBox;
import com.nicktale.api.client.model.TabulaModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.jetbrains.annotations.NotNull;

public class LayerDragonBanner extends RenderLayer<DragonRenderState, TabulaModel> {
    public LayerDragonBanner(RenderLayerParent<DragonRenderState, TabulaModel> renderer) {
        super(renderer);
    }

    @Override
    public void submit(@NotNull PoseStack poseStack, @NotNull SubmitNodeCollector collector, int lightCoords,
                       DragonRenderState state, float yRot, float xRot) {
        if (state.bannerItem.isEmpty()) {
            return;
        }
        AdvancedModelBox body = getParentModel().getCube("BodyUpper");
        if (body == null) {
            return;
        }

        float dragonScale = state.renderSize / 3.0F;
        if (dragonScale <= 0.0F) {
            return;
        }
        poseStack.pushPose();
        postRender(body, poseStack, 0.0625F);
        poseStack.translate(0.0F, -0.2F, 0.4F);
        poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
        poseStack.scale(1.0F / dragonScale, 1.0F / dragonScale, 1.0F / dragonScale);
        state.bannerItem.submit(poseStack, collector, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
        poseStack.popPose();
    }

    private static void postRender(AdvancedModelBox part, PoseStack poseStack, float scale) {
        poseStack.translate(part.rotationPointX * scale, part.rotationPointY * scale, part.rotationPointZ * scale);
        if (part.rotateAngleZ != 0.0F) {
            poseStack.mulPose(Axis.ZP.rotation(part.rotateAngleZ));
        }
        if (part.rotateAngleY != 0.0F) {
            poseStack.mulPose(Axis.YP.rotation(part.rotateAngleY));
        }
        if (part.rotateAngleX != 0.0F) {
            poseStack.mulPose(Axis.XP.rotation(part.rotateAngleX));
        }
    }
}
