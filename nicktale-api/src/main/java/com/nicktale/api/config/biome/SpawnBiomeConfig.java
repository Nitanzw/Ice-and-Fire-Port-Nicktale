// SPDX-License-Identifier: LGPL-3.0-or-later
package com.nicktale.api.config.biome;

import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.biome.Biome;

import java.util.Objects;

/** Runtime biome matcher paired with its configuration identifier. */
public final class SpawnBiomeConfig {
    private final Identifier id;
    private final SpawnBiomeData data;

    private SpawnBiomeConfig(Identifier id, SpawnBiomeData data) {
        this.id = Objects.requireNonNull(id, "id");
        this.data = Objects.requireNonNull(data, "data");
    }

    public static SpawnBiomeConfig create(Identifier id, SpawnBiomeData data) {
        return new SpawnBiomeConfig(id, data);
    }

    public Identifier getId() {
        return id;
    }

    public SpawnBiomeData getData() {
        return data;
    }

    public boolean matches(Holder<Biome> biome, Identifier name) {
        return data.matches(Objects.requireNonNull(biome, "biome"), name);
    }
}
