package com.github.alexthe666.iceandfire.client;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.gui.IafGuiRegistry;
import com.github.alexthe666.iceandfire.client.model.*;
import com.github.alexthe666.iceandfire.client.model.animator.FireDragonTabulaModelAnimator;
import com.github.alexthe666.iceandfire.client.model.animator.IceDragonTabulaModelAnimator;
import com.github.alexthe666.iceandfire.client.model.animator.LightningTabulaDragonAnimator;
import com.github.alexthe666.iceandfire.client.model.animator.SeaSerpentTabulaModelAnimator;
import com.github.alexthe666.iceandfire.client.model.util.*;
import com.github.alexthe666.iceandfire.client.render.entity.*;
import com.github.alexthe666.iceandfire.client.render.pathfinding.PathfindingDebugRenderer;
import com.github.alexthe666.iceandfire.client.render.tile.*;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.entity.tile.IafTileEntityRegistry;
import com.github.alexthe666.iceandfire.message.PathfindingDebugSync;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.MNode;
import com.nicktale.api.client.model.TabulaModel;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@EventBusSubscriber(value = Dist.CLIENT, modid = IceAndFire.MODID)
public class IafClientSetup {

    public static TabulaModel FIRE_DRAGON_BASE_MODEL;
    public static TabulaModel ICE_DRAGON_BASE_MODEL;
    public static TabulaModel SEA_SERPENT_BASE_MODEL;
    public static TabulaModel LIGHTNING_DRAGON_BASE_MODEL;
    public static final Identifier GHOST_CHEST_LOCATION = Identifier.fromNamespaceAndPath(IceAndFire.MODID, "models/ghost/ghost_chest");
    public static final Identifier GHOST_CHEST_LEFT_LOCATION = Identifier.fromNamespaceAndPath(IceAndFire.MODID, "models/ghost/ghost_chest_left");
    public static final Identifier GHOST_CHEST_RIGHT_LOCATION = Identifier.fromNamespaceAndPath(IceAndFire.MODID, "models/ghost/ghost_chest_right");

