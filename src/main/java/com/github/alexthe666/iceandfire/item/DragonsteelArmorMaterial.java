package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IafConfig;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.equipment.ArmorType;

public class DragonsteelArmorMaterial extends IafArmorMaterial {

    public DragonsteelArmorMaterial(String name, int durability, int[] damageReduction, int encantability, Holder<SoundEvent> sound, float toughness) {
        super(name, durability, damageReduction, encantability, sound, toughness);
    }

    @Override
    public int getDefenseForType(ArmorType slotIn) {
        return switch (slotIn) {
            case BOOTS -> IafConfig.dragonsteelBaseArmor - 6;
            case LEGGINGS -> IafConfig.dragonsteelBaseArmor - 3;
            case CHESTPLATE -> IafConfig.dragonsteelBaseArmor;
            case HELMET -> IafConfig.dragonsteelBaseArmor - 5;
            case BODY -> IafConfig.dragonsteelBaseArmor;
        };
    }

    @Override
    public float getToughness() {
        return IafConfig.dragonsteelBaseArmorToughness;
    }

    @Override
    public int getDurabilityForType(ArmorType slotIn) {
        return (int) (slotIn.getDurability(1) * 0.02D * IafConfig.dragonsteelBaseDurabilityEquipment);
    }
}
