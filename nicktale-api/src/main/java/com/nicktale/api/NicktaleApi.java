package com.nicktale.api;

import com.nicktale.api.animation.AnimationSync;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

@Mod(NicktaleApi.MOD_ID)
public class NicktaleApi {
    public static final String MOD_ID = "nicktaleapi";

    public NicktaleApi(IEventBus modBus) {
        AnimationSync.register(modBus);
        NeoForge.EVENT_BUS.register(new com.nicktale.api.server.respawn.SiteRespawns.Events());
    }
}
