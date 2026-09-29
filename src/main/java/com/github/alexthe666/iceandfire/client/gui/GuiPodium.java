package com.github.alexthe666.iceandfire.client.gui;

import com.github.alexthe666.iceandfire.inventory.ContainerPodium;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class GuiPodium extends AbstractContainerScreen<ContainerPodium> {
    public static final Identifier PODIUM_TEXTURE = Identifier.parse("iceandfire:textures/gui/podium.png");

    public GuiPodium(ContainerPodium menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 176, 133);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        GuiDrawUtils.blit(graphics, PODIUM_TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        String title = I18n.get("block.iceandfire.podium");
        graphics.text(this.font, title, this.imageWidth / 2 - this.font.width(title) / 2, 6, 4210752);
        graphics.text(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752);
    }
}
