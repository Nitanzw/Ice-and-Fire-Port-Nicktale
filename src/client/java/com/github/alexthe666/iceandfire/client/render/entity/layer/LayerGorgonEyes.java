package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.iceandfire.client.model.GorgonRenderState;
import com.github.alexthe666.iceandfire.client.model.ModelGorgon;
import com.github.alexthe666.iceandfire.client.render.entity.RenderGorgon;
import com.github.alexthe666.iceandfire.entity.EntityGorgon;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.resources.Identifier;

public class LayerGorgonEyes extends IafRenderLayer<GorgonRenderState, ModelGorgon> {
    private static final Identifier TEXTURE = Identifier.parse("iceandfire:textures/models/gorgon/gorgon_eyes.png");

    public LayerGorgonEyes(RenderGorgon renderIn) {
        super(renderIn);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, GorgonRenderState state, float yRot, float xRot) {
        if (state.animation == EntityGorgon.ANIMATION_SCARE || state.animation == EntityGorgon.ANIMATION_HIT) {
            submitGlowing(collector, poseStack, state, TEXTURE);
        }
    }
}
