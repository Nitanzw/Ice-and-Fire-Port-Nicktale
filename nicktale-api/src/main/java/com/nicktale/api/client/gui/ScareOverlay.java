package com.nicktale.api.client.gui;

import com.nicktale.api.NicktaleApi;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;

/**
 * First-person scare shown as a fading full-screen colour flash. Replaces particles that drew a model in front of the
 * camera, which the 26.x particle pipeline no longer allows. Call {@link #trigger} from anywhere on the client.
 */
@EventBusSubscriber(value = Dist.CLIENT, modid = NicktaleApi.MOD_ID)
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
        event.registerAboveAll(Identifier.fromNamespaceAndPath(NicktaleApi.MOD_ID, "scare"), ScareOverlay::render);
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
