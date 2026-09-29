package com.github.alexthe666.iceandfire.client.render;

import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;

/**
 * Render types used by Ice and Fire. On 26.2 these map to the closest vanilla pipelines; the previous custom
 * shader/blend states (ghost blend, dread portal, decal cracks) need dedicated render pipelines to be restored.
 */
public final class IafRenderType {

    private static final Identifier STONE_TEXTURE = Identifier.parse("textures/block/stone.png");

    private IafRenderType() {
    }

    public static RenderType getGhost(Identifier locationIn) {
        return RenderTypes.entityTranslucent(locationIn);
    }

    public static RenderType getGhostDaytime(Identifier locationIn) {
        return RenderTypes.entityTranslucent(locationIn);
    }

    public static RenderType getDreadlandsPortal() {
        return RenderTypes.endPortal();
    }

    public static RenderType getStoneMobRenderType(float x, float y) {
        return RenderTypes.entityCutout(STONE_TEXTURE);
    }

    public static RenderType getIce(Identifier locationIn) {
        return RenderTypes.entityTranslucent(locationIn);
    }

    public static RenderType getStoneCrackRenderType(Identifier crackTex) {
        return RenderTypes.entityTranslucent(crackTex);
    }
}
