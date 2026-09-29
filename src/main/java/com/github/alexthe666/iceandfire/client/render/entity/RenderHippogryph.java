package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.HippogryphRenderState;
import com.github.alexthe666.iceandfire.client.model.ModelHippogryph;
import com.github.alexthe666.iceandfire.entity.EntityHippogryph;
import com.github.alexthe666.iceandfire.enums.EnumHippogryphTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public class RenderHippogryph extends MobRenderer<EntityHippogryph, HippogryphRenderState, ModelHippogryph> {

    private static final RenderType SADDLE_TEXTURE = RenderTypes.entityCutout(Identifier.parse("iceandfire:textures/models/hippogryph/saddle.png"));
    private static final RenderType BRIDLE = RenderTypes.entityCutout(Identifier.parse("iceandfire:textures/models/hippogryph/bridle.png"));
    private static final RenderType CHEST = RenderTypes.entityTranslucent(Identifier.parse("iceandfire:textures/models/hippogryph/chest.png"));
    private static final RenderType TEXTURE_DIAMOND = RenderTypes.entityCutout(Identifier.parse("iceandfire:textures/models/hippogryph/armor_diamond.png"));
    private static final RenderType TEXTURE_GOLD = RenderTypes.entityCutout(Identifier.parse("iceandfire:textures/models/hippogryph/armor_gold.png"));
    private static final RenderType TEXTURE_IRON = RenderTypes.entityCutout(Identifier.parse("iceandfire:textures/models/hippogryph/armor_iron.png"));

    public RenderHippogryph(EntityRendererProvider.Context context) {
        super(context, new ModelHippogryph(), 0.8F);
        this.addLayer(new LayerHippogriffSaddle(this));
    }

    @Override
    protected HippogryphRenderState createRenderState() {
        return new HippogryphRenderState();
    }

    @Override
    public void extractRenderState(EntityHippogryph entity, HippogryphRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.animation = entity.getAnimation();
        state.animationTick = entity.getAnimationTick() + partialTick;
        state.sitProgress = entity.sitProgress;
        state.hoverProgress = entity.hoverProgress;
        state.flyProgress = entity.flyProgress;
        state.flying = entity.isFlying();
        state.hovering = entity.isHovering();
        state.airBorneCounter = entity.airBorneCounter;
        state.dodo = entity.getEnumVariant() == EnumHippogryphTypes.DODO;
        state.texture = entity.isBlinking() ? entity.getEnumVariant().TEXTURE_BLINK : entity.getEnumVariant().TEXTURE;
        state.armor = entity.getArmor();
        state.saddled = entity.isSaddled();
        state.chested = entity.isChested();
        state.hasControllingPassenger = entity.getControllingPassenger() != null;
    }

    @Override
    protected void scale(HippogryphRenderState state, PoseStack poseStack) {
        poseStack.scale(1.2F, 1.2F, 1.2F);
    }

    @Override
    public Identifier getTextureLocation(HippogryphRenderState state) {
        return state.texture;
    }

    private static final class LayerHippogriffSaddle extends RenderLayer<HippogryphRenderState, ModelHippogryph> {
        private LayerHippogriffSaddle(RenderHippogryph renderer) {
            super(renderer);
        }

        @Override
        public void submit(@NotNull PoseStack poseStack, @NotNull SubmitNodeCollector collector, int lightCoords,
                           HippogryphRenderState state, float yRot, float xRot) {
            RenderType armor = switch (state.armor) {
                case 1 -> TEXTURE_IRON;
                case 2 -> TEXTURE_GOLD;
                case 3 -> TEXTURE_DIAMOND;
                default -> null;
            };
            if (armor != null) {
                submitModel(collector, poseStack, lightCoords, state, armor);
            }
            if (state.saddled) {
                submitModel(collector, poseStack, lightCoords, state, SADDLE_TEXTURE);
            }
            if (state.saddled && state.hasControllingPassenger) {
                submitModel(collector, poseStack, lightCoords, state, BRIDLE);
            }
            if (state.chested) {
                submitModel(collector, poseStack, lightCoords, state, CHEST);
            }
        }

        private void submitModel(SubmitNodeCollector collector, PoseStack poseStack, int lightCoords,
                                 HippogryphRenderState state, RenderType renderType) {
            collector.submitModel(getParentModel(), state, poseStack, renderType, lightCoords,
                    LivingEntityRenderer.getOverlayCoords(state, 0.0F), -1, null, state.outlineColor, null);
        }
    }
}
