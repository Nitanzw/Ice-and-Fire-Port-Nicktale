// SPDX-License-Identifier: LGPL-3.0-or-later
package com.nicktale.api.config.biome;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * A set of ordered biome rule groups. Rules within a priority group are combined; distinct priority values are
 * alternatives. An inverted rule excludes its biome/tag from the group.
 */
public final class SpawnBiomeData {
    private final List<BiomeEntry> entries = new ArrayList<>();

    public SpawnBiomeData addBiomeEntry(BiomeEntryType type, boolean inverted, String value, int priority) {
        entries.add(new BiomeEntry(type, inverted, value, priority));
        return this;
    }

    public List<BiomeEntry> getEntries() {
        return List.copyOf(entries);
    }

    boolean matches(Holder<Biome> biome, Identifier name) {
        if (entries.isEmpty()) {
            return false;
        }
        List<BiomeEntry> ordered = entries.stream().sorted(Comparator.comparingInt(BiomeEntry::priority)).toList();
        int index = 0;
        while (index < ordered.size()) {
            int priority = ordered.get(index).priority();
            boolean hasInclude = false;
            boolean groupMatches = true;
            while (index < ordered.size() && ordered.get(index).priority() == priority) {
                BiomeEntry entry = ordered.get(index++);
                boolean matched = entry.matches(biome, name);
                if (entry.inverted()) {
                    if (matched) {
                        groupMatches = false;
                    }
                } else {
                    hasInclude = true;
                    if (!matched) {
                        groupMatches = false;
                    }
                }
            }
            if (hasInclude && groupMatches) {
                return true;
            }
        }
        return false;
    }

    public record BiomeEntry(BiomeEntryType type, boolean inverted, String value, int priority) {
        public BiomeEntry {
            Objects.requireNonNull(type, "type");
            Objects.requireNonNull(value, "value");
            if (priority < 0) {
                throw new IllegalArgumentException("priority must be non-negative");
            }
        }

        boolean matches(Holder<Biome> biome, Identifier name) {
            Identifier id = Identifier.parse(value);
            return switch (type) {
                case BIOME_TAG -> biome.is(TagKey.create(Registries.BIOME, id));
                case REGISTRY_NAME -> id.equals(name);
            };
        }
    }
}
