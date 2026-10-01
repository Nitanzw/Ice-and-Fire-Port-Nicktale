package com.nicktale.api.client.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3fc;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Lets a mod draw an item with custom code that sees the {@link ItemStack} (replacing the pre-1.21.5
 * BlockEntityWithoutLevelRenderer). Create an {@link Unbaked} from a drawer factory, register its {@link Unbaked#type()}
 * with {@code RegisterSpecialModelRendererEvent} and reference the id from an item definition as a
 * {@code minecraft:special} model.
 */
public final class StackSpecialModel {
    private StackSpecialModel() {
    }

    /** Draws an item from its stack. */
    @FunctionalInterface
    public interface StackDrawer {
        void submit(ItemStack stack, PoseStack poseStack, SubmitNodeCollector collector, int light, int overlay, boolean foil, int outline);
    }

    private static final class Renderer implements SpecialModelRenderer<ItemStack> {
        private final StackDrawer drawer;

        private Renderer(StackDrawer drawer) {
            this.drawer = drawer;
        }

        @Override
        public void submit(@Nullable ItemStack stack, PoseStack poseStack, SubmitNodeCollector collector, int light, int overlay, boolean foil, int outline) {
            this.drawer.submit(stack == null ? ItemStack.EMPTY : stack, poseStack, collector, light, overlay, foil, outline);
        }

        @Override
        public void getExtents(Consumer<Vector3fc> output) {
            PoseStack poseStack = new PoseStack();
            org.joml.Vector3f v = new org.joml.Vector3f();
            for (float x : new float[]{0F, 1F}) {
                for (float y : new float[]{0F, 1F}) {
                    for (float z : new float[]{0F, 1F}) {
                        output.accept(poseStack.last().pose().transformPosition(x, y, z, v));
                    }
                }
            }
        }

        @Override
        public ItemStack extractArgument(ItemStack stack) {
            return stack;
        }
    }

    public static final class Unbaked implements SpecialModelRenderer.Unbaked<ItemStack> {
        private final Supplier<StackDrawer> drawerFactory;
        private final MapCodec<Unbaked> codec;

        /** @param drawerFactory called once when the model is baked; may build and keep models and render types */
        public Unbaked(Supplier<StackDrawer> drawerFactory) {
            this.drawerFactory = drawerFactory;
            this.codec = MapCodec.unit(this);
        }

        @Override
        public SpecialModelRenderer<ItemStack> bake(SpecialModelRenderer.BakingContext context) {
            return new Renderer(this.drawerFactory.get());
        }

        @Override
        public MapCodec<? extends SpecialModelRenderer.Unbaked<ItemStack>> type() {
            return this.codec;
        }
    }
}
