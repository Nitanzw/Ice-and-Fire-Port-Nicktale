package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.TrollRenderState;
import com.github.alexthe666.iceandfire.client.model.ModelTroll;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerTrollEyes;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerTrollWeapon;
import com.github.alexthe666.iceandfire.entity.EntityTroll;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class RenderTroll extends IafMobRenderer<EntityTroll, TrollRenderState, ModelTroll> {

    public RenderTroll(EntityRendererProvider.Context context) {
        super(context, new ModelTroll(), 0.9F);
        this.addLayer(new LayerTrollWeapon(this));
        this.addLayer(new LayerTrollEyes(this));
    }

    @Override
    public TrollRenderState createRenderState() {
        return new TrollRenderState();
    }

    @Override
    protected void extract(EntityTroll entity, TrollRenderState state, float partialTick) {
        state.stoneProgress = entity.stoneProgress;
    }

    @Override
    protected Identifier textureFor(EntityTroll troll) {
        return troll.getTrollType().TEXTURE;
    }
}
