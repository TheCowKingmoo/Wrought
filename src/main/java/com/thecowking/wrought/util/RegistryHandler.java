package com.thecowking.wrought.util;

import com.thecowking.wrought.Wrought;
import com.thecowking.wrought.blocks.honeycomb_coke_oven.HCCokeOvenControllerBlock;
import com.thecowking.wrought.blocks.honeycomb_coke_oven.HCCokeOvenFrameBlock;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RegistryHandler {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Wrought.MODID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Wrought.MODID);

    public static void init()  {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    // Items
    // Blocks
    public static final RegistryObject<Block> H_C_COKE_CONTROLLER_BLOCK = BLOCKS.register("h_c_coke_controller_block", HCCokeOvenControllerBlock::new);
    public static final RegistryObject<Block> H_C_COKE_FRAME_BLOCK = BLOCKS.register("h_c_coke_frame_block", HCCokeOvenFrameBlock::new);

    // Block Items
    public static final RegistryObject<Item> H_C_COKE_CONTROLLER_BLOCK_ITEM = ITEMS.register("h_c_coke_controller_block", () -> new BlockItemBase(H_C_COKE_CONTROLLER_BLOCK.get()));
    public static final RegistryObject<Item> H_C_COKE_FRAME_BLOCK_ITEM = ITEMS.register("h_c_coke_frame_block", () -> new BlockItemBase(H_C_COKE_FRAME_BLOCK.get()));

}
