package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.iceandfire.client.ClientProxy;
import com.github.alexthe666.iceandfire.client.model.DragonRenderState;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.nicktale.api.client.model.AdvancedModelBox;
import com.nicktale.api.client.model.TabulaModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import org.jetbrains.annotations.NotNull;

public class LayerDragonRider extends RenderLayer<DragonRenderState, TabulaModel> {
    private final boolean excludeDreadQueenMob;

    public LayerDragonRider(RenderLayerParent<DragonRenderState, TabulaModel> renderer, boolean excludeDreadQueenMob) {
        super(renderer);
        this.excludeDreadQueenMob = excludeDreadQueenMob;
    }

    @Override
    public void submit(@NotNull PoseStack poseStack, @NotNull SubmitNodeCollector collector, int lightCoords,
                       DragonRenderState state, float yRot, float xRot) {
        if (state.riders.isEmpty()) {
            return;
        }
        float dragonScale = state.renderSize / 3.0F;
        if (dragonScale <= 0.0F) {
            return;
        }
        EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        CameraRenderState camera = Minecraft.getInstance().gameRenderer.gameRenderState().levelRenderState.cameraRenderState;
        for (DragonRenderState.Rider rider : state.riders) {
            if (excludeDreadQueenMob && rider.dreadQueen()) {
                continue;
            }
            int animationTicks = state.animation == EntityDragonBase.ANIMATION_SHAKEPREY ? state.animationTick : 0;
            poseStack.pushPose();
            if (animationTicks == 0 || animationTicks >= 15) {
                translateToBody(poseStack);
            }
            if (rider.prey()) {
                if (animationTicks == 0 || animationTicks >= 15 || state.flying) {
                    translateToHead(poseStack);
                    offsetPerDragonType(state.dragonType, poseStack);
                    poseStack.translate(-0.15F * rider.renderState().boundingBoxHeight,
                            0.1F * dragonScale - 0.1F * rider.renderState().boundingBoxHeight,
                            -0.1F * dragonScale - 0.1F * rider.renderState().boundingBoxWidth);
                    if ((rider.renderState().boundingBoxHeight > rider.renderState().boundingBoxWidth || rider.humanoidModel())
                            && !rider.quadrupedModel() && !rider.horseModel()) {
                        poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
                        poseStack.mulPose(Axis.YP.rotationDegrees(45.0F));
                    } else {
                        poseStack.mulPose(Axis.XN.rotationDegrees(90.0F));
                    }
                } else {
                    poseStack.translate(0.0F, 0.555F * dragonScale, -0.5F * dragonScale);
                }
            } else {
                poseStack.translate(0.0F, -0.01F * dragonScale, -0.035F * dragonScale);
            }

            poseStack.pushPose();
            poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
            poseStack.mulPose(Axis.YP.rotationDegrees(rider.yaw() + 180.0F));
            poseStack.scale(1.0F / dragonScale, 1.0F / dragonScale, 1.0F / dragonScale);
            poseStack.translate(0.0F, -0.25F, 0.0F);
            ClientProxy.currentDragonRiders.remove(rider.uuid());
            EntityRenderer<?, ?> riderRenderer = dispatcher.getRenderer(rider.renderState());
            submitRider(riderRenderer, rider.renderState(), poseStack, collector, camera);
            ClientProxy.currentDragonRiders.add(rider.uuid());
            poseStack.popPose();
            poseStack.popPose();
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void submitRider(EntityRenderer renderer, EntityRenderState renderState, PoseStack poseStack,
                                    SubmitNodeCollector collector, CameraRenderState camera) {
        renderer.submit(renderState, poseStack, collector, camera);
    }

    protected void translateToBody(PoseStack poseStack) {
        postRender(getParentModel().getCube("BodyUpper"), poseStack);
        postRender(getParentModel().getCube("Neck1"), poseStack);
    }

    protected void translateToHead(PoseStack poseStack) {
        postRender(getParentModel().getCube("Neck2"), poseStack);
        postRender(getParentModel().getCube("Neck3"), poseStack);
        postRender(getParentModel().getCube("Head"), poseStack);
    }

    private static void postRender(AdvancedModelBox part, PoseStack poseStack) {
        if (part == null) {
            return;
        }
        poseStack.translate(part.rotationPointX * 0.0625F, part.rotationPointY * 0.0625F, part.rotationPointZ * 0.0625F);
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

    private static void offsetPerDragonType(int dragonType, PoseStack poseStack) {
        if (dragonType == 2) {
            poseStack.translate(0.1F, -0.2F, -0.1F);
        }
    }
}
