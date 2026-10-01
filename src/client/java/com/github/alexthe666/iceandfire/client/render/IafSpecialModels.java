package com.github.alexthe666.iceandfire.client.render;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.model.ModelDeathWormGauntlet;
import com.github.alexthe666.iceandfire.client.model.ModelGorgonHead;
import com.github.alexthe666.iceandfire.client.model.ModelGorgonHeadActive;
import com.github.alexthe666.iceandfire.client.model.ModelTideTrident;
import com.github.alexthe666.iceandfire.client.model.ModelTrollWeapon;
import com.github.alexthe666.iceandfire.client.render.entity.RenderDeathWorm;
import com.github.alexthe666.iceandfire.client.render.entity.RenderTideTrident;
import com.github.alexthe666.iceandfire.client.render.tile.PoseStates;
import com.github.alexthe666.iceandfire.client.render.tile.RenderPixieHouse;
import com.github.alexthe666.iceandfire.entity.props.EntityDataProvider;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.item.ItemStackData;
import com.github.alexthe666.iceandfire.item.ItemTrollWeapon;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.nicktale.api.client.item.StackSpecialModel;
import com.nicktale.api.client.model.AdvancedEntityModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterSpecialModelRendererEvent;


/**
 * Special item model renderers replacing the 1.20 BlockEntityWithoutLevelRenderer classes. Each is referenced from an
 * item model definition ({@code assets/iceandfire/items/*.json}) as {@code iceandfire:<name>}.
 */
@EventBusSubscriber(value = Dist.CLIENT, modid = IceAndFire.MODID)
public final class IafSpecialModels {
    private IafSpecialModels() {
    }

    private static final StackSpecialModel.Unbaked TIDE_TRIDENT = new StackSpecialModel.Unbaked(() -> {
        ModelTideTrident model = new ModelTideTrident();
        return (stack, pose, collector, light, overlay, foil, outline) -> {
            pose.pushPose();
            pose.translate(0.5F, 0.5F, 0.5F);
            pose.mulPose(Axis.XP.rotationDegrees(160.0F));
            collector.submitModel(model, new PoseStates.Generic(model::resetToDefaultPose), pose, RenderTypes.entityCutout(RenderTideTrident.TRIDENT), light, overlay, -1, null, outline, null);
            pose.popPose();
        };
    });

    private static final StackSpecialModel.Unbaked GORGON_HEAD = new StackSpecialModel.Unbaked(() -> {
        AdvancedEntityModel<?> active = new ModelGorgonHeadActive();
        AdvancedEntityModel<?> inactive = new ModelGorgonHead();
        RenderType activeType = RenderTypes.entityCutout(Identifier.parse("iceandfire:textures/models/gorgon/head_active.png"));
        RenderType inactiveType = RenderTypes.entityCutout(Identifier.parse("iceandfire:textures/models/gorgon/head_inactive.png"));
        return (stack, pose, collector, light, overlay, foil, outline) -> {
            boolean isActive = stack.getItem() == IafItemRegistry.GORGON_HEAD.get() && ItemStackData.has(stack)
                && ItemStackData.get(stack).getBooleanOr("Active", false);
            AdvancedEntityModel<?> model = isActive ? active : inactive;
            pose.pushPose();
            pose.translate(0.5F, isActive ? 1.5F : 1.25F, 0.5F);
            submitStatic(collector, model, pose, isActive ? activeType : inactiveType, light, overlay, outline);
            pose.popPose();
        };
    });

    private static final StackSpecialModel.Unbaked DEATHWORM_GAUNTLET = new StackSpecialModel.Unbaked(() -> {
        ModelDeathWormGauntlet model = new ModelDeathWormGauntlet();
        return (stack, pose, collector, light, overlay, foil, outline) -> {
            RenderType texture;
            if (stack.getItem() == IafItemRegistry.DEATHWORM_GAUNTLET_RED.get()) {
                texture = RenderTypes.entityCutout(RenderDeathWorm.TEXTURE_RED);
            } else if (stack.getItem() == IafItemRegistry.DEATHWORM_GAUNTLET_WHITE.get()) {
                texture = RenderTypes.entityCutout(RenderDeathWorm.TEXTURE_WHITE);
            } else {
                texture = RenderTypes.entityCutout(RenderDeathWorm.TEXTURE_YELLOW);
            }
            pose.pushPose();
            pose.translate(0.5F, 0.5F, 0.5F);
            float partialTick = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false);
            PoseStates.Generic state = new PoseStates.Generic(() -> model.animate(lungeTicks(partialTick)));
            collector.submitModel(model, state, pose, texture, light, overlay, -1, null, outline, null);
            pose.popPose();
        };
    });

    private static final StackSpecialModel.Unbaked TROLL_WEAPON = new StackSpecialModel.Unbaked(() -> {
        ModelTrollWeapon model = new ModelTrollWeapon();
        return (stack, pose, collector, light, overlay, foil, outline) -> {
            com.github.alexthe666.iceandfire.enums.EnumTroll.Weapon weapon = com.github.alexthe666.iceandfire.enums.EnumTroll.Weapon.AXE;
            if (stack.getItem() instanceof ItemTrollWeapon trollWeapon) {
                weapon = trollWeapon.weapon;
            }
            pose.pushPose();
            pose.translate(0.5F, -0.75F, 0.5F);
            submitStatic(collector, model, pose, RenderTypes.entityCutout(weapon.TEXTURE), light, overlay, outline);
            pose.popPose();
        };
    });

    private static final StackSpecialModel.Unbaked PIXIE_HOUSE = new StackSpecialModel.Unbaked(() -> (stack, pose, collector, light, overlay, foil, outline) -> {
        if (stack.getItem() instanceof BlockItem blockItem) {
            RenderPixieHouse.submitItemHouse(blockItem, pose, collector, light, overlay);
        }
    });

    private static float lungeTicks(float partialTick) {
        var player = Minecraft.getInstance().player;
        if (player == null) {
            return 0;
        }
        return EntityDataProvider.getCapability(player).map(data -> data.miscData.lungeTicks + partialTick).orElse(0F);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void submitStatic(SubmitNodeCollector collector, AdvancedEntityModel model, PoseStack pose, RenderType type, int light, int overlay, int outline) {
        collector.submitModel(model, new PoseStates.Generic(model::resetToDefaultPose), pose, type, light, overlay, -1, null, outline, null);
    }

    @SubscribeEvent
    public static void register(RegisterSpecialModelRendererEvent event) {
        event.register(id("tide_trident"), TIDE_TRIDENT.type());
        event.register(id("gorgon_head"), GORGON_HEAD.type());
        event.register(id("deathworm_gauntlet"), DEATHWORM_GAUNTLET.type());
        event.register(id("troll_weapon"), TROLL_WEAPON.type());
        event.register(id("pixie_house"), PIXIE_HOUSE.type());
    }

    private static Identifier id(String name) {
        return Identifier.fromNamespaceAndPath(IceAndFire.MODID, name);
    }
}
