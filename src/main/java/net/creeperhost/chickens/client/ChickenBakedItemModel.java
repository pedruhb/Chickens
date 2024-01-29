package net.creeperhost.chickens.client;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraftforge.client.model.BakedModelWrapper;

public class ChickenBakedItemModel extends BakedModelWrapper<BakedModel> {
    public ChickenBakedItemModel(BakedModel originalModel) {
        super(originalModel);
    }

    @Override
    public boolean isCustomRenderer() {
        return true;
    }

    public BakedModel applyTransform(ItemDisplayContext cameraTransformType, PoseStack poseStack, boolean applyLeftHandTransform) {
        super.applyTransform(cameraTransformType, poseStack, applyLeftHandTransform);
        return this;
    }
}
