package com.github.alexthe666.iceandfire;

import com.github.alexthe666.iceandfire.misc.IafDataSerializers;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.config.ConfigHolder;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.entity.IafVillagerRegistry;
import com.github.alexthe666.iceandfire.entity.props.CapabilityHandler;
import com.github.alexthe666.iceandfire.entity.props.EntityDataProvider;
import com.github.alexthe666.iceandfire.entity.tile.IafTileEntityRegistry;
import com.github.alexthe666.iceandfire.inventory.IafContainerRegistry;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.item.IafTabRegistry;
import com.github.alexthe666.iceandfire.loot.IafLootRegistry;
import com.github.alexthe666.iceandfire.message.IafNetwork;
import com.github.alexthe666.iceandfire.misc.IafDamageRegistry;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.github.alexthe666.iceandfire.datagen.DataGenerators;
import com.github.alexthe666.iceandfire.recipe.IafBannerPatterns;
import com.github.alexthe666.iceandfire.recipe.IafRecipeRegistry;
import com.github.alexthe666.iceandfire.world.*;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(IceAndFire.MODID)
public class IceAndFire {
    public static final Logger LOGGER = LoggerFactory.getLogger("iceandfire");
    public static final String MODID = "iceandfire";
    public static final NetworkFacade NETWORK_WRAPPER = new NetworkFacade();
    public static boolean DEBUG = true;
    public static String VERSION = "UNKNOWN";
    public static CommonProxy PROXY;

    public IceAndFire(IEventBus modBus, ModContainer modContainer) {
        VERSION = modContainer.getModInfo().getVersion().toString();
        PROXY = createProxy();

        modContainer.registerConfig(ModConfig.Type.CLIENT, ConfigHolder.CLIENT_SPEC);
        modContainer.registerConfig(ModConfig.Type.SERVER, ConfigHolder.SERVER_SPEC);
        PROXY.init();

        NeoForge.EVENT_BUS.addListener(IceAndFire::onServerStarted);


        registerBiomeModifierSerializers(modBus);

        modBus.addListener(CommonProxy::onModConfigEvent);
        modBus.addListener(CommonProxy::onModConfigReloading);
        modBus.addListener(IafSoundRegistry::registerSoundEvents);
        modBus.addListener(IafDamageRegistry::gatherData);
        modBus.addListener(IafTileEntityRegistry::registerCapabilities);
        modBus.addListener(IafRecipeRegistry::preInit);
        modBus.addListener(DataGenerators::gatherData);
        NeoForge.EVENT_BUS.addListener(IafRecipeRegistry::registerBrewingRecipes);
        IafNetwork.init(modBus);

        com.github.alexthe666.iceandfire.misc.IafDataSerializers.SERIALIZERS.register(modBus);
        IafItemRegistry.ITEMS.register(modBus);
        // Deferred items must be bound before repair ingredients resolve their suppliers.
        modBus.addListener(IafItemRegistry::setRepairMaterials);
        IafBlockRegistry.BLOCKS.register(modBus);
        IafTabRegistry.TAB_REGISTER.register(modBus);
        IafEntityRegistry.ENTITIES.register(modBus);
        IafEntityRegistry.init(modBus);
        EntityDataProvider.ATTACHMENTS.register(modBus);
        CapabilityHandler.init(modBus);
        IafTileEntityRegistry.TYPES.register(modBus);
        IafPlacementFilterRegistry.PLACEMENT_MODIFIER_TYPES.register(modBus);
        IafWorldRegistry.FEATURES.register(modBus);
        IafLootRegistry.LOOT_FUNCTIONS.register(modBus);
        IafRecipeRegistry.RECIPE_TYPE.register(modBus);
        IafStructureTypes.STRUCTURE_TYPES.register(modBus);
        IafContainerRegistry.CONTAINERS.register(modBus);
        IafRecipeRegistry.RECIPE_SERIALIZER.register(modBus);
        IafProcessors.PROCESSORS.register(modBus);

        IafVillagerRegistry.POI_TYPES.register(modBus);
        IafVillagerRegistry.PROFESSIONS.register(modBus);

        modBus.addListener(IceAndFire::setup);
        modBus.addListener(IceAndFire::setupComplete);
        modBus.addListener(IceAndFire::setupClient);
    }

    private static CommonProxy createProxy() {
        if (!FMLEnvironment.getDist().isClient()) {
            return new CommonProxy();
        }
        try {
            Class<?> clientProxy = Class.forName("com.github.alexthe666.iceandfire.client.ClientProxy");
            return (CommonProxy) clientProxy.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Unable to initialize the Ice and Fire client proxy", exception);
        }
    }

    private static void registerBiomeModifierSerializers(IEventBus modBus) {
        DeferredRegister<MapCodec<? extends BiomeModifier>> serializers = DeferredRegister.create(
                NeoForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, MODID);
        serializers.register("iaf_mob_spawns", IafMobSpawnBiomeModifier::makeCodec);
        serializers.register("iaf_features", IafFeatureBiomeModifier::makeCodec);
        serializers.register(modBus);
    }

    public static void onServerStarted(ServerStartedEvent event) {
        LOGGER.info("Loaded world features: {}", IafWorldRegistry.LOADED_FEATURES);
        LOGGER.info("Loaded entities: {}", IafEntityRegistry.LOADED_ENTITIES);
        IafWorldRegistry.LOADED_FEATURES.clear();
    }

    public static void sendMSGToServer(CustomPacketPayload message) {
        IafNetwork.sendToServer(message);
    }

    public static void sendMSGToAll(CustomPacketPayload message) {
        IafNetwork.sendToAll(message);
    }

    public static void sendMSGToPlayer(CustomPacketPayload message, ServerPlayer player) {
        IafNetwork.sendToPlayer(player, message);
    }

    public static final class NetworkFacade {
        private NetworkFacade() {
        }

        public void sendToServer(CustomPacketPayload payload) {
            IafNetwork.sendToServer(payload);
        }
    }

    private static void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            PROXY.setup();
        });
    }

    private static void setupClient(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> PROXY.clientInit());
    }

    private static void setupComplete(final FMLLoadCompleteEvent event) {
        PROXY.postInit();
    }

}
