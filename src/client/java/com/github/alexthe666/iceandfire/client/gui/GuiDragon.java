package com.github.alexthe666.iceandfire.client.gui;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.StatCollector;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.inventory.ContainerDragon;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;

public class GuiDragon extends AbstractContainerScreen<ContainerDragon> {
    private static final Identifier TEXTURE = Identifier.parse("iceandfire:textures/gui/dragon.png");

    public GuiDragon(ContainerDragon menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 176, 214);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        GuiDrawUtils.blit(graphics, TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        Entity entity = IceAndFire.PROXY.getReferencedMob();
        if (entity instanceof EntityDragonBase dragon) {
            int x0 = this.leftPos + 52;
            int y0 = this.topPos + 22;
            int x1 = this.leftPos + 124;
            int y1 = this.topPos + 104;
            float xAngle = (float) Math.atan((((x0 + x1) / 2.0F) - mouseX) / 40.0F);
            float yAngle = (float) Math.atan((((y0 + y1) / 2.0F) - mouseY) / 40.0F);
            InventoryScreen.renderEntityInInventoryFollowsAngle(
                    graphics, x0, y0, x1, y1, 23, dragon.flyProgress * 0.5F, xAngle, yAngle, dragon);
        }
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        Entity entity = IceAndFire.PROXY.getReferencedMob();
        if (!(entity instanceof EntityDragonBase dragon)) {
            return;
        }

        Font font = this.font;
        int center = this.imageWidth / 2;
        String name = dragon.getCustomName() == null
                ? StatCollector.translateToLocal("dragon.unnamed")
                : StatCollector.translateToLocal("dragon.name") + " " + dragon.getCustomName().getString();
        String health = StatCollector.translateToLocal("dragon.health") + " "
                + Math.floor(Math.min(dragon.getHealth(), dragon.getMaxHealth())) + " / " + dragon.getMaxHealth();
        String gender = StatCollector.translateToLocal("dragon.gender")
                + StatCollector.translateToLocal(dragon.isMale() ? "dragon.gender.male" : "dragon.gender.female");
        String hunger = StatCollector.translateToLocal("dragon.hunger") + dragon.getHunger() + "/100";
        String stage = StatCollector.translateToLocal("dragon.stage") + " " + dragon.getDragonStage() + " "
                + StatCollector.translateToLocal("dragon.days.front") + dragon.getAgeInDays() + " "
                + StatCollector.translateToLocal("dragon.days.back");
        String owner = dragon.getOwner() != null
                ? StatCollector.translateToLocal("dragon.owner") + dragon.getOwner().getName().getString()
                : StatCollector.translateToLocal("dragon.untamed");

        drawCentered(graphics, font, name, center, 75);
        drawCentered(graphics, font, health, center, 84);
        drawCentered(graphics, font, gender, center, 93);
        drawCentered(graphics, font, hunger, center, 102);
        drawCentered(graphics, font, stage, center, 111);
        drawCentered(graphics, font, owner, center, 120);
    }

    private static void drawCentered(GuiGraphicsExtractor graphics, Font font, String text, int centerX, int y) {
        graphics.text(font, text, centerX - font.width(text) / 2, y, 0xFFFFFF);
    }
}
