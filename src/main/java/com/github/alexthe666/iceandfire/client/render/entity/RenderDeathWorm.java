package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.DeathWormRenderState;
import com.github.alexthe666.iceandfire.client.model.ModelDeathWorm;
import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;



public class RenderDeathWorm extends IafMobRenderer<EntityDeathWorm, DeathWormRenderState, ModelDeathWorm> {
    public static final Identifier TEXTURE_RED = Identifier.parse("iceandfire:textures/models/deathworm/deathworm_red.png");
    public static final Identifier TEXTURE_WHITE = Identifier.parse("iceandfire:textures/models/deathworm/deathworm_white.png");
    public static final Identifier TEXTURE_YELLOW = Identifier.parse("iceandfire:textures/models/deathworm/deathworm_yellow.png");

    public RenderDeathWorm(EntityRendererProvider.Context context) {
        super(context, new ModelDeathWorm(), 0);
    }

    @Override
    public DeathWormRenderState createRenderState() {
        return new DeathWormRenderState();
    }

    @Override
    protected void extract(EntityDeathWorm entity, DeathWormRenderState state, float partialTick) {
        state.getWormJumping = entity.getWormJumping();
        state.jumpProgress = entity.jumpProgress;
        state.prevJumpProgress = entity.prevJumpProgress;
        state.tail_buffer = entity.tail_buffer;
        state.tickCount = entity.tickCount;
    }

    @Override
    protected void scaleFor(EntityDeathWorm entity, PoseStack matrixStackIn, float partialTickTime) {
        this.shadowRadius = entity.getScale() / 3;
        matrixStackIn.scale(entity.getScale(), entity.getScale(), entity.getScale());
    }


    @Override
    protected int getBlockLightLevel(EntityDeathWorm entityIn, @NotNull BlockPos partialTicks) {
        return entityIn.isOnFire() ? 15 : entityIn.getWormBrightness(false);
    }

    @Override
    protected int getSkyLightLevel(EntityDeathWorm entity, @NotNull BlockPos pos) {
        return entity.getWormBrightness(true);
    }

    @Override
    protected Identifier textureFor(EntityDeathWorm entity) {
        return entity.getVariant() == 2 ? TEXTURE_WHITE : entity.getVariant() == 1 ? TEXTURE_RED : TEXTURE_YELLOW;
    }
}
