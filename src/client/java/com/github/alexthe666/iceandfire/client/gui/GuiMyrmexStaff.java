package com.github.alexthe666.iceandfire.client.gui;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.ClientProxy;
import com.github.alexthe666.iceandfire.client.gui.bestiary.ChangePageButton;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.message.MessageGetMyrmexHive;
import com.github.alexthe666.iceandfire.world.gen.WorldGenMyrmexHive;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GuiMyrmexStaff extends Screen {
    private static final Identifier JUNGLE_TEXTURE = Identifier.parse("iceandfire:textures/gui/myrmex_staff_jungle.png");
    private static final Identifier DESERT_TEXTURE = Identifier.parse("iceandfire:textures/gui/myrmex_staff_desert.png");
    private static final WorldGenMyrmexHive.RoomType[] ROOMS = {
            WorldGenMyrmexHive.RoomType.FOOD,
            WorldGenMyrmexHive.RoomType.NURSERY,
            WorldGenMyrmexHive.RoomType.EMPTY
    };
    private static final int ROOMS_PER_PAGE = 5;
    private final List<Room> rooms = new ArrayList<>();
    private final List<MyrmexDeleteButton> deleteButtons = new ArrayList<>();
    public ChangePageButton previousPage;
    public ChangePageButton nextPage;
    private int ticksSinceDeleted;
    private int currentPage;
    private final boolean jungle;

    public GuiMyrmexStaff(ItemStack staff) {
        super(Component.translatable("myrmex_staff_screen"));
        this.jungle = staff.getItem() == IafItemRegistry.MYRMEX_JUNGLE_STAFF.get();
    }

    @Override
    protected void init() {
        super.init();
        this.clearWidgets();
        this.deleteButtons.clear();
        var hive = ClientProxy.getReferedClientHive();
        if (hive == null) {
            this.rooms.clear();
            return;
        }
        this.populateRoomList();

        int left = (this.width - 248) / 2;
        int top = (this.height - 166) / 2;
        Button breedingButton = Button.builder(
                        breedingMessage(hive.reproduces),
                        button -> {
                            var currentHive = ClientProxy.getReferedClientHive();
                            if (currentHive != null) {
                                currentHive.reproduces = !currentHive.reproduces;
                                button.setMessage(breedingMessage(currentHive.reproduces));
                            }
                        })
                .pos(left + 124, top + 15)
                .size(120, 20)
                .build();
        this.addRenderableWidget(breedingButton);

        this.previousPage = new ChangePageButton(left + 5, top + 150, false, this.jungle ? 2 : 1, button -> {
            if (this.currentPage > 0) {
                this.currentPage--;
                this.refreshRoomButtons();
            }
        });
        this.nextPage = new ChangePageButton(left + 225, top + 150, true, this.jungle ? 2 : 1, button -> {
            if (this.currentPage < this.maxPage()) {
                this.currentPage++;
                this.refreshRoomButtons();
            }
        });
        this.addRenderableWidget(this.previousPage);
        this.addRenderableWidget(this.nextPage);

        for (int index = 0; index < this.rooms.size(); index++) {
            Room room = this.rooms.get(index);
            int y = top + 37 + (index % ROOMS_PER_PAGE) * 22;
            MyrmexDeleteButton button = new MyrmexDeleteButton(left + 193, y, room.pos(),
                    Component.translatable("myrmex.message.delete"), ignored -> this.deleteRoom(room.pos()));
            this.deleteButtons.add(button);
            this.addRenderableWidget(button);
        }
        this.refreshRoomButtons();
    }

    private static Component breedingMessage(boolean enabled) {
        return Component.translatable(enabled ? "myrmex.message.disablebreeding" : "myrmex.message.enablebreeding");
    }

    private void populateRoomList() {
        this.rooms.clear();
        var hive = ClientProxy.getReferedClientHive();
        if (hive == null) {
            return;
        }

        for (WorldGenMyrmexHive.RoomType type : ROOMS) {
            String name = switch (type) {
                case FOOD -> "food";
                case NURSERY -> "nursery";
                default -> "misc";
            };
            for (BlockPos pos : hive.getRooms(type)) {
                this.rooms.add(new Room(pos, name));
            }
        }
        for (BlockPos pos : hive.getEntrances().keySet()) {
            this.rooms.add(new Room(pos, "enterance_surface"));
        }
        for (BlockPos pos : hive.getEntranceBottoms().keySet()) {
            this.rooms.add(new Room(pos, "enterance_bottom"));
        }
        this.currentPage = Math.min(this.currentPage, this.maxPage());
    }

    private int maxPage() {
        return Math.max(0, (this.rooms.size() - 1) / ROOMS_PER_PAGE);
    }

    private void refreshRoomButtons() {
        for (int index = 0; index < this.deleteButtons.size(); index++) {
            MyrmexDeleteButton button = this.deleteButtons.get(index);
            button.visible = index >= ROOMS_PER_PAGE * this.currentPage
                    && index < ROOMS_PER_PAGE * (this.currentPage + 1);
        }
        if (this.previousPage != null) {
            this.previousPage.active = this.currentPage > 0;
        }
        if (this.nextPage != null) {
            this.nextPage.active = this.currentPage < this.maxPage();
        }
    }

    private void deleteRoom(BlockPos pos) {
        var hive = ClientProxy.getReferedClientHive();
        if (this.ticksSinceDeleted > 0 || hive == null) {
            return;
        }
        hive.removeRoom(pos);
        this.ticksSinceDeleted = 5;
        this.rebuildWidgets();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.ticksSinceDeleted > 0) {
            this.ticksSinceDeleted--;
        }
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

        int left = (this.width - 248) / 2 + 10;
        int top = (this.height - 166) / 2 + 8;
        int color = this.jungle ? 0x35EA15 : 0xFFBF00;
        String title = hive.colonyName.isEmpty()
                ? I18n.get("myrmex.message.colony")
                : I18n.get("myrmex.message.colony_named", hive.colonyName);
        graphics.centeredText(this.font, title, (this.width / 2) + 10, top - 3, color);
        if (Minecraft.getInstance().player != null) {
            int opinion = hive.getPlayerReputation(Minecraft.getInstance().player.getUUID());
            graphics.text(this.font, I18n.get("myrmex.message.hive_opinion", opinion), left, top + 12, color);
        }
        graphics.text(this.font, I18n.get("myrmex.message.rooms"), left, top + 25, color);

        int visibleRoom = 0;
        int firstRoom = ROOMS_PER_PAGE * this.currentPage;
        int lastRoom = Math.min(firstRoom + ROOMS_PER_PAGE, this.rooms.size());
        for (int index = firstRoom; index < lastRoom; index++) {
            Room room = this.rooms.get(index);
            String key = "myrmex.message.room." + room.name();
            graphics.text(this.font, I18n.get(key, room.pos().getX(), room.pos().getY(), room.pos().getZ()),
                    left, top + 36 + visibleRoom * 22, color);
            visibleRoom++;
        }
    }

    @Override
    public void removed() {
        var hive = ClientProxy.getReferedClientHive();
        if (hive != null) {
            IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageGetMyrmexHive(hive.toNBT()));
        }
        super.removed();
    }

    private record Room(BlockPos pos, String name) {
    }
}
