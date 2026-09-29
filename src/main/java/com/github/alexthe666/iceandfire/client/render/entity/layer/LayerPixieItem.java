package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.iceandfire.client.model.ModelPixie;
import com.github.alexthe666.iceandfire.client.model.PixieRenderState;
import com.github.alexthe666.iceandfire.client.render.entity.RenderPixie;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;

/** Item carried by a pixie. TODO: needs the 26.x item render state; the held stack is already in the state. */
public class LayerPixieItem extends IafRenderLayer<PixieRenderState, ModelPixie> {

    public LayerPixieItem(RenderPixie renderer) {
        super(renderer);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, PixieRenderState state, float yRot, float xRot) {
    }
}
