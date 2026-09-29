package com.github.alexthe666.iceandfire.client.model;

import com.nicktale.api.client.model.AdvancedEntityModel;
import com.nicktale.api.client.model.AdvancedModelBox;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityEggInIce;

public class ModelDragonEgg extends AdvancedEntityModel<DragonEggRenderState> {

    public AdvancedModelBox Egg1;
    public AdvancedModelBox Egg2;
    public AdvancedModelBox Egg3;
    public AdvancedModelBox Egg4;

    public ModelDragonEgg() {
        this.texWidth = 64;
        this.texHeight = 32;
        this.Egg3 = new AdvancedModelBox(this, 0, 0);
        this.Egg3.setPos(0.0F, 0.0F, 0.0F);
        this.Egg3.addBox(-2.5F, -4.6F, -2.5F, 5, 5, 5, 0.0F);
        this.Egg2 = new AdvancedModelBox(this, 22, 2);
        this.Egg2.setPos(0.0F, 0.0F, 0.0F);
        this.Egg2.addBox(-2.5F, -0.6F, -2.5F, 5, 5, 5, 0.0F);
        this.Egg1 = new AdvancedModelBox(this, 0, 12);
        this.Egg1.setPos(0.0F, 19.6F, 0.0F);
        this.Egg1.addBox(-3.0F, -2.8F, -3.0F, 6, 6, 6, 0.0F);
        this.Egg4 = new AdvancedModelBox(this, 28, 16);
        this.Egg4.setPos(0.0F, -0.9F, 0.0F);
        this.Egg4.addBox(-2.0F, -4.8F, -2.0F, 4, 4, 4, 0.0F);
        this.Egg1.addChild(this.Egg3);
        this.Egg1.addChild(this.Egg2);
        this.Egg3.addChild(this.Egg4);
        this.updateDefaultPose();
    }



    @Override
    protected void animate(DragonEggRenderState state) {
        float f2 = state.ageInTicks;
        this.resetToDefaultPose();
        this.Egg1.setPos(0.0F, 19.6F, 0.0F);
        this.Egg4.setPos(0.0F, -0.9F, 0.0F);
        if (state.shaking) {
            this.walk(Egg1, 0.3F, 0.3F, true, 1, 0, f2, 1);
            this.flap(Egg1, 0.3F, 0.3F, false, 0, 0, f2, 1);
        }
    }

    public void renderPodium() {
        Egg1.rotateAngleX = (float) Math.toRadians(-180);

    }

    public void renderFrozen(TileEntityEggInIce tile) {
        this.resetToDefaultPose();
        Egg1.rotateAngleX = (float) Math.toRadians(-180);
        this.walk(Egg1, 0.3F, 0.1F, true, 1, 0, tile.ticksExisted, 1);
        this.flap(Egg1, 0.3F, 0.1F, false, 0, 0, tile.ticksExisted, 1);
    }
}
