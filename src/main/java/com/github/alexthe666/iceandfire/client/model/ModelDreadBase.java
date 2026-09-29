package com.github.alexthe666.iceandfire.client.model;

import com.nicktale.api.animation.Animation;

abstract class ModelDreadBase<S extends BipedRenderState> extends ModelBipedBase<S> {

    ModelDreadBase() {
        super();
    }

    public abstract Animation getSpawnAnimation();

    @Override
    protected void animate(S state) {
        super.animate(state);
        setRotationAnglesSpawn(state);
    }

    public void setRotationAnglesSpawn(S state) {
        if (state.animation == getSpawnAnimation()) {
            if (state.animationTick < 30) {
                this.flap(armRight, 0.5F, 0.5F, false, 2, -0.7F, state.tickCount, 1);
                this.flap(armLeft, 0.5F, 0.5F, true, 2, -0.7F, state.tickCount, 1);
                this.walk(armRight, 0.5F, 0.5F, true, 1, 0, state.tickCount, 1);
                this.walk(armLeft, 0.5F, 0.5F, true, 1, 0, state.tickCount, 1);
            }
        }
    }

    @Override
    public void animate(S state, float f, float f1, float f2, float f3, float f4, float f5) {
        animator.update(state.animation, state.animationTick);
        if (animator.setAnimation(getSpawnAnimation())) {
            animator.startKeyframe(0);
            animator.move(this.body, 0, 35, 0);
            rotate(animator, this.armLeft, -180, 0, 0);
            rotate(animator, this.armRight, -180, 0, 0);
            animator.endKeyframe();
            animator.startKeyframe(30);
            animator.move(this.body, 0, 0, 0);
            rotate(animator, this.armLeft, -180, 0, 0);
            rotate(animator, this.armRight, -180, 0, 0);
            animator.endKeyframe();
            animator.resetKeyframe(5);
        }
    }

}
