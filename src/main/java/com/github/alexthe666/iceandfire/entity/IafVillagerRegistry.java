package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.datagen.IafProcessorLists;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.item.trading.TradeSet;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.ArrayList;
import java.util.List;

public class IafVillagerRegistry {

    public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(Registries.POINT_OF_INTEREST_TYPE, IceAndFire.MODID);
    public static final DeferredHolder<PoiType, PoiType> SCRIBE_POI = POI_TYPES.register("scribe", () -> new PoiType(ImmutableSet.copyOf(IafBlockRegistry.LECTERN.get().getStateDefinition().getPossibleStates()), 1, 1));
    public static final DeferredRegister<VillagerProfession> PROFESSIONS = DeferredRegister.create(Registries.VILLAGER_PROFESSION, IceAndFire.MODID);
    public static final DeferredHolder<VillagerProfession, VillagerProfession> SCRIBE = PROFESSIONS.register("scribe", () -> new VillagerProfession(
        Component.translatable("entity." + IceAndFire.MODID + ".villager.scribe"),
        holder -> holder.is(SCRIBE_POI.getKey()),
        holder -> holder.is(SCRIBE_POI.getKey()),
        ImmutableSet.of(),
        ImmutableSet.of(),
        SoundEvents.VILLAGER_WORK_LIBRARIAN,
        Int2ObjectMap.ofEntries(
            Int2ObjectMap.entry(1, scribeTradeSet(1)),
            Int2ObjectMap.entry(2, scribeTradeSet(2)),
            Int2ObjectMap.entry(3, scribeTradeSet(3)),
            Int2ObjectMap.entry(4, scribeTradeSet(4)),
            Int2ObjectMap.entry(5, scribeTradeSet(5)))));

    private static ResourceKey<TradeSet> scribeTradeSet(int level) {
        return ResourceKey.create(Registries.TRADE_SET, Identifier.fromNamespaceAndPath(IceAndFire.MODID, "scribe/level_" + level));
    }

    public static void addBuildingToPool(Registry<StructureTemplatePool> templatePoolRegistry,
                                         Registry<StructureProcessorList> processorListRegistry,
                                         Identifier poolRL,
                                         String nbtPieceRL,
                                         int weight) {

        Holder<StructureProcessorList> villageHouseProcessorList = processorListRegistry.getHolderOrThrow(IafProcessorLists.HOUSE_PROCESSOR);

        // Grab the pool we want to add to
        StructureTemplatePool pool = templatePoolRegistry.get(poolRL);
        if (pool == null) return;

        // Grabs the nbt piece and creates a SinglePoolElement of it that we can add to a structure's pool.
        // Use .legacy( for villages/outposts and .single( for everything else
        SinglePoolElement piece = SinglePoolElement.legacy(nbtPieceRL, villageHouseProcessorList).apply(StructureTemplatePool.Projection.RIGID);

        // Use AccessTransformer or Accessor Mixin to make StructureTemplatePool's templates field public for us to see.
        // Weight is handled by how many times the entry appears in this list.
        // We do not need to worry about immutability as this field is created using Lists.newArrayList(); which makes a mutable list.
        for (int i = 0; i < weight; i++) {
            pool.templates.add(piece);
        }

        // Use AccessTransformer or Accessor Mixin to make StructureTemplatePool's rawTemplates field public for us to see.
        // This list of pairs of pieces and weights is not used by vanilla by default but another mod may need it for efficiency.
        // So lets add to this list for completeness. We need to make a copy of the array as it can be an immutable list.
        List<Pair<StructurePoolElement, Integer>> listOfPieceEntries = new ArrayList<>(pool.rawTemplates);
        listOfPieceEntries.add(new Pair<>(piece, weight));
        pool.rawTemplates = listOfPieceEntries;
    }


}
