package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelDreadBeast;
import com.github.alexthe666.iceandfire.client.model.DreadBeastRenderState;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerGenericGlowing;
import com.github.alexthe666.iceandfire.entity.EntityDreadBeast;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class RenderDreadBeast extends IafMobRenderer<EntityDreadBeast, DreadBeastRenderState, ModelDreadBeast> {

    public static final Identifier TEXTURE_EYES = Identifier.parse("iceandfire:textures/models/dread/dread_beast_eyes.png");
    public static final Identifier TEXTURE_0 = Identifier.parse("iceandfire:textures/models/dread/dread_beast_1.png");
    public static final Identifier TEXTURE_1 = Identifier.parse("iceandfire:textures/models/dread/dread_beast_2.png");

    public RenderDreadBeast(EntityRendererProvider.Context context) {
        super(context, new ModelDreadBeast(), 0.5F);
        this.addLayer(new LayerGenericGlowing<>(this, TEXTURE_EYES));
    }

    @Override
    public DreadBeastRenderState createRenderState() {
        return new DreadBeastRenderState();
    }

    @Override
    protected void scaleFor(EntityDreadBeast entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(entity.getSize(), entity.getSize(), entity.getSize());
    }

    @Override
    protected Identifier textureFor(EntityDreadBeast entity) {
        return entity.getVariant() == 1 ? TEXTURE_1 : TEXTURE_0;
    }
}
