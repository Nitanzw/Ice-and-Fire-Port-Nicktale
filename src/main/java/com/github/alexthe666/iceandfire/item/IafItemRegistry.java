package com.github.alexthe666.iceandfire.item;

import com.nicktale.api.server.item.CustomArmorMaterial;
import com.nicktale.api.server.item.CustomToolMaterial;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.datagen.tags.BannerPatternTagGenerator;
import com.github.alexthe666.iceandfire.datagen.tags.IafItemTags;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.enums.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;`r`nimport net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.function.Supplier;
import java.util.function.Function;

import static com.github.alexthe666.iceandfire.item.DragonSteelTier.*;

public class IafItemRegistry {
    public static CustomArmorMaterial SILVER_ARMOR_MATERIAL = new IafArmorMaterial("silver", 15, new int[]{1, 4, 5, 2}, 20, SoundEvents.ARMOR_EQUIP_CHAIN, 0);
    public static CustomArmorMaterial COPPER_ARMOR_MATERIAL = new IafArmorMaterial("copper", 10, new int[]{1, 3, 4, 2}, 15, SoundEvents.ARMOR_EQUIP_GOLD, 0);
    public static CustomArmorMaterial BLINDFOLD_ARMOR_MATERIAL = new IafArmorMaterial("blindfold", 5, new int[]{1, 1, 1, 1}, 10, SoundEvents.ARMOR_EQUIP_LEATHER, 0);
    public static CustomArmorMaterial SHEEP_ARMOR_MATERIAL = new IafArmorMaterial("sheep", 5, new int[]{1, 3, 2, 1}, 15, SoundEvents.ARMOR_EQUIP_LEATHER, 0);
    public static CustomArmorMaterial MYRMEX_DESERT_ARMOR_MATERIAL = new IafArmorMaterial("myrmexdesert", 20, new int[]{3, 5, 8, 4}, 15, SoundEvents.ARMOR_EQUIP_LEATHER, 0);
    public static CustomArmorMaterial MYRMEX_JUNGLE_ARMOR_MATERIAL = new IafArmorMaterial("myrmexjungle", 20, new int[]{3, 5, 8, 4}, 15, SoundEvents.ARMOR_EQUIP_LEATHER, 0);
    public static CustomArmorMaterial EARPLUGS_ARMOR_MATERIAL = new IafArmorMaterial("earplugs", 5, new int[]{1, 1, 1, 1}, 10, SoundEvents.ARMOR_EQUIP_LEATHER, 0);
    public static CustomArmorMaterial DEATHWORM_0_ARMOR_MATERIAL = new IafArmorMaterial("yellow seathworm", 15, new int[]{2, 5, 7, 3}, 5, SoundEvents.ARMOR_EQUIP_LEATHER, 1.5F);
    public static CustomArmorMaterial DEATHWORM_1_ARMOR_MATERIAL = new IafArmorMaterial("white seathworm", 15, new int[]{2, 5, 7, 3}, 5, SoundEvents.ARMOR_EQUIP_LEATHER, 1.5F);
    public static CustomArmorMaterial DEATHWORM_2_ARMOR_MATERIAL = new IafArmorMaterial("red deathworm", 15, new int[]{2, 5, 7, 3}, 5, SoundEvents.ARMOR_EQUIP_LEATHER, 1.5F);
    public static CustomArmorMaterial TROLL_MOUNTAIN_ARMOR_MATERIAL = new IafArmorMaterial("mountain troll", 20, new int[]{2, 5, 7, 3}, 10, SoundEvents.ARMOR_EQUIP_LEATHER, 1F);
    public static CustomArmorMaterial TROLL_FOREST_ARMOR_MATERIAL = new IafArmorMaterial("forest troll", 20, new int[]{2, 5, 7, 3}, 10, SoundEvents.ARMOR_EQUIP_LEATHER, 1F);
    public static CustomArmorMaterial TROLL_FROST_ARMOR_MATERIAL = new IafArmorMaterial("frost troll", 20, new int[]{2, 5, 7, 3}, 10, SoundEvents.ARMOR_EQUIP_LEATHER, 1F);
    public static CustomArmorMaterial DRAGONSTEEL_FIRE_ARMOR_MATERIAL = new DragonsteelArmorMaterial("dragonsteel_fire", (int) (0.02D * IafConfig.dragonsteelBaseDurabilityEquipment), new int[]{IafConfig.dragonsteelBaseArmor - 6, IafConfig.dragonsteelBaseArmor - 3, IafConfig.dragonsteelBaseArmor, IafConfig.dragonsteelBaseArmor - 5}, 30, SoundEvents.ARMOR_EQUIP_DIAMOND, IafConfig.dragonsteelBaseArmorToughness);
    public static CustomArmorMaterial DRAGONSTEEL_ICE_ARMOR_MATERIAL = new DragonsteelArmorMaterial("dragonsteel_ice", (int) (0.02D * IafConfig.dragonsteelBaseDurabilityEquipment), new int[]{IafConfig.dragonsteelBaseArmor - 6, IafConfig.dragonsteelBaseArmor - 3, IafConfig.dragonsteelBaseArmor, IafConfig.dragonsteelBaseArmor - 5}, 30, SoundEvents.ARMOR_EQUIP_DIAMOND, IafConfig.dragonsteelBaseArmorToughness);
    public static CustomArmorMaterial DRAGONSTEEL_LIGHTNING_ARMOR_MATERIAL = new DragonsteelArmorMaterial("dragonsteel_lightning", (int) (0.02D * IafConfig.dragonsteelBaseDurabilityEquipment), new int[]{IafConfig.dragonsteelBaseArmor - 6, IafConfig.dragonsteelBaseArmor - 3, IafConfig.dragonsteelBaseArmor, IafConfig.dragonsteelBaseArmor - 5}, 30, SoundEvents.ARMOR_EQUIP_DIAMOND, IafConfig.dragonsteelBaseArmorToughness);
    public static CustomToolMaterial SILVER_TOOL_MATERIAL = new CustomToolMaterial("silver", 2, 460, 1.0F, 11.0F, 18);
    public static CustomToolMaterial COPPER_TOOL_MATERIAL = new CustomToolMaterial("copper", 2, 300, 0.0F, 0.7F, 10);
    public static CustomToolMaterial DRAGONBONE_TOOL_MATERIAL = new CustomToolMaterial("Dragonbone", 3, 1660, 4.0F, 10.0F, 22);
    public static CustomToolMaterial FIRE_DRAGONBONE_TOOL_MATERIAL = new CustomToolMaterial("FireDragonbone", 3, 2000, 5.5F, 10F, 22);
    public static CustomToolMaterial ICE_DRAGONBONE_TOOL_MATERIAL = new CustomToolMaterial("IceDragonbone", 3, 2000, 5.5F, 10F, 22);
    public static CustomToolMaterial LIGHTNING_DRAGONBONE_TOOL_MATERIAL = new CustomToolMaterial("LightningDragonbone", 3, 2000, 5.5F, 10F, 22);
    public static CustomToolMaterial TROLL_WEAPON_TOOL_MATERIAL = new CustomToolMaterial("trollWeapon", 2, 300, 1F, 10F, 1);
    public static CustomToolMaterial MYRMEX_CHITIN_TOOL_MATERIAL = new CustomToolMaterial("MyrmexChitin", 3, 600, 1.0F, 6.0F, 8);
    public static CustomToolMaterial HIPPOGRYPH_SWORD_TOOL_MATERIAL = new CustomToolMaterial("HippogryphSword", 2, 500, 2.5F, 10F, 10);
    public static CustomToolMaterial STYMHALIAN_SWORD_TOOL_MATERIAL = new CustomToolMaterial("StymphalianSword", 2, 500, 2, 10.0F, 10);
    public static CustomToolMaterial AMPHITHERE_SWORD_TOOL_MATERIAL = new CustomToolMaterial("AmphithereSword", 2, 500, 1F, 10F, 10);
    public static CustomToolMaterial HIPPOCAMPUS_SWORD_TOOL_MATERIAL = new CustomToolMaterial("HippocampusSword", 0, 500, -2F, 0F, 50);
    public static CustomToolMaterial DREAD_SWORD_TOOL_MATERIAL = new CustomToolMaterial("DreadSword", 0, 100, 1F, 10F, 0);
    public static CustomToolMaterial DREAD_KNIGHT_TOOL_MATERIAL = new CustomToolMaterial("DreadKnightSword", 0, 1200, 13F, 0F, 10);
    public static CustomToolMaterial GHOST_SWORD_TOOL_MATERIAL = new CustomToolMaterial("GhostSword", 2, 3000, 5, 10.0F, 25);

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(IceAndFire.MODID);
    private static final ThreadLocal<Item.Properties> ACTIVE_ITEM_PROPERTIES = new ThreadLocal<>();


