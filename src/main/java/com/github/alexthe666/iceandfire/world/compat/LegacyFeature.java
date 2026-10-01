//#if MC >= 26.3
//$$ package com.github.alexthe666.iceandfire.world.compat;
//$$
//$$ import com.mojang.serialization.Codec;
//$$ import com.mojang.serialization.MapCodec;
//$$ import net.minecraft.core.BlockPos;
//$$ import net.minecraft.util.RandomSource;
//$$ import net.minecraft.world.level.WorldGenLevel;
//$$ import net.minecraft.world.level.chunk.ChunkGenerator;
//$$ import net.minecraft.world.level.levelgen.feature.Feature;
//$$
//$$ /**
//$$  * Bridges the 26.2 feature model (one Feature class + a configuration) to 26.3, where a feature is a configured
//$$  * instance whose type is a MapCodec. Each Ice and Fire feature has no configuration, so its type is a unit codec
//$$  * that always yields this instance.
//$$  */
//$$ public abstract class LegacyFeature<C extends FeatureConfiguration> implements Feature {
//$$     private final MapCodec<LegacyFeature<C>> codec = MapCodec.unit(this);
//$$
//$$     protected LegacyFeature(Codec<C> configCodec) {
//$$     }
//$$
//$$     public MapCodec<LegacyFeature<C>> typeCodec() {
//$$         return codec;
//$$     }
//$$
//$$     @Override
//$$     public MapCodec<? extends Feature> codec() {
//$$         return codec;
//$$     }
//$$
//$$     public abstract boolean place(FeaturePlaceContext<C> context);
//$$
//$$     @Override
//$$     @SuppressWarnings("unchecked")
//$$     public boolean place(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin) {
//$$         return place(new FeaturePlaceContext<>(level, chunkGenerator, random, origin, (C) NoneFeatureConfiguration.INSTANCE));
//$$     }
//$$ }
//#endif
