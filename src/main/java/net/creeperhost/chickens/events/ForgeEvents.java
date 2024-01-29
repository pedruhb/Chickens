package net.creeperhost.chickens.events;

import com.mojang.serialization.Codec;

import net.creeperhost.chickens.ChickensMod;
import net.creeperhost.chickens.entity.EntityChickensChicken;
import net.creeperhost.chickens.registry.ChickensRegistry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = ChickensMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEvents
{

    static DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS =
    DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, ChickensMod.MODID);

    //todo
    /*@SubscribeEvent
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
    }*/

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
                EntityType<?> entityType = ForgeRegistries.ENTITY_TYPES.getValue(ChickensRegistry.SMART_CHICKEN_ID);
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
