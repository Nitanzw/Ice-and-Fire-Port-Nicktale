package com.github.alexthe666.iceandfire.client.model;

import com.nicktale.api.animation.Animation;
import com.nicktale.api.animation.IAnimatedEntity;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;

/** Common render state for Ice and Fire's humanoid mobs (ghost, dread undead). */
public class BipedRenderState extends HumanoidRenderState {
    public Animation animation = IAnimatedEntity.NO_ANIMATION;
    /** Animation tick including the partial tick. */
    public float animationTick;
    public net.minecraft.world.entity.Entity entity;
    public float partialTick;
    /** Entity tick count, used for spawn flailing. */
    public int tickCount;
    /** Ghost transparency (0-1) and mode flags; unused by the other humanoids. */
    public float alpha = 1.0F;
    public boolean ghostDaytime;
    public boolean ghostShoppingList;
}
