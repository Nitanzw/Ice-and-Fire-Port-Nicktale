package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelDreadScuttler;
import com.github.alexthe666.iceandfire.client.model.DreadScuttlerRenderState;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerGenericGlowing;
import com.github.alexthe666.iceandfire.entity.EntityDreadScuttler;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class RenderDreadScuttler extends IafMobRenderer<EntityDreadScuttler, DreadScuttlerRenderState, ModelDreadScuttler> {

    public static final Identifier TEXTURE_EYES = Identifier.parse("iceandfire:textures/models/dread/dread_scuttler_eyes.png");
    public static final Identifier TEXTURE = Identifier.parse("iceandfire:textures/models/dread/dread_scuttler.png");

    public RenderDreadScuttler(EntityRendererProvider.Context context) {
        super(context, new ModelDreadScuttler(), 0.75F);
        this.addLayer(new LayerGenericGlowing<>(this, TEXTURE_EYES));
    }

    @Override
    public DreadScuttlerRenderState createRenderState() {
        return new DreadScuttlerRenderState();
    }

    @Override
    protected void scaleFor(EntityDreadScuttler entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(entity.getSize(), entity.getSize(), entity.getSize());
    }

    @Override
    protected Identifier textureFor(EntityDreadScuttler entity) {
        return TEXTURE;
    }
}
