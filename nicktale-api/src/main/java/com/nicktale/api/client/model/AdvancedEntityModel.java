package com.nicktale.api.client.model;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Base for models built from {@link AdvancedModelBox} bones. Subclasses fill {@link #texWidth}
 * and {@link #texHeight}, create their boxes, call {@link #updateDefaultPose()} once and pose
 * them in {@link #animate(EntityRenderState)}. The bones are then mirrored into vanilla model
 * parts, so any vanilla renderer or layer can draw this model.
 */
public abstract class AdvancedEntityModel<S extends EntityRenderState> extends EntityModel<S> {
    public int texWidth = 64;
    public int texHeight = 32;

    private final Map<String, ModelPart> rootChildren;
    private final List<AdvancedModelBox> boxes = new ArrayList<>();
    private final Map<AdvancedModelBox, ModelPart[]> parts = new IdentityHashMap<>();
    private boolean baked;

    protected AdvancedEntityModel() {
        this(new LinkedHashMap<>());
    }

    private AdvancedEntityModel(Map<String, ModelPart> rootChildren) {
        super(new ModelPart(List.of(), rootChildren));
        this.rootChildren = rootChildren;
    }

    void register(AdvancedModelBox box) {
        boxes.add(box);
        baked = false;
    }

    public List<AdvancedModelBox> getAllBoxes() {
        return boxes;
    }

    public void updateDefaultPose() {
        for (AdvancedModelBox box : boxes) {
            box.updateDefaultPose();
        }
    }

    public void resetToDefaultPose() {
        for (AdvancedModelBox box : boxes) {
            box.resetToDefaultPose();
        }
    }

    /** Poses the bones for this frame. */
    protected abstract void animate(S state);

    @Override
    public void setupAnim(S state) {
        animate(state);
        sync();
    }

    /**
     * Returns the posed model part of a single bone (with its children), independent of the bone's ancestors, so a
     * bone can be drawn on its own (mob skulls, held pieces). Poses are synced before returning.
     */
    public ModelPart getPart(AdvancedModelBox box) {
        sync();
        ModelPart[] pair = parts.get(box);
        return pair == null ? null : pair[0];
    }

    private void bake() {
        rootChildren.clear();
        parts.clear();
        int index = 0;
        for (AdvancedModelBox box : boxes) {
            if (box.getParent() == null) {
                rootChildren.put("b" + index++, build(box));
            }
        }
        baked = true;
    }

    private ModelPart build(AdvancedModelBox box) {
        Map<String, ModelPart> kids = new LinkedHashMap<>();
        ModelPart self = new ModelPart(box.cubes(), new LinkedHashMap<>());
        kids.put("self", self);
        int i = 0;
        for (AdvancedModelBox child : box.getChildren()) {
            kids.put("c" + i++, build(child));
        }
        ModelPart pivot = new ModelPart(List.of(), kids);
        parts.put(box, new ModelPart[]{pivot, self});
        return pivot;
    }

    private void sync() {
        if (!baked) {
            bake();
        }
        for (Map.Entry<AdvancedModelBox, ModelPart[]> entry : parts.entrySet()) {
            AdvancedModelBox box = entry.getKey();
            ModelPart pivot = entry.getValue()[0];
            ModelPart self = entry.getValue()[1];
            pivot.setPos(box.rotationPointX + box.offsetX * 16.0F,
                    box.rotationPointY + box.offsetY * 16.0F,
                    box.rotationPointZ + box.offsetZ * 16.0F);
            pivot.setRotation(box.rotateAngleX, box.rotateAngleY, box.rotateAngleZ);
            pivot.visible = box.showModel;
            self.visible = box.showSelf && !box.hidesOwnCubes();
            if (box.shouldScaleChildren()) {
                pivot.xScale = box.scaleX;
                pivot.yScale = box.scaleY;
                pivot.zScale = box.scaleZ;
                self.xScale = self.yScale = self.zScale = 1.0F;
            } else {
                pivot.xScale = pivot.yScale = pivot.zScale = 1.0F;
                self.xScale = box.scaleX;
                self.yScale = box.scaleY;
                self.zScale = box.scaleZ;
            }
        }
    }

    // ---- pose helpers: angles in radians, f = swing time, f1 = swing amount ----

    public void setRotateAngle(AdvancedModelBox box, float x, float y, float z) {
        box.setRotationAngle(x, y, z);
    }

    /** Adds a keyframe rotation whose arguments are expressed in degrees. */
    public void rotate(ModelAnimator animator, AdvancedModelBox box, float x, float y, float z) {
        animator.rotate(box, (float) Math.toRadians(x), (float) Math.toRadians(y), (float) Math.toRadians(z));
    }

    /** Rotates to the requested absolute angles relative to the stored default pose. */
    public void rotateMinus(ModelAnimator animator, AdvancedModelBox box, float x, float y, float z) {
        animator.rotate(box,
                (float) Math.toRadians(x) - box.defaultRotationX,
                (float) Math.toRadians(y) - box.defaultRotationY,
                (float) Math.toRadians(z) - box.defaultRotationZ);
    }

    private static float wave(float speed, float degree, boolean invert, float offset, float weight, float f, float f1) {
        float value = (float) Math.cos(f * speed + offset) * degree * f1 + weight * f1;
        return invert ? -value : value;
    }

    /** Rotates around X. */
    public void walk(AdvancedModelBox box, float speed, float degree, boolean invert, float offset, float weight, float f, float f1) {
        box.rotateAngleX += wave(speed, degree, invert, offset, weight, f, f1);
    }

    /** Rotates around Y. */
    public void swing(AdvancedModelBox box, float speed, float degree, boolean invert, float offset, float weight, float f, float f1) {
        box.rotateAngleY += wave(speed, degree, invert, offset, weight, f, f1);
    }

    /** Rotates around Z. */
    public void flap(AdvancedModelBox box, float speed, float degree, boolean invert, float offset, float weight, float f, float f1) {
        box.rotateAngleZ += wave(speed, degree, invert, offset, weight, f, f1);
    }

    /** Moves the bone up and down. */
    public void bob(AdvancedModelBox box, float speed, float degree, boolean bounce, float f, float f1) {
        float value = (float) (Math.sin(f * speed) * f1 * degree);
        box.rotationPointY += bounce ? -Math.abs(value) : value - f1 * degree;
    }

    private static float chainOffset(float rootOffset, int count) {
        return count <= 1 ? 0.0F : rootOffset * (float) Math.PI / (2.0F * (count - 1));
    }

    public void chainWave(AdvancedModelBox[] boxes, float speed, float degree, double rootOffset, float f, float f1) {
        float offset = chainOffset((float) rootOffset, boxes.length);
        for (int i = 0; i < boxes.length; i++) {
            boxes[i].rotateAngleX += (float) Math.cos(f * speed + offset * i) * f1 * degree;
        }
    }

    public void chainSwing(AdvancedModelBox[] boxes, float speed, float degree, double rootOffset, float f, float f1) {
        float offset = chainOffset((float) rootOffset, boxes.length);
        for (int i = 0; i < boxes.length; i++) {
            boxes[i].rotateAngleY += (float) Math.cos(f * speed + offset * i) * f1 * degree;
        }
    }

    public void chainFlap(AdvancedModelBox[] boxes, float speed, float degree, double rootOffset, float f, float f1) {
        float offset = chainOffset((float) rootOffset, boxes.length);
        for (int i = 0; i < boxes.length; i++) {
            boxes[i].rotateAngleZ += (float) Math.cos(f * speed + offset * i) * f1 * degree;
        }
    }

    /** Spreads a head yaw/pitch (degrees) over the given bones. */
    public void faceTarget(float yaw, float pitch, float rotationDivisor, AdvancedModelBox... boxes) {
        float divisor = rotationDivisor * boxes.length;
        float yawAmount = yaw * (float) Math.PI / 180.0F / divisor;
        float pitchAmount = pitch * (float) Math.PI / 180.0F / divisor;
        for (AdvancedModelBox box : boxes) {
            box.rotateAngleY += yawAmount;
            box.rotateAngleX += pitchAmount;
        }
    }
}
