package com.nicktale.api.animation;

import net.minecraft.world.entity.Entity;

/** Implemented by entities that play {@link Animation} clips driven by a tick counter. */
public interface IAnimatedEntity {
    /** Shared "nothing is playing" marker. Every entity compares against this instance. */
    Animation NO_ANIMATION = Animation.create(0);

    int getAnimationTick();

    void setAnimationTick(int tick);

    Animation getAnimation();

    void setAnimation(Animation animation);

    Animation[] getAnimations();

    /** Changes the current clip and broadcasts it to clients tracking the entity. */
    default void setAnimationAndSync(Entity entity, Animation animation) {
        setAnimation(animation);
        AnimationSync.synchronize(entity, this);
    }
}
