package com.github.alexthe666.iceandfire.recipe;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityCockatriceEgg;
import com.github.alexthe666.iceandfire.entity.EntityDeathWormEgg;
import com.github.alexthe666.iceandfire.entity.EntityDragonArrow;
import com.github.alexthe666.iceandfire.entity.EntityHippogryphEgg;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.ProjectileDispenseBehavior;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.DispenserBlock;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
//#if MC < 26.3
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
//#endif
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.Direction;

public final class IafRecipeRegistry {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPE =
        DeferredRegister.create(Registries.RECIPE_TYPE, IceAndFire.MODID);
    public static final DeferredHolder<RecipeType<?>, RecipeType<DragonForgeRecipe>> DRAGON_FORGE_TYPE =
        RECIPE_TYPE.register("dragonforge", () -> RecipeType.simple(Identifier.fromNamespaceAndPath(IceAndFire.MODID, "dragonforge")));
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZER =
        DeferredRegister.create(Registries.RECIPE_SERIALIZER, IceAndFire.MODID);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<DragonForgeRecipe>> DRAGON_FORGE_SERIALIZER =
        RECIPE_SERIALIZER.register("dragonforge", () -> DragonForgeRecipe.Serializer.INSTANCE);

    private IafRecipeRegistry() {}

    public static void preInit(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            registerArrowDispenser(IafItemRegistry.STYMPHALIAN_ARROW.get());
            registerArrowDispenser(IafItemRegistry.AMPHITHERE_ARROW.get());
            registerArrowDispenser(IafItemRegistry.SEA_SERPENT_ARROW.get());
            registerArrowDispenser(IafItemRegistry.DRAGONBONE_ARROW.get());
            registerArrowDispenser(IafItemRegistry.HYDRA_ARROW.get());
            registerEggDispenser(IafItemRegistry.HIPPOGRYPH_EGG.get(), (level, position, stack) ->
                new EntityHippogryphEgg(IafEntityRegistry.HIPPOGRYPH_EGG.get(), level, position.x(), position.y(), position.z(), stack));
            registerEggDispenser(IafItemRegistry.ROTTEN_EGG.get(), (level, position, stack) ->
                new EntityCockatriceEgg(IafEntityRegistry.COCKATRICE_EGG.get(), position.x(), position.y(), position.z(), level));
            registerEggDispenser(IafItemRegistry.DEATHWORM_EGG.get(), (level, position, stack) ->
                new EntityDeathWormEgg(IafEntityRegistry.DEATH_WORM_EGG.get(), position.x(), position.y(), position.z(), level, false));
            registerEggDispenser(IafItemRegistry.DEATHWORM_EGG_GIGANTIC.get(), (level, position, stack) ->
                new EntityDeathWormEgg(IafEntityRegistry.DEATH_WORM_EGG.get(), position.x(), position.y(), position.z(), level, true));
        });
    }

    //#if MC < 26.3
    public static void registerBrewingRecipes(RegisterBrewingRecipesEvent event) {
        event.getBuilder().addMix(Potions.WATER, IafItemRegistry.SHINY_SCALES.get(), Potions.WATER_BREATHING);
    }
    //#endif

    private static void registerArrowDispenser(Item item) {
        DispenserBlock.registerBehavior(item, new ProjectileDispenseBehavior(item));
    }

    private static void registerEggDispenser(Item item, EggProjectileFactory factory) {
        DispenserBlock.registerBehavior(item, new DefaultDispenseItemBehavior() {
            @Override
            protected ItemStack execute(BlockSource source, ItemStack dispensed) {
                ServerLevel level = source.level();
                Direction direction = source.state().getValue(DispenserBlock.FACING);
                Position position = DispenserBlock.getDispensePosition(source);
                Projectile projectile = factory.create(level, position, dispensed);
                Projectile.spawnProjectileUsingShoot(projectile, level, dispensed,
                    direction.getStepX(), direction.getStepY(), direction.getStepZ(), 1.1F, 6.0F);
                dispensed.shrink(1);
                return dispensed;
            }
        });
    }

    @FunctionalInterface
    private interface EggProjectileFactory {
        Projectile create(ServerLevel level, Position position, ItemStack stack);
    }
}
