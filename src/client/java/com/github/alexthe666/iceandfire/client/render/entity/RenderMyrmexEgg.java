package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.DragonEggRenderState;
import com.github.alexthe666.iceandfire.client.model.ModelDragonEgg;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexEgg;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public class RenderMyrmexEgg extends IafLivingRenderer<EntityMyrmexEgg, DragonEggRenderState, ModelDragonEgg> {

    public static final Identifier EGG_JUNGLE = Identifier.parse("iceandfire:textures/models/myrmex/myrmex_jungle_egg.png");
    public static final Identifier EGG_DESERT = Identifier.parse("iceandfire:textures/models/myrmex/myrmex_desert_egg.png");

    public RenderMyrmexEgg(EntityRendererProvider.Context context) {
        super(context, new ModelDragonEgg(), 0.3F);
    }

    @Override
    public @NotNull DragonEggRenderState createRenderState() {
        return new DragonEggRenderState();
    }

    @Override
    protected boolean shouldShowName(EntityMyrmexEgg entity, double distanceToCamera) {
        return entity.shouldShowName() && entity.hasCustomName() && super.shouldShowName(entity, distanceToCamera);
    }

    @Override
    protected @NotNull Identifier textureFor(EntityMyrmexEgg entity) {
        return entity.isJungle() ? EGG_JUNGLE : EGG_DESERT;
    }

}
