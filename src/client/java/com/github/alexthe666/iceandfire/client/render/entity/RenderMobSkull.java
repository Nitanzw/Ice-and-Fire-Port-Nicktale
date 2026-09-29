package com.github.alexthe666.iceandfire.client.render.entity;

import com.nicktale.api.client.model.AdvancedEntityModel;
import com.nicktale.api.client.model.AdvancedModelBox;
import com.nicktale.api.client.model.TabulaModel;
import com.github.alexthe666.iceandfire.client.model.*;
import com.github.alexthe666.iceandfire.entity.EntityMobSkull;
import com.github.alexthe666.iceandfire.enums.EnumSkullType;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import com.github.alexthe666.iceandfire.client.model.SimpleEntityRenderState;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Map;

public class RenderMobSkull extends EntityRenderer<EntityMobSkull, RenderMobSkull.SkullRenderState> {

    private static final Map<String, Identifier> SKULL_TEXTURE_CACHE = Maps.newHashMap();
    private final ModelHippogryph hippogryphModel;
    private final ModelCyclops cyclopsModel;
    private final ModelCockatrice cockatriceModel;
    private final ModelStymphalianBird stymphalianBirdModel;
    private final ModelTroll trollModel;
    private final ModelAmphithere amphithereModel;
    private final ModelHydraHead hydraModel;
    private final TabulaModel seaSerpentModel;

    public RenderMobSkull(EntityRendererProvider.Context context, AdvancedEntityModel seaSerpentModel) {
        super(context);
        this.hippogryphModel = new ModelHippogryph();
        this.cyclopsModel = new ModelCyclops();
        this.cockatriceModel = new ModelCockatrice();
        this.stymphalianBirdModel = new ModelStymphalianBird();
        this.trollModel = new ModelTroll();
        this.amphithereModel = new ModelAmphithere();
        this.seaSerpentModel = (TabulaModel) seaSerpentModel;
        this.hydraModel = new ModelHydraHead(0);
    }

    private static void setRotationAngles(AdvancedModelBox cube, float rotX, float rotY, float rotZ) {
        cube.rotateAngleX = rotX;
        cube.rotateAngleY = rotY;
        cube.rotateAngleZ = rotZ;
    }

    @Override
    public @NotNull SkullRenderState createRenderState() {
        return new SkullRenderState();
    }

    @Override
    public void extractRenderState(@NotNull EntityMobSkull entity, @NotNull SkullRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.entity = entity;
        state.skullType = entity.getSkullType();
        state.onWall = entity.isOnWall();
        state.skullYaw = entity.getYaw();
    }

    @Override
    public void submit(@NotNull SkullRenderState state, @NotNull PoseStack matrixStackIn, @NotNull SubmitNodeCollector collector, @NotNull CameraRenderState camera) {
        super.submit(state, matrixStackIn, collector, camera);
        matrixStackIn.pushPose();
        matrixStackIn.mulPose(Axis.XP.rotationDegrees(-180.0F));
        matrixStackIn.mulPose(Axis.YN.rotationDegrees(180.0F - state.skullYaw));
        float size = 1.0F;
        matrixStackIn.scale(size, size, size);
        matrixStackIn.translate(0, state.onWall ? -0.24F : -0.12F, 0.5F);
        renderForEnum(state.skullType, state.onWall, matrixStackIn, collector, state.lightCoords);
        matrixStackIn.popPose();
    }

    private static void submitPart(SubmitNodeCollector collector, PoseStack poseStack, AdvancedEntityModel<?> model, AdvancedModelBox box, RenderType renderType, int light) {
        ModelPart part = model.getPart(box);
        if (part != null) {
            collector.submitModelPart(part, poseStack, renderType, light, OverlayTexture.NO_OVERLAY, null);
        }
    }

