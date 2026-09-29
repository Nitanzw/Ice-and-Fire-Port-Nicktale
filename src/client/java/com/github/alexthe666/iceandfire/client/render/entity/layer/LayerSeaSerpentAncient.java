package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.iceandfire.client.model.SeaSerpentRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import com.nicktale.api.client.model.TabulaModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public class LayerSeaSerpentAncient extends IafRenderLayer<SeaSerpentRenderState, TabulaModel> {

    private static final Identifier TEXTURE = Identifier.parse("iceandfire:textures/models/seaserpent/ancient_overlay.png");
    private static final Identifier TEXTURE_BLINK = Identifier.parse("iceandfire:textures/models/seaserpent/ancient_overlay_blink.png");

    public LayerSeaSerpentAncient(RenderLayerParent<SeaSerpentRenderState, TabulaModel> renderer) {
        super(renderer);
    }

    @Override
    public void submit(@NotNull PoseStack poseStack, @NotNull SubmitNodeCollector collector, int light, @NotNull SeaSerpentRenderState state, float yRot, float xRot) {
        if (state.ancient) {
            collector.submitModel(getParentModel(), state, poseStack, RenderTypes.entityCutout(state.blinking ? TEXTURE_BLINK : TEXTURE), light,
                LivingEntityRenderer.getOverlayCoords(state, 0.0F), -1, null, state.outlineColor, null);
        }
    }
}
