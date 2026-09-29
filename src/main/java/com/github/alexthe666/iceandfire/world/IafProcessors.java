package com.github.alexthe666.iceandfire.world;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.world.gen.processor.DreadRuinProcessor;
import com.github.alexthe666.iceandfire.world.gen.processor.GorgonTempleProcessor;
import com.github.alexthe666.iceandfire.world.gen.processor.GraveyardProcessor;
import com.github.alexthe666.iceandfire.world.gen.processor.VillageHouseProcessor;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class IafProcessors {
    public static final DeferredRegister<MapCodec<? extends StructureProcessor>> PROCESSORS = DeferredRegister.create(Registries.STRUCTURE_PROCESSOR, IceAndFire.MODID);

    public static final Supplier<MapCodec<DreadRuinProcessor>> DREADRUINPROCESSOR = PROCESSORS.register("dread_mausoleum_processor", () -> DreadRuinProcessor.CODEC);
    public static final Supplier<MapCodec<GorgonTempleProcessor>> GORGONTEMPLEPROCESSOR = PROCESSORS.register("gorgon_temple_processor", () -> GorgonTempleProcessor.CODEC);
    public static final Supplier<MapCodec<GraveyardProcessor>> GRAVEYARDPROCESSOR = PROCESSORS.register("graveyard_processor", () -> GraveyardProcessor.CODEC);
    public static final Supplier<MapCodec<VillageHouseProcessor>> VILLAGEHOUSEPROCESSOR = PROCESSORS.register("village_house_processor", () -> VillageHouseProcessor.CODEC);
}
