package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ChainTieRenderState;
import com.github.alexthe666.iceandfire.client.model.ModelChainTie;
import com.github.alexthe666.iceandfire.entity.EntityChainTie;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.CameraRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public class RenderChainTie extends EntityRenderer<EntityChainTie, ChainTieRenderState> {
    private static final Identifier TEXTURE = Identifier.parse("iceandfire:textures/models/misc/chain_tie.png");
    private final ModelChainTie leashKnotModel = new ModelChainTie();

    public RenderChainTie(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected ChainTieRenderState createRenderState() {
        return new ChainTieRenderState();
    }

    @Override
    public void extractRenderState(EntityChainTie entity, ChainTieRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.knotYaw = entity.getYRot();
        state.knotPitch = entity.getXRot();
    }

    @Override
    public void submit(ChainTieRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        super.submit(state, poseStack, collector, camera);
        poseStack.pushPose();
        poseStack.translate(0.0F, 0.5F, 0.0F);
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        collector.submitModel(this.leashKnotModel, state, poseStack,
                RenderTypes.entityCutoutNoCull(TEXTURE), state.lightCoords, OverlayTexture.NO_OVERLAY,
                -1, null, state.outlineColor, null);
        poseStack.popPose();
    }

    @Override
    public @NotNull Identifier getTextureLocation(@NotNull ChainTieRenderState state) {
        return TEXTURE;
    }
}
