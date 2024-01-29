package net.creeperhost.chickens.client;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraftforge.client.model.BakedModelWrapper;

public class ChickenBakedItemModel extends BakedModelWrapper<BakedModel>
{
    public ChickenBakedItemModel(BakedModel originalModel)
    {
        super(originalModel);
    }

    @Override
    public boolean isCustomRenderer()
    {
        return true;
    }

}
