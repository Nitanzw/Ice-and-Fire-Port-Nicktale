package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.iceandfire.client.model.BipedRenderState;
import com.github.alexthe666.iceandfire.client.model.ModelBipedBase;
import com.github.alexthe666.iceandfire.entity.util.IHasArmorVariant;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public class LayerBipedArmorMultiple<R extends RenderLayerParent<BipedRenderState, M> & IHasArmorVariantResource,
    M extends ModelBipedBase<BipedRenderState>,
    A extends ModelBipedBase<BipedRenderState>> extends LayerBipedArmor<M, A> {

    private final R mobRenderer;

    public LayerBipedArmorMultiple(R mobRenderer, A modelLeggings, A modelArmor,
                                   Identifier defaultArmor, Identifier defaultLegArmor) {
        super(mobRenderer, modelLeggings, modelArmor, defaultArmor, defaultLegArmor);
        this.mobRenderer = mobRenderer;
    }

    @Override
    public Identifier getArmorResource(BipedRenderState state, ItemStack stack, EquipmentSlot slot) {
        int variant = state.entity instanceof IHasArmorVariant hasVariant ? hasVariant.getBodyArmorVariant() : 0;
        return this.mobRenderer.getArmorResource(variant, slot);
    }
}
