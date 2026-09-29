package com.github.alexthe666.iceandfire.client;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.item.ItemDragonHorn;
import com.github.alexthe666.iceandfire.item.ItemStackData;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterConditionalItemModelPropertyEvent;
import net.neoforged.neoforge.client.event.RegisterSelectItemModelPropertyEvent;

/** Item model properties replacing the removed 1.20 {@code ItemProperties.register} calls. */
@EventBusSubscriber(value = Dist.CLIENT, modid = IceAndFire.MODID)
public final class IafItemProperties {
    private IafItemProperties() {
    }

    /** True when a summoning crystal has a dragon bound to it. */
    public record HasDragon() implements ConditionalItemModelProperty {
        public static final MapCodec<HasDragon> MAP_CODEC = MapCodec.unit(new HasDragon());

        @Override
        public boolean get(ItemStack stack, ClientLevel level, LivingEntity entity, int seed, ItemDisplayContext context) {
            if (!ItemStackData.has(stack)) {
                return false;
            }
            for (String key : ItemStackData.get(stack).keySet()) {
                if (key.contains("Dragon")) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public MapCodec<HasDragon> type() {
            return MAP_CODEC;
        }
    }

    /** 0 for an empty horn, 1 fire, 2 ice, 3 lightning. */
    public record HornType() implements SelectItemModelProperty<Integer> {
        public static final MapCodec<HornType> MAP_CODEC = MapCodec.unit(new HornType());
        public static final SelectItemModelProperty.Type<HornType, Integer> TYPE = SelectItemModelProperty.Type.create(MAP_CODEC, Codec.INT);

        @Override
        public Integer get(ItemStack stack, ClientLevel level, LivingEntity entity, int seed, ItemDisplayContext context) {
            return ItemDragonHorn.getDragonType(stack);
        }

        @Override
        public Codec<Integer> valueCodec() {
            return Codec.INT;
        }

        @Override
        public SelectItemModelProperty.Type<HornType, Integer> type() {
            return TYPE;
        }
    }

    @SubscribeEvent
    public static void registerConditional(RegisterConditionalItemModelPropertyEvent event) {
        event.register(Identifier.fromNamespaceAndPath(IceAndFire.MODID, "has_dragon"), HasDragon.MAP_CODEC);
    }

    @SubscribeEvent
    public static void registerSelect(RegisterSelectItemModelPropertyEvent event) {
        event.register(Identifier.fromNamespaceAndPath(IceAndFire.MODID, "horn_type"), HornType.TYPE);
    }
}
