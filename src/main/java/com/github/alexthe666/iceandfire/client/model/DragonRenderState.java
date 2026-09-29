package com.github.alexthe666.iceandfire.client.model;

import com.nicktale.api.animation.Animation;
import com.nicktale.api.animation.IAnimatedEntity;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;

/** Per-frame snapshot used by the dragon model and renderer. */
public class DragonRenderState extends LivingEntityRenderState implements IAnimatedEntity {
    public Animation animation = IAnimatedEntity.NO_ANIMATION;
    public int animationTick;

    public int dragonType;
    public int variant;
    public int dragonStage;
    public int armorHead;
    public int armorNeck;
    public int armorLegs;
    public int armorFeet;
    public float renderSize;
    public float dragonPitch;
    public float previousDragonPitch;

    public boolean male;
    public boolean skeletal;
    public boolean sleeping;
    public boolean blinking;
    public boolean modelDead;
    public boolean aiDisabled;
    public boolean hovering;
    public boolean flying;
    public boolean swimming;
    public boolean breathingFire;
    public boolean actuallyBreathingFire;
    public boolean vehicle;
    public boolean passenger;
    public boolean eyesVisible;

    public int walkCycle;
    public int flightCycle;
    public int swimCycle;
    public float swimProgress;
    public float sitProgress;
    public float sleepProgress;
    public float hoverProgress;
    public float flyProgress;
    public float tackleProgress;
    public float ridingProgress;
    public float diveProgress;
    public float previousDiveProgress;
    public float fireBreathProgress;
    public float previousFireBreathProgress;
    public float modelDeadProgress;
    public float previousModelDeadProgress;
    public float[] previousAnimationProgresses = new float[10];

    public Identifier baseTexture;
    public Identifier maleOverlay;
    public Identifier emptyOverlay;
    public Identifier eyeTexture;
    public Identifier[] armorLayerTextures = new Identifier[4];

    @Override
    public int getAnimationTick() {
        return animationTick;
    }

    @Override
    public void setAnimationTick(int tick) {
        animationTick = tick;
    }

    @Override
    public Animation getAnimation() {
        return animation;
    }

    @Override
    public void setAnimation(Animation animation) {
        this.animation = animation;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{IAnimatedEntity.NO_ANIMATION};
    }
}
