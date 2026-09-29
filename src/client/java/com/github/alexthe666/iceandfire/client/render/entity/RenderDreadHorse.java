package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerGenericGlowing;
import com.github.alexthe666.iceandfire.entity.EntityDreadHorse;
import net.minecraft.client.model.animal.equine.HorseModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.AbstractHorseRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EquineRenderState;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public class RenderDreadHorse extends AbstractHorseRenderer<EntityDreadHorse, EquineRenderState, HorseModel> {
    public static final Identifier TEXTURE = Identifier.parse("iceandfire:textures/models/dread/dread_knight_horse.png");
    public static final Identifier TEXTURE_EYES = Identifier.parse("iceandfire:textures/models/dread/dread_knight_horse_eyes.png");

    public RenderDreadHorse(EntityRendererProvider.Context context) {
        super(context, new HorseModel(context.bakeLayer(ModelLayers.HORSE)), new HorseModel(context.bakeLayer(ModelLayers.HORSE_BABY)));
        this.addLayer(new LayerGenericGlowing<>(this, TEXTURE_EYES));
    }

    @Override
    public @NotNull EquineRenderState createRenderState() {
        return new EquineRenderState();
    }

    @Override
    public @NotNull Identifier getTextureLocation(@NotNull EquineRenderState state) {
        return TEXTURE;
    }
}
