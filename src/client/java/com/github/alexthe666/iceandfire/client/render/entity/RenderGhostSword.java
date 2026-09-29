package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.SimpleEntityRenderState;
import com.github.alexthe666.iceandfire.entity.EntityGhostSword;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ThrownItemRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class RenderGhostSword extends EntityRenderer<EntityGhostSword, RenderGhostSword.GhostSwordRenderState> {

    private final ItemModelResolver itemModelResolver;

    public RenderGhostSword(EntityRendererProvider.Context context) {
        super(context);
        this.itemModelResolver = context.getItemModelResolver();
    }

    @Override
    public @NotNull GhostSwordRenderState createRenderState() {
        return new GhostSwordRenderState();
    }

    @Override
    public void extractRenderState(@NotNull EntityGhostSword entity, @NotNull GhostSwordRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.entity = entity;
        state.partialTick = partialTick;
        state.yRot = Mth.lerp(partialTick, entity.yRotO, entity.getYRot());
        state.xRot = Mth.lerp(partialTick, entity.xRotO, entity.getXRot());
        state.age = entity.tickCount + partialTick;
        this.itemModelResolver.updateForNonLiving(state.item, new ItemStack(IafItemRegistry.GHOST_SWORD.get()), ItemDisplayContext.GROUND, entity);
    }

    @Override
    public void submit(@NotNull GhostSwordRenderState state, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector collector, @NotNull CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(state.yRot - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(state.xRot));
        poseStack.translate(0, 0.5F, 0);
        poseStack.scale(2F, 2F, 2F);
        poseStack.mulPose(Axis.ZN.rotationDegrees(state.age * 30.0F));
        poseStack.translate(0, -0.15F, 0);
        state.item.submit(poseStack, collector, 240, OverlayTexture.NO_OVERLAY, state.outlineColor);
        poseStack.popPose();
        super.submit(state, poseStack, collector, camera);
    }

    public static class GhostSwordRenderState extends SimpleEntityRenderState {
        public final net.minecraft.client.renderer.item.ItemStackRenderState item = new net.minecraft.client.renderer.item.ItemStackRenderState();
        public float age;
    }
}
