package com.github.alexthe666.iceandfire.client.model;

import com.nicktale.api.animation.Animation;
import com.nicktale.api.animation.IAnimatedEntity;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    public boolean hasLightningTarget;
    public Vec3 lightningStart = Vec3.ZERO;
    public Vec3 lightningEnd = Vec3.ZERO;
    public float lightningScale;

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
    public final ItemStackRenderState bannerItem = new ItemStackRenderState();
    public final List<Rider> riders = new ArrayList<>();

    /** Render-only passenger snapshot; it deliberately contains no live entity reference. */
    public record Rider(EntityRenderState renderState, UUID uuid, boolean prey, boolean dreadQueen, float yaw,
                        boolean humanoidModel, boolean quadrupedModel, boolean horseModel) {
    }

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
