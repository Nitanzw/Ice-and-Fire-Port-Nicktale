package com.github.alexthe666.iceandfire.client.gui;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityLectern;
import com.github.alexthe666.iceandfire.enums.EnumBestiaryPages;
import com.github.alexthe666.iceandfire.inventory.ContainerLectern;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.book.BookModel;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.Random;

public class GuiLectern extends AbstractContainerScreen<ContainerLectern> {
    private static final Identifier SCREEN_TEXTURE = Identifier.parse("iceandfire:textures/gui/lectern.png");
    private static final Identifier BOOK_TEXTURE = Identifier.parse("iceandfire:textures/models/lectern_book.png");
    private final Random random = new Random();
    private final Component title;
    private BookModel bookModel;
    public int ticks;
    public float flip;
    public float oFlip;
    public float flipT;
    public float flipA;
    public float open;
    public float oOpen;
    private ItemStack last = ItemStack.EMPTY;
    private int flapTimer;

    public GuiLectern(ContainerLectern menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 176, 166);
        this.title = title;
    }

    @Override
    protected void init() {
        super.init();
        this.bookModel = new BookModel(this.minecraft.getEntityModels().bakeLayer(ModelLayers.BOOK));
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        graphics.text(this.font, this.title, 12, 4, 4210752);
        graphics.text(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752);
    }

    @Override
    public void containerTick() {
        super.containerTick();
        this.menu.onUpdate();
        this.tickBook();
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        double x = event.x() - this.leftPos;
        double y = event.y() - this.topPos;
        for (int button = 0; button < 3; button++) {
            double buttonX = x - 60;
            double buttonY = y - (14 + 19 * button);
            if (buttonX >= 0 && buttonY >= 0 && buttonX < 108 && buttonY < 19
                    && this.minecraft.player != null && this.menu.clickMenuButton(this.minecraft.player, button)) {
                this.flapTimer = 5;
                if (this.minecraft.gameMode != null) {
                    this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, button);
                }
                return true;
            }
        }
        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        GuiDrawUtils.blit(graphics, SCREEN_TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        float bookOpen = Mth.lerp(partialTick, this.oOpen, this.open);
        float bookFlip = Mth.lerp(partialTick, this.oFlip, this.flip) + 0.25F;
        bookFlip = (bookFlip - Mth.floor(bookFlip)) * 1.6F - 0.3F;
        bookFlip = Mth.clamp(bookFlip, 0.0F, 1.0F);
        if (this.bookModel != null) {
            graphics.book(this.bookModel, BOOK_TEXTURE, 1.0F, bookOpen, bookFlip,
                    this.width / 2 - 55, this.height / 2 - 55, this.width / 2 + 55, this.height / 2 + 30);
        }

        EnumBestiaryPages[] pages = this.menu.getPossiblePages();
        if (pages == null) {
            return;
        }
        for (int pageIndex = 0; pageIndex < Math.min(3, pages.length); pageIndex++) {
            int buttonX = this.leftPos + 60;
            int buttonY = this.topPos + 14 + 19 * pageIndex;
            EnumBestiaryPages page = pages[pageIndex];
            if (page == null) {
                GuiDrawUtils.blit(graphics, SCREEN_TEXTURE, buttonX, buttonY, 0, 185, 108, 19);
                continue;
            }

            boolean hasBestiary = this.menu.getSlot(0).getItem().is(IafItemRegistry.BESTIARY.get());
            boolean hovered = mouseX >= buttonX && mouseY >= buttonY && mouseX < buttonX + 108 && mouseY < buttonY + 19;
            int labelColor = hovered ? 0xFFFF00 : 0x686868;
            int costColor = hovered ? 0xFFFF00 : 0x9F988C;
            String label = I18n.get("bestiary." + page.name().toLowerCase());
            String cost = "3";
            if (hasBestiary) {
                GuiDrawUtils.blit(graphics, SCREEN_TEXTURE, buttonX, buttonY, 0, hovered ? 204 : 166, 108, 19);
                GuiDrawUtils.blit(graphics, SCREEN_TEXTURE, buttonX + 1, buttonY + 1, 16 * pageIndex, 223, 16, 16);
                graphics.text(this.font, label, buttonX + 4, buttonY + 4, labelColor);
                graphics.text(this.font, cost, buttonX + 92 - this.font.width(cost), buttonY + 7, costColor);
            } else {
                GuiDrawUtils.blit(graphics, SCREEN_TEXTURE, buttonX, buttonY, 0, 185, 108, 19);
                GuiDrawUtils.blit(graphics, SCREEN_TEXTURE, buttonX + 1, buttonY + 1, 16 * pageIndex, 239, 16, 16);
            }
        }
    }

    private void tickBook() {
        ItemStack current = this.menu.getSlot(0).getItem();
        if (!ItemStack.matches(current, this.last)) {
            this.last = current.copy();
            do {
                this.flipT += this.random.nextInt(4) - this.random.nextInt(4);
            } while (this.flip <= this.flipT + 1.0F && this.flip >= this.flipT - 1.0F);
        }

        this.ticks++;
        this.oFlip = this.flip;
        this.oOpen = this.open;
        boolean hasPages = false;
        EnumBestiaryPages[] possiblePages = this.menu.getPossiblePages();
        if (possiblePages != null) {
            for (int i = 0; i < Math.min(3, possiblePages.length); i++) {
                hasPages |= possiblePages[i] != null;
            }
        }
        this.open = Mth.clamp(this.open + (hasPages ? 0.2F : -0.2F), 0.0F, 1.0F);

        float flap = (this.flipT - this.flip) * 0.4F;
        if (this.flapTimer > 0) {
            flap = (this.ticks + this.minecraft.getFrameTime()) * 0.5F;
            this.flapTimer--;
        }
        flap = Mth.clamp(flap, -0.2F, 0.2F);
        this.flipA += (flap - this.flipA) * 0.9F;
        this.flip += this.flipA;
    }
}
