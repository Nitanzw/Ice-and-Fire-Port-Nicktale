// SPDX-License-Identifier: LGPL-3.0-or-later
package com.nicktale.api.client.model;

import com.nicktale.api.client.model.container.TabulaModelContainer;
import com.nicktale.api.client.model.container.TabulaModelContainer.Cube;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/** Render-state based model built from a decoded Tabula hierarchy. */
public class TabulaModel extends AdvancedEntityModel<EntityRenderState> {
    protected final List<AdvancedModelBox> rootBoxes = new ArrayList<>();
    private final Map<String, AdvancedModelBox> cubes = new LinkedHashMap<>();
    private final ITabulaModelAnimator<?> tabulaAnimator;
    /** Llib keyframe animator, retained for source models which use keyframe clips. */
    public final ModelAnimator llibAnimator = ModelAnimator.create();

    public TabulaModel(TabulaModelContainer container) {
        this(container, null);
    }

    public TabulaModel(TabulaModelContainer container, ITabulaModelAnimator<?> animator) {
        super();
        Objects.requireNonNull(container, "container");
        this.texWidth = container.getTextureWidth();
        this.texHeight = container.getTextureHeight();
        this.tabulaAnimator = animator;
        for (Cube root : container.getRootCubes()) {
            rootBoxes.add(build(root, null));
        }
        updateDefaultPose();
    }

    private AdvancedModelBox build(Cube cube, AdvancedModelBox parent) {
        int[] uv = cube.textureOffset();
        AdvancedModelBox box = new AdvancedModelBox(this, uv[0], uv[1]);
        box.boxName = cube.name();
        box.mirror = cube.mirror();
        box.showModel = !cube.hidden();
        float[] pos = cube.position();
        box.setPos(pos[0], pos[1], pos[2]);
        float[] rotation = cube.rotation();
        box.setRotationAngle((float) Math.toRadians(rotation[0]), (float) Math.toRadians(rotation[1]),
                (float) Math.toRadians(rotation[2]));
        float[] scale = cube.scale();
        box.setScale(scale[0], scale[1], scale[2]);
        float[] dimensions = cube.dimensions();
        float[] offset = cube.offset();
        if (dimensions[0] > 0 && dimensions[1] > 0 && dimensions[2] > 0) {
            box.addBox(offset[0], offset[1], offset[2], dimensions[0], dimensions[1], dimensions[2], cube.inflate());
        }
        if (parent != null) {
            parent.addChild(box);
        }
        cubes.putIfAbsent(cube.name(), box);
        for (Cube child : cube.children()) {
            build(child, box);
        }
        return box;
    }

    public AdvancedModelBox getCube(String name) {
        return cubes.get(name);
    }

    public Map<String, AdvancedModelBox> getCubes() {
        return Collections.unmodifiableMap(cubes);
    }

    public List<AdvancedModelBox> getRootBoxes() {
        return Collections.unmodifiableList(rootBoxes);
    }

    @Override
    protected void animate(EntityRenderState state) {
        resetToDefaultPose();
        if (tabulaAnimator == null) {
            return;
        }
        float limbSwing = 0;
        float limbSwingAmount = 0;
        float yaw = 0;
        float pitch = 0;
        if (state instanceof LivingEntityRenderState living) {
            limbSwing = living.walkAnimationPos;
            limbSwingAmount = living.walkAnimationSpeed;
            yaw = living.yRot;
            pitch = living.xRot;
        }
        invokeAnimator(state, limbSwing, limbSwingAmount, state.ageInTicks, yaw, pitch, 1.0F);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void invokeAnimator(EntityRenderState state, float limbSwing, float limbSwingAmount,
                                float ageInTicks, float rotationYaw, float rotationPitch, float scale) {
        ((ITabulaModelAnimator) tabulaAnimator).setRotationAngles(this, state,
                limbSwing, limbSwingAmount, ageInTicks, rotationYaw, rotationPitch, scale);
    }
}
