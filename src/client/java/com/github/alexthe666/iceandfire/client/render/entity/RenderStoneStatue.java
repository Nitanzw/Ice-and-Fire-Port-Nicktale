package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.model.ModelStonePlayer;
import com.github.alexthe666.iceandfire.client.model.SimpleEntityRenderState;
import com.github.alexthe666.iceandfire.client.render.IafRenderType;
import com.github.alexthe666.iceandfire.entity.EntityStoneStatue;
import com.github.alexthe666.iceandfire.entity.EntityTroll;
import com.github.alexthe666.iceandfire.entity.util.EntityDataIO;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Draws a petrified copy of another entity by reusing that entity's own renderer model and render state,
 * with a stone texture instead of the original one.
 */
public class RenderStoneStatue extends EntityRenderer<EntityStoneStatue, RenderStoneStatue.StatueRenderState> {

    protected static final Identifier[] DESTROY_STAGES = new Identifier[]{Identifier.parse("textures/block/destroy_stage_0.png"), Identifier.parse("textures/block/destroy_stage_1.png"), Identifier.parse("textures/block/destroy_stage_2.png"), Identifier.parse("textures/block/destroy_stage_3.png"), Identifier.parse("textures/block/destroy_stage_4.png"), Identifier.parse("textures/block/destroy_stage_5.png"), Identifier.parse("textures/block/destroy_stage_6.png"), Identifier.parse("textures/block/destroy_stage_7.png"), Identifier.parse("textures/block/destroy_stage_8.png"), Identifier.parse("textures/block/destroy_stage_9.png")};
    private final Map<String, Entity> hollowEntityMap = new HashMap<>();
    private final EntityRendererProvider.Context context;
    private ModelStonePlayer playerModel;

    public RenderStoneStatue(EntityRendererProvider.Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public @NotNull StatueRenderState createRenderState() {
        return new StatueRenderState();
    }

    private Entity getFakeEntity(EntityStoneStatue statue) {
        String key = statue.getTrappedEntityTypeString();
        Entity cached = this.hollowEntityMap.get(key);
        if (cached != null) {
            return cached;
        }
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            return null;
        }
        EntityType<?> type = statue.getTrappedEntityType();
        Entity build = type.create(minecraft.level, EntitySpawnReason.LOAD);
        if (build != null) {
            build.setId(-(this.hollowEntityMap.size() + 2));
            try {
                build.load(EntityDataIO.input(minecraft.level.registryAccess(), statue.getTrappedTag()));
            } catch (Exception e) {
                IceAndFire.LOGGER.warn("Mob " + key + " could not build statue NBT");
            }
            this.hollowEntityMap.put(key, build);
        }
        return build;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void extractRenderState(@NotNull EntityStoneStatue entity, @NotNull StatueRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.entity = entity;
        state.partialTick = partialTick;
        state.yRot = entity.yRotO + (entity.getYRot() - entity.yRotO) * partialTick;
        state.statueScale = entity.getScale() < 0.01F ? 1F : entity.getScale();
        state.crackAmount = entity.getCrackAmount();
        state.model = null;
        state.modelState = null;
        state.texture = null;
        Entity fake = getFakeEntity(entity);
        if (fake != null) {
            EntityRenderer renderer = this.context.getEntityRenderDispatcher().getRenderer(fake);
            if (renderer instanceof LivingEntityRenderer livingRenderer) {
                state.model = livingRenderer.getModel();
                state.modelState = renderer.createRenderState(fake, partialTick);
            }
            if (fake instanceof EntityTroll troll) {
                state.texture = troll.getTrollType().TEXTURE_STONE;
            }
        } else if (entity.getTrappedEntityType() == EntityTypes.PLAYER) {
            if (this.playerModel == null) {
                this.playerModel = new ModelStonePlayer(this.context.bakeLayer(ModelLayers.PLAYER));
            }
            state.model = this.playerModel;
            state.modelState = new HumanoidRenderState();
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void submit(@NotNull StatueRenderState state, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector collector, @NotNull CameraRenderState camera) {
        if (state.model == null || state.modelState == null) {
            return;
        }
        Model model = state.model;
        RenderType tex = state.texture != null ? RenderTypes.entityCutout(state.texture) : IafRenderType.getStoneMobRenderType(200, 200);
        poseStack.pushPose();
        poseStack.scale(state.statueScale, state.statueScale, state.statueScale);
        poseStack.translate(0, 1.5F, 0);
        poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees(state.yRot));
        collector.submitModel(model, state.modelState, poseStack, tex, state.lightCoords, OverlayTexture.NO_OVERLAY, -1, null, state.outlineColor, null);
        if (state.crackAmount >= 1) {
            int i = Mth.clamp(state.crackAmount - 1, 0, DESTROY_STAGES.length - 1);
            RenderType crackTex = IafRenderType.getStoneCrackRenderType(DESTROY_STAGES[i]);
            collector.submitModel(model, state.modelState, poseStack, crackTex, state.lightCoords, OverlayTexture.NO_OVERLAY, -1, null, state.outlineColor, null);
        }
        poseStack.popPose();
    }

    public static class StatueRenderState extends SimpleEntityRenderState {
        public EntityModel<?> model;
        public EntityRenderState modelState;
        public float statueScale = 1F;
        public int crackAmount;
        public Identifier texture;
    }
}