    public static final DeferredItem<Item> BESTIARY = registerItem("bestiary", ItemBestiary::new);
    public static final DeferredItem<Item> MANUSCRIPT = registerItem("manuscript", () -> new ItemGeneric());
    public static final DeferredItem<Item> SAPPHIRE_GEM = registerItem("sapphire_gem", () -> new ItemGeneric());
    public static final DeferredItem<Item> SILVER_INGOT = registerItem("silver_ingot", () -> new ItemGeneric());
    public static final DeferredItem<Item> SILVER_NUGGET = registerItem("silver_nugget", () -> new ItemGeneric());
    public static final DeferredItem<Item> RAW_SILVER = registerItem("raw_silver", () -> new ItemGeneric());
    public static final DeferredItem<Item> COPPER_NUGGET = registerItem("copper_nugget", () -> new ItemGeneric());
    public static final DeferredItem<Item> SILVER_HELMET = registerItem("armor_silver_metal_helmet", () -> new ItemSilverArmor(SILVER_ARMOR_MATERIAL, ArmorType.HELMET));
    public static final DeferredItem<Item> SILVER_CHESTPLATE = registerItem("armor_silver_metal_chestplate", () -> new ItemSilverArmor(SILVER_ARMOR_MATERIAL, ArmorType.CHESTPLATE));
    public static final DeferredItem<Item> SILVER_LEGGINGS = registerItem("armor_silver_metal_leggings", () -> new ItemSilverArmor(SILVER_ARMOR_MATERIAL, ArmorType.LEGGINGS));
    public static final DeferredItem<Item> SILVER_BOOTS = registerItem("armor_silver_metal_boots", () -> new ItemSilverArmor(SILVER_ARMOR_MATERIAL, ArmorType.BOOTS));
    public static final DeferredItem<Item> SILVER_SWORD = registerItem("silver_sword", () -> new ItemModSword(SILVER_TOOL_MATERIAL));
    public static final DeferredItem<Item> SILVER_SHOVEL = registerItem("silver_shovel", () -> new ItemModShovel(SILVER_TOOL_MATERIAL));
    public static final DeferredItem<Item> SILVER_PICKAXE = registerItem("silver_pickaxe", () -> new ItemModPickaxe(SILVER_TOOL_MATERIAL));
    public static final DeferredItem<Item> SILVER_AXE = registerItem("silver_axe", () -> new ItemModAxe(SILVER_TOOL_MATERIAL));
    public static final DeferredItem<Item> SILVER_HOE = registerItem("silver_hoe", () -> new ItemModHoe(SILVER_TOOL_MATERIAL));

    public static final DeferredItem<Item> COPPER_HELMET = registerItem("armor_copper_metal_helmet", () -> new ItemCopperArmor(COPPER_ARMOR_MATERIAL, ArmorType.HELMET));
    public static final DeferredItem<Item> COPPER_CHESTPLATE = registerItem("armor_copper_metal_chestplate", () -> new ItemCopperArmor(COPPER_ARMOR_MATERIAL, ArmorType.CHESTPLATE));
    public static final DeferredItem<Item> COPPER_LEGGINGS = registerItem("armor_copper_metal_leggings", () -> new ItemCopperArmor(COPPER_ARMOR_MATERIAL, ArmorType.LEGGINGS));
    public static final DeferredItem<Item> COPPER_BOOTS = registerItem("armor_copper_metal_boots", () -> new ItemCopperArmor(COPPER_ARMOR_MATERIAL, ArmorType.BOOTS));
    public static final DeferredItem<Item> COPPER_SWORD = registerItem("copper_sword", () -> new ItemModSword(COPPER_TOOL_MATERIAL));
    public static final DeferredItem<Item> COPPER_SHOVEL = registerItem("copper_shovel", () -> new ItemModShovel(COPPER_TOOL_MATERIAL));
    public static final DeferredItem<Item> COPPER_PICKAXE = registerItem("copper_pickaxe", () -> new ItemModPickaxe(COPPER_TOOL_MATERIAL));
    public static final DeferredItem<Item> COPPER_AXE = registerItem("copper_axe", () -> new ItemModAxe(COPPER_TOOL_MATERIAL));
    public static final DeferredItem<Item> COPPER_HOE = registerItem("copper_hoe", () -> new ItemModHoe(COPPER_TOOL_MATERIAL));

