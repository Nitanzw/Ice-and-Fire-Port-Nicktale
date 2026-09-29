package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.SirenRenderState;
import com.github.alexthe666.iceandfire.client.model.ModelSiren;
import com.github.alexthe666.iceandfire.entity.EntitySiren;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class RenderSiren extends IafMobRenderer<EntitySiren, SirenRenderState, ModelSiren> {

    public static final Identifier TEXTURE_0 = Identifier.parse("iceandfire:textures/models/siren/siren_0.png");
    public static final Identifier TEXTURE_0_AGGRESSIVE = Identifier.parse("iceandfire:textures/models/siren/siren_0_aggressive.png");
    public static final Identifier TEXTURE_1 = Identifier.parse("iceandfire:textures/models/siren/siren_1.png");
    public static final Identifier TEXTURE_1_AGGRESSIVE = Identifier.parse("iceandfire:textures/models/siren/siren_1_aggressive.png");
    public static final Identifier TEXTURE_2 = Identifier.parse("iceandfire:textures/models/siren/siren_2.png");
    public static final Identifier TEXTURE_2_AGGRESSIVE = Identifier.parse("iceandfire:textures/models/siren/siren_2_aggressive.png");

    public RenderSiren(EntityRendererProvider.Context context) {
        super(context, new ModelSiren(), 0.8F);
    }

    @Override
    public SirenRenderState createRenderState() {
        return new SirenRenderState();
    }

    @Override
    protected void extract(EntitySiren entity, SirenRenderState state, float partialTick) {
        state.getSingingPose = entity.getSingingPose();
        state.isSinging = entity.isSinging();
        state.isSwimming = entity.isSwimming();
        state.onGround = entity.onGround();
        state.singProgress = entity.singProgress;
        state.swimProgress = entity.swimProgress;
        state.tail_buffer = entity.tail_buffer;
    }

    @Override
    protected void scaleFor(EntitySiren LivingEntityIn, PoseStack stack, float partialTickTime) {
        stack.translate(0, 0, -0.5F);

    }

    @Override
    protected Identifier textureFor(EntitySiren siren) {
        switch (siren.getHairColor()) {
            default:
                return siren.isAgressive() ? TEXTURE_0_AGGRESSIVE : TEXTURE_0;
            case 1:
                return siren.isAgressive() ? TEXTURE_1_AGGRESSIVE : TEXTURE_1;
            case 2:
                return siren.isAgressive() ? TEXTURE_2_AGGRESSIVE : TEXTURE_2;
        }
    }

    public static Identifier getSirenOverlayTexture(int siren) {
        switch (siren) {
            default:
                return TEXTURE_0;
            case 1:
                return TEXTURE_1;
            case 2:
                return TEXTURE_2;
        }
    }

}
