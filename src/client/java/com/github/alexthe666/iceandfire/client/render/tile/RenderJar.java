package com.github.alexthe666.iceandfire.client.render.tile;

import com.github.alexthe666.iceandfire.client.model.ModelPixie;
import com.github.alexthe666.iceandfire.client.render.entity.RenderPixie;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityJar;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class RenderJar<T extends TileEntityJar> implements BlockEntityRenderer<T, TileRenderState> {

    public static final RenderType TEXTURE_0 = RenderTypes.entityCutout(RenderPixie.TEXTURE_0, false);
    public static final RenderType TEXTURE_1 = RenderTypes.entityCutout(RenderPixie.TEXTURE_1, false);
    public static final RenderType TEXTURE_2 = RenderTypes.entityCutout(RenderPixie.TEXTURE_2, false);
    public static final RenderType TEXTURE_3 = RenderTypes.entityCutout(RenderPixie.TEXTURE_3, false);
    public static final RenderType TEXTURE_4 = RenderTypes.entityCutout(RenderPixie.TEXTURE_4, false);
    public static final RenderType TEXTURE_5 = RenderTypes.entityCutout(RenderPixie.TEXTURE_5, false);
    public static final RenderType TEXTURE_0_GLO = RenderTypes.eyes(RenderPixie.TEXTURE_0);
    public static final RenderType TEXTURE_1_GLO = RenderTypes.eyes(RenderPixie.TEXTURE_1);
    public static final RenderType TEXTURE_2_GLO = RenderTypes.eyes(RenderPixie.TEXTURE_2);
    public static final RenderType TEXTURE_3_GLO = RenderTypes.eyes(RenderPixie.TEXTURE_3);
    public static final RenderType TEXTURE_4_GLO = RenderTypes.eyes(RenderPixie.TEXTURE_4);
    public static final RenderType TEXTURE_5_GLO = RenderTypes.eyes(RenderPixie.TEXTURE_5);
    private final ModelPixie modelPixie = new ModelPixie();

    public RenderJar(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public @NotNull TileRenderState createRenderState() {
        return new TileRenderState();
    }

    @Override
    public void extractRenderState(@NotNull T jar, @NotNull TileRenderState state, float partialTick, @NotNull Vec3 cameraPos, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(jar, state, partialTick, cameraPos, breakProgress);
        state.tile = jar;
        state.partialTick = partialTick;
    }

    @Override
    public void submit(@NotNull TileRenderState state, @NotNull PoseStack matrixStackIn, @NotNull SubmitNodeCollector collector, @NotNull CameraRenderState camera) {
        TileEntityJar entity = (TileEntityJar) state.tile;
        if (entity.getLevel() == null || !entity.hasPixie) {
            return;
        }
        int meta = entity.pixieType;
        matrixStackIn.pushPose();
        matrixStackIn.translate(0.5F, 1.501F, 0.5F);
        matrixStackIn.mulPose(Axis.XP.rotationDegrees(180.0F));
        RenderType type;
        RenderType typeGlow;
        switch (meta) {
            default:
                type = TEXTURE_0;
                typeGlow = TEXTURE_0_GLO;
                break;
            case 1:
                type = TEXTURE_1;
                typeGlow = TEXTURE_1_GLO;
                break;
            case 2:
                type = TEXTURE_2;
                typeGlow = TEXTURE_2_GLO;
                break;
            case 3:
                type = TEXTURE_3;
                typeGlow = TEXTURE_3_GLO;
                break;
            case 4:
                type = TEXTURE_4;
                typeGlow = TEXTURE_4_GLO;
                break;
        }
        if (entity.hasProduced) {
            matrixStackIn.translate(0F, 0.90F, 0F);
        } else {
            matrixStackIn.translate(0F, 0.60F, 0F);
        }
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(this.interpolateRotation(entity.prevRotationYaw, entity.rotationYaw, state.partialTick)));
        matrixStackIn.scale(0.50F, 0.50F, 0.50F);
        PoseStates.Pixie pixieState = new PoseStates.Pixie();
        final float partialTick = state.partialTick;
        pixieState.pose = () -> modelPixie.animateInJar(entity.hasProduced, entity, 0, partialTick);
        collector.submitModel(modelPixie, pixieState, matrixStackIn, type, state.lightCoords, OverlayTexture.NO_OVERLAY, -1, null, 0, null);
        collector.submitModel(modelPixie, pixieState, matrixStackIn, typeGlow, state.lightCoords, OverlayTexture.NO_OVERLAY, -1, null, 0, null);
        matrixStackIn.popPose();
    }

    protected float interpolateRotation(float prevYawOffset, float yawOffset, float partialTicks) {
        float f;

        for (f = yawOffset - prevYawOffset; f < -180.0F; f += 360.0F) {
        }

        while (f >= 180.0F) {
            f -= 360.0F;
        }

        return prevYawOffset + partialTicks * f;
    }


}
