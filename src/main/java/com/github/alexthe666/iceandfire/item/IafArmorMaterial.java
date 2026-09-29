package com.github.alexthe666.iceandfire.item;

import com.nicktale.api.server.item.CustomArmorMaterial;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.equipment.ArmorType;

public class IafArmorMaterial extends CustomArmorMaterial {

    protected static final int[] MAX_DAMAGE_ARRAY = new int[]{13, 15, 16, 11};
    private final int maxDamageFactor;

    public IafArmorMaterial(String name, int durability, int[] damageReduction, int encantability, Holder<SoundEvent> sound, float toughness) {
        super(name, durability, damageReduction, encantability, sound, toughness, 0);
        this.maxDamageFactor = durability;
    }

    @Override
    public int getDurabilityForType(ArmorType slotIn) {
        return slotIn.getDurability(this.maxDamageFactor);
    }
}
