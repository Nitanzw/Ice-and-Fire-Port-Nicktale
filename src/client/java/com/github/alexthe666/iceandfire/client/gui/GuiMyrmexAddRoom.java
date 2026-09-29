package com.github.alexthe666.iceandfire.client.gui;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.ClientProxy;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.message.MessageGetMyrmexHive;
import com.github.alexthe666.iceandfire.world.gen.WorldGenMyrmexHive;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class GuiMyrmexAddRoom extends Screen {
    private static final Identifier JUNGLE_TEXTURE = Identifier.parse("iceandfire:textures/gui/myrmex_staff_jungle.png");
    private static final Identifier DESERT_TEXTURE = Identifier.parse("iceandfire:textures/gui/myrmex_staff_desert.png");
    private final boolean jungle;
    private final BlockPos interactPos;
    private final Direction facing;

    public GuiMyrmexAddRoom(ItemStack staff, BlockPos interactPos, Direction facing) {
        super(Component.translatable("myrmex_add_room"));
        this.jungle = staff.getItem() == IafItemRegistry.MYRMEX_JUNGLE_STAFF.get();
        this.interactPos = interactPos;
        this.facing = facing;
    }

    public static void onGuiClosed() {
        var hive = ClientProxy.getReferedClientHive();
        if (hive != null) {
            IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageGetMyrmexHive(hive.toNBT()));
        }
    }

    @Override
    protected void init() {
        super.init();
        this.clearWidgets();
        var hive = ClientProxy.getReferedClientHive();
        Player player = Minecraft.getInstance().player;
        if (hive == null || player == null) {
            return;
        }

        int left = (this.width - 248) / 2;
        int top = (this.height - 166) / 2;
        this.addRenderableWidget(roomButton("food", left, top + 35, () -> hive.addRoomWithMessage(player, this.interactPos, WorldGenMyrmexHive.RoomType.FOOD)));
        this.addRenderableWidget(roomButton("nursery", left, top + 60, () -> hive.addRoomWithMessage(player, this.interactPos, WorldGenMyrmexHive.RoomType.NURSERY)));
        this.addRenderableWidget(roomButton("enterance_surface", left, top + 85, () -> hive.addEnteranceWithMessage(player, false, this.interactPos, this.facing)));
        this.addRenderableWidget(roomButton("enterance_bottom", left, top + 110, () -> hive.addEnteranceWithMessage(player, true, this.interactPos, this.facing)));
        this.addRenderableWidget(roomButton("misc", left, top + 135, () -> hive.addRoomWithMessage(player, this.interactPos, WorldGenMyrmexHive.RoomType.EMPTY)));
    }

    private Button roomButton(String key, int left, int y, Runnable action) {
        return Button.builder(Component.translatable("myrmex.message.establishroom_" + key), button -> {
                    action.run();
                    onGuiClosed();
                    Minecraft.getInstance().setScreen(null);
                })
                .pos(left + 50, y)
                .size(150, 20)
                .build();
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        int left = (this.width - 248) / 2;
        int top = (this.height - 166) / 2;
        GuiDrawUtils.blit(graphics, this.jungle ? JUNGLE_TEXTURE : DESERT_TEXTURE, left, top, 0, 0, 248, 166);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        var hive = ClientProxy.getReferedClientHive();
        if (hive == null) {
            return;
        }
        int left = (this.width - 248) / 2;
        int top = (this.height - 166) / 2;
        int color = this.jungle ? 0x35EA15 : 0xFFBF00;
        String title = hive.colonyName.isEmpty()
                ? I18n.get("myrmex.message.colony")
                : I18n.get("myrmex.message.colony_named", hive.colonyName);
        graphics.centeredText(this.font, title, left + 124, top + 5, color);
        graphics.text(this.font,
                I18n.get("myrmex.message.create_new_room", this.interactPos.getX(), this.interactPos.getY(), this.interactPos.getZ()),
                left + 30, top + 14, color);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
