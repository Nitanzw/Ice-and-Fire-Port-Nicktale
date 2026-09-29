package com.nicktale.api;

import com.nicktale.api.animation.AnimationSync;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(NicktaleApi.MOD_ID)
public class NicktaleApi {
    public static final String MOD_ID = "nicktaleapi";

    public NicktaleApi(IEventBus modBus) {
        AnimationSync.register(modBus);
    }
}
