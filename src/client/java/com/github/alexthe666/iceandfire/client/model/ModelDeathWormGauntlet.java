package com.github.alexthe666.iceandfire.client.model;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import com.nicktale.api.client.model.AdvancedModelBox;
import com.nicktale.api.client.model.AdvancedModelBox;

public class ModelDeathWormGauntlet extends ModelDragonBase<EntityRenderState> {
    public AdvancedModelBox Head;
    public AdvancedModelBox JawExtender;
    public AdvancedModelBox HeadInner;
    public AdvancedModelBox ToothB;
    public AdvancedModelBox ToothT;
    public AdvancedModelBox ToothL;
    public AdvancedModelBox ToothL_1;
    public AdvancedModelBox JawExtender2;
    public AdvancedModelBox JawExtender3;
    public AdvancedModelBox JawExtender4;
    public AdvancedModelBox TopJaw;
    public AdvancedModelBox BottomJaw;
    public AdvancedModelBox JawHook;

    public ModelDeathWormGauntlet() {
        this.texWidth = 128;
        this.texHeight = 64;
        this.Head = new AdvancedModelBox(this, 0, 29);
        this.Head.setPos(0.0F, 0.0F, 1.5F);
        this.Head.addBox(-5.0F, -5.0F, -8.0F, 10, 10, 8, 0.0F);
        this.TopJaw = new AdvancedModelBox(this, 19, 7);
        this.TopJaw.setPos(0.0F, -0.2F, -11.4F);
        this.TopJaw.addBox(-2.0F, -1.5F, -6.4F, 4, 2, 6, 0.0F);
        this.setRotateAngle(TopJaw, 0.091106186954104F, 0.0F, 0.0F);
        this.JawHook = new AdvancedModelBox(this, 0, 7);
        this.JawHook.setPos(0.0F, -0.3F, -6.0F);
        this.JawHook.addBox(-0.5F, -0.7F, -2.0F, 1, 1, 3, 0.0F);
        this.setRotateAngle(JawHook, 1.730144887501979F, 0.0F, 0.0F);
        this.ToothL = new AdvancedModelBox(this, 52, 34);
        this.ToothL.setPos(4.5F, 0.0F, -7.5F);
        this.ToothL.addBox(-0.5F, -0.4F, 0.0F, 1, 1, 3, 0.0F);
        this.setRotateAngle(ToothL, -3.141592653589793F, 0.3490658503988659F, 0.0F);
        this.ToothB = new AdvancedModelBox(this, 52, 34);
        this.ToothB.setPos(0.0F, 4.5F, -7.5F);
        this.ToothB.addBox(-0.5F, -0.4F, 0.0F, 1, 1, 3, 0.0F);
        this.setRotateAngle(ToothB, 2.7930504019665254F, -0.0F, 0.0F);
        this.JawExtender = new AdvancedModelBox(this, 0, 7);
        this.JawExtender.setPos(0.0F, 0.0F, 10.0F);
        this.JawExtender.addBox(-1.5F, -1.5F, -13.0F, 3, 3, 13, 0.0F);
        this.BottomJaw = new AdvancedModelBox(this, 40, 7);
        this.BottomJaw.setPos(0.0F, 0.8F, -12.3F);
        this.BottomJaw.addBox(-2.0F, 0.2F, -4.9F, 4, 1, 5, 0.0F);
        this.setRotateAngle(BottomJaw, -0.045553093477052F, 0.0F, 0.0F);
        this.ToothT = new AdvancedModelBox(this, 52, 34);
        this.ToothT.setPos(0.0F, -4.5F, -7.5F);
        this.ToothT.addBox(-0.5F, -0.4F, 0.0F, 1, 1, 3, 0.0F);
        this.setRotateAngle(ToothT, -2.7930504019665254F, -0.0F, 0.0F);
        this.HeadInner = new AdvancedModelBox(this, 0, 48);
        this.HeadInner.setPos(0.0F, 0.0F, -0.3F);
        this.HeadInner.addBox(-4.0F, -4.0F, -8.0F, 8, 8, 8, 0.0F);
        this.ToothL_1 = new AdvancedModelBox(this, 52, 34);
        this.ToothL_1.setPos(-4.5F, 0.0F, -7.5F);
        this.ToothL_1.addBox(-0.5F, -0.4F, 0.0F, 1, 1, 3, 0.0F);
        this.setRotateAngle(ToothL_1, -3.141592653589793F, -0.3490658503988659F, 0.0F);
        this.JawExtender2 = new AdvancedModelBox(this, 0, 7);
        this.JawExtender2.setPos(0.0F, 0.0F, 0.0F);
        this.JawExtender2.addBox(-1.5F, -1.5F, -13.0F, 3, 3, 13, 0.0F);
        this.JawExtender3 = new AdvancedModelBox(this, 0, 7);
        this.JawExtender3.setPos(0.0F, 0.0F, 0.0F);
        this.JawExtender3.addBox(-1.5F, -1.5F, -13.0F, 3, 3, 13, 0.0F);
        this.JawExtender4 = new AdvancedModelBox(this, 0, 7);
        this.JawExtender4.setPos(0.0F, 0.0F, 0.0F);
        this.JawExtender4.addBox(-1.5F, -1.5F, -13.0F, 3, 3, 13, 0.0F);
        this.TopJaw.addChild(this.JawHook);
        this.Head.addChild(this.ToothL);
        this.Head.addChild(this.ToothB);
        this.Head.addChild(this.ToothT);
        this.Head.addChild(this.HeadInner);
        this.Head.addChild(this.ToothL_1);
        this.JawExtender.addChild(this.JawExtender2);
        this.JawExtender2.addChild(this.JawExtender3);
        this.JawExtender3.addChild(this.JawExtender4);
        this.JawExtender4.addChild(this.TopJaw);
        this.JawExtender4.addChild(this.BottomJaw);
        this.updateDefaultPose();
    }

    @Override
    protected void animate(EntityRenderState state) {
    }

    /** Poses the gauntlet; lungeTicks (with partial tick) comes from the holder's extra entity data. */
    public void animate(float lungeTicks) {
        this.resetToDefaultPose();
        progressRotation(TopJaw, lungeTicks, (float) Math.toRadians(-30), 0, 0);
        progressRotation(BottomJaw, lungeTicks, (float) Math.toRadians(30), 0, 0);
        progressPosition(JawExtender, lungeTicks, 0, 0, -4);
        progressPosition(JawExtender2, lungeTicks, 0, 0, -10);
        progressPosition(JawExtender3, lungeTicks, 0, 0, -10);
        progressPosition(JawExtender4, lungeTicks, 0, 0, -10);
    }
}
