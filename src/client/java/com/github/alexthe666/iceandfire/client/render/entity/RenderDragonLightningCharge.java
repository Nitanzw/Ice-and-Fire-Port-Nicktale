package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelDreadLichSkull;
import com.github.alexthe666.iceandfire.client.model.SimpleEntityRenderState;
import com.github.alexthe666.iceandfire.entity.EntityDragonLightningCharge;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public class RenderDragonLightningCharge extends EntityRenderer<EntityDragonLightningCharge, SimpleEntityRenderState> {

    public static final Identifier TEXTURE = Identifier.parse("iceandfire:textures/models/lightningdragon/charge.png");
    public static final Identifier TEXTURE_CORE = Identifier.parse("iceandfire:textures/models/lightningdragon/charge_core.png");
    private static final ModelDreadLichSkull MODEL_SPIRIT = new ModelDreadLichSkull();

    public RenderDragonLightningCharge(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public @NotNull SimpleEntityRenderState createRenderState() {
        return new SimpleEntityRenderState();
    }

    @Override
    public void extractRenderState(@NotNull EntityDragonLightningCharge entity, @NotNull SimpleEntityRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.entity = entity;
        state.partialTick = partialTick;
        state.yRot = entity.yRotO + (entity.getYRot() - entity.yRotO) * partialTick;
    }

    @Override
    public void submit(@NotNull SimpleEntityRenderState state, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector collector, @NotNull CameraRenderState camera) {
        float f = (float) state.entity.tickCount + state.partialTick;
        float yaw = state.yRot;
        int light = state.lightCoords;

        poseStack.pushPose();
        poseStack.translate(0F, 0.5F, 0F);
        poseStack.translate(0F, -0.25F, 0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(yaw - 180.0F));
        poseStack.mulPose(Axis.XP.rotationDegrees(f * 20.0F));
        poseStack.translate(0F, 0.25F, 0F);
        collector.submitModel(MODEL_SPIRIT, state, poseStack, RenderTypes.eyes(TEXTURE_CORE), light,
            OverlayTexture.NO_OVERLAY, -1, null, state.outlineColor, null);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(0F, 0.5F, 0F);
        poseStack.translate(0F, -0.25F, 0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(yaw - 180.0F));
        poseStack.mulPose(Axis.XP.rotationDegrees(f * 15.0F));
        poseStack.translate(0F, 0.25F, 0F);
        poseStack.scale(1.5F, 1.5F, 1.5F);
        collector.submitModel(MODEL_SPIRIT, state, poseStack, RenderTypes.energySwirl(TEXTURE, f * 0.01F, f * 0.01F), light,
            OverlayTexture.NO_OVERLAY, -1, null, state.outlineColor, null);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(0F, 0.75F, 0F);
        poseStack.translate(0F, -0.25F, 0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(yaw - 180.0F));
        poseStack.mulPose(Axis.XP.rotationDegrees(f * 10.0F));
        poseStack.translate(0F, 0.75F, 0F);
        poseStack.scale(2.5F, 2.5F, 2.5F);
        collector.submitModel(MODEL_SPIRIT, state, poseStack, RenderTypes.energySwirl(TEXTURE, f * 0.01F, f * 0.01F), light,
            OverlayTexture.NO_OVERLAY, -1, null, state.outlineColor, null);
        poseStack.popPose();

        super.submit(state, poseStack, collector, camera);
    }
}
