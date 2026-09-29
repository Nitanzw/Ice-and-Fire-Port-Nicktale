package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.DragonRenderState;
import com.github.alexthe666.iceandfire.client.particle.LightningBoltData;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityLightningDragon;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.nicktale.api.client.model.TabulaModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RenderLightningDragon extends RenderDragonBase {

    public RenderLightningDragon(EntityRendererProvider.Context context, TabulaModel model, int dragonType) {
        super(context, model, dragonType);
    }

    @Override
    public void extractRenderState(EntityDragonBase entity, DragonRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        if (entity instanceof EntityLightningDragon dragon) {
            state.hasLightningTarget = dragon.hasLightningTarget();
            if (state.hasLightningTarget) {
                state.lightningStart = dragon.getHeadPosition();
                state.lightningEnd = new Vec3(dragon.getLightningTargetX(), dragon.getLightningTargetY(), dragon.getLightningTargetZ());
                state.lightningScale = dragon.getScale();
            }
        }
    }

    @Override
    public boolean shouldRender(@NotNull EntityDragonBase livingEntityIn, @NotNull Frustum camera,
                                double camX, double camY, double camZ, float partialTicks) {
        if (super.shouldRender(livingEntityIn, camera, camX, camY, camZ, partialTicks)) {
            return true;
        }
        if (livingEntityIn instanceof EntityLightningDragon lightningDragon && lightningDragon.hasLightningTarget()) {
            Vec3 start = lightningDragon.getHeadPosition();
            Vec3 end = new Vec3(lightningDragon.getLightningTargetX(), lightningDragon.getLightningTargetY(), lightningDragon.getLightningTargetZ());
            return camera.isVisible(new AABB(start, end).inflate(0.5D));
        }
        return false;
    }

    @Override
    public void submit(DragonRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        super.submit(state, poseStack, collector, camera);
        if (!state.hasLightningTarget) {
            return;
        }
        int renderDistance = Minecraft.getInstance().options.renderDistance().get() * 16;
        if (state.distanceToCameraSq > Math.pow(Math.max(256.0F, renderDistance), 2)) {
            return;
        }

        LightningBoltData bolt = new LightningBoltData(LightningBoltData.BoltRenderInfo.ELECTRICITY,
                state.lightningStart, state.lightningEnd, 15)
                .size(0.05F * boundedScale(0.4F * state.lightningScale, 0.5F, 2.0F))
                .lifespan(4)
                .spawn(LightningBoltData.SpawnFunction.NO_DELAY);
        List<LightningBoltData.BoltQuads> quads = bolt.generate();
        PoseStack worldPose = new PoseStack();
        worldPose.translate(-camera.pos.x(), -camera.pos.y(), -camera.pos.z());
        collector.submitCustomGeometry(worldPose, RenderTypes.lightning(), (pose, buffer) -> submitBolt(bolt, quads, pose, buffer));
    }

    private static void submitBolt(LightningBoltData bolt, List<LightningBoltData.BoltQuads> quads,
                                   PoseStack.Pose pose, VertexConsumer buffer) {
        var color = bolt.getColor();
        for (LightningBoltData.BoltQuads quad : quads) {
            for (Vec3 point : quad.getVecs()) {
                buffer.addVertex(pose, (float) point.x, (float) point.y, (float) point.z)
                        .setColor(color.x(), color.y(), color.z(), color.w());
            }
        }
    }

    private static float boundedScale(float scale, float min, float max) {
        return min + scale * (max - min);
    }
}
