package com.nicktale.api.server.worldgen;

/**
 * Thread-local switch mods can consult in their worldgen features: while it is on, chance rolls and "far enough from
 * other structures" checks should succeed. Used when a site is regenerated outside normal world generation.
 */
public final class ForcedGeneration {
    private static final ThreadLocal<Boolean> FORCING = ThreadLocal.withInitial(() -> false);

    private ForcedGeneration() {
    }

    public static void set(boolean force) {
        FORCING.set(force);
    }

    public static boolean isForcing() {
        return FORCING.get();
    }
}
