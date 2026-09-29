package com.github.alexthe666.iceandfire.event;

import net.neoforged.neoforge.event.tick.EntityTickEvent;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.ClientProxy;
import com.github.alexthe666.iceandfire.client.IafKeybindRegistry;
import com.github.alexthe666.iceandfire.client.gui.IceAndFireMainMenu;
import com.github.alexthe666.iceandfire.client.particle.CockatriceBeamRender;
import com.github.alexthe666.iceandfire.client.render.entity.RenderChain;
import com.github.alexthe666.iceandfire.client.render.pathfinding.PathfindingDebugRenderer;
import com.github.alexthe666.iceandfire.client.render.tile.RenderFrozenState;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.props.EntityDataProvider;
import com.github.alexthe666.iceandfire.entity.util.ICustomMoveController;
import com.github.alexthe666.iceandfire.enums.EnumParticles;
import com.github.alexthe666.iceandfire.message.MessageDragonControl;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.event.SubmitCustomGeometryEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.event.entity.EntityMountEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.bus.api.SubscribeEvent;

import java.util.Random;

public class ClientEvents {

    private static final Identifier SIREN_SHADER = Identifier.parse("iceandfire:siren");
    /** Source entity of a living render state, attached by the render state modifier registered in {@link IafClientSetup}. */
    public static final net.minecraft.util.context.ContextKey<LivingEntity> LIVING_KEY = new net.minecraft.util.context.ContextKey<>(Identifier.parse("iceandfire:living_entity"));

    private final Random rand = new Random();

    private static boolean shouldCancelRender(LivingEntity living) {
        if (living.getVehicle() != null && living.getVehicle() instanceof EntityDragonBase) {
            return ClientProxy.currentDragonRiders.contains(living.getUUID()) || living == Minecraft.getInstance().player && Minecraft.getInstance().options.getCameraType().isFirstPerson();
        }
        return false;
    }

    @SubscribeEvent
    public void submitPathfindingGeometry(final SubmitCustomGeometryEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null || minecraft.player == null) {
            return;
        }

