package com.thecowking.wrought.init;

import com.thecowking.wrought.client.screen.BuilderScreen;
import com.thecowking.wrought.client.screen.blast_furnace.BlastFurnaceMultiblockScreen;
import com.thecowking.wrought.client.screen.bloomery.BloomeryMultiblockScreen;
import com.thecowking.wrought.client.screen.honey_comb_coke_oven.HCCokeOvenScreenMultiblock;
import com.thecowking.wrought.data.BloomeryData;
import com.thecowking.wrought.init.RegistryHandler;
import com.thecowking.wrought.inventory.containers.bloomery.BloomeryContainerBuilder;
import com.thecowking.wrought.inventory.containers.bloomery.BloomeryContainerMultiblock;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = com.thecowking.wrought.Wrought.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    public static void init(final FMLClientSetupEvent event) {
        ScreenManager.registerFactory(RegistryHandler.H_C_CONTAINER.get(), HCCokeOvenScreenMultiblock::new);
        ScreenManager.registerFactory(RegistryHandler.H_C_CONTAINER_BUILDER.get(), BuilderScreen::new);
        ScreenManager.registerFactory(RegistryHandler.BLAST_FURANCE_MULTIBLOCK_CONTAINER.get(), BlastFurnaceMultiblockScreen::new);
        ScreenManager.registerFactory(RegistryHandler.BLAST_FURNACE_BUILDER_CONTAINER.get(), BuilderScreen::new);
        ScreenManager.registerFactory(RegistryHandler.BLOOMERY_MULTIBLOCK_CONTAINER.get(), BloomeryMultiblockScreen::new);
        ScreenManager.registerFactory(RegistryHandler.BLOOMERY_BUILDER_CONTAINER.get(), BuilderScreen::new);
    }

    @SubscribeEvent
    public void onTooltipPre(RenderTooltipEvent.Pre event) {
        Item item = event.getStack().getItem();
        if (item.getRegistryName().getNamespace().equals(com.thecowking.wrought.Wrought.MODID)) {
            event.setMaxWidth(200);
        }
    }
}


