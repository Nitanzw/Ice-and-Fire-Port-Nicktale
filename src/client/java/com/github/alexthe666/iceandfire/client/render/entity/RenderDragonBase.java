package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.DragonRenderState;
import com.github.alexthe666.iceandfire.client.ClientProxy;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerDragonBanner;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerDragonRider;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerDragonArmor;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerDragonEyes;
import com.github.alexthe666.iceandfire.client.texture.ArrayLayeredTexture;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityDreadQueen;
import com.github.alexthe666.iceandfire.enums.EnumDragonTextures;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.nicktale.api.client.model.TabulaModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.animal.equine.HorseModel;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RenderDragonBase extends MobRenderer<EntityDragonBase, DragonRenderState, TabulaModel> {

    private final Map<String, Identifier> layeredTextureCache = Maps.newHashMap();
    private final int dragonType;

    public RenderDragonBase(EntityRendererProvider.Context context, TabulaModel model, int dragonType) {
        super(context, model, 0.15F);
        this.dragonType = dragonType;
        this.addLayer(new LayerDragonEyes(this));
        this.addLayer(new LayerDragonRider(this, false));
        this.addLayer(new LayerDragonBanner(this));
        this.addLayer(new LayerDragonArmor(this));
    }

    @Override
    protected DragonRenderState createRenderState() {
        return new DragonRenderState();
    }

    @Override
    public void extractRenderState(EntityDragonBase entity, DragonRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.animation = entity.getAnimation();
        state.animationTick = entity.getAnimationTick();
        state.dragonType = entity.dragonType.getIntFromType();
        state.variant = entity.getVariant();
        state.dragonStage = entity.getDragonStage();
        state.armorHead = entity.getArmorOrdinal(entity.getItemBySlot(EquipmentSlot.HEAD));
        state.armorNeck = entity.getArmorOrdinal(entity.getItemBySlot(EquipmentSlot.CHEST));
        state.armorLegs = entity.getArmorOrdinal(entity.getItemBySlot(EquipmentSlot.LEGS));
        state.armorFeet = entity.getArmorOrdinal(entity.getItemBySlot(EquipmentSlot.FEET));
        state.renderSize = entity.getRenderSize();
        state.previousDragonPitch = entity.prevDragonPitch;
        state.dragonPitch = entity.getDragonPitch();
        state.turnBufferYawDegrees = entity.turn_buffer == null ? 0.0F : entity.turn_buffer.getInterpolatedYawVariation(partialTicks);
        state.tailBufferYawDegrees = entity.tail_buffer == null ? 0.0F : entity.tail_buffer.getInterpolatedYawVariation(partialTicks);
        state.rollBufferYawDegrees = entity.roll_buffer == null ? 0.0F : entity.roll_buffer.getInterpolatedYawVariation(partialTicks);
        state.pitchBufferBodyPitchDegrees = entity.pitch_buffer_body == null ? 0.0F : entity.pitch_buffer_body.getInterpolatedPitchVariation(partialTicks);
        state.pitchBufferPitchDegrees = entity.pitch_buffer == null ? 0.0F : entity.pitch_buffer.getInterpolatedPitchVariation(partialTicks);
        state.male = entity.isMale();
        state.skeletal = entity.isSkeletal();
        state.sleeping = entity.isSleeping();
        state.blinking = entity.isBlinking();
        state.modelDead = entity.isModelDead();
        state.aiDisabled = entity.isAiDisabled();
        state.hovering = entity.isHovering();
        state.flying = entity.isFlying();
        state.swimming = entity.isInWater();
        state.breathingFire = entity.isBreathingFire();
        state.actuallyBreathingFire = entity.isActuallyBreathingFire();
        state.vehicle = entity.isVehicle();
        state.passenger = entity.isPassenger();
        state.eyesVisible = entity.shouldRenderEyes();
        state.riders.clear();
        EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        Entity controllingPassenger = entity.getControllingPassenger();
        for (Entity passenger : entity.getPassengers()) {
            EntityRenderState passengerState = dispatcher.extractEntity(passenger, partialTicks);
            EntityRenderer<?, ?> passengerRenderer = dispatcher.getRenderer(passengerState);
            EntityModel<?> passengerModel = passengerRenderer instanceof MobRenderer<?, ?, ?> mobRenderer
                    ? mobRenderer.getModel()
                    : null;
            state.riders.add(new DragonRenderState.Rider(
                    passengerState,
                    passenger.getUUID(),
                    controllingPassenger == null || controllingPassenger.getId() != passenger.getId(),
                    passenger instanceof EntityDreadQueen,
                    passenger.yRotO + (passenger.getYRot() - passenger.yRotO) * partialTicks,
                    passengerModel instanceof net.minecraft.client.model.HumanoidModel<?>,
                    passengerModel instanceof net.minecraft.client.model.QuadrupedModel<?>,
                    passengerModel instanceof HorseModel));
            ClientProxy.currentDragonRiders.add(passenger.getUUID());
        }
        ItemStack banner = entity.getItemInHand(InteractionHand.OFF_HAND);
        state.bannerItem.clear();
        if (!banner.isEmpty() && banner.getItem() instanceof BannerItem) {
            Minecraft.getInstance().getItemModelResolver().updateForLiving(
                    state.bannerItem, banner, ItemDisplayContext.NONE, entity);
        }
        state.walkCycle = entity.walkCycle;
        state.flightCycle = entity.flightCycle;
        state.swimCycle = entity.swimCycle;
        state.swimProgress = entity.swimProgress;
        state.sitProgress = entity.sitProgress;
        state.sleepProgress = entity.sleepProgress;
        state.hoverProgress = entity.hoverProgress;
        state.flyProgress = entity.flyProgress;
        state.tackleProgress = entity.tackleProgress;
        state.ridingProgress = entity.ridingProgress;
        state.diveProgress = entity.diveProgress;
        state.previousDiveProgress = entity.prevDiveProgress;
        state.fireBreathProgress = entity.fireBreathProgress;
        state.previousFireBreathProgress = entity.prevFireBreathProgress;
        state.modelDeadProgress = entity.modelDeadProgress;
        state.previousModelDeadProgress = entity.prevModelDeadProgress;
        state.previousAnimationProgresses = entity.prevAnimationProgresses.clone();
        state.baseTexture = EnumDragonTextures.getTextureFromDragon(entity);
        state.emptyOverlay = EnumDragonTextures.Armor.EMPTY.FIRETEXTURE;
        state.maleOverlay = switch (dragonType) {
            case 0 -> EnumDragonTextures.getDragonEnum(entity).FIRE_MALE_OVERLAY;
            case 1 -> EnumDragonTextures.getDragonEnum(entity).ICE_MALE_OVERLAY;
            case 2 -> EnumDragonTextures.getDragonEnum(entity).LIGHTNING_MALE_OVERLAY;
            default -> null;
        };
        state.eyeTexture = EnumDragonTextures.getEyeTextureFromDragon(entity);
        EquipmentSlot[] slots = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
        for (int i = 0; i < slots.length; i++) {
            EnumDragonTextures.Armor armor = EnumDragonTextures.Armor.getArmorForDragon(entity, slots[i]);
            state.armorLayerTextures[i] = switch (dragonType) {
                case 0 -> armor.FIRETEXTURE;
                case 1 -> armor.ICETEXTURE;
                default -> armor.LIGHTNINGTEXTURE;
            };
        }
    }

    @Override
    protected void scale(DragonRenderState state, PoseStack poseStack) {
        float scale = state.renderSize / 3.0F;
        this.shadowRadius = scale;
        float pitch = state.previousDragonPitch + (state.dragonPitch - state.previousDragonPitch) * state.partialTick;
        poseStack.mulPose(Axis.XP.rotationDegrees(pitch));
        poseStack.scale(scale, scale, scale);
    }

    @Override
    public @NotNull Identifier getTextureLocation(DragonRenderState state) {
        String baseTexture = state.dragonType + "_" + state.variant + "_" + state.dragonStage + "_"
                + state.modelDead + "_" + state.male + "_" + state.skeletal + "_" + state.sleeping + "_" + state.blinking;
        Identifier texture = layeredTextureCache.get(baseTexture);
        if (texture == null) {
            texture = Identifier.fromNamespaceAndPath("iceandfire", "dragon_texture_" + baseTexture);
            List<String> layers = new ArrayList<>(2);
            layers.add(state.baseTexture.toString());
            layers.add(state.male && !state.skeletal && state.maleOverlay != null
                    ? state.maleOverlay.toString()
                    : state.emptyOverlay.toString());
            Minecraft.getInstance().getTextureManager().register(texture, new ArrayLayeredTexture(layers));
            layeredTextureCache.put(baseTexture, texture);
        }
        return texture;
    }
}
