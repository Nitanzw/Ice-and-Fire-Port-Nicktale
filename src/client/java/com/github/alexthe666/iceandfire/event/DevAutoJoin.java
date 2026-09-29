package com.github.alexthe666.iceandfire.event;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

/** Development aid: with {@code -Diaf.smoketest=true} opens the local world {@code iaftest} from the title screen. */
@EventBusSubscriber(value = Dist.CLIENT, modid = IceAndFire.MODID)
public final class DevAutoJoin {
    private static boolean tried;

    private DevAutoJoin() {
    }

    @SubscribeEvent
    public static void onTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (!tried && Boolean.getBoolean("iaf.smoketest") && mc.level == null && mc.gui.screen() instanceof TitleScreen) {
            tried = true;
            IceAndFire.LOGGER.info("SMOKETEST opening world iaftest");
            mc.createWorldOpenFlows().openWorld("iaftest", () -> IceAndFire.LOGGER.error("SMOKETEST world open failed"));
        }
    }
}
