package com.github.alexthe666.iceandfire.client.gui.bestiary;

import com.github.alexthe666.iceandfire.client.gui.GuiDrawUtils;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class IndexPageButton extends Button {

    public IndexPageButton(int x, int y, Component buttonText,
                           net.minecraft.client.gui.components.Button.OnPress butn) {
        super(x, y, 160, 32, buttonText, butn, DEFAULT_NARRATION);
        this.width = 160;
        this.height = 32;
    }

    @Override
    protected void extractContents(GuiGraphicsExtractor pGuiGraphicsExtractor, int mouseX, int mouseY, float partial) {
        if (this.active) {
            Font font = IafConfig.useVanillaFont ? Minecraft.getInstance().font : (Font) IceAndFire.PROXY.getFontRenderer();
            boolean flag = isHoveredOrFocused();
            GuiDrawUtils.blit(pGuiGraphicsExtractor, Identifier.parse("iceandfire:textures/gui/bestiary/widgets.png"), this.getX(), this.getY(), 0, flag ? 32 : 0, this.width, this.height);
            int color = getFGColor() & 0x00FFFFFF | (Math.round(this.alpha * 255.0F) << 24);
            pGuiGraphicsExtractor.centeredText(font, this.getMessage(), this.getX() + this.width / 2,
                this.getY() + (this.height - 9) / 2, color);
        }
    }
}
