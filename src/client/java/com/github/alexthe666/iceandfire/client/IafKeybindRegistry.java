package com.github.alexthe666.iceandfire.client;

import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

public class IafKeybindRegistry {
    public static final KeyMapping dragon_fireAttack = new KeyMapping("key.dragon_fireAttack", 82, KeyMapping.Category.GAMEPLAY);
    public static final KeyMapping dragon_strike = new KeyMapping("key.dragon_strike", 71, KeyMapping.Category.GAMEPLAY);
    public static final KeyMapping dragon_down = new KeyMapping("key.dragon_down", 88, KeyMapping.Category.GAMEPLAY);
    public static final KeyMapping dragon_change_view = new KeyMapping("key.dragon_change_view", 296, KeyMapping.Category.MISC);

    /** Key mappings are registered through the NeoForge event now; nothing else to do here. */
    public static void init() {
    }

    public static void register(RegisterKeyMappingsEvent event) {
        event.register(dragon_fireAttack);
        event.register(dragon_strike);
        event.register(dragon_down);
        event.register(dragon_change_view);
    }
}
