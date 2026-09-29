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
        TileEntityLectern::new, "lectern", IafBlockRegistry.LECTERN.get());
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEntityPodium>> PODIUM = registerTileEntity(
        TileEntityPodium::new, "podium", IafBlockRegistry.PODIUM_OAK.get(), IafBlockRegistry.PODIUM_BIRCH.get(),
        IafBlockRegistry.PODIUM_SPRUCE.get(), IafBlockRegistry.PODIUM_JUNGLE.get(), IafBlockRegistry.PODIUM_DARK_OAK.get(),
        IafBlockRegistry.PODIUM_ACACIA.get(), IafBlockRegistry.PODIUM_CRIMSON.get(), IafBlockRegistry.PODIUM_WARPED.get(),
        IafBlockRegistry.PODIUM_MANGROVE.get(), IafBlockRegistry.PODIUM_CHERRY.get());
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEntityEggInIce>> EGG_IN_ICE = registerTileEntity(
        TileEntityEggInIce::new, "egginice", IafBlockRegistry.EGG_IN_ICE.get());
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEntityPixieHouse>> PIXIE_HOUSE = registerTileEntity(
        TileEntityPixieHouse::new, "pixie_house", IafBlockRegistry.PIXIE_HOUSE_MUSHROOM_RED.get(),
        IafBlockRegistry.PIXIE_HOUSE_MUSHROOM_BROWN.get(), IafBlockRegistry.PIXIE_HOUSE_OAK.get(),
        IafBlockRegistry.PIXIE_HOUSE_BIRCH.get(), IafBlockRegistry.PIXIE_HOUSE_SPRUCE.get(),
        IafBlockRegistry.PIXIE_HOUSE_DARK_OAK.get());
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEntityJar>> PIXIE_JAR = registerTileEntity(
        TileEntityJar::new, "pixie_jar", IafBlockRegistry.JAR_EMPTY.get(), IafBlockRegistry.JAR_PIXIE_0.get(),
        IafBlockRegistry.JAR_PIXIE_1.get(), IafBlockRegistry.JAR_PIXIE_2.get(), IafBlockRegistry.JAR_PIXIE_3.get(),
        IafBlockRegistry.JAR_PIXIE_4.get());
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEntityMyrmexCocoon>> MYRMEX_COCOON = registerTileEntity(
        TileEntityMyrmexCocoon::new, "myrmex_cocoon", IafBlockRegistry.DESERT_MYRMEX_COCOON.get(),
        IafBlockRegistry.JUNGLE_MYRMEX_COCOON.get());
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEntityDragonforge>> DRAGONFORGE_CORE = registerTileEntity(
        TileEntityDragonforge::new, "dragonforge_core", IafBlockRegistry.DRAGONFORGE_FIRE_CORE.get(),
        IafBlockRegistry.DRAGONFORGE_ICE_CORE.get(), IafBlockRegistry.DRAGONFORGE_FIRE_CORE_DISABLED.get(),
        IafBlockRegistry.DRAGONFORGE_ICE_CORE_DISABLED.get(), IafBlockRegistry.DRAGONFORGE_LIGHTNING_CORE.get(),
        IafBlockRegistry.DRAGONFORGE_LIGHTNING_CORE_DISABLED.get());
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEntityDragonforgeBrick>> DRAGONFORGE_BRICK = registerTileEntity(
        TileEntityDragonforgeBrick::new, "dragonforge_brick", IafBlockRegistry.DRAGONFORGE_FIRE_BRICK.get(),
        IafBlockRegistry.DRAGONFORGE_ICE_BRICK.get(), IafBlockRegistry.DRAGONFORGE_LIGHTNING_BRICK.get());
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEntityDragonforgeInput>> DRAGONFORGE_INPUT = registerTileEntity(
        TileEntityDragonforgeInput::new, "dragonforge_input", IafBlockRegistry.DRAGONFORGE_FIRE_INPUT.get(),
        IafBlockRegistry.DRAGONFORGE_ICE_INPUT.get(), IafBlockRegistry.DRAGONFORGE_LIGHTNING_INPUT.get());
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEntityDreadPortal>> DREAD_PORTAL = registerTileEntity(
        TileEntityDreadPortal::new, "dread_portal", IafBlockRegistry.DREAD_PORTAL.get());
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEntityDreadSpawner>> DREAD_SPAWNER = registerTileEntity(
        TileEntityDreadSpawner::new, "dread_spawner", true, IafBlockRegistry.DREAD_SPAWNER.get());
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEntityGhostChest>> GHOST_CHEST = registerTileEntity(
        TileEntityGhostChest::new, "ghost_chest", IafBlockRegistry.GHOST_CHEST.get());

    private IafTileEntityRegistry() {}

    private static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> registerTileEntity(
        BlockEntityType.BlockEntitySupplier<? extends T> factory, String name, Block... validBlocks) {
        return TYPES.register(name, () -> new BlockEntityType<>(factory, Set.of(validBlocks)));
    }

    private static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> registerTileEntity(
        BlockEntityType.BlockEntitySupplier<? extends T> factory, String name, boolean onlyOpCanSetNbt, Block... validBlocks) {
        return TYPES.register(name, () -> new BlockEntityType<>(factory, Set.of(validBlocks), onlyOpCanSetNbt));
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
