package com.nicktale.api.client.model.armor;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;

/** Base for the custom armor layer models; poses come from the humanoid render state (armor stands included). */
public class ArmorModelBase extends HumanoidModel<HumanoidRenderState> {
    protected static float INNER_MODEL_OFFSET = 0.38F;
    protected static float OUTER_MODEL_OFFSET = 0.45F;

    public ArmorModelBase(ModelPart root) {
        super(root);
    }
}
