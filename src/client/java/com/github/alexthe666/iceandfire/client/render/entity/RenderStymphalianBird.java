package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.StymphalianBirdRenderState;
import com.github.alexthe666.iceandfire.client.model.ModelStymphalianBird;
import com.github.alexthe666.iceandfire.entity.EntityStymphalianBird;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class RenderStymphalianBird extends IafMobRenderer<EntityStymphalianBird, StymphalianBirdRenderState, ModelStymphalianBird> {

    public static final Identifier TEXTURE = Identifier.parse("iceandfire:textures/models/stymphalianbird/stymphalian_bird.png");

    public RenderStymphalianBird(EntityRendererProvider.Context context) {
        super(context, new ModelStymphalianBird(), 0.6F);
    }

    @Override
    public StymphalianBirdRenderState createRenderState() {
        return new StymphalianBirdRenderState();
    }

    @Override
    protected void extract(EntityStymphalianBird entity, StymphalianBirdRenderState state, float partialTick) {
        state.flyProgress = entity.flyProgress;
    }

    @Override
    protected void scaleFor(EntityStymphalianBird LivingEntityIn, PoseStack stack, float partialTickTime) {
        stack.scale(0.75F, 0.75F, 0.75F);
    }

    @Override
    protected Identifier textureFor(EntityStymphalianBird cyclops) {
        return TEXTURE;
    }

}
