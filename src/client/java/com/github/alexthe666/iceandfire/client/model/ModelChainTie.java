package com.github.alexthe666.iceandfire.client.model;

import com.nicktale.api.client.model.AdvancedEntityModel;
import com.nicktale.api.client.model.AdvancedModelBox;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

public class ModelChainTie extends AdvancedEntityModel<EntityRenderState> {
    public AdvancedModelBox knotRenderer;

    public ModelChainTie() {
        this(0, 0, 32, 32);
    }

    public ModelChainTie(int width, int height, int texWidth, int texHeight) {
        this.texWidth = texWidth;
        this.texHeight = texHeight;
        this.knotRenderer = new AdvancedModelBox(this, width, height);
        this.knotRenderer.addBox(-4.0F, 2.0F, -4.0F, 8, 12, 8, 1.0F);
        this.knotRenderer.setPos(0.0F, 0.0F, 0.0F);
        this.updateDefaultPose();
    }

    @Override
    protected void animate(EntityRenderState state) {
    }
}
