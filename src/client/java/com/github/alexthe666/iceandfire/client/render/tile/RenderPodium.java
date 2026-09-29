package com.github.alexthe666.iceandfire.client.render.tile;

import com.github.alexthe666.iceandfire.client.model.ModelDragonEgg;
import com.github.alexthe666.iceandfire.client.render.entity.RenderDragonEgg;
import com.github.alexthe666.iceandfire.client.render.entity.RenderMyrmexEgg;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityPodium;
import com.github.alexthe666.iceandfire.enums.EnumDragonEgg;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.item.ItemDragonEgg;
import com.github.alexthe666.iceandfire.item.ItemMyrmexEgg;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.phys.Vec3;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class RenderPodium<T extends TileEntityPodium> implements BlockEntityRenderer<T, RenderPodium.PodiumRenderState> {

    private final ModelDragonEgg model = new ModelDragonEgg();
    private final ItemModelResolver itemModelResolver;

    public RenderPodium(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    protected static RenderType getEggTexture(EnumDragonEgg type) {
        return switch (type) {
            default -> RenderTypes.entityCutout(RenderDragonEgg.EGG_RED);
            case GREEN -> RenderTypes.entityCutout(RenderDragonEgg.EGG_GREEN);
            case BRONZE -> RenderTypes.entityCutout(RenderDragonEgg.EGG_BRONZE);
            case GRAY -> RenderTypes.entityCutout(RenderDragonEgg.EGG_GREY);
            case BLUE -> RenderTypes.entityCutout(RenderDragonEgg.EGG_BLUE);
            case WHITE -> RenderTypes.entityCutout(RenderDragonEgg.EGG_WHITE);
            case SAPPHIRE -> RenderTypes.entityCutout(RenderDragonEgg.EGG_SAPPHIRE);
            case SILVER -> RenderTypes.entityCutout(RenderDragonEgg.EGG_SILVER);
            case ELECTRIC -> RenderTypes.entityCutout(RenderDragonEgg.EGG_ELECTRIC);
            case AMYTHEST -> RenderTypes.entityCutout(RenderDragonEgg.EGG_AMYTHEST);
            case COPPER -> RenderTypes.entityCutout(RenderDragonEgg.EGG_COPPER);
            case BLACK -> RenderTypes.entityCutout(RenderDragonEgg.EGG_BLACK);
        };
    }

    @Override
    public @NotNull PodiumRenderState createRenderState() {
        return new PodiumRenderState();
    }

    @Override
    public void extractRenderState(@NotNull T podium, @NotNull PodiumRenderState state, float partialTick, @NotNull Vec3 cameraPos, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(podium, state, partialTick, cameraPos, breakProgress);
        state.tile = podium;
        state.partialTick = partialTick;
        state.item.clear();
        ItemStack stack = podium.getItem(0);
        if (!stack.isEmpty() && !(stack.getItem() instanceof ItemDragonEgg) && !(stack.getItem() instanceof ItemMyrmexEgg)) {
            this.itemModelResolver.updateForTopItem(state.item, stack, ItemDisplayContext.FIXED, podium.getLevel(), null, 0);
        }
    }

    @Override
    public void submit(@NotNull PodiumRenderState state, @NotNull PoseStack matrixStackIn, @NotNull SubmitNodeCollector collector, @NotNull CameraRenderState camera) {
        TileEntityPodium podium = (TileEntityPodium) state.tile;
        int light = state.lightCoords;
        if (!podium.getItem(0).isEmpty()) {
            PoseStates.Egg eggState = new PoseStates.Egg();
            eggState.pose = () -> {
                model.resetToDefaultPose();
                model.renderPodium();
            };
            if (podium.getItem(0).getItem() instanceof ItemDragonEgg) {
                ItemDragonEgg item = (ItemDragonEgg) podium.getItem(0).getItem();
                matrixStackIn.pushPose();
                matrixStackIn.translate(0.5F, 0.475F, 0.5F);
                collector.submitModel(model, eggState, matrixStackIn, RenderPodium.getEggTexture(item.type), light, OverlayTexture.NO_OVERLAY, -1, null, 0, null);
                matrixStackIn.popPose();
            } else if (podium.getItem(0).getItem() instanceof ItemMyrmexEgg) {
                boolean jungle = podium.getItem(0).getItem() == IafItemRegistry.MYRMEX_JUNGLE_EGG.get();
                matrixStackIn.pushPose();
                matrixStackIn.translate(0.5F, 0.475F, 0.5F);
                collector.submitModel(model, eggState, matrixStackIn, RenderTypes.entityCutout(jungle ? RenderMyrmexEgg.EGG_JUNGLE : RenderMyrmexEgg.EGG_DESERT), light, OverlayTexture.NO_OVERLAY, -1, null, 0, null);
                matrixStackIn.popPose();
            } else if (!state.item.isEmpty()) {
                matrixStackIn.pushPose();
                float f2 = ((float) podium.prevTicksExisted + (podium.ticksExisted - podium.prevTicksExisted) * state.partialTick);
                float f3 = Mth.sin(f2 / 10.0F) * 0.1F + 0.1F;
                matrixStackIn.translate(0.5F, 1.55F + f3, 0.5F);
                float f4 = (f2 / 20.0F);
                matrixStackIn.mulPose(Axis.YP.rotation(f4));
                matrixStackIn.pushPose();
                matrixStackIn.translate(0, 0.2F, 0);
                matrixStackIn.scale(0.65F, 0.65F, 0.65F);
                state.item.submit(matrixStackIn, collector, light, OverlayTexture.NO_OVERLAY, 0);
                matrixStackIn.popPose();
                matrixStackIn.popPose();
            }
        }
    }

    public static class PodiumRenderState extends TileRenderState {
    }
}
