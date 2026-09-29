package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.iceandfire.client.model.ModelPixie;
import com.github.alexthe666.iceandfire.client.model.PixieRenderState;
import com.github.alexthe666.iceandfire.client.render.entity.RenderPixie;
import com.github.alexthe666.iceandfire.entity.EntityPixie;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.resources.Identifier;

public class LayerPixieGlow extends IafRenderLayer<PixieRenderState, ModelPixie> {

    public LayerPixieGlow(RenderPixie renderIn) {
        super(renderIn);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, PixieRenderState state, float yRot, float xRot) {
        EntityPixie pixie = entityOf(state);
        Identifier texture = switch (pixie.getColor()) {
            case 1 -> RenderPixie.TEXTURE_1;
            case 2 -> RenderPixie.TEXTURE_2;
            case 3 -> RenderPixie.TEXTURE_3;
            case 4 -> RenderPixie.TEXTURE_4;
            case 5 -> RenderPixie.TEXTURE_5;
            default -> RenderPixie.TEXTURE_0;
        };
        submitGlowing(collector, poseStack, state, texture);
    }
}
