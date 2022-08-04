package net.creeperhost.chickens;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.event.events.common.InteractionEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.platform.Platform;
import dev.architectury.registry.level.entity.EntityAttributeRegistry;
import net.creeperhost.chickens.api.ChickenAPI;
import net.creeperhost.chickens.api.ChickenTransformationRecipe;
import net.creeperhost.chickens.config.ConfigHandler;
import net.creeperhost.chickens.entity.EntityChickensChicken;
import net.creeperhost.chickens.init.*;
import net.creeperhost.chickens.network.PacketHandler;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Chickens
{
    public static final String MOD_ID = "chickens";
    public static final Logger LOGGER = LogManager.getLogger();

    public static void init()
    {
        ConfigHandler.LoadConfigs(ModChickens.generateDefaultChickens());
        ModBlocks.BLOCKS.register();
        ModEntities.ENTITIES.register();
        ModBlocks.TILES_ENTITIES.register();
        ModItems.ITEMS.register();
        ModContainers.CONTAINERS.register();
        ModSounds.SOUNDS.register();
        ClientLifecycleEvent.CLIENT_SETUP.register(ChickensClient::clientSetup);

        ModEntities.CHICKENS.forEach((chickensRegistryItem, entityTypeSupplier) -> EntityAttributeRegistry.register(entityTypeSupplier, EntityChickensChicken::prepareAttributes));

        InteractionEvent.INTERACT_ENTITY.register(Chickens::onEntityInteract);
        PacketHandler.init();

        if (Platform.isFabric())
        {
            ModEntities.CHICKENS.forEach((chickensRegistryItem, entityTypeSupplier) -> ModEntities.registerSpawn(entityTypeSupplier, chickensRegistryItem));
        }

        //We need to do this late or the entities are not registered yet
        LifecycleEvent.SETUP.register(ModRecipes::init);
    }



    private static EventResult onEntityInteract(Player player, Entity entity, InteractionHand interactionHand)
    {
        Level level = player.getLevel();
        if(!player.getItemInHand(interactionHand).isEmpty())
        {
            for (ChickenTransformationRecipe transformationRecipe : ChickenAPI.TRANSFORMATION_RECIPES)
            {
                if(transformationRecipe.getEntityTypeIn() == entity.getType() && player.getItemInHand(interactionHand).sameItem(transformationRecipe.getStack()))
                {
                    Entity newEntity = transformationRecipe.getEntityTypeOut().create(level);
                    if(newEntity != null)
                    {
                        newEntity.setPos(entity.position());
                        level.addFreshEntity(newEntity);
                        entity.remove(Entity.RemovalReason.DISCARDED);
                        if (!player.isCreative())
                        {
                            player.getItemInHand(interactionHand).shrink(1);
                        }
                    }
                }
            }
        }
        return EventResult.pass();
    }
}
