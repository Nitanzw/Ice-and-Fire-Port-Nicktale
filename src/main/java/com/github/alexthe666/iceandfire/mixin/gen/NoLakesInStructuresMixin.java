package com.github.alexthe666.iceandfire.mixin.gen;

import com.github.alexthe666.iceandfire.datagen.IafStructures;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.LakeFeature;
//#if MC < 26.3
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
//#endif
import net.minecraft.world.level.levelgen.structure.Structure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

// Based on code from TelepathicGrunts RepurposedStructures
@Mixin(LakeFeature.class)
public class NoLakesInStructuresMixin {


    //#if MC >= 26.3
    //$$ @Inject(
    //$$         method = "place(Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/world/level/chunk/ChunkGenerator;Lnet/minecraft/util/RandomSource;Lnet/minecraft/core/BlockPos;)Z",
    //$$         at = @At(value = "HEAD"),
    //$$         cancellable = true
    //$$ )
    //$$ private void iaf_noLakesInMausoleum(net.minecraft.world.level.WorldGenLevel level, net.minecraft.world.level.chunk.ChunkGenerator generator,
    //$$         net.minecraft.util.RandomSource random, net.minecraft.core.BlockPos origin, CallbackInfoReturnable<Boolean> cir) {
    //$$     var context = new com.github.alexthe666.iceandfire.world.compat.FeaturePlaceContext<>(level, generator, random, origin,
    //$$             com.github.alexthe666.iceandfire.world.compat.NoneFeatureConfiguration.INSTANCE);
    //#else
    @Inject(
            method = "place(Lnet/minecraft/world/level/levelgen/feature/FeaturePlaceContext;)Z",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    private void iaf_noLakesInMausoleum(FeaturePlaceContext<BlockStateConfiguration> context, CallbackInfoReturnable<Boolean> cir) {
    //#endif
        if(!(context.level() instanceof WorldGenRegion)) {
            return;
        }
        Registry<Structure> configuredStructureFeatureRegistry = context.level().registryAccess().lookupOrThrow(Registries.STRUCTURE);
        StructureManager structureManager = (context.level()).getLevel().structureManager();
        var availableStructures  = List.of(configuredStructureFeatureRegistry.getOptional(IafStructures.MAUSOLEUM),configuredStructureFeatureRegistry.getOptional(IafStructures.GRAVEYARD),configuredStructureFeatureRegistry.getOptional(IafStructures.GORGON_TEMPLE));
        for (var structure : availableStructures) {
            if (structure.isPresent() && structureManager.getStructureAt(context.origin(), structure.get()).isValid()) {
                cir.setReturnValue(false);
                return;
            }
        }
    }
}