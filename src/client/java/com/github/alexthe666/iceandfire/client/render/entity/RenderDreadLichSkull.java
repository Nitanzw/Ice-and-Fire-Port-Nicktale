package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelDreadLichSkull;
import com.github.alexthe666.iceandfire.client.model.SimpleEntityRenderState;
import com.github.alexthe666.iceandfire.entity.EntityDreadLichSkull;
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

public class RenderDreadLichSkull extends EntityRenderer<EntityDreadLichSkull, SimpleEntityRenderState> {

    public static final Identifier TEXTURE = Identifier.parse("iceandfire:textures/models/dread/dread_lich_skull.png");
    private static final ModelDreadLichSkull MODEL_SPIRIT = new ModelDreadLichSkull();

    public RenderDreadLichSkull(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public @NotNull SimpleEntityRenderState createRenderState() {
        return new SimpleEntityRenderState();
    }

    @Override
    public void extractRenderState(@NotNull EntityDreadLichSkull entity, @NotNull SimpleEntityRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.entity = entity;
        state.partialTick = partialTick;
        state.yRot = entity.yRotO + (entity.getYRot() - entity.yRotO) * partialTick;
    }

    @Override
    public void submit(@NotNull SimpleEntityRenderState state, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector collector, @NotNull CameraRenderState camera) {
        if (state.entity != null && state.entity.tickCount > 3) {
            poseStack.pushPose();
            poseStack.scale(1.5F, -1.5F, 1.5F);
            poseStack.mulPose(Axis.YP.rotationDegrees(state.yRot - 180.0F));
            collector.submitModel(MODEL_SPIRIT, state, poseStack, RenderTypes.eyes(TEXTURE), 240,
                OverlayTexture.NO_OVERLAY, -1, null, state.outlineColor, null);
            poseStack.popPose();
        }
        super.submit(state, poseStack, collector, camera);
    }
}
