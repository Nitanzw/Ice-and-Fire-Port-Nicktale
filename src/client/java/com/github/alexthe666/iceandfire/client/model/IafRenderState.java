package com.github.alexthe666.iceandfire.client.model;

import com.nicktale.api.animation.Animation;
import com.nicktale.api.animation.IAnimatedEntity;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

/** Common render state for Ice and Fire mobs that play {@link Animation} clips. */
public class IafRenderState extends LivingEntityRenderState {
    public Animation animation = IAnimatedEntity.NO_ANIMATION;
    /** Animation tick including the partial tick. */
    public float animationTick;
    /** Source entity, for renderer and layer logic that has not been split into plain fields yet. Client thread only. */
    public net.minecraft.world.entity.Entity entity;
    public float partialTick;
}