        PathfindingDebugRenderer.render(
                event.getSubmitNodeCollector(),
                event.getPoseStack(),
                minecraft.gameRenderer.mainCamera().position());
    }

    @SubscribeEvent
    public void onCameraDistance(net.neoforged.neoforge.client.event.CalculateDetachedCameraDistanceEvent event) {
        Player player = Minecraft.getInstance().player;
        if (player != null && player.getVehicle() instanceof EntityDragonBase dragon) {
            int currentView = IceAndFire.PROXY.getDragon3rdPersonView();
            float scale = dragon.getRenderSize() / 3;
            if (currentView == 1) {
                event.setDistance(event.getDistance() + scale * 1.2F);
            } else if (currentView == 2) {
                event.setDistance(event.getDistance() + scale * 3F);
            } else if (currentView == 3) {
                event.setDistance(event.getDistance() + scale * 5F);
            }
        }
    }

    @SubscribeEvent
    public void onLivingUpdate(EntityTickEvent.Pre event) {
        if (!(event.getEntity() instanceof net.minecraft.world.entity.LivingEntity iafLiving)) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        if (iafLiving instanceof ICustomMoveController) {
            Entity entity = iafLiving;
            ICustomMoveController moveController = ((Entity & ICustomMoveController) iafLiving);
            if (entity.getVehicle() != null && entity.getVehicle() == mc.player) {
                byte previousState = moveController.getControlState();
                moveController.dismount(mc.options.keyShift.isDown());
                byte controlState = moveController.getControlState();
                if (controlState != previousState) {
                    IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageDragonControl(entity.getId(), controlState, entity.getX(), entity.getY(), entity.getZ()));
                }
            }
        }
        if (iafLiving instanceof Player player) {
            if (player.level().isClientSide()) {

                if (player.getVehicle() instanceof ICustomMoveController) {
                    Entity entity = player.getVehicle();
                    ICustomMoveController moveController = ((Entity & ICustomMoveController) player.getVehicle());
                    byte previousState = moveController.getControlState();
                    moveController.up(mc.options.keyJump.isDown());
                    moveController.down(IafKeybindRegistry.dragon_down.isDown());
                    moveController.attack(IafKeybindRegistry.dragon_strike.isDown());
                    moveController.dismount(mc.options.keyShift.isDown());
                    moveController.strike(IafKeybindRegistry.dragon_fireAttack.isDown());
                    byte controlState = moveController.getControlState();
                    if (controlState != previousState) {
                        IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageDragonControl(entity.getId(), controlState, entity.getX(), entity.getY(), entity.getZ()));
                    }
                }
            }
            if (player.level().isClientSide() && IafKeybindRegistry.dragon_change_view.isDown()) {
                int currentView = IceAndFire.PROXY.getDragon3rdPersonView();
                if (currentView + 1 > 3) {
                    currentView = 0;
                } else {
                    currentView++;
                }
                IceAndFire.PROXY.setDragon3rdPersonView(currentView);
            }

            if (player.level().isClientSide()) {
                GameRenderer renderer = Minecraft.getInstance().gameRenderer;

                EntityDataProvider.getCapability(player).ifPresent(data -> {
                    if (IafConfig.sirenShader && data.sirenData.charmedBy == null && renderer.currentPostEffect() != null) {
                        if (SIREN_SHADER.equals(renderer.currentPostEffect()))
                            renderer.clearPostEffect();
                    }

                    if (data.sirenData.charmedBy == null) {
                        return;
                    }

                    if (IafConfig.sirenShader && !data.sirenData.isCharmed && renderer.currentPostEffect() != null && SIREN_SHADER.equals(renderer.currentPostEffect())) {
                        renderer.clearPostEffect();
                    }

                if (data.sirenData.isCharmed) {
                    if (player.level().isClientSide() && rand.nextInt(40) == 0) {
                        IceAndFire.PROXY.spawnParticle(EnumParticles.Siren_Appearance, player.getX(), player.getY(), player.getZ(), data.sirenData.charmedBy.getHairColor(), 0, 0);
                    }

                        if (IafConfig.sirenShader && renderer.currentPostEffect() == null) {
                            renderer.setPostEffect(SIREN_SHADER);
                        }

                    }
                });
            }
        }
    }

    @SubscribeEvent
    public void onPreRenderLiving(RenderLivingEvent.Pre<?, ?, ?> event) {
        LivingEntity entity = event.getRenderState().getRenderData(LIVING_KEY);
        if (entity != null && shouldCancelRender(entity)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPostRenderLiving(RenderLivingEvent.Post<?, ?, ?> event) {
        LivingEntity entity = event.getRenderState().getRenderData(LIVING_KEY);
        if (entity == null) {
            return;
        }
        if (shouldCancelRender(entity)) {
            return;
        }

        EntityDataProvider.getCapability(entity).ifPresent(data -> {
            for (LivingEntity target : data.miscData.getTargetedByScepter()) {
                CockatriceBeamRender.render(entity, target, event.getPoseStack(), event.getSubmitNodeCollector(), event.getPartialTick());
            }

            if (data.frozenData.isFrozen) {
                RenderFrozenState.render(entity, event.getPoseStack(), event.getSubmitNodeCollector(), event.getRenderState().lightCoords, data.frozenData.frozenTicks);
            }

            RenderChain.render(entity, event.getPartialTick(), event.getPoseStack(), event.getSubmitNodeCollector(), event.getRenderState().lightCoords, data.chainData.getChainedTo());
        });
    }

    @SubscribeEvent
    public void onGuiOpened(ScreenEvent.Opening event) {
        if (IafConfig.customMainMenu && event.getScreen() instanceof TitleScreen && !(event.getScreen() instanceof IceAndFireMainMenu)) {
            event.setNewScreen(new IceAndFireMainMenu());
        }
    }

    // TODO: add this to client side config
    public final boolean AUTO_ADAPT_3RD_PERSON = true;

    @SubscribeEvent
    public void onEntityMount(EntityMountEvent event) {
        if (event.getEntityBeingMounted() instanceof EntityDragonBase dragon && event.getLevel().isClientSide() && event.getEntityMounting() == Minecraft.getInstance().player) {
            if (dragon.isTame() && dragon.isOwnedBy(Minecraft.getInstance().player)) {
                if (AUTO_ADAPT_3RD_PERSON) {
                    // Auto adjust 3rd person camera's according to dragon's size
                    IceAndFire.PROXY.setDragon3rdPersonView(2);
                }
                if (IafConfig.dragonAuto3rdPerson) {
                    if (event.isDismounting()) {
                        Minecraft.getInstance().options.setCameraType(CameraType.values()[IceAndFire.PROXY.getPreviousViewType()]);
                    } else {
                        IceAndFire.PROXY.setPreviousViewType(Minecraft.getInstance().options.getCameraType().ordinal());
                        Minecraft.getInstance().options.setCameraType(CameraType.values()[1]);
                    }
                }
            }
        }
    }
}
