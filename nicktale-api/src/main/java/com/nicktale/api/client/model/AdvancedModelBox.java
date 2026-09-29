package com.nicktale.api.client.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.core.Direction;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * A model bone: pivot point, rotation, scale, cubes and child bones, edited freely every frame.
 * Positions are in model pixels (1/16 block), angles in radians. {@link AdvancedEntityModel}
 * copies the whole hierarchy into vanilla {@link ModelPart}s so rendering stays vanilla.
 */
public class AdvancedModelBox {
    /** Stable name from the source model, used by procedural animators and model layers. */
    public String boxName = "";
    public float rotationPointX, rotationPointY, rotationPointZ;
    public float rotateAngleX, rotateAngleY, rotateAngleZ;
    public float scaleX = 1.0F, scaleY = 1.0F, scaleZ = 1.0F;
    /** Extra translation in blocks. */
    public float offsetX, offsetY, offsetZ;
    public float defaultRotationX, defaultRotationY, defaultRotationZ;
    public float defaultPositionX, defaultPositionY, defaultPositionZ;
    public float defaultScaleX = 1.0F, defaultScaleY = 1.0F, defaultScaleZ = 1.0F;
    public boolean mirror;
    /** Whether this bone's cubes are rendered; child bones remain independently visible. */
    public boolean showSelf = true;
    public boolean showModel = true;

    private final AdvancedEntityModel<?> model;
    private int textureOffsetX;
    private int textureOffsetY;
    private boolean scaleChildren;
    private AdvancedModelBox parent;
    private final List<AdvancedModelBox> children = new ArrayList<>();
    private final List<ModelPart.Cube> cubes = new ArrayList<>();

    public AdvancedModelBox(AdvancedEntityModel<?> model, int textureOffsetX, int textureOffsetY) {
        this.model = model;
        this.textureOffsetX = textureOffsetX;
        this.textureOffsetY = textureOffsetY;
        model.register(this);
    }

    public AdvancedModelBox(AdvancedEntityModel<?> model) {
        this(model, 0, 0);
    }

    public AdvancedModelBox setTextureOffset(int x, int y) {
        this.textureOffsetX = x;
        this.textureOffsetY = y;
        return this;
    }

    public void setPos(float x, float y, float z) {
        this.rotationPointX = x;
        this.rotationPointY = y;
        this.rotationPointZ = z;
    }

    public void setRotationPoint(float x, float y, float z) {
        setPos(x, y, z);
    }

    public void setRotationAngle(float x, float y, float z) {
        this.rotateAngleX = x;
        this.rotateAngleY = y;
        this.rotateAngleZ = z;
    }

    public void setScale(float x, float y, float z) {
        this.scaleX = x;
        this.scaleY = y;
        this.scaleZ = z;
    }

    /** When true the scale also applies to child bones, otherwise only to this bone's cubes. */
    public void setShouldScaleChildren(boolean scaleChildren) {
        this.scaleChildren = scaleChildren;
    }

    public boolean shouldScaleChildren() {
        return scaleChildren;
    }

    public AdvancedModelBox addBox(float x, float y, float z, float w, float h, float d) {
        return addBox(x, y, z, w, h, d, 0.0F);
    }

    public AdvancedModelBox addBox(float x, float y, float z, float w, float h, float d, float delta) {
        return addBox(x, y, z, w, h, d, delta, delta, delta);
    }

    /** Box with an explicit mirror flag (applies to this box only). */
    public AdvancedModelBox addBox(float x, float y, float z, float w, float h, float d, boolean mirrored) {
        boolean previous = this.mirror;
        this.mirror = mirrored;
        addBox(x, y, z, w, h, d, 0.0F);
        this.mirror = previous;
        return this;
    }

    /** Box with a different growth on each axis. */
    public AdvancedModelBox addBox(float x, float y, float z, float w, float h, float d,
                                   float deltaX, float deltaY, float deltaZ) {
        Set<Direction> faces = EnumSet.allOf(Direction.class);
        cubes.add(new ModelPart.Cube(textureOffsetX, textureOffsetY, x, y, z, w, h, d,
                deltaX, deltaY, deltaZ, mirror, model.texWidth, model.texHeight, faces));
        return this;
    }

    /** Applies this bone's pivot, rotation and scale to the pose stack (for item and arm attachments). */
    public void translateRotate(com.mojang.blaze3d.vertex.PoseStack poseStack) {
        poseStack.translate((rotationPointX + offsetX * 16.0F) / 16.0F,
                (rotationPointY + offsetY * 16.0F) / 16.0F,
                (rotationPointZ + offsetZ * 16.0F) / 16.0F);
        if (rotateAngleX != 0.0F || rotateAngleY != 0.0F || rotateAngleZ != 0.0F) {
            poseStack.mulPose(new org.joml.Quaternionf().rotationZYX(rotateAngleZ, rotateAngleY, rotateAngleX));
        }
        if (scaleX != 1.0F || scaleY != 1.0F || scaleZ != 1.0F) {
            poseStack.scale(scaleX, scaleY, scaleZ);
        }
    }

    public void addChild(AdvancedModelBox child) {
        if (child.parent != null) {
            child.parent.children.remove(child);
        }
        child.parent = this;
        children.add(child);
    }

    /** Override to draw only the children of this bone and skip its own cubes. */
    public boolean hidesOwnCubes() {
        return false;
    }

    public AdvancedModelBox getParent() {
        return parent;
    }

    public List<AdvancedModelBox> getChildren() {
        return children;
    }

    List<ModelPart.Cube> cubes() {
        return cubes;
    }

    /** Stores the current transform as the pose that {@link #resetToDefaultPose()} returns to. */
    public void updateDefaultPose() {
        defaultRotationX = rotateAngleX;
        defaultRotationY = rotateAngleY;
        defaultRotationZ = rotateAngleZ;
        defaultPositionX = rotationPointX;
        defaultPositionY = rotationPointY;
        defaultPositionZ = rotationPointZ;
        defaultScaleX = scaleX;
        defaultScaleY = scaleY;
        defaultScaleZ = scaleZ;
    }

    public void resetToDefaultPose() {
        rotateAngleX = defaultRotationX;
        rotateAngleY = defaultRotationY;
        rotateAngleZ = defaultRotationZ;
        rotationPointX = defaultPositionX;
        rotationPointY = defaultPositionY;
        rotationPointZ = defaultPositionZ;
        scaleX = defaultScaleX;
        scaleY = defaultScaleY;
        scaleZ = defaultScaleZ;
    }
}
