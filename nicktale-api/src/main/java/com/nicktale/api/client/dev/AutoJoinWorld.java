package com.nicktale.api.client.dev;

import com.nicktale.api.NicktaleApi;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Development aid for automated client tests: with {@code -Dnicktale.autojoin=<world folder name>} the client opens that
 * local singleplayer world as soon as the title screen shows.
 */
@EventBusSubscriber(value = Dist.CLIENT, modid = NicktaleApi.MOD_ID)
public final class AutoJoinWorld {
    private static final Logger LOGGER = LoggerFactory.getLogger(NicktaleApi.MOD_ID);
    private static final String WORLD = System.getProperty("nicktale.autojoin");
    private static boolean tried;
    private static int ticks;

    private AutoJoinWorld() {
    }

    @SubscribeEvent
    public static void onTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (WORLD == null || tried || mc.level != null) {
            return;
        }
        if (++ticks % 100 == 1) {
            LOGGER.info("SMOKETEST waiting, screen={}", mc.gui.screen());
        }
        if (mc.gui.screen() instanceof TitleScreen) {
            tried = true;
            LOGGER.info("SMOKETEST opening world {}", WORLD);
            mc.createWorldOpenFlows().openWorld(WORLD, () -> LOGGER.error("SMOKETEST world open failed"));
        }
    }
}
