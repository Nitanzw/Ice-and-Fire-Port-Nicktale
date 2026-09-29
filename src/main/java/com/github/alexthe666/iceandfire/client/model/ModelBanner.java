package com.github.alexthe666.iceandfire.client.model;

import com.nicktale.api.client.model.AdvancedEntityModel;
import com.nicktale.api.client.model.AdvancedModelBox;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

public class ModelBanner extends AdvancedEntityModel<EntityRenderState> {
    public final AdvancedModelBox flag;
    public final AdvancedModelBox pole;
    public final AdvancedModelBox bar;

    public ModelBanner() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.flag = new AdvancedModelBox(this, 0, 0);
        this.flag.addBox(-10.0F, 0.0F, -2.0F, 20.0F, 40.0F, 1.0F, 0.0F);
        this.pole = new AdvancedModelBox(this, 44, 0);
        this.pole.addBox(-1.0F, -30.0F, -1.0F, 2.0F, 42.0F, 2.0F, 0.0F);
        this.bar = new AdvancedModelBox(this, 0, 42);
        this.bar.addBox(-10.0F, -32.0F, -1.0F, 20.0F, 2.0F, 2.0F, 0.0F);
        this.updateDefaultPose();
    }

    @Override
    protected void animate(EntityRenderState state) {
    }
}
