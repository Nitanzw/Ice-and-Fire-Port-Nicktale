package com.github.alexthe666.iceandfire.world;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.core.registries.Registries;
//#if MC < 26.3
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
//#endif
import net.neoforged.neoforge.registries.DeferredRegister;
import java.util.function.Supplier;

public class IafPlacementFilterRegistry {
    //#if MC >= 26.3
    //$$ public static final DeferredRegister<com.mojang.serialization.MapCodec<? extends net.minecraft.world.level.levelgen.placement.PlacementModifier>> PLACEMENT_MODIFIER_TYPES =
    //$$         DeferredRegister.create(Registries.PLACEMENT_MODIFIER_TYPE, IceAndFire.MODID);
    //$$ public static Supplier<com.mojang.serialization.MapCodec<CustomBiomeFilter>> CUSTOM_BIOME_FILTER = PLACEMENT_MODIFIER_TYPES.register("biome_extended", () -> CustomBiomeFilter.CODEC);
    //#else
    public static final DeferredRegister<PlacementModifierType<?>> PLACEMENT_MODIFIER_TYPES = DeferredRegister.create(Registries.PLACEMENT_MODIFIER_TYPE, IceAndFire.MODID);

    public static Supplier<PlacementModifierType<CustomBiomeFilter>> CUSTOM_BIOME_FILTER = PLACEMENT_MODIFIER_TYPES.register("biome_extended", () -> () -> CustomBiomeFilter.CODEC);
    //#endif
}
