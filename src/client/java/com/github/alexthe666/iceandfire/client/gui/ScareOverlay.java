package com.github.alexthe666.iceandfire.client.gui;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;

/**
 * First-person ghost / siren scare. The 1.20 particles drew a model in front of the camera, which the 26.x particle
 * pipeline no longer allows, so the scare is shown as a fading full-screen flash instead.
 */
@EventBusSubscriber(value = Dist.CLIENT, modid = IceAndFire.MODID)
public final class ScareOverlay {
    private static long startMillis;
    private static long durationMillis;
    private static int rgb;

    private ScareOverlay() {
    }

    public static void trigger(int ticks, int color) {
        startMillis = System.currentTimeMillis();
        durationMillis = ticks * 50L;
        rgb = color & 0xFFFFFF;
    }

    @SubscribeEvent
    public static void registerLayer(RegisterGuiLayersEvent event) {
        event.registerAboveAll(Identifier.fromNamespaceAndPath(IceAndFire.MODID, "scare"), ScareOverlay::render);
    }

    private static void render(GuiGraphicsExtractor graphics, DeltaTracker delta) {
        long elapsed = System.currentTimeMillis() - startMillis;
        if (durationMillis <= 0 || elapsed >= durationMillis) {
            return;
        }
        float strength = 1.0F - (float) elapsed / durationMillis;
        int alpha = (int) (strength * 0.55F * 255F);
        graphics.fill(0, 0, graphics.guiWidth(), graphics.guiHeight(), (alpha << 24) | rgb);
    }
}
