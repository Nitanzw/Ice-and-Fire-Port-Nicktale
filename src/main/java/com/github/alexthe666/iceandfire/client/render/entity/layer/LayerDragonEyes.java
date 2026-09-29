package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.iceandfire.client.model.DragonRenderState;
import com.github.alexthe666.iceandfire.client.model.util.TabulaModelHandlerHelper;
import com.github.alexthe666.iceandfire.client.render.TabulaModelAccessor;
import com.mojang.blaze3d.vertex.PoseStack;
import com.nicktale.api.client.model.AdvancedModelBox;
import com.nicktale.api.client.model.TabulaModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class LayerDragonEyes extends RenderLayer<DragonRenderState, TabulaModel> {
    private final TabulaModel fireHead;
    private final TabulaModel iceHead;
    private final TabulaModel lightningHead;

    public LayerDragonEyes(RenderLayerParent<DragonRenderState, TabulaModel> renderer) {
        super(renderer);
        try {
            fireHead = onlyKeepCubes(new TabulaModelAccessor(
                    TabulaModelHandlerHelper.loadTabulaModel("/assets/iceandfire/models/tabula/firedragon/firedragon_Ground")));
            iceHead = onlyKeepCubes(new TabulaModelAccessor(
                    TabulaModelHandlerHelper.loadTabulaModel("/assets/iceandfire/models/tabula/icedragon/icedragon_Ground")));
            lightningHead = onlyKeepCubes(new TabulaModelAccessor(
                    TabulaModelHandlerHelper.loadTabulaModel("/assets/iceandfire/models/tabula/lightningdragon/lightningdragon_Ground")));
        } catch (Exception exception) {
            throw new IllegalStateException("Could not load dragon eye models", exception);
        }
    }

    @Override
    public void submit(@NotNull PoseStack poseStack, @NotNull SubmitNodeCollector collector, int lightCoords,
                       DragonRenderState state, float yRot, float xRot) {
        if (!state.eyesVisible || state.eyeTexture == null) {
            return;
        }
        TabulaModel head = switch (state.dragonType) {
            case 1 -> iceHead;
            case 2 -> lightningHead;
            default -> fireHead;
        };
        copyPositions(head, getParentModel());
        collector.submitModel(head, state, poseStack, RenderTypes.eyes(state.eyeTexture), lightCoords,
                LivingEntityRenderer.getOverlayCoords(state, 0.0F), -1, null, state.outlineColor);
    }

    private static TabulaModel onlyKeepCubes(TabulaModelAccessor model) {
        Set<AdvancedModelBox> keep = new HashSet<>();
        AdvancedModelBox head = model.getCube("HeadFront");
        while (head != null && keep.add(head)) {
            head = head.getParent();
        }
        model.getCubes().values().forEach(box -> box.showModel = keep.contains(box));
        return model;
    }

    private static void copyPositions(TabulaModel model, TabulaModel modelTo) {
        model.resetToDefaultPose();
        for (AdvancedModelBox cube : model.getCubes().values()) {
            AdvancedModelBox target = modelTo.getCube(cube.boxName);
            if (target == null) {
                continue;
            }
            cube.rotateAngleX = target.rotateAngleX;
            cube.rotateAngleY = target.rotateAngleY;
            cube.rotateAngleZ = target.rotateAngleZ;
            cube.rotationPointX = target.rotationPointX;
            cube.rotationPointY = target.rotationPointY;
            cube.rotationPointZ = target.rotationPointZ;
        }
        model.updateDefaultPose();
    }
}
