package com.github.alexthe666.iceandfire.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.nicktale.api.client.model.AdvancedModelBox;

public abstract class ModelMyrmexBase<S extends MyrmexRenderState> extends ModelDragonBase<S> {

    public void postRenderArm(float scale, PoseStack stackIn) {
        for (AdvancedModelBox renderer : this.getHeadParts()) {
            renderer.translateRotate(stackIn);
        }
    }

    public abstract AdvancedModelBox[] getHeadParts();
}
