package com.github.alexthe666.iceandfire.client.model;

import com.nicktale.api.animation.Animation;
import com.nicktale.api.animation.IAnimatedEntity;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

/** Everything ModelHippogryph needs from the entity, copied once per frame by the renderer. */
public class HippogryphRenderState extends LivingEntityRenderState {
    public Animation animation = IAnimatedEntity.NO_ANIMATION;
    /** Animation tick including the partial tick. */
    public float animationTick;
    public float sitProgress;
    public float hoverProgress;
    public float flyProgress;
    public boolean flying;
    public boolean hovering;
    public int airBorneCounter;
    public boolean dodo;
}
