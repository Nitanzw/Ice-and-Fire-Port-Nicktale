package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.iceandfire.client.model.DragonRenderState;
import com.github.alexthe666.iceandfire.client.texture.ArrayLayeredTexture;
import com.github.alexthe666.iceandfire.entity.DragonType;
import com.mojang.blaze3d.vertex.PoseStack;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class LayerDragonArmor extends RenderLayer<DragonRenderState, com.nicktale.api.client.model.TabulaModel> {
    private static final Map<String, Identifier> LAYERED_ARMOR_CACHE = Maps.newHashMap();

    public LayerDragonArmor(RenderLayerParent<DragonRenderState, com.nicktale.api.client.model.TabulaModel> renderer) {
        super(renderer);
    }

    public static void clearCache(String key) {
        LAYERED_ARMOR_CACHE.remove(key);
    }

    @Override
    public void submit(@NotNull PoseStack poseStack, @NotNull SubmitNodeCollector collector, int lightCoords,
                       DragonRenderState state, float yRot, float xRot) {
        if (state.armorHead == 0 && state.armorNeck == 0 && state.armorLegs == 0 && state.armorFeet == 0) {
            return;
        }
        String type = switch (state.dragonType) {
            case 0 -> DragonType.FIRE.getName();
            case 1 -> DragonType.ICE.getName();
            default -> DragonType.LIGHTNING.getName();
        };
        String key = type + "_" + state.armorHead + "_" + state.armorNeck + "_" + state.armorLegs + "_" + state.armorFeet;
        Identifier texture = LAYERED_ARMOR_CACHE.get(key);
        if (texture == null) {
            texture = Identifier.fromNamespaceAndPath("iceandfire", "dragon_armor_" + key);
            var layers = Arrays.stream(state.armorLayerTextures).map(Identifier::toString).collect(Collectors.toList());
            Minecraft.getInstance().getTextureManager().register(texture, new ArrayLayeredTexture(layers));
            LAYERED_ARMOR_CACHE.put(key, texture);
        }
        collector.submitModel(getParentModel(), state, poseStack, RenderTypes.entityCutoutCull(texture), lightCoords,
                LivingEntityRenderer.getOverlayCoords(state, 0.0F), -1, null, state.outlineColor, null);
    }
}
