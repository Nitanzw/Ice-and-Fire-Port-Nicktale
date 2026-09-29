package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelDragonEgg;
import com.github.alexthe666.iceandfire.entity.EntityDragonEgg;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import com.github.alexthe666.iceandfire.client.model.DragonEggRenderState;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;


public class RenderDragonEgg extends IafLivingRenderer<EntityDragonEgg, DragonEggRenderState, ModelDragonEgg> {

    public static final Identifier EGG_RED = Identifier.parse("iceandfire:textures/models/firedragon/egg_red.png");
    public static final Identifier EGG_GREEN = Identifier.parse("iceandfire:textures/models/firedragon/egg_green.png");
    public static final Identifier EGG_BRONZE = Identifier.parse("iceandfire:textures/models/firedragon/egg_bronze.png");
    public static final Identifier EGG_GREY = Identifier.parse("iceandfire:textures/models/firedragon/egg_gray.png");
    public static final Identifier EGG_BLUE = Identifier.parse("iceandfire:textures/models/icedragon/egg_blue.png");
    public static final Identifier EGG_WHITE = Identifier.parse("iceandfire:textures/models/icedragon/egg_white.png");
    public static final Identifier EGG_SAPPHIRE = Identifier.parse("iceandfire:textures/models/icedragon/egg_sapphire.png");
    public static final Identifier EGG_SILVER = Identifier.parse("iceandfire:textures/models/icedragon/egg_silver.png");
    public static final Identifier EGG_ELECTRIC = Identifier.parse("iceandfire:textures/models/lightningdragon/egg_electric.png");
    public static final Identifier EGG_AMYTHEST = Identifier.parse("iceandfire:textures/models/lightningdragon/egg_amythest.png");
    public static final Identifier EGG_BLACK = Identifier.parse("iceandfire:textures/models/lightningdragon/egg_black.png");
    public static final Identifier EGG_COPPER = Identifier.parse("iceandfire:textures/models/lightningdragon/egg_copper.png");

    public RenderDragonEgg(EntityRendererProvider.Context context) {
        super(context, new ModelDragonEgg(), 0.3F);
    }

    @Override
    public @NotNull DragonEggRenderState createRenderState() {
        return new DragonEggRenderState();
    }

    @Override
    protected boolean shouldShowName(EntityDragonEgg entity, double distanceToCamera) {
        return entity.shouldShowName() && entity.hasCustomName() && super.shouldShowName(entity, distanceToCamera);
    }

    @Override
    protected @NotNull Identifier textureFor(EntityDragonEgg entity) {
        switch (entity.getEggType()) {
            default:
                return EGG_RED;
            case GREEN:
                return EGG_GREEN;
            case BRONZE:
                return EGG_BRONZE;
            case GRAY:
                return EGG_GREY;
            case BLUE:
                return EGG_BLUE;
            case WHITE:
                return EGG_WHITE;
            case SAPPHIRE:
                return EGG_SAPPHIRE;
            case SILVER:
                return EGG_SILVER;
            case ELECTRIC:
                return EGG_ELECTRIC;
            case AMYTHEST:
                return EGG_AMYTHEST;
            case COPPER:
                return EGG_COPPER;
            case BLACK:
                return EGG_BLACK;

        }
    }

}
