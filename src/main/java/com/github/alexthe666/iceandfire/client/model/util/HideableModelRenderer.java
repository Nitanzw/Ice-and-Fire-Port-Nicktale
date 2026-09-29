package com.github.alexthe666.iceandfire.client.model.util;

import com.nicktale.api.client.model.AdvancedEntityModel;
import com.nicktale.api.client.model.AdvancedModelBox;

/**
 * A bone that can hide its own cubes while its children keep rendering (hiding a normal bone
 * hides the whole subtree). Used to make armor overlays and hidden limbs work.
 */
public class HideableModelRenderer extends AdvancedModelBox {

    public boolean invisible;

    public HideableModelRenderer(AdvancedEntityModel<?> model, int i, int i1) {
        super(model, i, i1);
    }

    @Override
    public boolean hidesOwnCubes() {
        return invisible;
    }

    public void copyFrom(AdvancedModelBox currentModel) {
        this.rotateAngleX = currentModel.rotateAngleX;
        this.rotateAngleY = currentModel.rotateAngleY;
        this.rotateAngleZ = currentModel.rotateAngleZ;
        this.rotationPointX = currentModel.rotationPointX;
        this.rotationPointY = currentModel.rotationPointY;
        this.rotationPointZ = currentModel.rotationPointZ;
    }
}
