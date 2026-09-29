package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.entity.EntityGorgon;
import com.github.alexthe666.iceandfire.client.model.HydraRenderState;
import com.github.alexthe666.iceandfire.client.model.ModelHydraBody;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerGenericGlowing;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerHydraHead;
import com.github.alexthe666.iceandfire.entity.EntityHydra;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class RenderHydra extends IafMobRenderer<EntityHydra, HydraRenderState, ModelHydraBody> {

    public static final Identifier TEXUTURE_0 = Identifier.parse("iceandfire:textures/models/hydra/hydra_0.png");
    public static final Identifier TEXUTURE_1 = Identifier.parse("iceandfire:textures/models/hydra/hydra_1.png");
    public static final Identifier TEXUTURE_2 = Identifier.parse("iceandfire:textures/models/hydra/hydra_2.png");
    public static final Identifier TEXUTURE_EYES = Identifier.parse("iceandfire:textures/models/hydra/hydra_eyes.png");

    public RenderHydra(EntityRendererProvider.Context context) {
        super(context, new ModelHydraBody(), 1.2F);
        this.addLayer(new LayerHydraHead(this));
        this.addLayer(new LayerGenericGlowing(this, TEXUTURE_EYES));
    }

    @Override
    public HydraRenderState createRenderState() {
        return new HydraRenderState();
    }

    @Override
    protected void extract(EntityHydra entity, HydraRenderState state, float partialTick) {
        state.stoneMob = EntityGorgon.isStoneMob(entity);
        state.breathProgress = entity.breathProgress;
        state.getSeveredHead = entity.getSeveredHead();
        state.isAlive = entity.isAlive();
        state.prevBreathProgress = entity.prevBreathProgress;
        state.prevSpeakingProgress = entity.prevSpeakingProgress;
        state.prevStrikeProgress = entity.prevStrikeProgress;
        state.speakingProgress = entity.speakingProgress;
        state.strikingProgress = entity.strikingProgress;
    }

    @Override
    protected void scaleFor(EntityHydra LivingEntityIn, PoseStack stack, float partialTickTime) {
        stack.scale(1.75F, 1.75F, 1.75F);
    }

    @Override
    protected Identifier textureFor(EntityHydra gorgon) {
        switch (gorgon.getVariant()) {
            default:
                return TEXUTURE_0;
            case 1:
                return TEXUTURE_1;
            case 2:
                return TEXUTURE_2;
        }
    }

}
