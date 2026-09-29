package com.nicktale.api.client.model;

import com.nicktale.api.animation.Animation;
import com.nicktale.api.animation.IAnimatedEntity;

import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Keyframe player. Per frame: {@code update}, then for each clip {@code if (setAnimation(clip))}
 * followed by {@code startKeyframe / rotate / move / endKeyframe} groups. Values are offsets from
 * the default pose, blended linearly from the previous keyframe.
 */
public class ModelAnimator {
    private Animation current = IAnimatedEntity.NO_ANIMATION;
    private float tick;
    private boolean active;
    private boolean pending;
    private int elapsed;
    private int duration;
    private final Map<AdvancedModelBox, float[]> previous = new IdentityHashMap<>();
    private final Map<AdvancedModelBox, float[]> target = new IdentityHashMap<>();

    public static ModelAnimator create() {
        return new ModelAnimator();
    }

    public void update(IAnimatedEntity entity) {
        update(entity, 0.0F);
    }

    public void update(IAnimatedEntity entity, float partialTicks) {
        update(entity.getAnimation(), entity.getAnimationTick() + partialTicks);
    }

    public void update(Animation animation, float animationTick) {
        this.current = animation;
        this.tick = animationTick;
    }

    /** True if {@code animation} is the one playing now; also resets the keyframe state. */
    public boolean setAnimation(Animation animation) {
        previous.clear();
        target.clear();
        elapsed = 0;
        pending = false;
        active = current == animation && animation != IAnimatedEntity.NO_ANIMATION;
        return active;
    }

    public void startKeyframe(int ticks) {
        if (!active) {
            return;
        }
        duration = ticks;
        pending = true;
        target.clear();
        for (Map.Entry<AdvancedModelBox, float[]> e : previous.entrySet()) {
            target.put(e.getKey(), e.getValue().clone());
        }
    }

    private float[] slot(AdvancedModelBox box) {
        return target.computeIfAbsent(box, b -> new float[6]);
    }

    public void rotate(AdvancedModelBox box, float x, float y, float z) {
        if (active && pending) {
            float[] v = slot(box);
            v[0] = x;
            v[1] = y;
            v[2] = z;
        }
    }

    public void move(AdvancedModelBox box, float x, float y, float z) {
        if (active && pending) {
            float[] v = slot(box);
            v[3] = x;
            v[4] = y;
            v[5] = z;
        }
    }

    public void endKeyframe() {
        if (active && pending) {
            finish();
        }
    }

    /** Holds the previous keyframe for the given time. */
    public void setStaticKeyframe(int ticks) {
        if (!active) {
            return;
        }
        startKeyframe(ticks);
        finish();
    }

    /** Blends every touched bone back to the default pose. */
    public void resetKeyframe(int ticks) {
        if (!active) {
            return;
        }
        startKeyframe(ticks);
        for (float[] v : target.values()) {
            Arrays.fill(v, 0.0F);
        }
        finish();
    }

    private void finish() {
        pending = false;
        int start = elapsed;
        int end = elapsed + duration;
        elapsed = end;
        if (tick >= end) {
            previous.clear();
            for (Map.Entry<AdvancedModelBox, float[]> e : target.entrySet()) {
                previous.put(e.getKey(), e.getValue().clone());
            }
            return;
        }
        if (tick < start) {
            return;
        }
        float progress = duration <= 0 ? 1.0F : (tick - start) / duration;
        for (Map.Entry<AdvancedModelBox, float[]> e : target.entrySet()) {
            float[] from = previous.get(e.getKey());
            float[] to = e.getValue();
            AdvancedModelBox box = e.getKey();
            box.rotateAngleX += lerp(from, to, 0, progress);
            box.rotateAngleY += lerp(from, to, 1, progress);
            box.rotateAngleZ += lerp(from, to, 2, progress);
            box.rotationPointX += lerp(from, to, 3, progress);
            box.rotationPointY += lerp(from, to, 4, progress);
            box.rotationPointZ += lerp(from, to, 5, progress);
        }
    }

    private static float lerp(float[] from, float[] to, int i, float t) {
        float a = from == null ? 0.0F : from[i];
        return a + (to[i] - a) * t;
    }
}
