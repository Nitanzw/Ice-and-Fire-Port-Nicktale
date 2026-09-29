package com.github.alexthe666.iceandfire.client.gui;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityHippogryph;
import com.github.alexthe666.iceandfire.inventory.ContainerHippogryph;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;

public class GuiHippogryph extends AbstractContainerScreen<ContainerHippogryph> {
    private static final Identifier TEXTURE = Identifier.parse("iceandfire:textures/gui/hippogryph.png");

    public GuiHippogryph(ContainerHippogryph menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 176, 166);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        GuiDrawUtils.blit(graphics, TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        Entity entity = IceAndFire.PROXY.getReferencedMob();
        if (entity instanceof EntityHippogryph hippogryph) {
            if (hippogryph.isChested()) {
                GuiDrawUtils.blit(graphics, TEXTURE, this.leftPos + 79, this.topPos + 17, 0, this.imageHeight, 90, 54);
            }
            int x0 = this.leftPos + 34;
            int y0 = this.topPos + 30;
            int x1 = this.leftPos + 68;
            int y1 = this.topPos + 64;
            float xAngle = (float) Math.atan((((x0 + x1) / 2.0F) - mouseX) / 40.0F);
            float yAngle = (float) Math.atan((((y0 + y1) / 2.0F) - mouseY) / 40.0F);
            InventoryScreen.renderEntityInInventoryFollowsAngle(graphics, x0, y0, x1, y1, 17, 0, xAngle, yAngle, hippogryph);
        }
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        Entity entity = IceAndFire.PROXY.getReferencedMob();
        if (entity instanceof EntityHippogryph hippogryph) {
            graphics.text(this.font, hippogryph.getDisplayName(), 8, 6, 4210752);
        }
        graphics.text(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752);
    }
}
