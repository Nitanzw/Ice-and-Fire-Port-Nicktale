package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.BipedRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import com.nicktale.api.animation.IAnimatedEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Mob;

/**
 * Base for Ice and Fire humanoid mobs on the render-state pipeline. Extracts held items and equipment through the
 * vanilla humanoid path and copies animation clips, so layers can keep working with the source entity.
 */
public abstract class IafBipedRenderer<E extends Mob, M extends EntityModel<BipedRenderState>> extends MobRenderer<E, BipedRenderState, M> {

    protected IafBipedRenderer(EntityRendererProvider.Context context, M model, float shadow) {
        super(context, model, shadow);
    }

    @Override
    public BipedRenderState createRenderState() {
        return new BipedRenderState();
    }

    @Override
    public void extractRenderState(E entity, BipedRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        HumanoidMobRenderer.extractHumanoidRenderState(entity, state, partialTick, this.itemModelResolver);
        state.entity = entity;
        state.partialTick = partialTick;
        state.tickCount = entity.tickCount;
        if (entity instanceof IAnimatedEntity animated) {
            state.animation = animated.getAnimation();
            state.animationTick = animated.getAnimationTick() + partialTick;
        }
        extract(entity, state, partialTick);
    }

    /** Copies the mob specific values the model needs. */
    protected void extract(E entity, BipedRenderState state, float partialTick) {
    }

    @SuppressWarnings("unchecked")
    protected E entityOf(BipedRenderState state) {
        return (E) state.entity;
    }

    @Override
    protected void scale(BipedRenderState state, PoseStack poseStack) {
        scaleFor(entityOf(state), poseStack, state.partialTick);
    }

    protected void scaleFor(E entity, PoseStack poseStack, float partialTick) {
    }

    @Override
    public Identifier getTextureLocation(BipedRenderState state) {
        return textureFor(entityOf(state));
    }

    protected abstract Identifier textureFor(E entity);
}
