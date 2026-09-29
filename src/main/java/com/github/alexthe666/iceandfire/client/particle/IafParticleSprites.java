package com.github.alexthe666.iceandfire.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.data.AtlasIds;
import net.minecraft.resources.Identifier;

/**
 * Looks up sprites of Ice and Fire particles in the vanilla particle atlas.
 * The textures under {@code textures/particles} are added to that atlas by
 * {@code assets/minecraft/atlases/particles.json}.
 */
public final class IafParticleSprites {
    private IafParticleSprites() {
    }

    /** @param name file name without extension, e.g. {@code "blood"} */
    public static TextureAtlasSprite get(String name) {
        return Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(AtlasIds.PARTICLES)
                .getSprite(Identifier.fromNamespaceAndPath("iceandfire", "particles/" + name));
    }
}
