package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.iceandfire.client.model.ModelTroll;
import com.github.alexthe666.iceandfire.client.model.TrollRenderState;
import com.github.alexthe666.iceandfire.client.render.entity.RenderTroll;
import com.github.alexthe666.iceandfire.entity.EntityGorgon;
import com.github.alexthe666.iceandfire.entity.EntityTroll;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;

public class LayerTrollEyes extends IafRenderLayer<TrollRenderState, ModelTroll> {

    public LayerTrollEyes(RenderTroll renderer) {
        super(renderer);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, TrollRenderState state, float yRot, float xRot) {
        EntityTroll troll = entityOf(state);
        if (!EntityGorgon.isStoneMob(troll)) {
            submitGlowing(collector, poseStack, state, troll.getTrollType().TEXTURE_EYES);
        }
    }
}
