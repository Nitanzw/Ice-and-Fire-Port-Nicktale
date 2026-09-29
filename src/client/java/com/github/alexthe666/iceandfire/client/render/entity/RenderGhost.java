package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.BipedRenderState;
import com.github.alexthe666.iceandfire.client.model.ModelGhost;
import com.github.alexthe666.iceandfire.entity.EntityGhost;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RenderGhost extends IafBipedRenderer<EntityGhost, ModelGhost> {

    public static final Identifier TEXTURE_0 = Identifier.parse("iceandfire:textures/models/ghost/ghost_white.png");
    public static final Identifier TEXTURE_1 = Identifier.parse("iceandfire:textures/models/ghost/ghost_blue.png");
    public static final Identifier TEXTURE_2 = Identifier.parse("iceandfire:textures/models/ghost/ghost_green.png");
    public static final Identifier TEXTURE_SHOPPING_LIST = Identifier.parse("iceandfire:textures/models/ghost/haunted_shopping_list.png");

    public RenderGhost(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ModelGhost(0.0F), 0.0F);
    }

    public static Identifier getGhostOverlayForType(int ghost) {
        switch (ghost) {
            case 1:
                return TEXTURE_1;
            case 2:
                return TEXTURE_2;
            case -1:
                return TEXTURE_SHOPPING_LIST;
            default:
                return TEXTURE_0;
        }
    }

    @Override
    protected void extract(EntityGhost entity, BipedRenderState state, float partialTick) {
        state.alpha = getAlphaForRender(entity, partialTick);
        state.ghostDaytime = entity.isDaytimeMode();
        state.ghostShoppingList = entity.isHauntedShoppingList();
        state.lightCoords = 240;
    }

    @Override
    public void submit(@NotNull BipedRenderState state, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector collector, @NotNull CameraRenderState camera) {
        if (state.ghostShoppingList) {
            submitShoppingList(state, poseStack, collector);
            return;
        }
        super.submit(state, poseStack, collector, camera);
    }

    private void submitShoppingList(BipedRenderState state, PoseStack poseStack, SubmitNodeCollector collector) {
        if (state.isInvisible) {
            return;
        }
        int alpha = (int) (state.alpha * 255);
        RenderType renderType = RenderTypes.entityTranslucent(TEXTURE_SHOPPING_LIST);
        poseStack.pushPose();
        poseStack.translate(0, 0.8F + Mth.sin(state.ageInTicks * 0.15F) * 0.1F, 0);
        poseStack.scale(0.6F, 0.6F, 0.6F);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        collector.submitCustomGeometry(poseStack, renderType, (pose, consumer) -> {
            quad(pose, consumer, alpha, 1F, 0.5F, 0.0F);
        });
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        collector.submitCustomGeometry(poseStack, renderType, (pose, consumer) -> {
            quad(pose, consumer, alpha, 0.0F, 0.5F, 0.0F);
        });
        poseStack.popPose();
    }

    private static void quad(com.mojang.blaze3d.vertex.PoseStack.Pose pose, com.mojang.blaze3d.vertex.VertexConsumer consumer, int alpha, float u0, float u1, float unused) {
        vertex(pose, consumer, alpha, -1, -2, u0, 0.0F);
        vertex(pose, consumer, alpha, 1, -2, u1, 0.0F);
        vertex(pose, consumer, alpha, 1, 2, u1, 1F);
        vertex(pose, consumer, alpha, -1, 2, u0, 1F);
    }

    private static void vertex(com.mojang.blaze3d.vertex.PoseStack.Pose pose, com.mojang.blaze3d.vertex.VertexConsumer consumer, int alpha, int x, int y, float u, float v) {
        consumer.addVertex(pose, (float) x, (float) y, 0.0F).setColor(255, 255, 255, alpha).setUv(u, v)
            .setOverlay(OverlayTexture.NO_OVERLAY).setLight(240).setNormal(pose, 0.0F, 1.0F, 0.0F);
    }

    @Override
    protected @Nullable RenderType getRenderType(BipedRenderState state, boolean isVisible, boolean translucent, boolean glowing) {
        return isVisible || translucent ? RenderTypes.entityTranslucent(getTextureLocation(state)) : null;
    }

    @Override
    protected int getModelTint(BipedRenderState state) {
        return ((int) (Mth.clamp(state.alpha, 0F, 1F) * 255) << 24) | 0xFFFFFF;
    }

    public float getAlphaForRender(EntityGhost entityIn, float partialTicks) {
        if (entityIn.isDaytimeMode()) {
            return Mth.clamp((101 - Math.min(entityIn.getDaytimeCounter(), 100)) / 100F, 0, 1);
        }
        return Mth.clamp((Mth.sin((entityIn.tickCount + partialTicks) * 0.1F) + 1F) * 0.5F + 0.1F, 0F, 1F);
    }

    @Override
    protected float getFlipDegrees() {
        return 0.0F;
    }

    @Override
    protected Identifier textureFor(EntityGhost ghost) {
        switch (ghost.getColor()) {
            case 1:
                return TEXTURE_1;
            case 2:
                return TEXTURE_2;
            case -1:
                return TEXTURE_SHOPPING_LIST;
            default:
                return TEXTURE_0;
        }
    }
}
