package com.github.alexthe666.iceandfire.client.gui;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.DragonType;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDragonforge;
import com.github.alexthe666.iceandfire.inventory.ContainerDragonForge;
import com.github.alexthe666.iceandfire.recipe.DragonForgeRecipe;
import com.github.alexthe666.iceandfire.recipe.IafRecipeRegistry;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

public class GuiDragonForge extends AbstractContainerScreen<ContainerDragonForge> {
    private static final Identifier TEXTURE_FIRE = Identifier.parse("iceandfire:textures/gui/dragonforge_fire.png");
    private static final Identifier TEXTURE_ICE = Identifier.parse("iceandfire:textures/gui/dragonforge_ice.png");
    private static final Identifier TEXTURE_LIGHTNING = Identifier.parse("iceandfire:textures/gui/dragonforge_lightning.png");
    private final int dragonType;

    public GuiDragonForge(ContainerDragonForge container, Inventory inventory, Component title) {
        super(container, inventory, title, 176, 166);
        this.dragonType = container.fireType;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        Identifier texture = switch (this.dragonType) {
            case 0 -> TEXTURE_FIRE;
            case 1 -> TEXTURE_ICE;
            default -> TEXTURE_LIGHTNING;
        };
        GuiDrawUtils.blit(graphics, texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        int progress = this.getCookProgress(126);
        if (progress > 0) {
            GuiDrawUtils.blit(graphics, texture, this.leftPos + 12, this.topPos + 23, 0, 166, progress, 38);
        }
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        String title = I18n.get("block.iceandfire.dragonforge_" + DragonType.getNameFromInt(this.dragonType) + "_core");
        graphics.text(this.font, title, this.imageWidth / 2 - this.font.width(title) / 2, 6, 4210752);
        graphics.text(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752);
    }

    private int getCookProgress(int progressWidth) {
        if (this.minecraft.level == null) {
            return 0;
        }

        BlockEntity blockEntity = IceAndFire.PROXY.getRefrencedTE();
        if (!(blockEntity instanceof TileEntityDragonforge forge)) {
            return 0;
        }

        // Recipes only exist on the server; the tile falls back to its default cook time on the client.
        int maxCookTime = Math.max(1, forge.getMaxCookTime());
        int cookTime = Math.min(forge.cookTime, maxCookTime);
        return cookTime == 0 ? 0 : cookTime * progressWidth / maxCookTime;
    }
}
