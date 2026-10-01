package com.github.alexthe666.iceandfire.client.model.armor;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.item.ItemModArmor;
import com.nicktale.api.client.model.armor.ArmorModelBase;
import com.nicktale.api.client.model.armor.ArmorModels;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

import java.util.function.Function;

/**
 * Maps the mod's armor items to their custom armor geometry (crests, horns, spines...). Their textures are laid out
 * for these models, so the vanilla humanoid armor model scrambles them. The registration mechanics live in the
 * Nicktale API ({@code ArmorModels}).
 */
@EventBusSubscriber(value = Dist.CLIENT, modid = IceAndFire.MODID)
public final class IafArmorModels {
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
            ArmorModels.register(event, item, armor.getArmorType(), asset, factory);
        }
    }
}
