package com.github.alexthe666.iceandfire.client.gui;

import com.github.alexthe666.iceandfire.inventory.IafContainerRegistry;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

public class IafGuiRegistry {

    public static void register(RegisterMenuScreensEvent event) {
        event.register(IafContainerRegistry.IAF_LECTERN_CONTAINER.get(), GuiLectern::new);
        event.register(IafContainerRegistry.PODIUM_CONTAINER.get(), GuiPodium::new);
        event.register(IafContainerRegistry.DRAGON_CONTAINER.get(), GuiDragon::new);
        event.register(IafContainerRegistry.HIPPOGRYPH_CONTAINER.get(), GuiHippogryph::new);
        event.register(IafContainerRegistry.HIPPOCAMPUS_CONTAINER.get(), GuiHippocampus::new);
        event.register(IafContainerRegistry.DRAGON_FORGE_CONTAINER.get(), GuiDragonForge::new);
    }
}
