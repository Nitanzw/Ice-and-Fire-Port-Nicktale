package com.github.alexthe666.iceandfire.inventory;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class IafContainerRegistry {
    public static final DeferredRegister<MenuType<?>> CONTAINERS =
        DeferredRegister.create(BuiltInRegistries.MENU, IceAndFire.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<ContainerLectern>> IAF_LECTERN_CONTAINER = register(
        "iaf_lectern", (id, inventory, data) -> new ContainerLectern(id, inventory));
    public static final DeferredHolder<MenuType<?>, MenuType<ContainerPodium>> PODIUM_CONTAINER = register(
        "podium", (id, inventory, data) -> new ContainerPodium(id, inventory));
    public static final DeferredHolder<MenuType<?>, MenuType<ContainerDragon>> DRAGON_CONTAINER = register(
        "dragon", (id, inventory, data) -> new ContainerDragon(id, inventory));
    public static final DeferredHolder<MenuType<?>, MenuType<ContainerHippogryph>> HIPPOGRYPH_CONTAINER = register(
        "hippogryph", (id, inventory, data) -> new ContainerHippogryph(id, inventory));
    public static final DeferredHolder<MenuType<?>, MenuType<HippocampusContainerMenu>> HIPPOCAMPUS_CONTAINER = register(
        "hippocampus", (id, inventory, data) -> new HippocampusContainerMenu(id, inventory));
    public static final DeferredHolder<MenuType<?>, MenuType<ContainerDragonForge>> DRAGON_FORGE_CONTAINER = register(
        "dragon_forge", (id, inventory, data) -> new ContainerDragonForge(id, inventory));

    private IafContainerRegistry() {}

    private static <C extends net.minecraft.world.inventory.AbstractContainerMenu> DeferredHolder<MenuType<?>, MenuType<C>> register(
        String name, IContainerFactory<C> factory) {
        return CONTAINERS.register(name, () -> IMenuTypeExtension.create(factory));
    }
}
