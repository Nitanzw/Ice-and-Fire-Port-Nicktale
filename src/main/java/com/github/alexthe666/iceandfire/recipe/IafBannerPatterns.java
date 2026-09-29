package com.github.alexthe666.iceandfire.recipe;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class IafBannerPatterns {
    public static final DeferredRegister<BannerPattern> BANNERS = DeferredRegister.create(Registries.BANNER_PATTERN, IceAndFire.MODID);
    public static final DeferredHolder<BannerPattern, BannerPattern> PATTERN_FIRE = register("fire");
    public static final DeferredHolder<BannerPattern, BannerPattern> PATTERN_ICE = register("ice");
    public static final DeferredHolder<BannerPattern, BannerPattern> PATTERN_LIGHTNING = register("lightning");
    public static final DeferredHolder<BannerPattern, BannerPattern> PATTERN_FIRE_HEAD = register("fire_head");
    public static final DeferredHolder<BannerPattern, BannerPattern> PATTERN_ICE_HEAD = register("ice_head");
    public static final DeferredHolder<BannerPattern, BannerPattern> PATTERN_LIGHTNING_HEAD = register("lightning_head");
    public static final DeferredHolder<BannerPattern, BannerPattern> PATTERN_AMPHITHERE = register("amphithere");
    public static final DeferredHolder<BannerPattern, BannerPattern> PATTERN_BIRD = register("bird");
    public static final DeferredHolder<BannerPattern, BannerPattern> PATTERN_EYE = register("eye");
    public static final DeferredHolder<BannerPattern, BannerPattern> PATTERN_FAE = register("fae");
    public static final DeferredHolder<BannerPattern, BannerPattern> PATTERN_FEATHER = register("feather");
    public static final DeferredHolder<BannerPattern, BannerPattern> PATTERN_GORGON = register("gorgon");
    public static final DeferredHolder<BannerPattern, BannerPattern> PATTERN_HIPPOCAMPUS = register("hippocampus");
    public static final DeferredHolder<BannerPattern, BannerPattern> PATTERN_HIPPOGRYPH_HEAD = register("hippogryph_head");
    public static final DeferredHolder<BannerPattern, BannerPattern> PATTERN_MERMAID = register("mermaid");
    public static final DeferredHolder<BannerPattern, BannerPattern> PATTERN_SEA_SERPENT = register("sea_serpent");
    public static final DeferredHolder<BannerPattern, BannerPattern> PATTERN_TROLL = register("troll");
    public static final DeferredHolder<BannerPattern, BannerPattern> PATTERN_WEEZER = register("weezer");
    public static final DeferredHolder<BannerPattern, BannerPattern> PATTERN_DREAD = register("dread");

    private IafBannerPatterns() {}

    private static DeferredHolder<BannerPattern, BannerPattern> register(String name) {
        Identifier id = Identifier.fromNamespaceAndPath(IceAndFire.MODID, name);
        return BANNERS.register(name, () -> new BannerPattern(id, "pattern." + IceAndFire.MODID + "." + name));
    }
}
