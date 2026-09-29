package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.iceandfire.client.model.ModelPixie;
import com.github.alexthe666.iceandfire.client.model.PixieRenderState;
import com.github.alexthe666.iceandfire.client.render.entity.RenderPixie;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.texture.OverlayTexture;

/** Item carried by a pixie, drawn at its right hand. */
public class LayerPixieItem extends IafRenderLayer<PixieRenderState, ModelPixie> {

    public LayerPixieItem(RenderPixie renderer) {
        super(renderer);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, PixieRenderState state, float yRot, float xRot) {
        if (state.heldItemState.isEmpty() || !(this.getParentModel() instanceof ModelPixie pixie)) {
            return;
        }
        poseStack.pushPose();
        pixie.Right_Arm.translateRotate(poseStack);
        poseStack.translate(0.0F, 0.6F, 0.0F);
        poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
        poseStack.scale(0.6F, 0.6F, 0.6F);
        state.heldItemState.submit(poseStack, collector, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
        poseStack.popPose();
    }
}
