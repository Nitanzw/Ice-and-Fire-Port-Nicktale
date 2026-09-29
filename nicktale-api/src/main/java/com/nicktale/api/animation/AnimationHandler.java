package com.nicktale.api.animation;

import net.minecraft.world.entity.Entity;

/** Advances the animation tick of an {@link IAnimatedEntity}. Call once per entity tick. */
public final class AnimationHandler {
    public static final AnimationHandler INSTANCE = new AnimationHandler();

    private AnimationHandler() {
    }

    public <T extends Entity & IAnimatedEntity> void updateAnimations(T entity) {
        Animation animation = entity.getAnimation();
        if (animation == null || animation == IAnimatedEntity.NO_ANIMATION) {
            entity.setAnimationTick(0);
            return;
        }
        int tick = entity.getAnimationTick() + 1;
        if (tick >= animation.getDuration()) {
            entity.setAnimation(IAnimatedEntity.NO_ANIMATION);
            entity.setAnimationTick(0);
        } else {
            entity.setAnimationTick(tick);
        }
    }
}
