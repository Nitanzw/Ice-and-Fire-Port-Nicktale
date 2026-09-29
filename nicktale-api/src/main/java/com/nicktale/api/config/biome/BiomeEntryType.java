// SPDX-License-Identifier: LGPL-3.0-or-later
package com.nicktale.api.config.biome;

/** How a biome rule resolves its value. */
public enum BiomeEntryType {
    /** Match a biome tag such as {@code minecraft:is_overworld}. */
    BIOME_TAG,
    /** Match one biome registry identifier such as {@code terralith:steppe}. */
    REGISTRY_NAME
}
