package net.creeperhost.chickens.client;

import net.creeperhost.chickens.ChickensMod;
import net.creeperhost.chickens.init.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;


public class ChickensCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ChickensMod.MODID);

    public static final RegistryObject<CreativeModeTab> CHICKENS_TAB = CREATIVE_MODE_TABS.register("chickens_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> ModItems.CHICKEN_ITEM.get().getDefaultInstance())
            .build());

    public static final RegistryObject<CreativeModeTab> CHICKENS_SPAWN_EGGS = CREATIVE_MODE_TABS.register("chickens_spawn_egg_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> ModItems.SPAWN_EGG.get().getDefaultInstance())
            .build());

    public static final RegistryObject<CreativeModeTab> CHICKENS_BLOCKS = CREATIVE_MODE_TABS.register("chickens_blocks_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> ModItems.BREEDER.get().getDefaultInstance())
            .build());
            
}
