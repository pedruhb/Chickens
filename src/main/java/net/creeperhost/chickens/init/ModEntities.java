package net.creeperhost.chickens.init;

import dev.architectury.hooks.level.biome.BiomeProperties;
import dev.architectury.registry.level.biome.BiomeModifications;
import net.creeperhost.chickens.ChickensMod;
import net.creeperhost.chickens.entity.EntityChickensChicken;
import net.creeperhost.chickens.entity.EntityColoredEgg;
import net.creeperhost.chickens.handler.SpawnType;
import net.creeperhost.chickens.registry.ChickensRegistry;
import net.creeperhost.chickens.registry.ChickensRegistryItem;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.*;
import java.util.function.Supplier;

public class ModEntities
{
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registry.ENTITY_TYPE_REGISTRY, ChickensMod.MODID);
    public static final RegistryObject<EntityType<EntityColoredEgg>> EGG = ENTITIES.register("egg", () -> EntityType.Builder.<EntityColoredEgg>of(EntityColoredEgg::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10).build("egg"));

    @Deprecated
    public static final List<EntityType<?>> SPAWNABLE_CHICKENS = new ArrayList<>();

    public static final Map<ChickensRegistryItem, Supplier<EntityType<EntityChickensChicken>>> CHICKENS = Util.make(new LinkedHashMap<>(), map ->
    {
        for (ChickensRegistryItem item : ChickensRegistry.getItems())
        {
            map.put(item, ENTITIES.register(item.getEntityName(), () -> EntityType.Builder.of(EntityChickensChicken::new, MobCategory.CREATURE).sized(0.6F, 1.7F).clientTrackingRange(8).build(item.getEntityName())));
        }
    });

    public static void registerSpawn(Supplier<EntityType<EntityChickensChicken>> entityType, ChickensRegistryItem chickensRegistryItem)
    {
        if(chickensRegistryItem.canSpawn())
        {
            BiomeModifications.addProperties(biomeContext -> canSpawnBiome(biomeContext.getProperties()), (biomeContext, mutable) -> mutable.getSpawnProperties().addSpawn(MobCategory.MONSTER,
                    new MobSpawnSettings.SpawnerData(entityType.get(), 4, 4, 10)));

            SpawnPlacements.register(entityType.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    (p_21781_, p_21782_, p_21783_, p_21784_, p_21785_) -> checkAnimalSpawnRules(p_21781_, p_21782_, p_21783_, p_21784_, p_21785_, chickensRegistryItem));
        }
    }

    public static boolean canSpawnBiome(BiomeProperties biomeProperties)
    {
        return true;
    }


    @Deprecated
    public static void init()
    {
//        for (ChickensRegistryItem item : ChickensRegistry.getItems())
//        {
//            EntityType<EntityChickensChicken> entityType = EntityType.Builder.of(EntityChickensChicken::new, MobCategory.CREATURE).sized(0.6F, 1.7F).clientTrackingRange(8).build(item.getEntityName());
//            ENTITIES.register(item.getEntityName(), () -> entityType);
//            if(item.canSpawn())
//            {
//                SPAWNABLE_CHICKENS.add(entityType);
//                SpawnPlacements.register(entityType, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (p_21781_, p_21782_, p_21783_, p_21784_, p_21785_)
//                        -> checkAnimalSpawnRules(p_21781_, p_21782_, p_21783_, p_21784_, p_21785_, item));
//            }
//        }
    }

    public static boolean checkAnimalSpawnRules(EntityType<?> entityType, LevelAccessor levelAccessor, MobSpawnType mobSpawnType, BlockPos blockPos, RandomSource random, ChickensRegistryItem chickensRegistryItem)
    {
        if(!levelAccessor.getBlockState(blockPos.below()).is(BlockTags.ANIMALS_SPAWNABLE_ON)) return false;
        if(!isBrightEnoughToSpawn(levelAccessor, blockPos)) return false;
        if(!chickensRegistryItem.canSpawn()) return false;
        if(chickensRegistryItem.getSpawnType() == SpawnType.NORMAL)
        {
            return levelAccessor.dimensionType().natural();
        }
        if(chickensRegistryItem.getSpawnType() == SpawnType.HELL)
        {
            return levelAccessor.dimensionType().piglinSafe();
        }
        return true;
    }

    public static boolean isBrightEnoughToSpawn(BlockAndTintGetter blockAndTintGetter, BlockPos blockPos)
    {
        return blockAndTintGetter.getRawBrightness(blockPos, 0) > 8;
    }
}
