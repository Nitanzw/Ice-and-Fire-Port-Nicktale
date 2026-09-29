package com.github.alexthe666.iceandfire.mixin.gen;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.WorldGenRegion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * Avoid log spam for dragon caves and other big features: some blocks exceed the writable/readable worldgen area
 * due to their size. Out-of-area writes are still dropped; only the error logging is skipped for our features.
 */
@Mixin(WorldGenRegion.class)
public class WorldGenRegionMixin {
    @WrapOperation(
        method = {"ensureCanWrite", "getChunk(IILnet/minecraft/world/level/chunk/status/ChunkStatus;Z)Lnet/minecraft/world/level/chunk/ChunkAccess;"},
        at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Util;logAndPauseIfInIde(Ljava/lang/String;)V")
    )
    private void iaf$skipLog(final String message, final Operation<Void> original) {
        if (this.currentlyGenerating != null && this.currentlyGenerating.get().contains(IceAndFire.MODID)) {
            return;
        }
        original.call(message);
    }

    @Shadow @Nullable private Supplier<String> currentlyGenerating;
}
