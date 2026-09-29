package com.github.alexthe666.iceandfire.client.render.entity;

import net.minecraft.util.ARGB;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import com.github.alexthe666.iceandfire.client.render.entity.layer.IafRenderLayer;
import com.github.alexthe666.iceandfire.client.model.HippocampusRenderState;
import com.github.alexthe666.iceandfire.client.model.ModelHippocampus;
import com.github.alexthe666.iceandfire.entity.EntityHippocampus;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;



public class RenderHippocampus extends IafMobRenderer<EntityHippocampus, HippocampusRenderState, ModelHippocampus> {

    private static final Identifier VARIANT_0 = Identifier.parse("iceandfire:textures/models/hippocampus/hippocampus_0.png");
    private static final Identifier VARIANT_0_BLINK = Identifier.parse("iceandfire:textures/models/hippocampus/hippocampus_0_blinking.png");
    private static final Identifier VARIANT_1 = Identifier.parse("iceandfire:textures/models/hippocampus/hippocampus_1.png");
    private static final Identifier VARIANT_1_BLINK = Identifier.parse("iceandfire:textures/models/hippocampus/hippocampus_1_blinking.png");
    private static final Identifier VARIANT_2 = Identifier.parse("iceandfire:textures/models/hippocampus/hippocampus_2.png");
    private static final Identifier VARIANT_2_BLINK = Identifier.parse("iceandfire:textures/models/hippocampus/hippocampus_2_blinking.png");
    private static final Identifier VARIANT_3 = Identifier.parse("iceandfire:textures/models/hippocampus/hippocampus_3.png");
    private static final Identifier VARIANT_3_BLINK = Identifier.parse("iceandfire:textures/models/hippocampus/hippocampus_3_blinking.png");
    private static final Identifier VARIANT_4 = Identifier.parse("iceandfire:textures/models/hippocampus/hippocampus_4.png");
    private static final Identifier VARIANT_4_BLINK = Identifier.parse("iceandfire:textures/models/hippocampus/hippocampus_4_blinking.png");
    private static final Identifier VARIANT_5 = Identifier.parse("iceandfire:textures/models/hippocampus/hippocampus_5.png");
    private static final Identifier VARIANT_5_BLINK = Identifier.parse("iceandfire:textures/models/hippocampus/hippocampus_5_blinking.png");


    public RenderHippocampus(EntityRendererProvider.Context context) {
        super(context, new ModelHippocampus(), 0.8F);
        this.addLayer(new LayerHippocampusRainbow(this));
        this.addLayer(new LayerHippocampusSaddle(this));
    }

    @Override
    public HippocampusRenderState createRenderState() {
        return new HippocampusRenderState();
    }

    @Override
    protected void extract(EntityHippocampus entity, HippocampusRenderState state, float partialTick) {
        state.onGround = entity.onGround();
        state.onLandProgress = entity.onLandProgress;
        state.sitProgress = entity.sitProgress;
        state.tail_buffer = entity.tail_buffer;
    }

    @Override
    protected Identifier textureFor(EntityHippocampus entity) {
        switch (entity.getVariant()) {
            default:
                return entity.isBlinking() ? VARIANT_0_BLINK : VARIANT_0;
            case 1:
                return entity.isBlinking() ? VARIANT_1_BLINK : VARIANT_1;
            case 2:
                return entity.isBlinking() ? VARIANT_2_BLINK : VARIANT_2;
            case 3:
                return entity.isBlinking() ? VARIANT_3_BLINK : VARIANT_3;
            case 4:
                return entity.isBlinking() ? VARIANT_4_BLINK : VARIANT_4;
            case 5:
                return entity.isBlinking() ? VARIANT_5_BLINK : VARIANT_5;

        }
    }


    private static class LayerHippocampusSaddle extends IafRenderLayer<HippocampusRenderState, ModelHippocampus> {
        private final RenderType SADDLE_TEXTURE = RenderTypes.entityCutout(Identifier.parse("iceandfire:textures/models/hippocampus/saddle.png"), false);
        private final RenderType BRIDLE = RenderTypes.entityCutout(Identifier.parse("iceandfire:textures/models/hippocampus/bridle.png"), false);
        private final RenderType CHEST = RenderTypes.entityTranslucent(Identifier.parse("iceandfire:textures/models/hippocampus/chest.png"));
        private final RenderType TEXTURE_DIAMOND = RenderTypes.entityCutout(Identifier.parse("iceandfire:textures/models/hippocampus/armor_diamond.png"));
        private final RenderType TEXTURE_GOLD = RenderTypes.entityCutout(Identifier.parse("iceandfire:textures/models/hippocampus/armor_gold.png"));
        private final RenderType TEXTURE_IRON = RenderTypes.entityCutout(Identifier.parse("iceandfire:textures/models/hippocampus/armor_iron.png"));

        public LayerHippocampusSaddle(RenderHippocampus renderer) {
            super(renderer);
        }

        @Override
        public void submit(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, HippocampusRenderState state, float yRot, float xRot) {
            EntityHippocampus hippo = entityOf(state);
            if (hippo.isSaddled()) {
                submitParentModel(collector, poseStack, lightCoords, state, SADDLE_TEXTURE);
            }
            if (hippo.isSaddled() && hippo.getControllingPassenger() != null) {
                submitParentModel(collector, poseStack, lightCoords, state, BRIDLE);
            }
            if (hippo.isChested()) {
                submitParentModel(collector, poseStack, lightCoords, state, CHEST);
            }
            RenderType type = switch (hippo.getArmor()) {
                case 1 -> TEXTURE_IRON;
                case 2 -> TEXTURE_GOLD;
                case 3 -> TEXTURE_DIAMOND;
                default -> null;
            };
            if (type != null) {
                submitParentModel(collector, poseStack, lightCoords, state, type);
            }
        }
    }

    private static class LayerHippocampusRainbow extends IafRenderLayer<HippocampusRenderState, ModelHippocampus> {
        private final RenderType TEXTURE = RenderTypes.entityCutout(Identifier.parse("iceandfire:textures/models/hippocampus/rainbow.png"), false);
        private final RenderType TEXTURE_BLINK = RenderTypes.entityCutout(Identifier.parse("iceandfire:textures/models/hippocampus/rainbow_blink.png"), false);

        public LayerHippocampusRainbow(RenderHippocampus renderer) {
            super(renderer);
        }

        @Override
        public void submit(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, HippocampusRenderState state, float yRot, float xRot) {
            EntityHippocampus hippo = entityOf(state);
            if (hippo.hasCustomName() && hippo.getCustomName().getString().toLowerCase().contains("rainbow")) {
                int i = hippo.tickCount / 25 + hippo.getId();
                int j = DyeColor.values().length;
                int k = i % j;
                int l = (i + 1) % j;
                float f = ((float) (hippo.tickCount % 25) + state.partialTick) / 25.0F;
                int c1 = DyeColor.byId(k).getTextureDiffuseColor();
                int c2 = DyeColor.byId(l).getTextureDiffuseColor();
                int color = ARGB.opaque(ARGB.srgbLerp(f, c1, c2));
                submitParentModel(collector, poseStack, lightCoords, state, hippo.isBlinking() ? TEXTURE_BLINK : TEXTURE, color);
            }
        }
    }
}
