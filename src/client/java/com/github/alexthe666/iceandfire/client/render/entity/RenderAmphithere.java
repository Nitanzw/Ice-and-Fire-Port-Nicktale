package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.AmphithereRenderState;
import com.github.alexthe666.iceandfire.client.model.ModelAmphithere;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;


public class RenderAmphithere extends IafMobRenderer<EntityAmphithere, AmphithereRenderState, ModelAmphithere> {

    public static final Identifier TEXTURE_BLUE = Identifier.parse("iceandfire:textures/models/amphithere/amphithere_blue.png");
    public static final Identifier TEXTURE_BLUE_BLINK = Identifier.parse("iceandfire:textures/models/amphithere/amphithere_blue_blink.png");
    public static final Identifier TEXTURE_GREEN = Identifier.parse("iceandfire:textures/models/amphithere/amphithere_green.png");
    public static final Identifier TEXTURE_GREEN_BLINK = Identifier.parse("iceandfire:textures/models/amphithere/amphithere_green_blink.png");
    public static final Identifier TEXTURE_OLIVE = Identifier.parse("iceandfire:textures/models/amphithere/amphithere_olive.png");
    public static final Identifier TEXTURE_OLIVE_BLINK = Identifier.parse("iceandfire:textures/models/amphithere/amphithere_olive_blink.png");
    public static final Identifier TEXTURE_RED = Identifier.parse("iceandfire:textures/models/amphithere/amphithere_red.png");
    public static final Identifier TEXTURE_RED_BLINK = Identifier.parse("iceandfire:textures/models/amphithere/amphithere_red_blink.png");
    public static final Identifier TEXTURE_YELLOW = Identifier.parse("iceandfire:textures/models/amphithere/amphithere_yellow.png");
    public static final Identifier TEXTURE_YELLOW_BLINK = Identifier.parse("iceandfire:textures/models/amphithere/amphithere_yellow_blink.png");

    public RenderAmphithere(EntityRendererProvider.Context context) {
        super(context, new ModelAmphithere(), 1.6F);
    }

    @Override
    public AmphithereRenderState createRenderState() {
        return new AmphithereRenderState();
    }

    @Override
    protected void extract(EntityAmphithere entity, AmphithereRenderState state, float partialTick) {
        state.diveProgress = entity.diveProgress;
        state.flapProgress = entity.flapProgress;
        state.groundProgress = entity.groundProgress;
        state.onGround = entity.onGround();
        state.pitch_buffer = entity.pitch_buffer;
        state.roll_buffer = entity.roll_buffer;
        state.sitProgress = entity.sitProgress;
        state.tail_buffer = entity.tail_buffer;
    }

    @Override
    protected void scaleFor(EntityAmphithere entity, PoseStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(2.0F, 2.0F, 2.0F);

    }

    @Override
    protected Identifier textureFor(EntityAmphithere amphithere) {
        switch (amphithere.getVariant()) {
            case 0:
                if (amphithere.isBlinking()) {
                    return TEXTURE_BLUE_BLINK;
                } else {
                    return TEXTURE_BLUE;
                }
            case 1:
                if (amphithere.isBlinking()) {
                    return TEXTURE_GREEN_BLINK;
                } else {
                    return TEXTURE_GREEN;
                }
            case 2:
                if (amphithere.isBlinking()) {
                    return TEXTURE_OLIVE_BLINK;
                } else {
                    return TEXTURE_OLIVE;
                }
            case 3:
                if (amphithere.isBlinking()) {
                    return TEXTURE_RED_BLINK;
                } else {
                    return TEXTURE_RED;
                }
            case 4:
                if (amphithere.isBlinking()) {
                    return TEXTURE_YELLOW_BLINK;
                } else {
                    return TEXTURE_YELLOW;
                }
        }
        return TEXTURE_GREEN;
    }

}
