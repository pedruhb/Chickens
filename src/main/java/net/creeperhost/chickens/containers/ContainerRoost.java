package net.creeperhost.chickens.containers;

import net.creeperhost.chickens.blockentities.BlockEntityRoost;
import net.creeperhost.chickens.containers.slots.SlotChicken;
import net.creeperhost.chickens.containers.slots.SlotOutput;
import net.creeperhost.chickens.init.ModContainers;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;

public class ContainerRoost extends ContainerBase
{
    ContainerData containerData;
    BlockEntityRoost roost;

    public ContainerRoost(int id, Inventory playerInv, FriendlyByteBuf extraData)
    {
        this(id, playerInv, (BlockEntityRoost) Minecraft.getInstance().level.getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(1));
    }

    public ContainerRoost(int id, Inventory playerInv, BlockEntityRoost blockEntityBreeder, ContainerData containerData)
    {
        super(ModContainers.ROOST_CONTAINER.get(), id);
        this.containerData = containerData;
        this.roost = blockEntityBreeder;

        addSlot(new SlotChicken(blockEntityBreeder.inventory, 0, 26, 20));

        for (int i = 0; i < 4; ++i) {
            addSlot(new SlotOutput(blockEntityBreeder.inventory, i + 1, 80 + i * 18, 20));
        }

        for (int l = 0; l < 3; ++l)
        {
            for (int k = 0; k < 9; ++k)
            {
                this.addSlot(new Slot(playerInv, k + l * 9 + 9, 8 + k * 18, l * 18 + 51));
            }
        }

        for (int i1 = 0; i1 < 9; ++i1)
        {
            this.addSlot(new Slot(playerInv, i1, 8 + i1 * 18, 109));
        }
        addDataSlots(containerData);
    }

    @Override
    public boolean stillValid(Player player)
    {
        return player.level().getBlockEntity(roost.getBlockPos()) != null;
    }

    public int getProgress()
    {
        return containerData.get(0);
    }
}