    /** Kept for the proxy hook; renderer registration happens through {@link #registerRenderers}. */
    public static void clientInit() {
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(IafEntityRegistry.FIRE_DRAGON.get(), x -> new RenderDragonBase(x, FIRE_DRAGON_BASE_MODEL, 0));
        event.registerEntityRenderer(IafEntityRegistry.ICE_DRAGON.get(), manager -> new RenderDragonBase(manager, ICE_DRAGON_BASE_MODEL, 1));
        event.registerEntityRenderer(IafEntityRegistry.LIGHTNING_DRAGON.get(), manager -> new RenderLightningDragon(manager, LIGHTNING_DRAGON_BASE_MODEL, 2));
        event.registerEntityRenderer(IafEntityRegistry.DRAGON_EGG.get(), RenderDragonEgg::new);
        event.registerEntityRenderer(IafEntityRegistry.DRAGON_ARROW.get(), RenderDragonArrow::new);
        event.registerEntityRenderer(IafEntityRegistry.DRAGON_SKULL.get(), manager -> new RenderDragonSkull(manager, FIRE_DRAGON_BASE_MODEL, ICE_DRAGON_BASE_MODEL, LIGHTNING_DRAGON_BASE_MODEL));
        event.registerEntityRenderer(IafEntityRegistry.FIRE_DRAGON_CHARGE.get(), manager -> new RenderDragonFireCharge(manager, true));
        event.registerEntityRenderer(IafEntityRegistry.ICE_DRAGON_CHARGE.get(), manager -> new RenderDragonFireCharge(manager, false));
        event.registerEntityRenderer(IafEntityRegistry.LIGHTNING_DRAGON_CHARGE.get(), RenderDragonLightningCharge::new);
        event.registerEntityRenderer(IafEntityRegistry.HIPPOGRYPH_EGG.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(IafEntityRegistry.HIPPOGRYPH.get(), RenderHippogryph::new);
        event.registerEntityRenderer(IafEntityRegistry.STONE_STATUE.get(), RenderStoneStatue::new);
        event.registerEntityRenderer(IafEntityRegistry.GORGON.get(), RenderGorgon::new);
        event.registerEntityRenderer(IafEntityRegistry.PIXIE.get(), RenderPixie::new);
        event.registerEntityRenderer(IafEntityRegistry.CYCLOPS.get(), RenderCyclops::new);
        event.registerEntityRenderer(IafEntityRegistry.SIREN.get(), RenderSiren::new);
        event.registerEntityRenderer(IafEntityRegistry.HIPPOCAMPUS.get(), RenderHippocampus::new);
        event.registerEntityRenderer(IafEntityRegistry.DEATH_WORM.get(), RenderDeathWorm::new);
        event.registerEntityRenderer(IafEntityRegistry.DEATH_WORM_EGG.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(IafEntityRegistry.COCKATRICE.get(), RenderCockatrice::new);
        event.registerEntityRenderer(IafEntityRegistry.COCKATRICE_EGG.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(IafEntityRegistry.STYMPHALIAN_BIRD.get(), RenderStymphalianBird::new);
        event.registerEntityRenderer(IafEntityRegistry.STYMPHALIAN_FEATHER.get(), RenderStymphalianFeather::new);
        event.registerEntityRenderer(IafEntityRegistry.STYMPHALIAN_ARROW.get(), RenderStymphalianArrow::new);
        event.registerEntityRenderer(IafEntityRegistry.TROLL.get(), RenderTroll::new);
        event.registerEntityRenderer(IafEntityRegistry.MYRMEX_WORKER.get(), manager -> new RenderMyrmexBase(manager, new ModelMyrmexWorker(), 0.5F));
        event.registerEntityRenderer(IafEntityRegistry.MYRMEX_SOLDIER.get(), manager -> new RenderMyrmexBase(manager, new ModelMyrmexSoldier(), 0.75F));
        event.registerEntityRenderer(IafEntityRegistry.MYRMEX_QUEEN.get(), manager -> new RenderMyrmexBase(manager, new ModelMyrmexQueen(), 1.25F));
        event.registerEntityRenderer(IafEntityRegistry.MYRMEX_EGG.get(), RenderMyrmexEgg::new);
        event.registerEntityRenderer(IafEntityRegistry.MYRMEX_SENTINEL.get(), manager -> new RenderMyrmexBase(manager, new ModelMyrmexSentinel(), 0.85F));
        event.registerEntityRenderer(IafEntityRegistry.MYRMEX_ROYAL.get(), manager -> new RenderMyrmexBase(manager, new ModelMyrmexRoyal(), 0.75F));
        event.registerEntityRenderer(IafEntityRegistry.MYRMEX_SWARMER.get(), manager -> new RenderMyrmexBase(manager, new ModelMyrmexRoyal(), 0.25F));
        event.registerEntityRenderer(IafEntityRegistry.AMPHITHERE.get(), RenderAmphithere::new);
        event.registerEntityRenderer(IafEntityRegistry.AMPHITHERE_ARROW.get(), RenderAmphithereArrow::new);
        event.registerEntityRenderer(IafEntityRegistry.SEA_SERPENT.get(), manager -> new RenderSeaSerpent(manager, SEA_SERPENT_BASE_MODEL));
        event.registerEntityRenderer(IafEntityRegistry.SEA_SERPENT_BUBBLES.get(), RenderNothing::new);
        event.registerEntityRenderer(IafEntityRegistry.SEA_SERPENT_ARROW.get(), RenderSeaSerpentArrow::new);
        event.registerEntityRenderer(IafEntityRegistry.CHAIN_TIE.get(), RenderChainTie::new);
        event.registerEntityRenderer(IafEntityRegistry.PIXIE_CHARGE.get(), RenderNothing::new);
        event.registerEntityRenderer(IafEntityRegistry.TIDE_TRIDENT.get(), RenderTideTrident::new);
        event.registerEntityRenderer(IafEntityRegistry.MOB_SKULL.get(), manager -> new RenderMobSkull(manager, SEA_SERPENT_BASE_MODEL));
        event.registerEntityRenderer(IafEntityRegistry.DREAD_SCUTTLER.get(), RenderDreadScuttler::new);
        event.registerEntityRenderer(IafEntityRegistry.DREAD_GHOUL.get(), RenderDreadGhoul::new);
        event.registerEntityRenderer(IafEntityRegistry.DREAD_BEAST.get(), RenderDreadBeast::new);
        event.registerEntityRenderer(IafEntityRegistry.DREAD_THRALL.get(), RenderDreadThrall::new);
        event.registerEntityRenderer(IafEntityRegistry.DREAD_LICH.get(), RenderDreadLich::new);
        event.registerEntityRenderer(IafEntityRegistry.DREAD_LICH_SKULL.get(), RenderDreadLichSkull::new);
        event.registerEntityRenderer(IafEntityRegistry.DREAD_KNIGHT.get(), RenderDreadKnight::new);
        event.registerEntityRenderer(IafEntityRegistry.DREAD_HORSE.get(), RenderDreadHorse::new);
        event.registerEntityRenderer(IafEntityRegistry.HYDRA.get(), RenderHydra::new);
        event.registerEntityRenderer(IafEntityRegistry.HYDRA_BREATH.get(), RenderNothing::new);
        event.registerEntityRenderer(IafEntityRegistry.HYDRA_ARROW.get(), RenderHydraArrow::new);
        event.registerEntityRenderer(IafEntityRegistry.SLOW_MULTIPART.get(), RenderNothing::new);
        event.registerEntityRenderer(IafEntityRegistry.DRAGON_MULTIPART.get(), RenderNothing::new);
        event.registerEntityRenderer(IafEntityRegistry.CYCLOPS_MULTIPART.get(), RenderNothing::new);
        event.registerEntityRenderer(IafEntityRegistry.HYDRA_MULTIPART.get(), RenderNothing::new);
        event.registerEntityRenderer(IafEntityRegistry.GHOST.get(), RenderGhost::new);
        event.registerEntityRenderer(IafEntityRegistry.GHOST_SWORD.get(), RenderGhostSword::new);
        event.registerBlockEntityRenderer(IafTileEntityRegistry.PODIUM.get(), RenderPodium::new);
        event.registerBlockEntityRenderer(IafTileEntityRegistry.IAF_LECTERN.get(), RenderLectern::new);
        event.registerBlockEntityRenderer(IafTileEntityRegistry.EGG_IN_ICE.get(), RenderEggInIce::new);
        event.registerBlockEntityRenderer(IafTileEntityRegistry.PIXIE_HOUSE.get(), RenderPixieHouse::new);
        event.registerBlockEntityRenderer(IafTileEntityRegistry.PIXIE_JAR.get(), RenderJar::new);
        event.registerBlockEntityRenderer(IafTileEntityRegistry.DREAD_PORTAL.get(), RenderDreadPortal::new);
        event.registerBlockEntityRenderer(IafTileEntityRegistry.DREAD_SPAWNER.get(), RenderDreadSpawner::new);
        event.registerBlockEntityRenderer(IafTileEntityRegistry.GHOST_CHEST.get(), RenderGhostChest::new);
    }

    @SubscribeEvent
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void registerRenderStateModifiers(net.neoforged.neoforge.client.renderstate.RegisterRenderStateModifiersEvent event) {
        event.registerEntityModifier((Class) net.minecraft.client.renderer.entity.LivingEntityRenderer.class,
            (java.util.function.BiConsumer) (java.util.function.BiConsumer<net.minecraft.world.entity.LivingEntity, net.minecraft.client.renderer.entity.state.LivingEntityRenderState>)
                (entity, state) -> state.setRenderData(com.github.alexthe666.iceandfire.event.ClientEvents.LIVING_KEY, entity));
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        IafGuiRegistry.register(event);
    }

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        IafKeybindRegistry.register(event);
    }

    @SubscribeEvent
    public static void setupClient(FMLClientSetupEvent event) {
        PathfindingDebugSync.registerClientHandler(new PathfindingDebugSync.ClientHandler() {
            @Override
            public void onPathSync(Set<MNode> visited, Set<MNode> notVisited, Set<MNode> path) {
                PathfindingDebugRenderer.lastDebugNodesVisited = new HashSet<>(visited);
                PathfindingDebugRenderer.lastDebugNodesNotVisited = new HashSet<>(notVisited);
                PathfindingDebugRenderer.lastDebugNodesPath = new HashSet<>(path);
            }

            @Override
            public void onPathReached(Set<BlockPos> reached) {
                for (MNode node : new HashSet<>(PathfindingDebugRenderer.lastDebugNodesPath)) {
                    node.setReachedByWorker(reached.contains(node.pos));
                }
            }
        });
        event.enqueueWork(() -> {
            EnumSeaSerpentAnimations.initializeSerpentModels();
            DragonAnimationsLibrary.register(EnumDragonPoses.values(), EnumDragonModelTypes.values());

            try {
                SEA_SERPENT_BASE_MODEL = new TabulaModel(TabulaModelHandlerHelper.loadTabulaModel("/assets/iceandfire/models/tabula/seaserpent/seaserpent_base"), new SeaSerpentTabulaModelAnimator());
                FIRE_DRAGON_BASE_MODEL = new TabulaModel(TabulaModelHandlerHelper.loadTabulaModel("/assets/iceandfire/models/tabula/firedragon/firedragon_ground"), new FireDragonTabulaModelAnimator());
                ICE_DRAGON_BASE_MODEL = new TabulaModel(TabulaModelHandlerHelper.loadTabulaModel("/assets/iceandfire/models/tabula/icedragon/icedragon_ground"), new IceDragonTabulaModelAnimator());
                LIGHTNING_DRAGON_BASE_MODEL = new TabulaModel(TabulaModelHandlerHelper.loadTabulaModel("/assets/iceandfire/models/tabula/lightningdragon/lightningdragon_ground"), new LightningTabulaDragonAnimator());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
