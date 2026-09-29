package com.github.alexthe666.iceandfire.client.render;

import com.nicktale.api.client.model.AdvancedModelBox;
import com.nicktale.api.client.model.ITabulaModelAnimator;
import com.nicktale.api.client.model.TabulaModel;
import com.nicktale.api.client.model.container.TabulaModelContainer;

import java.util.List;

public class TabulaModelAccessor extends TabulaModel {
    public TabulaModelAccessor(TabulaModelContainer container, ITabulaModelAnimator tabulaAnimator) {
        super(container, tabulaAnimator);
    }

    public TabulaModelAccessor(TabulaModelContainer container) {
        super(container);
    }

    public List<AdvancedModelBox> getRootBox() {
        return super.rootBoxes;
    }
}