    public static final DeferredItem<Item> FIRE_STEW = registerItem("fire_stew", () -> new ItemGeneric());
    public static final DeferredItem<Item> FROST_STEW = registerItem("frost_stew", () -> new ItemGeneric());
    public static final DeferredItem<Item> LIGHTNING_STEW = registerItem("lightning_stew", () -> new ItemGeneric());
    public static final DeferredItem<Item> DRAGONEGG_RED = registerItem("dragonegg_red", () -> new ItemDragonEgg(EnumDragonEgg.RED));
    public static final DeferredItem<Item> DRAGONEGG_GREEN = registerItem("dragonegg_green", () -> new ItemDragonEgg(EnumDragonEgg.GREEN));
    public static final DeferredItem<Item> DRAGONEGG_BRONZE = registerItem("dragonegg_bronze", () -> new ItemDragonEgg(EnumDragonEgg.BRONZE));
    public static final DeferredItem<Item> DRAGONEGG_GRAY = registerItem("dragonegg_gray", () -> new ItemDragonEgg(EnumDragonEgg.GRAY));
    public static final DeferredItem<Item> DRAGONEGG_BLUE = registerItem("dragonegg_blue", () -> new ItemDragonEgg(EnumDragonEgg.BLUE));
    public static final DeferredItem<Item> DRAGONEGG_WHITE = registerItem("dragonegg_white", () -> new ItemDragonEgg(EnumDragonEgg.WHITE));
    public static final DeferredItem<Item> DRAGONEGG_SAPPHIRE = registerItem("dragonegg_sapphire", () -> new ItemDragonEgg(EnumDragonEgg.SAPPHIRE));
    public static final DeferredItem<Item> DRAGONEGG_SILVER = registerItem("dragonegg_silver", () -> new ItemDragonEgg(EnumDragonEgg.SILVER));
    public static final DeferredItem<Item> DRAGONEGG_ELECTRIC = registerItem("dragonegg_electric", () -> new ItemDragonEgg(EnumDragonEgg.ELECTRIC));
    public static final DeferredItem<Item> DRAGONEGG_AMYTHEST = registerItem("dragonegg_amythest", () -> new ItemDragonEgg(EnumDragonEgg.AMYTHEST));
    public static final DeferredItem<Item> DRAGONEGG_COPPER = registerItem("dragonegg_copper", () -> new ItemDragonEgg(EnumDragonEgg.COPPER));
    public static final DeferredItem<Item> DRAGONEGG_BLACK = registerItem("dragonegg_black", () -> new ItemDragonEgg(EnumDragonEgg.BLACK));
    public static final DeferredItem<Item> DRAGONSCALES_RED = registerItem("dragonscales_red", () -> new ItemDragonScales(EnumDragonEgg.RED));
    public static final DeferredItem<Item> DRAGONSCALES_GREEN = registerItem("dragonscales_green", () -> new ItemDragonScales(EnumDragonEgg.GREEN));
    public static final DeferredItem<Item> DRAGONSCALES_BRONZE = registerItem("dragonscales_bronze", () -> new ItemDragonScales(EnumDragonEgg.BRONZE));
    public static final DeferredItem<Item> DRAGONSCALES_GRAY = registerItem("dragonscales_gray", () -> new ItemDragonScales(EnumDragonEgg.GRAY));
    public static final DeferredItem<Item> DRAGONSCALES_BLUE = registerItem("dragonscales_blue", () -> new ItemDragonScales(EnumDragonEgg.BLUE));
    public static final DeferredItem<Item> DRAGONSCALES_WHITE = registerItem("dragonscales_white", () -> new ItemDragonScales(EnumDragonEgg.WHITE));
    public static final DeferredItem<Item> DRAGONSCALES_SAPPHIRE = registerItem("dragonscales_sapphire", () -> new ItemDragonScales(EnumDragonEgg.SAPPHIRE));
    public static final DeferredItem<Item> DRAGONSCALES_SILVER = registerItem("dragonscales_silver", () -> new ItemDragonScales(EnumDragonEgg.SILVER));
    public static final DeferredItem<Item> DRAGONSCALES_ELECTRIC = registerItem("dragonscales_electric", () -> new ItemDragonScales(EnumDragonEgg.ELECTRIC));
    public static final DeferredItem<Item> DRAGONSCALES_AMYTHEST = registerItem("dragonscales_amythest", () -> new ItemDragonScales(EnumDragonEgg.AMYTHEST));
    public static final DeferredItem<Item> DRAGONSCALES_COPPER = registerItem("dragonscales_copper", () -> new ItemDragonScales(EnumDragonEgg.COPPER));
    public static final DeferredItem<Item> DRAGONSCALES_BLACK = registerItem("dragonscales_black", () -> new ItemDragonScales(EnumDragonEgg.BLACK));
    public static final DeferredItem<Item> DRAGON_BONE = registerItem("dragonbone", () -> new ItemDragonBone());
    public static final DeferredItem<Item> WITHERBONE = registerItem("witherbone", () -> new ItemGeneric());
    public static final DeferredItem<Item> FISHING_SPEAR = registerItem("fishing_spear", () -> new ItemFishingSpear());
    public static final DeferredItem<Item> WITHER_SHARD = registerItem("wither_shard", () -> new ItemGeneric());
    public static final DeferredItem<Item> DRAGONBONE_SWORD = registerItem("dragonbone_sword", () -> new ItemModSword(DRAGONBONE_TOOL_MATERIAL));
    public static final DeferredItem<Item> DRAGONBONE_SHOVEL = registerItem("dragonbone_shovel", () -> new ItemModShovel(DRAGONBONE_TOOL_MATERIAL));
    public static final DeferredItem<Item> DRAGONBONE_PICKAXE = registerItem("dragonbone_pickaxe", () -> new ItemModPickaxe(DRAGONBONE_TOOL_MATERIAL));
    public static final DeferredItem<Item> DRAGONBONE_AXE = registerItem("dragonbone_axe", () -> new ItemModAxe(DRAGONBONE_TOOL_MATERIAL));
    public static final DeferredItem<Item> DRAGONBONE_HOE = registerItem("dragonbone_hoe", () -> new ItemModHoe(DRAGONBONE_TOOL_MATERIAL));
    public static final DeferredItem<Item> DRAGONBONE_SWORD_FIRE = registerItem("dragonbone_sword_fire", () -> new ItemAlchemySword(FIRE_DRAGONBONE_TOOL_MATERIAL));
    public static final DeferredItem<Item> DRAGONBONE_SWORD_ICE = registerItem("dragonbone_sword_ice", () -> new ItemAlchemySword(ICE_DRAGONBONE_TOOL_MATERIAL));
    public static final DeferredItem<Item> DRAGONBONE_SWORD_LIGHTNING = registerItem("dragonbone_sword_lightning", () -> new ItemAlchemySword(LIGHTNING_DRAGONBONE_TOOL_MATERIAL));
    public static final DeferredItem<Item> DRAGONBONE_ARROW = registerItem("dragonbone_arrow", () -> new ItemDragonArrow());
    public static final DeferredItem<Item> DRAGON_BOW = registerItem("dragonbone_bow", () -> new ItemDragonBow());
    public static final DeferredItem<Item> DRAGON_SKULL_FIRE = registerItem(ItemDragonSkull.getName(0), () -> new ItemDragonSkull(0));
    public static final DeferredItem<Item> DRAGON_SKULL_ICE = registerItem(ItemDragonSkull.getName(1), () -> new ItemDragonSkull(1));
    public static final DeferredItem<Item> DRAGON_SKULL_LIGHTNING = registerItem(ItemDragonSkull.getName(2), () -> new ItemDragonSkull(2));
    public static final DeferredItem<ItemDragonArmor> DRAGONARMOR_IRON_0 = registerItem("dragonarmor_iron_" + ItemDragonArmor.getNameForSlot(0), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.IRON, 0));
    public static final DeferredItem<ItemDragonArmor> DRAGONARMOR_IRON_1 = registerItem("dragonarmor_iron_" + ItemDragonArmor.getNameForSlot(1), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.IRON, 1));
    public static final DeferredItem<ItemDragonArmor> DRAGONARMOR_IRON_2 = registerItem("dragonarmor_iron_" + ItemDragonArmor.getNameForSlot(2), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.IRON, 2));
    public static final DeferredItem<ItemDragonArmor> DRAGONARMOR_IRON_3 = registerItem("dragonarmor_iron_" + ItemDragonArmor.getNameForSlot(3), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.IRON, 3));
    public static final DeferredItem<ItemDragonArmor> DRAGONARMOR_COPPER_0 = registerItem("dragonarmor_copper_" + ItemDragonArmor.getNameForSlot(0), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.COPPER, 0));
    public static final DeferredItem<ItemDragonArmor> DRAGONARMOR_COPPER_1 = registerItem("dragonarmor_copper_" + ItemDragonArmor.getNameForSlot(1), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.COPPER, 1));
    public static final DeferredItem<ItemDragonArmor> DRAGONARMOR_COPPER_2 = registerItem("dragonarmor_copper_" + ItemDragonArmor.getNameForSlot(2), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.COPPER, 2));
    public static final DeferredItem<ItemDragonArmor> DRAGONARMOR_COPPER_3 = registerItem("dragonarmor_copper_" + ItemDragonArmor.getNameForSlot(3), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.COPPER, 3));
    public static final DeferredItem<ItemDragonArmor> DRAGONARMOR_GOLD_0 = registerItem("dragonarmor_gold_" + ItemDragonArmor.getNameForSlot(0), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.GOLD, 0));
    public static final DeferredItem<ItemDragonArmor> DRAGONARMOR_GOLD_1 = registerItem("dragonarmor_gold_" + ItemDragonArmor.getNameForSlot(1), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.GOLD, 1));
    public static final DeferredItem<ItemDragonArmor> DRAGONARMOR_GOLD_2 = registerItem("dragonarmor_gold_" + ItemDragonArmor.getNameForSlot(2), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.GOLD, 2));
    public static final DeferredItem<ItemDragonArmor> DRAGONARMOR_GOLD_3 = registerItem("dragonarmor_gold_" + ItemDragonArmor.getNameForSlot(3), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.GOLD, 3));
    public static final DeferredItem<ItemDragonArmor> DRAGONARMOR_DIAMOND_0 = registerItem("dragonarmor_diamond_" + ItemDragonArmor.getNameForSlot(0), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.DIAMOND, 0));
    public static final DeferredItem<ItemDragonArmor> DRAGONARMOR_DIAMOND_1 = registerItem("dragonarmor_diamond_" + ItemDragonArmor.getNameForSlot(1), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.DIAMOND, 1));
    public static final DeferredItem<ItemDragonArmor> DRAGONARMOR_DIAMOND_2 = registerItem("dragonarmor_diamond_" + ItemDragonArmor.getNameForSlot(2), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.DIAMOND, 2));
    public static final DeferredItem<ItemDragonArmor> DRAGONARMOR_DIAMOND_3 = registerItem("dragonarmor_diamond_" + ItemDragonArmor.getNameForSlot(3), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.DIAMOND, 3));
    public static final DeferredItem<ItemDragonArmor> DRAGONARMOR_SILVER_0 = registerItem("dragonarmor_silver_" + ItemDragonArmor.getNameForSlot(0), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.SILVER, 0));
    public static final DeferredItem<ItemDragonArmor> DRAGONARMOR_SILVER_1 = registerItem("dragonarmor_silver_" + ItemDragonArmor.getNameForSlot(1), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.SILVER, 1));
    public static final DeferredItem<ItemDragonArmor> DRAGONARMOR_SILVER_2 = registerItem("dragonarmor_silver_" + ItemDragonArmor.getNameForSlot(2), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.SILVER, 2));
    public static final DeferredItem<ItemDragonArmor> DRAGONARMOR_SILVER_3 = registerItem("dragonarmor_silver_" + ItemDragonArmor.getNameForSlot(3), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.SILVER, 3));
    public static final DeferredItem<ItemDragonArmor> DRAGONARMOR_DRAGONSTEEL_FIRE_0 = registerItem("dragonarmor_dragonsteel_fire_" + ItemDragonArmor.getNameForSlot(0), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.FIRE, 0));
    public static final DeferredItem<ItemDragonArmor> DRAGONARMOR_DRAGONSTEEL_FIRE_1 = registerItem("dragonarmor_dragonsteel_fire_" + ItemDragonArmor.getNameForSlot(1), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.FIRE, 1));
    public static final DeferredItem<ItemDragonArmor> DRAGONARMOR_DRAGONSTEEL_FIRE_2 = registerItem("dragonarmor_dragonsteel_fire_" + ItemDragonArmor.getNameForSlot(2), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.FIRE, 2));
    public static final DeferredItem<ItemDragonArmor> DRAGONARMOR_DRAGONSTEEL_FIRE_3 = registerItem("dragonarmor_dragonsteel_fire_" + ItemDragonArmor.getNameForSlot(3), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.FIRE, 3));
    public static final DeferredItem<ItemDragonArmor> DRAGONARMOR_DRAGONSTEEL_ICE_0 = registerItem("dragonarmor_dragonsteel_ice_" + ItemDragonArmor.getNameForSlot(0), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.ICE, 0));
    public static final DeferredItem<ItemDragonArmor> DRAGONARMOR_DRAGONSTEEL_ICE_1 = registerItem("dragonarmor_dragonsteel_ice_" + ItemDragonArmor.getNameForSlot(1), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.ICE, 1));
    public static final DeferredItem<ItemDragonArmor> DRAGONARMOR_DRAGONSTEEL_ICE_2 = registerItem("dragonarmor_dragonsteel_ice_" + ItemDragonArmor.getNameForSlot(2), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.ICE, 2));
    public static final DeferredItem<ItemDragonArmor> DRAGONARMOR_DRAGONSTEEL_ICE_3 = registerItem("dragonarmor_dragonsteel_ice_" + ItemDragonArmor.getNameForSlot(3), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.ICE, 3));
    public static final DeferredItem<ItemDragonArmor> DRAGONARMOR_DRAGONSTEEL_LIGHTNING_0 = registerItem("dragonarmor_dragonsteel_lightning_" + ItemDragonArmor.getNameForSlot(0), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.LIGHTNING, 0));
    public static final DeferredItem<ItemDragonArmor> DRAGONARMOR_DRAGONSTEEL_LIGHTNING_1 = registerItem("dragonarmor_dragonsteel_lightning_" + ItemDragonArmor.getNameForSlot(1), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.LIGHTNING, 1));
    public static final DeferredItem<ItemDragonArmor> DRAGONARMOR_DRAGONSTEEL_LIGHTNING_2 = registerItem("dragonarmor_dragonsteel_lightning_" + ItemDragonArmor.getNameForSlot(2), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.LIGHTNING, 2));
    public static final DeferredItem<ItemDragonArmor> DRAGONARMOR_DRAGONSTEEL_LIGHTNING_3 = registerItem("dragonarmor_dragonsteel_lightning_" + ItemDragonArmor.getNameForSlot(3), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.LIGHTNING, 3));
    public static final DeferredItem<Item> DRAGON_MEAL = registerItem("dragon_meal", () -> new ItemGeneric());
    public static final DeferredItem<Item> SICKLY_DRAGON_MEAL = registerItem("sickly_dragon_meal", () -> new ItemGeneric(1));
    public static final DeferredItem<Item> CREATIVE_DRAGON_MEAL = registerItem("creative_dragon_meal", () -> new ItemGeneric(2));
    public static final DeferredItem<Item> FIRE_DRAGON_FLESH = registerItem(ItemDragonFlesh.getNameForType(0), () -> new ItemDragonFlesh(0));
    public static final DeferredItem<Item> ICE_DRAGON_FLESH = registerItem(ItemDragonFlesh.getNameForType(1), () -> new ItemDragonFlesh(1));
    public static final DeferredItem<Item> LIGHTNING_DRAGON_FLESH = registerItem(ItemDragonFlesh.getNameForType(2), () -> new ItemDragonFlesh(2));
    public static final DeferredItem<Item> FIRE_DRAGON_HEART = registerItem("fire_dragon_heart", () -> new ItemGeneric());
    public static final DeferredItem<Item> ICE_DRAGON_HEART = registerItem("ice_dragon_heart", () -> new ItemGeneric());
    public static final DeferredItem<Item> LIGHTNING_DRAGON_HEART = registerItem("lightning_dragon_heart", () -> new ItemGeneric());
    public static final DeferredItem<Item> FIRE_DRAGON_BLOOD = registerItem("fire_dragon_blood", () -> new ItemGeneric());
    public static final DeferredItem<Item> ICE_DRAGON_BLOOD = registerItem("ice_dragon_blood", () -> new ItemGeneric());
    public static final DeferredItem<Item> LIGHTNING_DRAGON_BLOOD = registerItem("lightning_dragon_blood", () -> new ItemGeneric());
    public static final DeferredItem<Item> DRAGON_STAFF = registerItem("dragon_stick", () -> new ItemDragonStaff());
    public static final DeferredItem<Item> DRAGON_HORN = registerItem("dragon_horn", () -> new ItemDragonHorn());
    public static final DeferredItem<Item> DRAGON_FLUTE = registerItem("dragon_flute", () -> new ItemDragonFlute());
    public static final DeferredItem<Item> SUMMONING_CRYSTAL_FIRE = registerItem("summoning_crystal_fire", () -> new ItemSummoningCrystal());
    public static final DeferredItem<Item> SUMMONING_CRYSTAL_ICE = registerItem("summoning_crystal_ice", () -> new ItemSummoningCrystal());
    public static final DeferredItem<Item> SUMMONING_CRYSTAL_LIGHTNING = registerItem("summoning_crystal_lightning", () -> new ItemSummoningCrystal());
    public static final DeferredItem<Item> HIPPOGRYPH_EGG = registerItem("hippogryph_egg", () -> new ItemHippogryphEgg());
    public static final DeferredItem<Item> IRON_HIPPOGRYPH_ARMOR = registerItem("iron_hippogryph_armor", () -> new ItemGeneric(0, 1));
    public static final DeferredItem<Item> GOLD_HIPPOGRYPH_ARMOR = registerItem("gold_hippogryph_armor", () -> new ItemGeneric(0, 1));
    public static final DeferredItem<Item> DIAMOND_HIPPOGRYPH_ARMOR = registerItem("diamond_hippogryph_armor", () -> new ItemGeneric(0, 1));
    public static final DeferredItem<Item> HIPPOGRYPH_TALON = registerItem("hippogryph_talon", () -> new ItemGeneric(1));
    public static final DeferredItem<Item> HIPPOGRYPH_SWORD = registerItem("hippogryph_sword", () -> new ItemHippogryphSword());
    public static final DeferredItem<Item> GORGON_HEAD = registerItem("gorgon_head", () -> new ItemGorgonHead());
    public static final DeferredItem<Item> STONE_STATUE = registerItem("stone_statue", () -> new ItemStoneStatue());
    public static final DeferredItem<Item> BLINDFOLD = registerItem("blindfold", () -> new ItemBlindfold());
    public static final DeferredItem<Item> PIXIE_DUST = registerItem("pixie_dust", () -> new ItemPixieDust());
    public static final DeferredItem<Item> PIXIE_WINGS = registerItem("pixie_wings", () -> new ItemGeneric(1));
    public static final DeferredItem<Item> PIXIE_WAND = registerItem("pixie_wand", () -> new ItemPixieWand());
    public static final DeferredItem<Item> AMBROSIA = registerItem("ambrosia", () -> new ItemAmbrosia());
    public static final DeferredItem<Item> CYCLOPS_EYE = registerItem("cyclops_eye", () -> new ItemCyclopsEye());
    public static final DeferredItem<Item> SHEEP_HELMET = registerItem("sheep_helmet", () -> new ItemModArmor(SHEEP_ARMOR_MATERIAL, ArmorType.HELMET));
    public static final DeferredItem<Item> SHEEP_CHESTPLATE = registerItem("sheep_chestplate", () -> new ItemModArmor(SHEEP_ARMOR_MATERIAL, ArmorType.CHESTPLATE));
    public static final DeferredItem<Item> SHEEP_LEGGINGS = registerItem("sheep_leggings", () -> new ItemModArmor(SHEEP_ARMOR_MATERIAL, ArmorType.LEGGINGS));
    public static final DeferredItem<Item> SHEEP_BOOTS = registerItem("sheep_boots", () -> new ItemModArmor(SHEEP_ARMOR_MATERIAL, ArmorType.BOOTS));
    public static final DeferredItem<Item> SHINY_SCALES = registerItem("shiny_scales", () -> new ItemGeneric());
    public static final DeferredItem<Item> SIREN_TEAR = registerItem("siren_tear", () -> new ItemGeneric(1));
    public static final DeferredItem<Item> SIREN_FLUTE = registerItem("siren_flute", () -> new ItemSirenFlute());
    public static final DeferredItem<Item> HIPPOCAMPUS_FIN = registerItem("hippocampus_fin", () -> new ItemGeneric(1));
    public static final DeferredItem<Item> HIPPOCAMPUS_SLAPPER = registerItem("hippocampus_slapper", () -> new ItemHippocampusSlapper());
    public static final DeferredItem<Item> EARPLUGS = registerItem("earplugs", () -> new ItemModArmor(EARPLUGS_ARMOR_MATERIAL, ArmorType.HELMET));
    public static final DeferredItem<Item> DEATH_WORM_CHITIN_YELLOW = registerItem("deathworm_chitin_yellow", () -> new ItemGeneric());
    public static final DeferredItem<Item> DEATH_WORM_CHITIN_WHITE = registerItem("deathworm_chitin_white", () -> new ItemGeneric());
    public static final DeferredItem<Item> DEATH_WORM_CHITIN_RED = registerItem("deathworm_chitin_red", () -> new ItemGeneric());
    public static final DeferredItem<Item> DEATHWORM_YELLOW_HELMET = registerItem("deathworm_yellow_helmet", () -> new ItemDeathwormArmor(DEATHWORM_0_ARMOR_MATERIAL, ArmorType.HELMET));
    public static final DeferredItem<Item> DEATHWORM_YELLOW_CHESTPLATE = registerItem("deathworm_yellow_chestplate", () -> new ItemDeathwormArmor(DEATHWORM_0_ARMOR_MATERIAL, ArmorType.CHESTPLATE));
    public static final DeferredItem<Item> DEATHWORM_YELLOW_LEGGINGS = registerItem("deathworm_yellow_leggings", () -> new ItemDeathwormArmor(DEATHWORM_0_ARMOR_MATERIAL, ArmorType.LEGGINGS));
    public static final DeferredItem<Item> DEATHWORM_YELLOW_BOOTS = registerItem("deathworm_yellow_boots", () -> new ItemDeathwormArmor(DEATHWORM_0_ARMOR_MATERIAL, ArmorType.BOOTS));
    public static final DeferredItem<Item> DEATHWORM_WHITE_HELMET = registerItem("deathworm_white_helmet", () -> new ItemDeathwormArmor(DEATHWORM_1_ARMOR_MATERIAL, ArmorType.HELMET));
    public static final DeferredItem<Item> DEATHWORM_WHITE_CHESTPLATE = registerItem("deathworm_white_chestplate", () -> new ItemDeathwormArmor(DEATHWORM_1_ARMOR_MATERIAL, ArmorType.CHESTPLATE));
    public static final DeferredItem<Item> DEATHWORM_WHITE_LEGGINGS = registerItem("deathworm_white_leggings", () -> new ItemDeathwormArmor(DEATHWORM_1_ARMOR_MATERIAL, ArmorType.LEGGINGS));
    public static final DeferredItem<Item> DEATHWORM_WHITE_BOOTS = registerItem("deathworm_white_boots", () -> new ItemDeathwormArmor(DEATHWORM_1_ARMOR_MATERIAL, ArmorType.BOOTS));
    public static final DeferredItem<Item> DEATHWORM_RED_HELMET = registerItem("deathworm_red_helmet", () -> new ItemDeathwormArmor(DEATHWORM_2_ARMOR_MATERIAL, ArmorType.HELMET));
    public static final DeferredItem<Item> DEATHWORM_RED_CHESTPLATE = registerItem("deathworm_red_chestplate", () -> new ItemDeathwormArmor(DEATHWORM_2_ARMOR_MATERIAL, ArmorType.CHESTPLATE));
    public static final DeferredItem<Item> DEATHWORM_RED_LEGGINGS = registerItem("deathworm_red_leggings", () -> new ItemDeathwormArmor(DEATHWORM_2_ARMOR_MATERIAL, ArmorType.LEGGINGS));
    public static final DeferredItem<Item> DEATHWORM_RED_BOOTS = registerItem("deathworm_red_boots", () -> new ItemDeathwormArmor(DEATHWORM_2_ARMOR_MATERIAL, ArmorType.BOOTS));
    public static final DeferredItem<Item> DEATHWORM_EGG = registerItem("deathworm_egg", () -> new ItemDeathwormEgg(false));
    public static final DeferredItem<Item> DEATHWORM_EGG_GIGANTIC = registerItem("deathworm_egg_giant", () -> new ItemDeathwormEgg(true));
    public static final DeferredItem<Item> DEATHWORM_TOUNGE = registerItem("deathworm_tounge", () -> new ItemGeneric(1));
    public static final DeferredItem<Item> DEATHWORM_GAUNTLET_YELLOW = registerItem("deathworm_gauntlet_yellow", () -> new ItemDeathwormGauntlet());
    public static final DeferredItem<Item> DEATHWORM_GAUNTLET_WHITE = registerItem("deathworm_gauntlet_white", () -> new ItemDeathwormGauntlet());
    public static final DeferredItem<Item> DEATHWORM_GAUNTLET_RED = registerItem("deathworm_gauntlet_red", () -> new ItemDeathwormGauntlet());
    public static final DeferredItem<Item> ROTTEN_EGG = registerItem("rotten_egg", () -> new ItemRottenEgg());
    public static final DeferredItem<Item> COCKATRICE_EYE = registerItem("cockatrice_eye", () -> new ItemGeneric(1));
    public static final DeferredItem<Item> ITEM_COCKATRICE_SCEPTER = registerItem("cockatrice_scepter", () -> new ItemCockatriceScepter());
    public static final DeferredItem<Item> STYMPHALIAN_BIRD_FEATHER = registerItem("stymphalian_bird_feather", () -> new ItemGeneric());
    public static final DeferredItem<Item> STYMPHALIAN_ARROW = registerItem("stymphalian_arrow", () -> new ItemStymphalianArrow());
    public static final DeferredItem<Item> STYMPHALIAN_FEATHER_BUNDLE = registerItem("stymphalian_feather_bundle", () -> new ItemStymphalianFeatherBundle());
    public static final DeferredItem<Item> STYMPHALIAN_DAGGER = registerItem("stymphalian_bird_dagger", () -> new ItemStymphalianDagger());
    public static final DeferredItem<Item> TROLL_TUSK = registerItem("troll_tusk", () -> new ItemGeneric());
    public static final DeferredItem<Item> MYRMEX_DESERT_EGG = registerItem("myrmex_desert_egg", () -> new ItemMyrmexEgg(false));
    public static final DeferredItem<Item> MYRMEX_JUNGLE_EGG = registerItem("myrmex_jungle_egg", () -> new ItemMyrmexEgg(true));
    public static final DeferredItem<Item> MYRMEX_DESERT_RESIN = registerItem("myrmex_desert_resin", () -> new ItemGeneric());
    public static final DeferredItem<Item> MYRMEX_JUNGLE_RESIN = registerItem("myrmex_jungle_resin", () -> new ItemGeneric());
    public static final DeferredItem<Item> MYRMEX_DESERT_CHITIN = registerItem("myrmex_desert_chitin", () -> new ItemGeneric());
    public static final DeferredItem<Item> MYRMEX_JUNGLE_CHITIN = registerItem("myrmex_jungle_chitin", () -> new ItemGeneric());
    public static final DeferredItem<Item> MYRMEX_STINGER = registerItem("myrmex_stinger", () -> new ItemGeneric());
    public static final DeferredItem<Item> MYRMEX_DESERT_SWORD = registerItem("myrmex_desert_sword", () -> new ItemModSword(MYRMEX_CHITIN_TOOL_MATERIAL));
    public static final DeferredItem<Item> MYRMEX_DESERT_SWORD_VENOM = registerItem("myrmex_desert_sword_venom", () -> new ItemModSword(MYRMEX_CHITIN_TOOL_MATERIAL));
    public static final DeferredItem<Item> MYRMEX_DESERT_SHOVEL = registerItem("myrmex_desert_shovel", () -> new ItemModShovel(MYRMEX_CHITIN_TOOL_MATERIAL));
    public static final DeferredItem<Item> MYRMEX_DESERT_PICKAXE = registerItem("myrmex_desert_pickaxe", () -> new ItemModPickaxe(MYRMEX_CHITIN_TOOL_MATERIAL));
    public static final DeferredItem<Item> MYRMEX_DESERT_AXE = registerItem("myrmex_desert_axe", () -> new ItemModAxe(MYRMEX_CHITIN_TOOL_MATERIAL));
    public static final DeferredItem<Item> MYRMEX_DESERT_HOE = registerItem("myrmex_desert_hoe", () -> new ItemModHoe(MYRMEX_CHITIN_TOOL_MATERIAL));
    public static final DeferredItem<Item> MYRMEX_JUNGLE_SWORD = registerItem("myrmex_jungle_sword", () -> new ItemModSword(MYRMEX_CHITIN_TOOL_MATERIAL));
    public static final DeferredItem<Item> MYRMEX_JUNGLE_SWORD_VENOM = registerItem("myrmex_jungle_sword_venom", () -> new ItemModSword(MYRMEX_CHITIN_TOOL_MATERIAL));
    public static final DeferredItem<Item> MYRMEX_JUNGLE_SHOVEL = registerItem("myrmex_jungle_shovel", () -> new ItemModShovel(MYRMEX_CHITIN_TOOL_MATERIAL));
    public static final DeferredItem<Item> MYRMEX_JUNGLE_PICKAXE = registerItem("myrmex_jungle_pickaxe", () -> new ItemModPickaxe(MYRMEX_CHITIN_TOOL_MATERIAL));
    public static final DeferredItem<Item> MYRMEX_JUNGLE_AXE = registerItem("myrmex_jungle_axe", () -> new ItemModAxe(MYRMEX_CHITIN_TOOL_MATERIAL));
    public static final DeferredItem<Item> MYRMEX_JUNGLE_HOE = registerItem("myrmex_jungle_hoe", () -> new ItemModHoe(MYRMEX_CHITIN_TOOL_MATERIAL));
    public static final DeferredItem<Item> MYRMEX_DESERT_STAFF = registerItem("myrmex_desert_staff", () -> new ItemMyrmexStaff(false));
    public static final DeferredItem<Item> MYRMEX_JUNGLE_STAFF = registerItem("myrmex_jungle_staff", () -> new ItemMyrmexStaff(true));
    public static final DeferredItem<Item> MYRMEX_DESERT_HELMET = registerItem("myrmex_desert_helmet", () -> new ItemModArmor(MYRMEX_DESERT_ARMOR_MATERIAL, ArmorType.HELMET));
    public static final DeferredItem<Item> MYRMEX_DESERT_CHESTPLATE = registerItem("myrmex_desert_chestplate", () -> new ItemModArmor(MYRMEX_DESERT_ARMOR_MATERIAL, ArmorType.CHESTPLATE));
    public static final DeferredItem<Item> MYRMEX_DESERT_LEGGINGS = registerItem("myrmex_desert_leggings", () -> new ItemModArmor(MYRMEX_DESERT_ARMOR_MATERIAL, ArmorType.LEGGINGS));
    public static final DeferredItem<Item> MYRMEX_DESERT_BOOTS = registerItem("myrmex_desert_boots", () -> new ItemModArmor(MYRMEX_DESERT_ARMOR_MATERIAL, ArmorType.BOOTS));
    public static final DeferredItem<Item> MYRMEX_JUNGLE_HELMET = registerItem("myrmex_jungle_helmet", () -> new ItemModArmor(MYRMEX_JUNGLE_ARMOR_MATERIAL, ArmorType.HELMET));
    public static final DeferredItem<Item> MYRMEX_JUNGLE_CHESTPLATE = registerItem("myrmex_jungle_chestplate", () -> new ItemModArmor(MYRMEX_JUNGLE_ARMOR_MATERIAL, ArmorType.CHESTPLATE));
    public static final DeferredItem<Item> MYRMEX_JUNGLE_LEGGINGS = registerItem("myrmex_jungle_leggings", () -> new ItemModArmor(MYRMEX_JUNGLE_ARMOR_MATERIAL, ArmorType.LEGGINGS));
    public static final DeferredItem<Item> MYRMEX_JUNGLE_BOOTS = registerItem("myrmex_jungle_boots", () -> new ItemModArmor(MYRMEX_JUNGLE_ARMOR_MATERIAL, ArmorType.BOOTS));
    public static final DeferredItem<Item> MYRMEX_DESERT_SWARM = registerItem("myrmex_desert_swarm", () -> new ItemMyrmexSwarm(false));
    public static final DeferredItem<Item> MYRMEX_JUNGLE_SWARM = registerItem("myrmex_jungle_swarm", () -> new ItemMyrmexSwarm(true));
    public static final DeferredItem<Item> AMPHITHERE_FEATHER = registerItem("amphithere_feather", () -> new ItemGeneric());
    public static final DeferredItem<Item> AMPHITHERE_ARROW = registerItem("amphithere_arrow", () -> new ItemAmphithereArrow());
    public static final DeferredItem<Item> AMPHITHERE_MACUAHUITL = registerItem("amphithere_macuahuitl", () -> new ItemAmphithereMacuahuitl());
    public static final DeferredItem<Item> SERPENT_FANG = registerItem("sea_serpent_fang", () -> new ItemGeneric());
    public static final DeferredItem<Item> SEA_SERPENT_ARROW = registerItem("sea_serpent_arrow", () -> new ItemSeaSerpentArrow());
    public static final DeferredItem<Item> TIDE_TRIDENT_INVENTORY = registerItem("tide_trident_inventory", () -> new ItemGeneric(0, true));
    public static final DeferredItem<Item> TIDE_TRIDENT = registerItem("tide_trident", () -> new ItemTideTrident());
    public static final DeferredItem<Item> CHAIN = registerItem("chain", () -> new ItemChain(false));
    public static final DeferredItem<Item> CHAIN_STICKY = registerItem("chain_sticky", () -> new ItemChain(true));
    public static final DeferredItem<Item> DRAGONSTEEL_FIRE_INGOT = registerItem("dragonsteel_fire_ingot", () -> new ItemGeneric());
    public static final DeferredItem<Item> DRAGONSTEEL_FIRE_SWORD = registerItem("dragonsteel_fire_sword", () -> new ItemModSword(DRAGONSTEEL_TIER_FIRE));
    public static final DeferredItem<Item> DRAGONSTEEL_FIRE_PICKAXE = registerItem("dragonsteel_fire_pickaxe", () -> new ItemModPickaxe(DRAGONSTEEL_TIER_FIRE));
    public static final DeferredItem<Item> DRAGONSTEEL_FIRE_AXE = registerItem("dragonsteel_fire_axe", () -> new ItemModAxe(DRAGONSTEEL_TIER_FIRE));
    public static final DeferredItem<Item> DRAGONSTEEL_FIRE_SHOVEL = registerItem("dragonsteel_fire_shovel", () -> new ItemModShovel(DRAGONSTEEL_TIER_FIRE));
    public static final DeferredItem<Item> DRAGONSTEEL_FIRE_HOE = registerItem("dragonsteel_fire_hoe", () -> new ItemModHoe(DRAGONSTEEL_TIER_FIRE));
    public static final DeferredItem<Item> DRAGONSTEEL_FIRE_HELMET = registerItem("dragonsteel_fire_helmet", () -> new ItemDragonsteelArmor(DRAGONSTEEL_FIRE_ARMOR_MATERIAL, 0, ArmorType.HELMET));
    public static final DeferredItem<Item> DRAGONSTEEL_FIRE_CHESTPLATE = registerItem("dragonsteel_fire_chestplate", () -> new ItemDragonsteelArmor(DRAGONSTEEL_FIRE_ARMOR_MATERIAL, 1, ArmorType.CHESTPLATE));
    public static final DeferredItem<Item> DRAGONSTEEL_FIRE_LEGGINGS = registerItem("dragonsteel_fire_leggings", () -> new ItemDragonsteelArmor(DRAGONSTEEL_FIRE_ARMOR_MATERIAL, 2, ArmorType.LEGGINGS));
    public static final DeferredItem<Item> DRAGONSTEEL_FIRE_BOOTS = registerItem("dragonsteel_fire_boots", () -> new ItemDragonsteelArmor(DRAGONSTEEL_FIRE_ARMOR_MATERIAL, 3, ArmorType.BOOTS));
    public static final DeferredItem<Item> DRAGONSTEEL_ICE_INGOT = registerItem("dragonsteel_ice_ingot", () -> new ItemGeneric());
    public static final DeferredItem<Item> DRAGONSTEEL_ICE_SWORD = registerItem("dragonsteel_ice_sword", () -> new ItemModSword(DRAGONSTEEL_TIER_ICE));
    public static final DeferredItem<Item> DRAGONSTEEL_ICE_PICKAXE = registerItem("dragonsteel_ice_pickaxe", () -> new ItemModPickaxe(DRAGONSTEEL_TIER_ICE));
    public static final DeferredItem<Item> DRAGONSTEEL_ICE_AXE = registerItem("dragonsteel_ice_axe", () -> new ItemModAxe(DRAGONSTEEL_TIER_ICE));
    public static final DeferredItem<Item> DRAGONSTEEL_ICE_SHOVEL = registerItem("dragonsteel_ice_shovel", () -> new ItemModShovel(DRAGONSTEEL_TIER_ICE));
    public static final DeferredItem<Item> DRAGONSTEEL_ICE_HOE = registerItem("dragonsteel_ice_hoe", () -> new ItemModHoe(DRAGONSTEEL_TIER_ICE));
    public static final DeferredItem<Item> DRAGONSTEEL_ICE_HELMET = registerItem("dragonsteel_ice_helmet", () -> new ItemDragonsteelArmor(DRAGONSTEEL_ICE_ARMOR_MATERIAL, 0, ArmorType.HELMET));
    public static final DeferredItem<Item> DRAGONSTEEL_ICE_CHESTPLATE = registerItem("dragonsteel_ice_chestplate", () -> new ItemDragonsteelArmor(DRAGONSTEEL_ICE_ARMOR_MATERIAL, 1, ArmorType.CHESTPLATE));
    public static final DeferredItem<Item> DRAGONSTEEL_ICE_LEGGINGS = registerItem("dragonsteel_ice_leggings", () -> new ItemDragonsteelArmor(DRAGONSTEEL_ICE_ARMOR_MATERIAL, 2, ArmorType.LEGGINGS));
    public static final DeferredItem<Item> DRAGONSTEEL_ICE_BOOTS = registerItem("dragonsteel_ice_boots", () -> new ItemDragonsteelArmor(DRAGONSTEEL_ICE_ARMOR_MATERIAL, 3, ArmorType.BOOTS));

    public static final DeferredItem<Item> DRAGONSTEEL_LIGHTNING_INGOT = registerItem("dragonsteel_lightning_ingot", () -> new ItemGeneric());
    public static final DeferredItem<Item> DRAGONSTEEL_LIGHTNING_SWORD = registerItem("dragonsteel_lightning_sword", () -> new ItemModSword(DRAGONSTEEL_TIER_LIGHTNING));
    public static final DeferredItem<Item> DRAGONSTEEL_LIGHTNING_PICKAXE = registerItem("dragonsteel_lightning_pickaxe", () -> new ItemModPickaxe(DRAGONSTEEL_TIER_LIGHTNING));
    public static final DeferredItem<Item> DRAGONSTEEL_LIGHTNING_AXE = registerItem("dragonsteel_lightning_axe", () -> new ItemModAxe(DRAGONSTEEL_TIER_LIGHTNING));
    public static final DeferredItem<Item> DRAGONSTEEL_LIGHTNING_SHOVEL = registerItem("dragonsteel_lightning_shovel", () -> new ItemModShovel(DRAGONSTEEL_TIER_LIGHTNING));
    public static final DeferredItem<Item> DRAGONSTEEL_LIGHTNING_HOE = registerItem("dragonsteel_lightning_hoe", () -> new ItemModHoe(DRAGONSTEEL_TIER_LIGHTNING));
    public static final DeferredItem<Item> DRAGONSTEEL_LIGHTNING_HELMET = registerItem("dragonsteel_lightning_helmet", () -> new ItemDragonsteelArmor(DRAGONSTEEL_LIGHTNING_ARMOR_MATERIAL, 0, ArmorType.HELMET));
    public static final DeferredItem<Item> DRAGONSTEEL_LIGHTNING_CHESTPLATE = registerItem("dragonsteel_lightning_chestplate", () -> new ItemDragonsteelArmor(DRAGONSTEEL_LIGHTNING_ARMOR_MATERIAL, 1, ArmorType.CHESTPLATE));
    public static final DeferredItem<Item> DRAGONSTEEL_LIGHTNING_LEGGINGS = registerItem("dragonsteel_lightning_leggings", () -> new ItemDragonsteelArmor(DRAGONSTEEL_LIGHTNING_ARMOR_MATERIAL, 2, ArmorType.LEGGINGS));
    public static final DeferredItem<Item> DRAGONSTEEL_LIGHTNING_BOOTS = registerItem("dragonsteel_lightning_boots", () -> new ItemDragonsteelArmor(DRAGONSTEEL_LIGHTNING_ARMOR_MATERIAL, 3, ArmorType.BOOTS));


    public static final DeferredItem<Item> WEEZER_BLUE_ALBUM = registerItem("weezer_blue_album", () -> new ItemGeneric(1, true));
    public static final DeferredItem<Item> DRAGON_DEBUG_STICK = registerItem("dragon_debug_stick", () -> new ItemGeneric(1, true), false);
    public static final DeferredItem<Item> DREAD_SWORD = registerItem("dread_sword", () -> new ItemModSword(DREAD_SWORD_TOOL_MATERIAL));
    public static final DeferredItem<Item> DREAD_KNIGHT_SWORD = registerItem("dread_knight_sword", () -> new ItemModSword(DREAD_KNIGHT_TOOL_MATERIAL));
    public static final DeferredItem<Item> LICH_STAFF = registerItem("lich_staff", () -> new ItemLichStaff());
    public static final DeferredItem<Item> DREAD_QUEEN_SWORD = registerItem("dread_queen_sword", () -> new ItemModSword(DRAGONSTEEL_TIER_DREAD_QUEEN));
    public static final DeferredItem<Item> DREAD_QUEEN_STAFF = registerItem("dread_queen_staff", () -> new ItemDreadQueenStaff());
    public static final DeferredItem<Item> DREAD_SHARD = registerItem("dread_shard", () -> new ItemGeneric(0));
    public static final DeferredItem<Item> DREAD_KEY = registerItem("dread_key", () -> new ItemGeneric(0));
    public static final DeferredItem<Item> HYDRA_FANG = registerItem("hydra_fang", () -> new ItemGeneric(0));
    public static final DeferredItem<Item> HYDRA_HEART = registerItem("hydra_heart", () -> new ItemHydraHeart());
    public static final DeferredItem<Item> HYDRA_ARROW = registerItem("hydra_arrow", () -> new ItemHydraArrow());
    public static final DeferredItem<Item> CANNOLI = registerItem("cannoli", () -> new ItemCannoli(), false);
    public static final DeferredItem<Item> ECTOPLASM = registerItem("ectoplasm", () -> new ItemGeneric());
    public static final DeferredItem<Item> GHOST_INGOT = registerItem("ghost_ingot", () -> new ItemGeneric(1));
    public static final DeferredItem<Item> GHOST_SWORD = registerItem("ghost_sword", () -> new ItemGhostSword());

    public static final DeferredItem<ItemBannerPattern> PATTERN_FIRE = registerItem("banner_pattern_fire", () -> new ItemBannerPattern(BannerPatternTagGenerator.FIRE_BANNER_PATTERN, unstackable()));
    public static final DeferredItem<ItemBannerPattern> PATTERN_ICE = registerItem("banner_pattern_ice", () -> new ItemBannerPattern(BannerPatternTagGenerator.ICE_BANNER_PATTERN, unstackable()));
    public static final DeferredItem<ItemBannerPattern> PATTERN_LIGHTNING = registerItem("banner_pattern_lightning", () -> new ItemBannerPattern(BannerPatternTagGenerator.LIGHTNING_BANNER_PATTERN, unstackable()));
    public static final DeferredItem<ItemBannerPattern> PATTERN_FIRE_HEAD = registerItem("banner_pattern_fire_head", () -> new ItemBannerPattern(BannerPatternTagGenerator.FIRE_HEAD_BANNER_PATTERN, unstackable()));
    public static final DeferredItem<ItemBannerPattern> PATTERN_ICE_HEAD = registerItem("banner_pattern_ice_head", () -> new ItemBannerPattern(BannerPatternTagGenerator.ICE_HEAD_BANNER_PATTERN , unstackable()));
    public static final DeferredItem<ItemBannerPattern> PATTERN_LIGHTNING_HEAD = registerItem("banner_pattern_lightning_head", () -> new ItemBannerPattern(BannerPatternTagGenerator.LIGHTNING_HEAD_BANNER_PATTERN, unstackable()));
    public static final DeferredItem<ItemBannerPattern> PATTERN_AMPHITHERE = registerItem("banner_pattern_amphithere", () -> new ItemBannerPattern(BannerPatternTagGenerator.AMPHITHERE_BANNER_PATTERN, unstackable()));
    public static final DeferredItem<ItemBannerPattern> PATTERN_BIRD = registerItem("banner_pattern_bird", () -> new ItemBannerPattern(BannerPatternTagGenerator.BIRD_BANNER_PATTERN, unstackable()));
    public static final DeferredItem<ItemBannerPattern> PATTERN_EYE = registerItem("banner_pattern_eye", () -> new ItemBannerPattern(BannerPatternTagGenerator.EYE_BANNER_PATTERN, unstackable()));
    public static final DeferredItem<ItemBannerPattern> PATTERN_FAE = registerItem("banner_pattern_fae", () -> new ItemBannerPattern(BannerPatternTagGenerator.FAE_BANNER_PATTERN, unstackable()));
    public static final DeferredItem<ItemBannerPattern> PATTERN_FEATHER = registerItem("banner_pattern_feather", () -> new ItemBannerPattern(BannerPatternTagGenerator.FEATHER_BANNER_PATTERN, unstackable()));
    public static final DeferredItem<ItemBannerPattern> PATTERN_GORGON = registerItem("banner_pattern_gorgon", () -> new ItemBannerPattern(BannerPatternTagGenerator.GORGON_BANNER_PATTERN, unstackable()));
    public static final DeferredItem<ItemBannerPattern> PATTERN_HIPPOCAMPUS = registerItem("banner_pattern_hippocampus", () -> new ItemBannerPattern(BannerPatternTagGenerator.HIPPOCAMPUS_BANNER_PATTERN, unstackable()));
    public static final DeferredItem<ItemBannerPattern> PATTERN_HIPPOGRYPH_HEAD = registerItem("banner_pattern_hippogryph_head", () -> new ItemBannerPattern(BannerPatternTagGenerator.HIPPOGRYPH_HEAD_BANNER_PATTERN, unstackable()));
    public static final DeferredItem<ItemBannerPattern> PATTERN_MERMAID = registerItem("banner_pattern_mermaid", () -> new ItemBannerPattern(BannerPatternTagGenerator.MERMAID_BANNER_PATTERN, unstackable()));
    public static final DeferredItem<ItemBannerPattern> PATTERN_SEA_SERPENT = registerItem("banner_pattern_sea_serpent", () -> new ItemBannerPattern(BannerPatternTagGenerator.SEA_SERPENT_BANNER_PATTERN, unstackable()));
    public static final DeferredItem<ItemBannerPattern> PATTERN_TROLL = registerItem("banner_pattern_troll", () -> new ItemBannerPattern(BannerPatternTagGenerator.TROLL_BANNER_PATTERN, unstackable()));
    public static final DeferredItem<ItemBannerPattern> PATTERN_WEEZER = registerItem("banner_pattern_weezer", () -> new ItemBannerPattern(BannerPatternTagGenerator.WEEZER_BANNER_PATTERN, unstackable()));
    public static final DeferredItem<ItemBannerPattern> PATTERN_DREAD = registerItem("banner_pattern_dread", () -> new ItemBannerPattern(BannerPatternTagGenerator.DREAD_BANNER_PATTERN, unstackable()));

    static {
        EnumDragonArmor.initArmors();
        EnumSeaSerpent.initArmors();
        EnumSkullType.initItems();
        EnumTroll.initArmors();
    }

    private static DeferredItem<Item> registerEgg(String name, Supplier<? extends net.minecraft.world.entity.EntityType<?>> entityType) {
        return registerItem(name, properties -> new SpawnEggItem(properties.spawnEgg(entityType.get())));
    }

    public static Item.Properties defaultBuilder() {
        return IafItemRegistry.itemProperties()/*.tab(IceAndFire.TAB_ITEMS)*/;
    }

    public static Item.Properties unstackable() {
        return defaultBuilder().stacksTo(1);
    }

    // Vanilla 26.2 spawn eggs use the entity_data item component. Their former Forge colors
    // belong in the item model tint sources; the model resources are maintained in assets.
    public static final DeferredItem<Item> SPAWN_EGG_FIRE_DRAGON = registerEgg("spawn_egg_fire_dragon", IafEntityRegistry.FIRE_DRAGON);
    public static final DeferredItem<Item> SPAWN_EGG_ICE_DRAGON = registerEgg("spawn_egg_ice_dragon", IafEntityRegistry.ICE_DRAGON);
    public static final DeferredItem<Item> SPAWN_EGG_LIGHTNING_DRAGON = registerEgg("spawn_egg_lightning_dragon", IafEntityRegistry.LIGHTNING_DRAGON);
    public static final DeferredItem<Item> SPAWN_EGG_HIPPOGRYPH = registerEgg("spawn_egg_hippogryph", IafEntityRegistry.HIPPOGRYPH);
    public static final DeferredItem<Item> SPAWN_EGG_GORGON = registerEgg("spawn_egg_gorgon", IafEntityRegistry.GORGON);
    public static final DeferredItem<Item> SPAWN_EGG_PIXIE = registerEgg("spawn_egg_pixie", IafEntityRegistry.PIXIE);
    public static final DeferredItem<Item> SPAWN_EGG_CYCLOPS = registerEgg("spawn_egg_cyclops", IafEntityRegistry.CYCLOPS);
    public static final DeferredItem<Item> SPAWN_EGG_SIREN = registerEgg("spawn_egg_siren", IafEntityRegistry.SIREN);
    public static final DeferredItem<Item> SPAWN_EGG_HIPPOCAMPUS = registerEgg("spawn_egg_hippocampus", IafEntityRegistry.HIPPOCAMPUS);
    public static final DeferredItem<Item> SPAWN_EGG_DEATH_WORM = registerEgg("spawn_egg_death_worm", IafEntityRegistry.DEATH_WORM);
    public static final DeferredItem<Item> SPAWN_EGG_COCKATRICE = registerEgg("spawn_egg_cockatrice", IafEntityRegistry.COCKATRICE);
    public static final DeferredItem<Item> SPAWN_EGG_STYMPHALIAN_BIRD = registerEgg("spawn_egg_stymphalian_bird", IafEntityRegistry.STYMPHALIAN_BIRD);
    public static final DeferredItem<Item> SPAWN_EGG_TROLL = registerEgg("spawn_egg_troll", IafEntityRegistry.TROLL);
    public static final DeferredItem<Item> SPAWN_EGG_MYRMEX_WORKER = registerEgg("spawn_egg_myrmex_worker", IafEntityRegistry.MYRMEX_WORKER);
    public static final DeferredItem<Item> SPAWN_EGG_MYRMEX_SOLDIER = registerEgg("spawn_egg_myrmex_soldier", IafEntityRegistry.MYRMEX_SOLDIER);
    public static final DeferredItem<Item> SPAWN_EGG_MYRMEX_SENTINEL = registerEgg("spawn_egg_myrmex_sentinel", IafEntityRegistry.MYRMEX_SENTINEL);
    public static final DeferredItem<Item> SPAWN_EGG_MYRMEX_ROYAL = registerEgg("spawn_egg_myrmex_royal", IafEntityRegistry.MYRMEX_ROYAL);
    public static final DeferredItem<Item> SPAWN_EGG_MYRMEX_QUEEN = registerEgg("spawn_egg_myrmex_queen", IafEntityRegistry.MYRMEX_QUEEN);
    public static final DeferredItem<Item> SPAWN_EGG_AMPHITHERE = registerEgg("spawn_egg_amphithere", IafEntityRegistry.AMPHITHERE);
    public static final DeferredItem<Item> SPAWN_EGG_SEA_SERPENT = registerEgg("spawn_egg_sea_serpent", IafEntityRegistry.SEA_SERPENT);
    public static final DeferredItem<Item> SPAWN_EGG_DREAD_THRALL = registerEgg("spawn_egg_dread_thrall", IafEntityRegistry.DREAD_THRALL);
    public static final DeferredItem<Item> SPAWN_EGG_DREAD_GHOUL = registerEgg("spawn_egg_dread_ghoul", IafEntityRegistry.DREAD_GHOUL);
    public static final DeferredItem<Item> SPAWN_EGG_DREAD_BEAST = registerEgg("spawn_egg_dread_beast", IafEntityRegistry.DREAD_BEAST);
    public static final DeferredItem<Item> SPAWN_EGG_DREAD_SCUTTLER = registerEgg("spawn_egg_dread_scuttler", IafEntityRegistry.DREAD_SCUTTLER);
    public static final DeferredItem<Item> SPAWN_EGG_LICH = registerEgg("spawn_egg_lich", IafEntityRegistry.DREAD_LICH);
    public static final DeferredItem<Item> SPAWN_EGG_DREAD_KNIGHT = registerEgg("spawn_egg_dread_knight", IafEntityRegistry.DREAD_KNIGHT);
    public static final DeferredItem<Item> SPAWN_EGG_DREAD_HORSE = registerEgg("spawn_egg_dread_horse", IafEntityRegistry.DREAD_HORSE);
    public static final DeferredItem<Item> SPAWN_EGG_HYDRA = registerEgg("spawn_egg_hydra", IafEntityRegistry.HYDRA);
    public static final DeferredItem<Item> SPAWN_EGG_GHOST = registerEgg("spawn_egg_ghost", IafEntityRegistry.GHOST);
    public static <I extends Item> DeferredItem<I> registerItem(String name, Supplier<I> item) {
        return registerItem(name, item, true);
    }

    /**
     * Preferred registration entry point for Minecraft 26.2. The registry injects the item's resource key into
     * {@code properties} before the item constructor runs, as required by the data component initializer system.
     */
    public static <I extends Item> DeferredItem<I> registerItem(String name, Function<Item.Properties, ? extends I> factory) {
        return registerItem(name, factory, true);
    }

    public static <I extends Item> DeferredItem<I> registerItem(String name, Function<Item.Properties, ? extends I> factory, boolean putInTab) {
        DeferredItem<I> itemRegistryObject = ITEMS.registerItem(name, factory);
        if (putInTab) {
            IafTabRegistry.TAB_ITEMS_LIST.add(itemRegistryObject);
        }
        return itemRegistryObject;
    }

    /**
     * Compatibility overload for legacy item constructors that create their own Item.Properties. The properties
     * supplied by the registry are exposed only while that item is being constructed, so they retain the required id.
     * New registrations should use the Function overload and pass the properties directly.
     */
    public static <I extends Item> DeferredItem<I> registerItem(String name, Supplier<I> factory, boolean putInTab) {
        return registerItem(name, properties -> {
            Item.Properties previous = ACTIVE_ITEM_PROPERTIES.get();
            ACTIVE_ITEM_PROPERTIES.set(properties);
            try {
                return factory.get();
            } finally {
                if (previous == null) {
                    ACTIVE_ITEM_PROPERTIES.remove();
                } else {
                    ACTIVE_ITEM_PROPERTIES.set(previous);
                }
            }
        }, putInTab);
    }

    public static Item.Properties itemProperties() {
        Item.Properties properties = ACTIVE_ITEM_PROPERTIES.get();
        if (properties == null) {
            throw new IllegalStateException("Item.Properties may only be requested during IafItemRegistry registration");
        }
        return properties;
    }


    /**
     Set repair materials etc.
     Due to the priority it should run after {@link DeferredRegister#addEntries( RegisterEvent)}
     (and therefor not cause issues when accessing the suppliers)
    */
    public static void setRepairMaterials(final RegisterEvent event) {
        if (event.getRegistryKey() != Registries.ITEM) {
            return;
        }

        IafItemRegistry.BLINDFOLD_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(Tags.Items.STRING));
        IafItemRegistry.SILVER_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(IafItemTags.INGOTS_SILVER));
        IafItemRegistry.SILVER_TOOL_MATERIAL.setRepairMaterial(Ingredient.of(IafItemTags.INGOTS_SILVER));
        IafItemRegistry.DRAGONBONE_TOOL_MATERIAL.setRepairMaterial(Ingredient.of(IafItemRegistry.DRAGON_BONE.get()));
        IafItemRegistry.FIRE_DRAGONBONE_TOOL_MATERIAL.setRepairMaterial(Ingredient.of(IafItemRegistry.DRAGON_BONE.get()));
        IafItemRegistry.ICE_DRAGONBONE_TOOL_MATERIAL.setRepairMaterial(Ingredient.of(IafItemRegistry.DRAGON_BONE.get()));
        IafItemRegistry.LIGHTNING_DRAGONBONE_TOOL_MATERIAL.setRepairMaterial(Ingredient.of(IafItemRegistry.DRAGON_BONE.get()));
        for (EnumDragonArmor armor : EnumDragonArmor.values()) {
            armor.armorMaterial.setRepairMaterial(Ingredient.of(EnumDragonArmor.getScaleItem(armor)));
        }
        IafItemRegistry.DRAGONSTEEL_FIRE_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(IafItemRegistry.DRAGONSTEEL_FIRE_INGOT.get()));
        IafItemRegistry.DRAGONSTEEL_ICE_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(IafItemRegistry.DRAGONSTEEL_ICE_INGOT.get()));
        IafItemRegistry.DRAGONSTEEL_LIGHTNING_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(IafItemRegistry.DRAGONSTEEL_LIGHTNING_INGOT.get()));
        DRAGONSTEEL_TIER_FIRE.setRepairMaterial(Ingredient.of(IafItemRegistry.DRAGONSTEEL_FIRE_INGOT.get()));
        DRAGONSTEEL_TIER_ICE.setRepairMaterial(Ingredient.of(IafItemRegistry.DRAGONSTEEL_ICE_INGOT.get()));
        DRAGONSTEEL_TIER_LIGHTNING.setRepairMaterial(Ingredient.of(IafItemRegistry.DRAGONSTEEL_LIGHTNING_INGOT.get()));
        IafItemRegistry.SHEEP_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(Items.WHITE_WOOL));
        IafItemRegistry.EARPLUGS_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(Blocks.OAK_BUTTON));
        IafItemRegistry.DEATHWORM_0_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(IafItemRegistry.DEATH_WORM_CHITIN_YELLOW.get()));
        IafItemRegistry.DEATHWORM_1_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(IafItemRegistry.DEATH_WORM_CHITIN_RED.get()));
        IafItemRegistry.DEATHWORM_2_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(IafItemRegistry.DEATH_WORM_CHITIN_WHITE.get()));
        IafItemRegistry.TROLL_WEAPON_TOOL_MATERIAL.setRepairMaterial(Ingredient.of(Tags.Items.STONE));
        IafItemRegistry.TROLL_MOUNTAIN_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(EnumTroll.MOUNTAIN.leather.get()));
        IafItemRegistry.TROLL_FOREST_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(EnumTroll.FOREST.leather.get()));
        IafItemRegistry.TROLL_FROST_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(EnumTroll.FROST.leather.get()));
        IafItemRegistry.HIPPOGRYPH_SWORD_TOOL_MATERIAL.setRepairMaterial(Ingredient.of(IafItemRegistry.HIPPOGRYPH_TALON.get()));
        IafItemRegistry.HIPPOCAMPUS_SWORD_TOOL_MATERIAL.setRepairMaterial(Ingredient.of(IafItemRegistry.SHINY_SCALES.get()));
        IafItemRegistry.AMPHITHERE_SWORD_TOOL_MATERIAL.setRepairMaterial(Ingredient.of(IafItemRegistry.AMPHITHERE_FEATHER.get()));
        IafItemRegistry.STYMHALIAN_SWORD_TOOL_MATERIAL.setRepairMaterial(Ingredient.of(IafItemRegistry.STYMPHALIAN_BIRD_FEATHER.get()));
        IafItemRegistry.MYRMEX_CHITIN_TOOL_MATERIAL.setRepairMaterial(Ingredient.of(IafItemRegistry.MYRMEX_DESERT_CHITIN.get()));
        IafItemRegistry.MYRMEX_DESERT_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(IafItemRegistry.MYRMEX_DESERT_CHITIN.get()));
        IafItemRegistry.MYRMEX_JUNGLE_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(IafItemRegistry.MYRMEX_JUNGLE_CHITIN.get()));
        IafItemRegistry.DREAD_SWORD_TOOL_MATERIAL.setRepairMaterial(Ingredient.of(IafItemRegistry.DREAD_SHARD.get()));
        IafItemRegistry.DREAD_KNIGHT_TOOL_MATERIAL.setRepairMaterial(Ingredient.of(IafItemRegistry.DREAD_SHARD.get()));
        for (EnumSeaSerpent serpent : EnumSeaSerpent.values()) {
            serpent.armorMaterial.setRepairMaterial(Ingredient.of(serpent.scale.get()));
        }
    }
}
