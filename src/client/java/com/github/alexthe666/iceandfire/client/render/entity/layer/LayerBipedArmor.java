package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.iceandfire.client.model.BipedRenderState;
import com.github.alexthe666.iceandfire.client.model.ModelBipedBase;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;
import org.jetbrains.annotations.NotNull;

/**
 * Armor for the dread mobs. Draws the layer models with mob-specific textures instead of vanilla equipment assets.
 * Base code from minecraft's HumanoidArmorLayer.
 */
public class LayerBipedArmor<M extends ModelBipedBase<BipedRenderState>, A extends ModelBipedBase<BipedRenderState>> extends RenderLayer<BipedRenderState, M> {

    private final A modelLeggings;
    private final A modelArmor;
    private final Identifier defaultLegArmor;
    private final Identifier defaultArmor;

    public LayerBipedArmor(RenderLayerParent<BipedRenderState, M> renderer, A modelLeggings, A modelArmor, Identifier defaultArmor, Identifier defaultLegArmor) {
        super(renderer);
        this.modelLeggings = modelLeggings;
        this.modelArmor = modelArmor;
        this.defaultLegArmor = defaultLegArmor;
        this.defaultArmor = defaultArmor;
    }

    @Override
    public void submit(@NotNull PoseStack poseStack, @NotNull SubmitNodeCollector collector, int light, @NotNull BipedRenderState state, float yRot, float xRot) {
        renderEquipment(poseStack, collector, light, state, state.chestEquipment, EquipmentSlot.CHEST);
        renderEquipment(poseStack, collector, light, state, state.legsEquipment, EquipmentSlot.LEGS);
        renderEquipment(poseStack, collector, light, state, state.feetEquipment, EquipmentSlot.FEET);
        renderEquipment(poseStack, collector, light, state, state.headEquipment, EquipmentSlot.HEAD);
    }

    private void renderEquipment(PoseStack poseStack, SubmitNodeCollector collector, int light, BipedRenderState state, ItemStack stack, EquipmentSlot slot) {
        if (stack == null || stack.isEmpty()) {
            return;
        }
        Equippable equippable = stack.get(DataComponents.EQUIPPABLE);
        if (equippable == null || equippable.slot() != slot) {
            return;
        }
        A model = this.getSlotModel(slot);
        this.setModelSlotVisible(model, slot);
        collector.submitModel(model, state, poseStack, RenderTypes.armorCutoutNoCull(this.getArmorResource(state, stack, slot)), light,
            LivingEntityRenderer.getOverlayCoords(state, 0.0F), -1, null, state.outlineColor, null);
    }

    protected void setModelSlotVisible(A modelIn, EquipmentSlot slotIn) {
        modelIn.setVisible(false);
        switch (slotIn) {
            case HEAD:
                modelIn.head.showSelf = true;
                modelIn.headware.showSelf = true;
                break;
            case CHEST:
                modelIn.body.showSelf = true;
                modelIn.armRight.showSelf = true;
                modelIn.armLeft.showSelf = true;
                break;
            case LEGS:
                modelIn.body.showSelf = true;
                modelIn.legRight.showSelf = true;
                modelIn.legLeft.showSelf = true;
                break;
            case FEET:
                modelIn.legRight.showSelf = true;
                modelIn.legLeft.showSelf = true;
                break;
            default:
                break;
        }
    }

    private A getSlotModel(EquipmentSlot equipmentSlotType) {
        return this.isLegSlot(equipmentSlotType) ? this.modelLeggings : this.modelArmor;
    }

    protected boolean isLegSlot(EquipmentSlot slotIn) {
        return slotIn == EquipmentSlot.LEGS;
    }

    public Identifier getArmorResource(BipedRenderState state, ItemStack stack, EquipmentSlot slot) {
        if (isLegSlot(slot))
            return defaultLegArmor;
        return defaultArmor;
    }

}
