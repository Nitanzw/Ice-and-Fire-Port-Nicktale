// SPDX-License-Identifier: LGPL-3.0-or-later
package com.nicktale.api.client.model.basic;

import com.nicktale.api.client.model.AdvancedEntityModel;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;

/**
 * Lightweight 26.2 entity-model base for models whose animation is driven directly by their parts.
 * Dynamic values needed for rendering belong in the render state; this class intentionally does not retain an
 * entity instance between extraction and drawing.
 */
public abstract class BasicEntityModel<T extends Entity> extends AdvancedEntityModel<EntityRenderState> {
    protected BasicEntityModel() {
        super();
    }

    @Override
    protected void animate(EntityRenderState state) {
        // Subclasses pose their AdvancedModelBox fields before rendering or override this state hook.
    }
}
