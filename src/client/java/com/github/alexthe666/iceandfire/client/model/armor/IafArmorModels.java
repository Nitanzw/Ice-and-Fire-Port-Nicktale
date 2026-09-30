package com.github.alexthe666.iceandfire.client.model.armor;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.item.ItemModArmor;
import net.minecraft.client.model.Model;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.ArmorType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Connects the mod's custom armor geometry (crests, horns, spines...) to its armor items. Their textures are laid out
 * for these models, so the vanilla humanoid armor model scrambles them.
 */
@EventBusSubscriber(value = Dist.CLIENT, modid = IceAndFire.MODID)
public final class IafArmorModels {
    private static final Map<String, Model> CACHE = new HashMap<>();

    private IafArmorModels() {
    }

    /** Model factory (argument: inner layer, i.e. leggings) for an armor material asset name, or null for vanilla. */
    private static Function<Boolean, ArmorModelBase> factoryFor(String asset) {
        if (asset.equals("silver")) {
            return ModelSilverArmor::new;
        }
        if (asset.equals("copper")) {
            return ModelCopperArmor::new;
        }
        if (asset.equals("dragonsteel_fire")) {
            return ModelDragonsteelFireArmor::new;
        }
        if (asset.equals("dragonsteel_ice")) {
            return ModelDragonsteelIceArmor::new;
        }
        if (asset.equals("dragonsteel_lightning")) {
            return ModelDragonsteelLightningArmor::new;
        }
        if (asset.endsWith("_troll")) {
            return ModelTrollArmor::new;
        }
        if (asset.startsWith("iceandfire_sea_serpent_scales_")) {
            return ModelSeaSerpentArmor::new;
        }
        if (asset.endsWith("_deathworm") || asset.endsWith("_seathworm")) {
            return inner -> new ModelDeathWormArmor(ModelDeathWormArmor.getBakedModel(inner));
        }
        if (asset.startsWith("iceandfire_armor_dragon_scales")) {
            try {
                int index = Integer.parseInt(asset.substring("iceandfire_armor_dragon_scales".length())) - 1;
                if (index < 4) {
                    return ModelFireDragonScaleArmor::new;
                } else if (index < 8) {
                    return ModelIceDragonScaleArmor::new;
                }
                return ModelLightningDragonScaleArmor::new;
            } catch (NumberFormatException ignored) {
                return null;
            }
        }
        return null;
    }

    /** Same part split as the vanilla per-slot armor models. */
    private static void showOnly(ArmorModelBase model, ArmorType type) {
        model.head.visible = type == ArmorType.HELMET;
        model.hat.visible = type == ArmorType.HELMET;
        model.body.visible = type == ArmorType.CHESTPLATE || type == ArmorType.LEGGINGS;
        model.rightArm.visible = type == ArmorType.CHESTPLATE;
        model.leftArm.visible = type == ArmorType.CHESTPLATE;
        model.rightLeg.visible = type == ArmorType.LEGGINGS || type == ArmorType.BOOTS;
        model.leftLeg.visible = type == ArmorType.LEGGINGS || type == ArmorType.BOOTS;
    }

    @SubscribeEvent
    public static void register(RegisterClientExtensionsEvent event) {
        for (Item item : BuiltInRegistries.ITEM) {
            if (!(item instanceof ItemModArmor armor)) {
                continue;
            }
            String asset = armor.getArmorMaterial().assetName();
            Function<Boolean, ArmorModelBase> factory = factoryFor(asset);
            if (factory == null) {
                continue;
            }
            event.registerItem(new IClientItemExtensions() {
                @Override
                public Model getHumanoidArmorModel(ItemStack stack, EquipmentClientInfo.LayerType layerType, Model original) {
                    boolean inner = layerType == EquipmentClientInfo.LayerType.HUMANOID_LEGGINGS;
                    // One instance per slot: rendering is deferred, so a shared model would show whichever visibility was set last.
                    return CACHE.computeIfAbsent(asset + inner + armor.getArmorType(), key -> {
                        ArmorModelBase model = factory.apply(inner);
                        showOnly(model, armor.getArmorType());
                        return model;
                    });
                }
            }, item);
        }
    }
}
