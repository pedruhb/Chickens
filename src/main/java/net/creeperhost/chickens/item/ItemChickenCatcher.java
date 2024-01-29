package net.creeperhost.chickens.item;

import net.creeperhost.chickens.data.ChickenStats;
import net.creeperhost.chickens.entity.EntityChickensChicken;
import net.creeperhost.chickens.init.ModItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemChickenCatcher extends Item
{
    public ItemChickenCatcher(Properties properties)
    {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player playerIn, LivingEntity livingEntity, InteractionHand hand) {

        if(livingEntity instanceof EntityChickensChicken entityChickensChicken)
        {
            Level level = livingEntity.level();
            ItemStack chicken = new ItemStack(ModItems.CHICKEN_ITEM.get());
            ItemChicken.applyEntityIdToItemStack(chicken, EntityType.getKey(livingEntity.getType()));
            if(chicken.getTag() != null)
            {
                chicken.getTag().putBoolean("baby", entityChickensChicken.isBaby());
                chicken.getTag().putInt("love", entityChickensChicken.getInLoveTime());
            }

            ChickenStats chickenStats = new ChickenStats(entityChickensChicken.getGain(), entityChickensChicken.getGrowth(), entityChickensChicken.getStrength());
            chickenStats.write(chicken);

            ItemEntity itemEntity = new ItemEntity(level, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), chicken);
            level.addFreshEntity(itemEntity);
            livingEntity.remove(Entity.RemovalReason.DISCARDED);
            return InteractionResult.PASS;
        }
        return InteractionResult.PASS;
    }
}
