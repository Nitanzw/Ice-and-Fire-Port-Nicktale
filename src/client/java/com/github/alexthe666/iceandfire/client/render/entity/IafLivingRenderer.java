package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.IafRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import com.nicktale.api.animation.IAnimatedEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;

/**
 * Base for Ice and Fire living-entity (non-Mob) renderers on the 26.x render-state pipeline. It copies the animation
 * clip and a reference to the source entity into the state, so the per-mob code can keep its
 * entity based texture and scale logic while models read plain state fields.
 */
public abstract class IafLivingRenderer<E extends LivingEntity, S extends IafRenderState, M extends EntityModel<? super S>> extends LivingEntityRenderer<E, S, M> {

    protected IafLivingRenderer(EntityRendererProvider.Context context, M model, float shadow) {
        super(context, model, shadow);
    }

    @Override
    public void extractRenderState(E entity, S state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.entity = entity;
        state.partialTick = partialTick;
        if (entity instanceof IAnimatedEntity animated) {
            state.animation = animated.getAnimation();
            state.animationTick = animated.getAnimationTick() + partialTick;
        }
        extract(entity, state, partialTick);
    }

    /** Copies the mob specific values the model needs. */
    protected void extract(E entity, S state, float partialTick) {
    }

    @SuppressWarnings("unchecked")
    protected E entityOf(S state) {
        return (E) state.entity;
    }

    @Override
    protected void scale(S state, PoseStack poseStack) {
        scaleFor(entityOf(state), poseStack, state.partialTick);
    }

    protected void scaleFor(E entity, PoseStack poseStack, float partialTick) {
    }

    @Override
    public Identifier getTextureLocation(S state) {
        return textureFor(entityOf(state));
    }

    protected abstract Identifier textureFor(E entity);
}
