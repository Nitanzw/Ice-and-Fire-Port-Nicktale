package com.github.alexthe666.iceandfire.client.model;

import com.nicktale.api.animation.Animation;
import com.nicktale.api.animation.IAnimatedEntity;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;

/** Common render state for Ice and Fire's humanoid mobs (ghost, dread undead). */
public class BipedRenderState extends HumanoidRenderState {
    public Animation animation = IAnimatedEntity.NO_ANIMATION;
    /** Animation tick including the partial tick. */
    public float animationTick;
    /** Entity tick count, used for spawn flailing. */
    public int tickCount;
}
