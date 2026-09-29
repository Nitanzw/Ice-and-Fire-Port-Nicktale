package com.github.alexthe666.iceandfire.entity.tile;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper;
import net.neoforged.neoforge.transfer.item.WorldlyContainerWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.function.Supplier;

public final class IafTileEntityRegistry {
    public static final DeferredRegister<BlockEntityType<?>> TYPES =
        DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, IceAndFire.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEntityLectern>> IAF_LECTERN = registerTileEntity(
        TileEntityLectern::new, "lectern", IafBlockRegistry.LECTERN);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEntityPodium>> PODIUM = registerTileEntity(
        TileEntityPodium::new, "podium", IafBlockRegistry.PODIUM_OAK, IafBlockRegistry.PODIUM_BIRCH,
        IafBlockRegistry.PODIUM_SPRUCE, IafBlockRegistry.PODIUM_JUNGLE, IafBlockRegistry.PODIUM_DARK_OAK,
        IafBlockRegistry.PODIUM_ACACIA, IafBlockRegistry.PODIUM_CRIMSON, IafBlockRegistry.PODIUM_WARPED,
        IafBlockRegistry.PODIUM_MANGROVE, IafBlockRegistry.PODIUM_CHERRY);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEntityEggInIce>> EGG_IN_ICE = registerTileEntity(
        TileEntityEggInIce::new, "egginice", IafBlockRegistry.EGG_IN_ICE);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEntityPixieHouse>> PIXIE_HOUSE = registerTileEntity(
        TileEntityPixieHouse::new, "pixie_house", IafBlockRegistry.PIXIE_HOUSE_MUSHROOM_RED,
        IafBlockRegistry.PIXIE_HOUSE_MUSHROOM_BROWN, IafBlockRegistry.PIXIE_HOUSE_OAK,
        IafBlockRegistry.PIXIE_HOUSE_BIRCH, IafBlockRegistry.PIXIE_HOUSE_SPRUCE,
        IafBlockRegistry.PIXIE_HOUSE_DARK_OAK);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEntityJar>> PIXIE_JAR = registerTileEntity(
        TileEntityJar::new, "pixie_jar", IafBlockRegistry.JAR_EMPTY, IafBlockRegistry.JAR_PIXIE_0,
        IafBlockRegistry.JAR_PIXIE_1, IafBlockRegistry.JAR_PIXIE_2, IafBlockRegistry.JAR_PIXIE_3,
        IafBlockRegistry.JAR_PIXIE_4);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEntityMyrmexCocoon>> MYRMEX_COCOON = registerTileEntity(
        TileEntityMyrmexCocoon::new, "myrmex_cocoon", IafBlockRegistry.DESERT_MYRMEX_COCOON,
        IafBlockRegistry.JUNGLE_MYRMEX_COCOON);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEntityDragonforge>> DRAGONFORGE_CORE = registerTileEntity(
        TileEntityDragonforge::new, "dragonforge_core", IafBlockRegistry.DRAGONFORGE_FIRE_CORE,
        IafBlockRegistry.DRAGONFORGE_ICE_CORE, IafBlockRegistry.DRAGONFORGE_FIRE_CORE_DISABLED,
        IafBlockRegistry.DRAGONFORGE_ICE_CORE_DISABLED, IafBlockRegistry.DRAGONFORGE_LIGHTNING_CORE,
        IafBlockRegistry.DRAGONFORGE_LIGHTNING_CORE_DISABLED);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEntityDragonforgeBrick>> DRAGONFORGE_BRICK = registerTileEntity(
        TileEntityDragonforgeBrick::new, "dragonforge_brick", IafBlockRegistry.DRAGONFORGE_FIRE_BRICK,
        IafBlockRegistry.DRAGONFORGE_ICE_BRICK, IafBlockRegistry.DRAGONFORGE_LIGHTNING_BRICK);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEntityDragonforgeInput>> DRAGONFORGE_INPUT = registerTileEntity(
        TileEntityDragonforgeInput::new, "dragonforge_input", IafBlockRegistry.DRAGONFORGE_FIRE_INPUT,
        IafBlockRegistry.DRAGONFORGE_ICE_INPUT, IafBlockRegistry.DRAGONFORGE_LIGHTNING_INPUT);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEntityDreadPortal>> DREAD_PORTAL = registerTileEntity(
        TileEntityDreadPortal::new, "dread_portal", IafBlockRegistry.DREAD_PORTAL);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEntityDreadSpawner>> DREAD_SPAWNER = registerTileEntity(
        TileEntityDreadSpawner::new, "dread_spawner", true, IafBlockRegistry.DREAD_SPAWNER);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEntityGhostChest>> GHOST_CHEST = registerTileEntity(
        TileEntityGhostChest::new, "ghost_chest", IafBlockRegistry.GHOST_CHEST);

    private IafTileEntityRegistry() {}

    @SafeVarargs
    private static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> registerTileEntity(
        BlockEntityType.BlockEntitySupplier<? extends T> factory, String name, Supplier<? extends Block>... validBlocks) {
        return TYPES.register(name, () -> new BlockEntityType<>(factory, resolve(validBlocks)));
    }

    @SafeVarargs
    private static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> registerTileEntity(
        BlockEntityType.BlockEntitySupplier<? extends T> factory, String name, boolean onlyOpCanSetNbt, Supplier<? extends Block>... validBlocks) {
        return TYPES.register(name, () -> new BlockEntityType<>(factory, resolve(validBlocks), onlyOpCanSetNbt));
    }

    /** Blocks are resolved when the block entity type is registered, after the block registry has been filled. */
    private static Set<Block> resolve(Supplier<? extends Block>[] blocks) {
        Set<Block> resolved = new java.util.LinkedHashSet<>();
        for (Supplier<? extends Block> block : blocks) {
            resolved.add(block.get());
        }
        return resolved;
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.Item.BLOCK, DRAGONFORGE_CORE.get(),
            (forge, side) -> new WorldlyContainerWrapper(forge, side));
        event.registerBlockEntity(Capabilities.Item.BLOCK, DRAGONFORGE_INPUT.get(),
            (input, side) -> capabilityForAdjacentForge(input, side));
        event.registerBlockEntity(Capabilities.Item.BLOCK, DRAGONFORGE_BRICK.get(),
            (brick, side) -> capabilityForAdjacentForge(brick, side));
        event.registerBlockEntity(Capabilities.Item.BLOCK, PIXIE_JAR.get(),
            (jar, side) -> side == Direction.DOWN ? jar.getItemHandler() : null);
        event.registerBlockEntity(Capabilities.Item.BLOCK, IAF_LECTERN.get(),
            (lectern, side) -> new WorldlyContainerWrapper(lectern, side));
        event.registerBlockEntity(Capabilities.Item.BLOCK, PODIUM.get(),
            (podium, side) -> new WorldlyContainerWrapper(podium, side));
    }

    private static ResourceHandler<ItemResource> capabilityForAdjacentForge(BlockEntity blockEntity, @Nullable Direction side) {
        if (blockEntity.getLevel() == null) {
            return null;
        }
        for (Direction direction : Direction.values()) {
            BlockEntity adjacent = blockEntity.getLevel().getBlockEntity(blockEntity.getBlockPos().relative(direction));
            if (adjacent instanceof TileEntityDragonforge forge) {
                return new WorldlyContainerWrapper(forge, side);
            }
        }
        return null;
    }
}
