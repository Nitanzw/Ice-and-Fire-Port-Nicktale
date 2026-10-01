package com.nicktale.api.client.model.armor;

import net.minecraft.client.model.Model;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.ArmorType;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Connects custom armor geometry to armor items. Vanilla builds one model per armor slot with only that slot's parts
 * visible; this helper does the same for custom {@link ArmorModelBase} models, so a helmet does not draw the whole set.
 */
public final class ArmorModels {
    private static final Map<String, Model> CACHE = new HashMap<>();

    private ArmorModels() {
    }

    /**
     * Registers a custom armor model for an item. Call from a {@link RegisterClientExtensionsEvent} handler.
     *
     * @param modelKey unique key of the model family (used for caching, e.g. the armor material name)
     * @param factory  builds the model; the argument is true for the leggings (inner) layer
     */
    public static void register(RegisterClientExtensionsEvent event, Item item, ArmorType type, String modelKey,
                                Function<Boolean, ? extends ArmorModelBase> factory) {
        event.registerItem(new IClientItemExtensions() {
            @Override
            public Model getHumanoidArmorModel(ItemStack stack, EquipmentClientInfo.LayerType layerType, Model original) {
                boolean inner = layerType == EquipmentClientInfo.LayerType.HUMANOID_LEGGINGS;
                // One instance per slot: rendering is deferred, so a shared model would show whichever visibility was set last.
                return CACHE.computeIfAbsent(modelKey + inner + type, key -> {
                    ArmorModelBase model = factory.apply(inner);
                    showOnly(model, type);
                    return model;
                });
            }
        }, item);
    }

    /** Same part split as the vanilla per-slot armor models. */
    public static void showOnly(ArmorModelBase model, ArmorType type) {
        model.head.visible = type == ArmorType.HELMET;
        model.hat.visible = type == ArmorType.HELMET;
        model.body.visible = type == ArmorType.CHESTPLATE || type == ArmorType.LEGGINGS;
        model.rightArm.visible = type == ArmorType.CHESTPLATE;
        model.leftArm.visible = type == ArmorType.CHESTPLATE;
        model.rightLeg.visible = type == ArmorType.LEGGINGS || type == ArmorType.BOOTS;
        model.leftLeg.visible = type == ArmorType.LEGGINGS || type == ArmorType.BOOTS;
    }
}
