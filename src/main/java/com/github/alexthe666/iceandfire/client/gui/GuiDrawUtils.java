package com.github.alexthe666.iceandfire.client.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

/** Small helpers for drawing atlas regions with the 26.2 GUI extractor. */
public final class GuiDrawUtils {
    private GuiDrawUtils() {
    }

    public static void blit(GuiGraphicsExtractor graphics, Identifier texture, int x, int y, int u, int v, int width, int height) {
        blit(graphics, texture, x, y, u, v, width, height, 256, 256);
    }

    public static void blit(GuiGraphicsExtractor graphics, Identifier texture, int x, int y, int u, int v, int width, int height,
                            int textureWidth, int textureHeight) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, u, v, width, height, width, height, textureWidth, textureHeight);
    }
}
