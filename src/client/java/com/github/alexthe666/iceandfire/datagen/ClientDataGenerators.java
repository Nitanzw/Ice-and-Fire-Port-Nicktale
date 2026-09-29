package com.github.alexthe666.iceandfire.datagen;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = IceAndFire.MODID, value = Dist.CLIENT)
public final class ClientDataGenerators {
    private ClientDataGenerators() {
    }

    @SubscribeEvent
    public static void gatherClientData(GatherDataEvent.Client event) {
        event.addProvider(new AtlasGenerator(event.getGenerator().getPackOutput(), event.getLookupProvider()));
    }
}