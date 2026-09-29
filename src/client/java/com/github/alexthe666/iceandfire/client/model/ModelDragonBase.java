package com.github.alexthe666.iceandfire.client.model;

import com.nicktale.api.client.model.AdvancedEntityModel;
import com.nicktale.api.client.model.AdvancedModelBox;
import com.nicktale.api.client.model.ModelAnimator;
import com.github.alexthe666.iceandfire.entity.util.ChainBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

public abstract class ModelDragonBase<S extends EntityRenderState> extends AdvancedEntityModel<S> {
    /** Baby flag copied from the render state before every animate() call. */
    protected boolean young;

    @Override
    public void setupAnim(S state) {
        this.young = state instanceof LivingEntityRenderState living && living.isBaby;
        super.setupAnim(state);
    }

    public void rotate(ModelAnimator animator, AdvancedModelBox model, float x, float y, float z) {
        animator.rotate(model, (float) Math.toRadians(x), (float) Math.toRadians(y), (float) Math.toRadians(z));
    }

    public void rotateMinus(ModelAnimator animator, AdvancedModelBox model, float x, float y, float z) {
        animator.rotate(model, (float) Math.toRadians(x) - model.defaultRotationX, (float) Math.toRadians(y) - model.defaultRotationY, (float) Math.toRadians(z) - model.defaultRotationZ);
    }

    public void progressRotationInterp(AdvancedModelBox model, float progress, float rotX, float rotY, float rotZ, float max) {
        model.rotateAngleX += progress * (rotX - model.defaultRotationX) / max;
        model.rotateAngleY += progress * (rotY - model.defaultRotationY) / max;
        model.rotateAngleZ += progress * (rotZ - model.defaultRotationZ) / max;
    }

    public void progressPositionInterp(AdvancedModelBox model, float progress, float x, float y, float z, float max) {
        model.rotationPointX += progress * (x) / max;
        model.rotationPointY += progress * (y) / max;
        model.rotationPointZ += progress * (z) / max;
    }

    public void progressRotation(AdvancedModelBox model, float progress, float rotX, float rotY, float rotZ) {
        model.rotateAngleX += progress * (rotX - model.defaultRotationX) / 20.0F;
        model.rotateAngleY += progress * (rotY - model.defaultRotationY) / 20.0F;
        model.rotateAngleZ += progress * (rotZ - model.defaultRotationZ) / 20.0F;
    }

    public void progressRotationPrev(AdvancedModelBox model, float progress, float rotX, float rotY, float rotZ) {
        model.rotateAngleX += progress * (rotX) / 20.0F;
        model.rotateAngleY += progress * (rotY) / 20.0F;
        model.rotateAngleZ += progress * (rotZ) / 20.0F;
    }

    public void progressPosition(AdvancedModelBox model, float progress, float x, float y, float z) {
        model.rotationPointX += progress * (x - model.defaultPositionX) / 20.0F;
        model.rotationPointY += progress * (y - model.defaultPositionY) / 20.0F;
        model.rotationPointZ += progress * (z - model.defaultPositionZ) / 20.0F;
    }

    public void progressPositionPrev(AdvancedModelBox model, float progress, float x, float y, float z) {
        model.rotationPointX += progress * x / 20.0F;
        model.rotationPointY += progress * y / 20.0F;
        model.rotationPointZ += progress * z / 20.0F;
    }

    protected static void applyChainYawToY(ChainBuffer buffer, AdvancedModelBox... boxes) {
        applyChainRotation(buffer == null ? 0.0F : buffer.getInterpolatedYawVariation(Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false)), boxes, RotationAxis.Y);
    }

    protected static void applyChainYawToZ(ChainBuffer buffer, AdvancedModelBox... boxes) {
        applyChainRotation(buffer == null ? 0.0F : buffer.getInterpolatedYawVariation(Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false)), boxes, RotationAxis.Z);
    }

    protected static void applyChainPitchToX(ChainBuffer buffer, AdvancedModelBox... boxes) {
        applyChainRotation(buffer == null ? 0.0F : buffer.getInterpolatedPitchVariation(Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false)), boxes, RotationAxis.X);
    }

    private static void applyChainRotation(float degrees, AdvancedModelBox[] boxes, RotationAxis axis) {
        if (degrees == 0.0F || boxes.length == 0) {
            return;
        }
        float radiansPerPart = (float) Math.toRadians(degrees) / boxes.length;
        for (AdvancedModelBox box : boxes) {
            switch (axis) {
                case X -> box.rotateAngleX += radiansPerPart;
                case Y -> box.rotateAngleY += radiansPerPart;
                case Z -> box.rotateAngleZ += radiansPerPart;
            }
        }
    }

    private enum RotationAxis {
        X,
        Y,
        Z
    }
}
