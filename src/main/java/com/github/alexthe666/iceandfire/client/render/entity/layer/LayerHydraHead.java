package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.iceandfire.client.model.HydraRenderState;
import com.github.alexthe666.iceandfire.client.model.ModelHydraBody;
import com.github.alexthe666.iceandfire.client.model.ModelHydraHead;
import com.github.alexthe666.iceandfire.client.render.entity.RenderHydra;
import com.github.alexthe666.iceandfire.entity.EntityHydra;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;

public class LayerHydraHead extends IafRenderLayer<HydraRenderState, ModelHydraBody> {
    public static final Identifier TEXTURE_STONE = Identifier.parse("iceandfire:textures/models/hydra/stone.png");
    private static final float[][] TRANSLATE = new float[][]{
        {0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F},// 1 total heads
        {-0.15F, 0.15F, 0F, 0F, 0F, 0F, 0F, 0F, 0F},// 2 total heads
        {-0.3F, 0F, 0.3F, 0F, 0F, 0F, 0F, 0F, 0F},// 3 total heads
        {-0.4F, -0.1F, 0.1F, 0.4F, 0F, 0F, 0F, 0F, 0F},//etc...
        {-0.5F, -0.2F, 0F, 0.2F, 0.5F, 0F, 0F, 0F, 0F},
        {-0.7F, -0.4F, -0.2F, 0.2F, 0.4F, 0.7F, 0F, 0F, 0F},
        {-0.7F, -0.4F, -0.2F, 0, 0.2F, 0.4F, 0.7F, 0F, 0F},
        {-0.6F, -0.4F, -0.2F, -0.1F, 0.1F, 0.2F, 0.4F, 0.6F, 0F},
        {-0.6F, -0.4F, -0.2F, -0.1F, 0.0F, 0.1F, 0.2F, 0.4F, 0.6F},
    };
    private static final float[][] ROTATE = new float[][]{
        {0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F},// 1 total heads
        {10F, -10F, 0F, 0F, 0F, 0F, 0F, 0F, 0F},// 2 total heads
        {10F, 0F, -10F, 0F, 0F, 0F, 0F, 0F, 0F},// 3 total heads
        {25F, 10F, -10F, -25F, 0F, 0F, 0F, 0F, 0F},//etc...
        {30F, 15F, 0F, -15F, -30F, 0F, 0F, 0F, 0F},
        {40F, 25F, 5F, -5F, -25F, -40F, 0F, 0F, 0F},
        {40F, 30F, 15F, 0F, -15F, -30F, -40F, 0F, 0F},
        {45F, 30F, 20F, 5F, -5F, -20F, -30F, -45F, 0F},
        {50F, 37F, 25F, 15F, 0, -15F, -25F, -37F, -50F},
    };
    private static final ModelHydraHead[] modelArr = new ModelHydraHead[EntityHydra.HEADS];

    static {
        for (int i = 0; i < modelArr.length; i++) {
            modelArr[i] = new ModelHydraHead(i);
        }
    }

    public LayerHydraHead(RenderHydra renderer) {
        super(renderer);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, HydraRenderState state, float yRot, float xRot) {
        if (state.isInvisible) {
            return;
        }
        submitHydraHeads(getParentModel(), false, poseStack, collector, lightCoords, state);
    }

    public static void submitHydraHeads(ModelHydraBody model, boolean stone, PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, HydraRenderState state) {
        EntityHydra hydra = entityOf(state);
        poseStack.pushPose();
        int heads = hydra.getHeadCount();
        model.BodyUpper.translateRotate(poseStack);
        var type = RenderTypes.entityCutout(stone ? TEXTURE_STONE : getHeadTexture(hydra));
        for (int head = 1; head <= heads; head++) {
            poseStack.pushPose();
            float bodyWidth = 0.5F;
            poseStack.translate(TRANSLATE[heads - 1][head - 1] * bodyWidth, 0, 0);
            poseStack.mulPose(Axis.YP.rotationDegrees(ROTATE[heads - 1][head - 1]));
            collector.submitModel(modelArr[head - 1], state, poseStack, type, lightCoords,
                    net.minecraft.client.renderer.entity.LivingEntityRenderer.getOverlayCoords(state, 0.0F), -1, null, state.outlineColor, null);
            poseStack.popPose();
        }
        poseStack.popPose();
    }

    public static Identifier getHeadTexture(EntityHydra gorgon) {
        switch (gorgon.getVariant()) {
            default:
                return RenderHydra.TEXUTURE_0;
            case 1:
                return RenderHydra.TEXUTURE_1;
            case 2:
                return RenderHydra.TEXUTURE_2;
        }
    }
}
