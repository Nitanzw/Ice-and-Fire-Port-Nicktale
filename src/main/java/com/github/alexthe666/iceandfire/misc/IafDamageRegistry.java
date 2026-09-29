package com.github.alexthe666.iceandfire.misc;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

import static com.github.alexthe666.iceandfire.IceAndFire.MODID;

public class IafDamageRegistry {
    public static final ResourceKey<DamageType> GORGON_DMG_TYPE = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.parse("iceandfire:gorgon"));
    public static final ResourceKey<DamageType> DRAGON_FIRE_TYPE = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.parse("iceandfire:dragon_fire"));
    public static final ResourceKey<DamageType> DRAGON_ICE_TYPE = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.parse("iceandfire:dragon_ice"));
    public static final ResourceKey<DamageType> DRAGON_LIGHTNING_TYPE = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.parse("iceandfire:dragon_lightning"));

    static class CustomEntityDamageSource extends DamageSource {
        public CustomEntityDamageSource(Holder<DamageType> damageTypeIn, @Nullable Entity damageSourceEntityIn) {
            super(damageTypeIn, damageSourceEntityIn);
        }

        @Override
        public @NotNull Component getLocalizedDeathMessage(LivingEntity entityLivingBaseIn) {
            LivingEntity livingentity = entityLivingBaseIn.getKillCredit();
            String s = "death.attack." + this.getMsgId();
            int index = entityLivingBaseIn.getRandom().nextInt(2);
            String s1 = s + "." + index;
            String s2 = s + ".attacker_" + index;
            return livingentity != null ? Component.translatable(s2, entityLivingBaseIn.getDisplayName(), livingentity.getDisplayName()) : Component.translatable(s1, entityLivingBaseIn.getDisplayName());
        }
    }

    static class CustomIndirectEntityDamageSource extends DamageSource {

        public CustomIndirectEntityDamageSource(Holder<DamageType> damageTypeIn, Entity source, @Nullable Entity indirectEntityIn) {
            super(damageTypeIn, source, indirectEntityIn);
        }

        @Override
        public @NotNull Component getLocalizedDeathMessage(LivingEntity entityLivingBaseIn) {
            LivingEntity livingentity = entityLivingBaseIn.getKillCredit();
            String s = "death.attack." + this.getMsgId();
            int index = entityLivingBaseIn.getRandom().nextInt(2);
            String s1 = s + "." + index;
            String s2 = s + ".attacker_" + index;
            return livingentity != null ? Component.translatable(s2, entityLivingBaseIn.getDisplayName(), livingentity.getDisplayName()) : Component.translatable(s1, entityLivingBaseIn.getDisplayName());
        }
    }

    public static CustomEntityDamageSource causeGorgonDamage(@Nullable Entity entity) {
        Holder<DamageType> holder = entity.level().registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(GORGON_DMG_TYPE);
        return new CustomEntityDamageSource(holder, entity);
    }

    public static CustomEntityDamageSource causeDragonFireDamage(@Nullable Entity entity) {
        Holder<DamageType> holder = entity.level().registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(DRAGON_FIRE_TYPE);
        return new CustomEntityDamageSource(holder, entity);
    }

    public static CustomIndirectEntityDamageSource causeIndirectDragonFireDamage(Entity source, @Nullable Entity indirectEntityIn) {
        Holder<DamageType> holder = indirectEntityIn.level().registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(DRAGON_FIRE_TYPE);
        return new CustomIndirectEntityDamageSource(holder, source, indirectEntityIn);
    }

    public static CustomEntityDamageSource causeDragonIceDamage(@Nullable Entity entity) {
        Holder<DamageType> holder = entity.level().registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(DRAGON_ICE_TYPE);
        return new CustomEntityDamageSource(holder, entity);
    }

    public static CustomIndirectEntityDamageSource causeIndirectDragonIceDamage(Entity source, @Nullable Entity indirectEntityIn) {
        Holder<DamageType> holder = indirectEntityIn.level().registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(DRAGON_ICE_TYPE);
        return new CustomIndirectEntityDamageSource(holder, source, indirectEntityIn);
    }

    public static CustomEntityDamageSource causeDragonLightningDamage(@Nullable Entity entity) {
        Holder<DamageType> holder = entity.level().registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(DRAGON_LIGHTNING_TYPE);
        return new CustomEntityDamageSource(holder, entity);
    }

    public static CustomIndirectEntityDamageSource causeIndirectDragonLightningDamage(Entity source, @Nullable Entity indirectEntityIn) {
        Holder<DamageType> holder = indirectEntityIn.level().registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(DRAGON_LIGHTNING_TYPE);
        return new CustomIndirectEntityDamageSource(holder, source, indirectEntityIn);
    }

    public static void gatherData(GatherDataEvent event) {
        event.getGenerator().addProvider(
                // Tell generator to run only when server data are generating
                event instanceof GatherDataEvent.Server,
                (DataProvider.Factory<IafDamageTypeTagsProvider>) output -> new IafDamageTypeTagsProvider(
                        event.getGenerator().getPackOutput(),
                        event.getLookupProvider(),
                        MODID
                )
        );
    }

    public static class IafDamageTypeTagsProvider extends DamageTypeTagsProvider {

        public IafDamageTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId) {
            super(output, lookupProvider, modId);
        }

        @Override
        public void addTags(HolderLookup.Provider pProvider) {
            this.tag(DamageTypeTags.BYPASSES_ARMOR).add(GORGON_DMG_TYPE);
            this.tag(DamageTypeTags.BYPASSES_EFFECTS).add(GORGON_DMG_TYPE);
        }
    }
}
