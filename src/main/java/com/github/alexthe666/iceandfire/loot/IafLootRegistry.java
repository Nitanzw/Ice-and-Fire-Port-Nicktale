package com.github.alexthe666.iceandfire.loot;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class IafLootRegistry {

    public static final DeferredRegister<MapCodec<? extends LootItemFunction>> LOOT_FUNCTIONS =
        DeferredRegister.create(Registries.LOOT_FUNCTION_TYPE, IceAndFire.MODID);

    public static final DeferredHolder<MapCodec<? extends LootItemFunction>, MapCodec<CustomizeToDragon>> CUSTOMIZE_TO_DRAGON =
        LOOT_FUNCTIONS.register("customize_to_dragon", () -> CustomizeToDragon.CODEC);
    public static final DeferredHolder<MapCodec<? extends LootItemFunction>, MapCodec<CustomizeToSeaSerpent>> CUSTOMIZE_TO_SERPENT =
        LOOT_FUNCTIONS.register("customize_to_sea_serpent", () -> CustomizeToSeaSerpent.CODEC);
}
