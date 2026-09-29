package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Supplier;

/**
 * Block properties must carry the block's registry key before the block is constructed (26.2). Block classes build
 * their own properties, so the key of the block currently being registered is tracked here and applied by {@link #of()}.
 */
public final class IafBlockProps {
    private static final ThreadLocal<ResourceKey<Block>> CURRENT_ID = new ThreadLocal<>();

    private IafBlockProps() {
    }

    public static BlockBehaviour.Properties of() {
        return withCurrentId(BlockBehaviour.Properties.of());
    }

    public static BlockBehaviour.Properties ofFullCopy(BlockBehaviour source) {
        return withCurrentId(BlockBehaviour.Properties.ofFullCopy(source));
    }

    private static BlockBehaviour.Properties withCurrentId(BlockBehaviour.Properties properties) {
        ResourceKey<Block> id = CURRENT_ID.get();
        return id == null ? properties : properties.setId(id);
    }

    /** Runs the block factory with the registry key of {@code name} applied to every properties object it creates. */
    public static <T extends Block> T construct(String name, Supplier<T> factory) {
        ResourceKey<Block> previous = CURRENT_ID.get();
        CURRENT_ID.set(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(IceAndFire.MODID, name)));
        try {
            return factory.get();
        } finally {
            if (previous == null) {
                CURRENT_ID.remove();
            } else {
                CURRENT_ID.set(previous);
            }
        }
    }
}
