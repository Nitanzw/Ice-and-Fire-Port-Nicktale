package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelMyrmexBase;
import com.github.alexthe666.iceandfire.client.model.ModelMyrmexLarva;
import com.github.alexthe666.iceandfire.client.model.ModelMyrmexPupa;
import com.github.alexthe666.iceandfire.client.model.MyrmexRenderState;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerMyrmexItem;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexWorker;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class RenderMyrmexBase extends IafMobRenderer<EntityMyrmexBase, MyrmexRenderState, EntityModel<MyrmexRenderState>> {

    private final EntityModel<MyrmexRenderState> larvaModel = new ModelMyrmexLarva();
    private final EntityModel<MyrmexRenderState> pupaModel = new ModelMyrmexPupa();
    private final EntityModel<MyrmexRenderState> adultModel;

    public RenderMyrmexBase(EntityRendererProvider.Context context, ModelMyrmexBase<MyrmexRenderState> model, float shadowSize) {
        super(context, model, shadowSize);
        this.adultModel = model;
        this.addLayer(new LayerMyrmexItem(this));
    }

    @Override
    public @NotNull MyrmexRenderState createRenderState() {
        return new MyrmexRenderState();
    }

    @Override
    protected void extract(EntityMyrmexBase entity, MyrmexRenderState state, float partialTick) {
        state.tickCount = entity.tickCount;
        state.growthStage = entity.getGrowthStage();
        state.onGround = entity.onGround();
        state.hasPassengers = entity.isVehicle();
        state.heldItem.clear();
        state.holdsBlockItem = false;
        if (entity instanceof EntityMyrmexWorker) {
            ItemStack stack = entity.getItemInHand(InteractionHand.MAIN_HAND);
            if (!stack.isEmpty()) {
                this.itemModelResolver.updateForLiving(state.heldItem, stack, ItemDisplayContext.FIXED, entity);
                state.holdsBlockItem = stack.getItem() instanceof BlockItem;
            }
        }
    }

    @Override
    public void submit(@NotNull MyrmexRenderState state, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector collector, @NotNull CameraRenderState camera) {
        if (state.growthStage == 0) {
            this.model = larvaModel;
        } else if (state.growthStage == 1) {
            this.model = pupaModel;
        } else {
            this.model = adultModel;
        }
        super.submit(state, poseStack, collector, camera);
    }

    public EntityModel<MyrmexRenderState> getAdultModel() {
        return adultModel;
    }

    @Override
    protected void scaleFor(EntityMyrmexBase myrmex, @NotNull PoseStack poseStack, float partialTick) {
        float scale = myrmex.getModelScale();
        if (myrmex.getGrowthStage() == 0) {
            scale /= 2;
        }
        if (myrmex.getGrowthStage() == 1) {
            scale /= 1.5F;
        }
        poseStack.scale(scale, scale, scale);
        if (myrmex.isPassenger() && myrmex.getGrowthStage() < 2) {
            poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
        }
    }

    @Override
    protected @NotNull Identifier textureFor(EntityMyrmexBase myrmex) {
        return myrmex.getTexture();
    }

}
