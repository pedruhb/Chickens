package net.creeperhost.chickens.events;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.creeperhost.chickens.ChickensMod;
import net.creeperhost.chickens.entity.EntityChickensChicken;
import net.creeperhost.chickens.init.ModEntities;
import net.creeperhost.chickens.registry.ChickensRegistry;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = ChickensMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEvents
{

    static DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS =
    DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, ChickensMod.MODID);

    @SubscribeEvent
    public static void onBiomeLoading(BiomeLoadingEvent event)
    {
        ModEntities.SPAWNABLE_CHICKENS.forEach(entityType ->
        {
            try
            {
                event.getSpawns().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(entityType, 10, 4, 4));
            }
            catch (Exception ignored){}
        });
    }

    @SubscribeEvent
    public static void playerInteractEvent(PlayerInteractEvent.EntityInteract event)
    {
        Player player = event.getEntity();
        Level level = event.getLevel();

        if(!player.getItemInHand(event.getHand()).isEmpty() && player.getItemInHand(event.getHand()).getItem() == Items.BOOK)
        {
            Entity entity = event.getTarget();
            if(entity.getType() == EntityType.CHICKEN)
            {
                EntityType<?> entityType = Registry.ENTITY_TYPE.get(ChickensRegistry.SMART_CHICKEN_ID);
                EntityChickensChicken chicken = (EntityChickensChicken) entityType.create(level);
                if(chicken != null)
                {
                    chicken.setPos(entity.position());
                    level.addFreshEntity(chicken);
                    entity.remove(Entity.RemovalReason.DISCARDED);
                    if (!player.isCreative())
                    {
                        player.getItemInHand(event.getHand()).shrink(1);
                    }
                }
            }
        }
    }
}
