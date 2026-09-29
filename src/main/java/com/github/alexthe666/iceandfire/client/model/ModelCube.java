package com.github.alexthe666.iceandfire.client.model;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import com.nicktale.api.client.model.AdvancedEntityModel;
import com.github.alexthe666.iceandfire.client.model.util.HideableModelRenderer;

public class ModelCube extends AdvancedEntityModel<EntityRenderState> {
    public HideableModelRenderer bipedHead;

    public ModelCube() {
        this(0.0F);
    }

    public ModelCube(float modelSize) {
        this.texHeight = 32;
        this.texWidth = 64;
        this.bipedHead = new HideableModelRenderer(this, 0, 0);
        this.bipedHead.addBox(-8.0F, -8.0F, -8.0F, 16, 16, 16, modelSize - 0.5F);
        this.bipedHead.setPos(0.0F, 0.0F, 0.0F);
        this.updateDefaultPose();
    }

    @Override
    protected void animate(EntityRenderState state) {
        this.resetToDefaultPose();
    }



}