    private void renderForEnum(EnumSkullType skull, boolean onWall, PoseStack matrixStackIn, SubmitNodeCollector collector, int packedLightIn) {
        RenderType renderType = RenderTypes.entityTranslucent(getSkullTexture(skull));
        switch (skull) {
            case HIPPOGRYPH:
                matrixStackIn.translate(0, -0.0F, -0.2F);
                matrixStackIn.scale(1.2F, 1.2F, 1.2F);
                hippogryphModel.resetToDefaultPose();
                setRotationAngles(hippogryphModel.Head, onWall ? (float) Math.toRadians(50F) : (float) Math.toRadians(-5), 0, 0);
                submitPart(collector, matrixStackIn, hippogryphModel, hippogryphModel.Head, renderType, packedLightIn);
                break;
            case CYCLOPS:
                matrixStackIn.translate(0, 1.8F, -0.5F);
                matrixStackIn.scale(2.25F, 2.25F, 2.25F);
                cyclopsModel.resetToDefaultPose();
                setRotationAngles(cyclopsModel.Head, onWall ? (float) Math.toRadians(50F) : 0F, 0, 0);
                submitPart(collector, matrixStackIn, cyclopsModel, cyclopsModel.Head, renderType, packedLightIn);

                break;
            case COCKATRICE:
                if (onWall) {
                    matrixStackIn.translate(0, 0F, 0.35F);
                }
                cockatriceModel.resetToDefaultPose();
                setRotationAngles(cockatriceModel.head, onWall ? (float) Math.toRadians(50F) : 0F, 0, 0);
                submitPart(collector, matrixStackIn, cockatriceModel, cockatriceModel.head, renderType, packedLightIn);

                break;
            case STYMPHALIAN:
                if (!onWall) {
                    matrixStackIn.translate(0, 0F, -0.35F);
                }
                stymphalianBirdModel.resetToDefaultPose();
                setRotationAngles(stymphalianBirdModel.HeadBase, onWall ? (float) Math.toRadians(50F) : 0F, 0, 0);
                submitPart(collector, matrixStackIn, stymphalianBirdModel, stymphalianBirdModel.HeadBase, renderType, packedLightIn);

                break;
            case TROLL:
                matrixStackIn.translate(0, 1F, -0.35F);
                if (onWall) {
                    matrixStackIn.translate(0, 0F, 0.35F);
                }
                trollModel.resetToDefaultPose();
                setRotationAngles(trollModel.head, onWall ? (float) Math.toRadians(50F) : (float) Math.toRadians(-20), 0, 0);
                submitPart(collector, matrixStackIn, trollModel, trollModel.head, renderType, packedLightIn);

                break;
            case AMPHITHERE:
                matrixStackIn.translate(0, -0.2F, 0.7F);
                matrixStackIn.scale(2.0F, 2.0F, 2.0F);
                amphithereModel.resetToDefaultPose();
                setRotationAngles(amphithereModel.Head, onWall ? (float) Math.toRadians(50F) : 0F, 0, 0);
                submitPart(collector, matrixStackIn, amphithereModel, amphithereModel.Head, renderType, packedLightIn);

                break;
            case SEASERPENT:
                matrixStackIn.translate(0, -0.35F, 0.8F);
                matrixStackIn.scale(2.5F, 2.5F, 2.5F);
                seaSerpentModel.resetToDefaultPose();
                setRotationAngles(seaSerpentModel.getCube("Head"), onWall ? (float) Math.toRadians(50F) : 0F, 0, 0);
                submitPart(collector, matrixStackIn, seaSerpentModel, seaSerpentModel.getCube("Head"), renderType, packedLightIn);

                break;
            case HYDRA:
                matrixStackIn.translate(0, -0.2F, -0.1F);
                matrixStackIn.scale(2.0F, 2.0F, 2.0F);
                hydraModel.resetToDefaultPose();
                setRotationAngles(hydraModel.Head1, onWall ? (float) Math.toRadians(50F) : 0F, 0, 0);
                submitPart(collector, matrixStackIn, hydraModel, hydraModel.Head1, renderType, packedLightIn);

                break;
        }
    }

    public Identifier getSkullTexture(EnumSkullType skull) {
        String s = "iceandfire:textures/models/skulls/skull_" + skull.name().toLowerCase(Locale.ROOT) + ".png";
        Identifier resourcelocation = SKULL_TEXTURE_CACHE.get(s);
        if (resourcelocation == null) {
            resourcelocation = Identifier.parse(s);
            SKULL_TEXTURE_CACHE.put(s, resourcelocation);
        }
        return resourcelocation;
    }

    public static class SkullRenderState extends SimpleEntityRenderState {
        public EnumSkullType skullType = EnumSkullType.HIPPOGRYPH;
        public boolean onWall;
        public float skullYaw;
    }

}
