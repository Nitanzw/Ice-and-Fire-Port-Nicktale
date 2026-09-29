package com.github.alexthe666.iceandfire.client.render.pathfinding;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.MNode;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Set;

public class PathfindingDebugRenderer {
    /** Set of visited nodes. */
    public static Set<MNode> lastDebugNodesVisited = new HashSet<>();
    /** Set of not visited nodes. */
    public static Set<MNode> lastDebugNodesNotVisited = new HashSet<>();
    /** Set of nodes that belong to the chosen path. */
    public static Set<MNode> lastDebugNodesPath = new HashSet<>();

    /** Submit pathfinding debug geometry to the deferred 26.2 renderer. */
    public static void render(@NotNull SubmitNodeCollector collector, @NotNull PoseStack poseStack, @NotNull Vec3 cameraPos) {
        poseStack.pushPose();
        poseStack.translate(-cameraPos.x(), -cameraPos.y(), -cameraPos.z());
        BlockPos cameraBlockPos = BlockPos.containing(cameraPos.x(), cameraPos.y(), cameraPos.z());
        try {
            for (MNode node : lastDebugNodesVisited) {
                debugDrawNode(node, 0xffff0000, collector, poseStack, cameraBlockPos);
            }

            for (MNode node : lastDebugNodesNotVisited) {
                debugDrawNode(node, 0xff0000ff, collector, poseStack, cameraBlockPos);
            }

            for (MNode node : lastDebugNodesPath) {
                debugDrawNode(node, node.isReachedByWorker() ? 0xffff6600 : 0xff00ff00, collector, poseStack, cameraBlockPos);
            }
        } catch (ConcurrentModificationException exception) {
            IceAndFire.LOGGER.error("Pathfinding debug error", exception);
        } finally {
            poseStack.popPose();
        }
    }

    private static void debugDrawNode(MNode node, int argbColor, SubmitNodeCollector collector, PoseStack poseStack, BlockPos cameraBlockPos) {
        poseStack.pushPose();
        poseStack.translate(node.pos.getX() + 0.375D, node.pos.getY() + 0.375D, node.pos.getZ() + 0.375D);
        if (node.pos.closerThan(cameraBlockPos, 5D)) {
            renderDebugText(node, collector, poseStack);
        }

        collector.submitCustomGeometry(poseStack, RenderTypes.debugQuads(), (pose, buffer) -> submitCube(buffer, pose, argbColor));

        if (node.parent != null) {
            float dx = node.parent.pos.getX() - node.pos.getX();
            float dy = node.parent.pos.getY() - node.pos.getY();
            float dz = node.parent.pos.getZ() - node.pos.getZ();
            collector.submitCustomGeometry(poseStack, RenderTypes.lines(), (pose, buffer) -> {
                buffer.addVertex(pose, 0.125F, 0.125F, 0.125F).setColor(0xFFBFBFBF);
                buffer.addVertex(pose, dx + 0.125F, dy + 0.125F, dz + 0.125F).setColor(0xFFBFBFBF);
            });
        }

        poseStack.popPose();
    }

    private static void submitCube(VertexConsumer buffer, PoseStack.Pose pose, int color) {
        // A small filled node marker, matching the legacy 0.25 block cube.
        quad(buffer, pose, color, 0, 0, 0, 0, 0.25F, 0, 0.25F, 0.25F, 0, 0.25F, 0, 0);
        quad(buffer, pose, color, 0, 0, 0.25F, 0.25F, 0, 0.25F, 0.25F, 0.25F, 0.25F, 0, 0, 0.25F);
        quad(buffer, pose, color, 0, 0, 0, 0.25F, 0, 0, 0.25F, 0, 0.25F, 0, 0, 0.25F);
        quad(buffer, pose, color, 0, 0.25F, 0, 0, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0.25F, 0);
        quad(buffer, pose, color, 0, 0, 0, 0, 0, 0.25F, 0, 0.25F, 0.25F, 0, 0.25F, 0);
        quad(buffer, pose, color, 0.25F, 0, 0, 0.25F, 0.25F, 0, 0.25F, 0.25F, 0.25F, 0.25F, 0, 0.25F);
    }

    private static void quad(VertexConsumer buffer, PoseStack.Pose pose, int color,
                             float x0, float y0, float z0, float x1, float y1, float z1,
                             float x2, float y2, float z2, float x3, float y3, float z3) {
        buffer.addVertex(pose, x0, y0, z0).setColor(color);
        buffer.addVertex(pose, x1, y1, z1).setColor(color);
        buffer.addVertex(pose, x2, y2, z2).setColor(color);
        buffer.addVertex(pose, x3, y3, z3).setColor(color);
    }

    private static void renderDebugText(@NotNull MNode node, SubmitNodeCollector collector, PoseStack poseStack) {
        Font font = Minecraft.getInstance().font;
        String f = String.format("F: %.3f [%d]", node.getCost(), node.getCounterAdded());
        String g = String.format("G: %.3f [%d]", node.getScore(), node.getCounterVisited());
        int halfWidth = Math.max(font.width(f), font.width(g)) / 2;

        poseStack.pushPose();
        poseStack.translate(0.125F, 0.75F, 0.125F);
        poseStack.mulPose(Minecraft.getInstance().gameRenderer.getMainCamera().rotation());
        poseStack.scale(-0.014F, -0.014F, 0.014F);
        poseStack.translate(0.0F, 18.0F, 0.0F);
        poseStack.translate(0.0F, -5.0F, 0.0F);
        collector.submitText(poseStack, -font.width(f) / 2.0F, 1, Component.literal(f).getVisualOrderText(),
                false, Font.DisplayMode.NORMAL, 15728880, 0xffffffff, 0, 0);
        poseStack.translate(0.0F, 8.0F, 0.0F);
        collector.submitText(poseStack, -font.width(g) / 2.0F, 1, Component.literal(g).getVisualOrderText(),
                false, Font.DisplayMode.NORMAL, 15728880, 0xffffffff, 0, 0);
        poseStack.popPose();
    }
}
