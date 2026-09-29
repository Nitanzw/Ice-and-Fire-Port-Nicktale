package com.github.alexthe666.iceandfire.client.model;

import com.nicktale.api.animation.Animation;
import com.nicktale.api.animation.IAnimatedEntity;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

/** Common render state for Ice and Fire mobs that play {@link Animation} clips. */
public class IafRenderState extends LivingEntityRenderState {
    public Animation animation = IAnimatedEntity.NO_ANIMATION;
    /** Animation tick including the partial tick. */
    public float animationTick;
}
