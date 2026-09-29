package com.github.alexthe666.iceandfire.client.texture;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.ReloadableTexture;
import net.minecraft.client.renderer.texture.TextureContents;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.ARGB;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

/** Composites several textures (alpha-blended, first is the base) into one dynamic texture. */
public class ArrayLayeredTexture extends ReloadableTexture {
    public final List<String> layeredTextureNames;

    public ArrayLayeredTexture(Identifier id, List<String> textureNames) {
        super(id);
        this.layeredTextureNames = textureNames;
    }

    @Override
    public @NotNull TextureContents loadContents(@NotNull ResourceManager manager) throws IOException {
        TextureContents base = TextureContents.load(manager, Identifier.parse(this.layeredTextureNames.get(0)));
        NativeImage baseImage = base.image();
        for (int layer = 1; layer < this.layeredTextureNames.size(); layer++) {
            String name = this.layeredTextureNames.get(layer);
            if (name == null) {
                continue;
            }
            try (TextureContents overlay = TextureContents.load(manager, Identifier.parse(name))) {
                NativeImage overlayImage = overlay.image();
                int height = Math.min(overlayImage.getHeight(), baseImage.getHeight());
                int width = Math.min(overlayImage.getWidth(), baseImage.getWidth());
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        baseImage.setPixel(x, y, ARGB.alphaBlend(baseImage.getPixel(x, y), overlayImage.getPixel(x, y)));
                    }
                }
            }
        }
        return base;
    }
}
