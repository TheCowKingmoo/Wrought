package com.thecowking.wrought.util;

import com.thecowking.wrought.Wrought;
import com.thecowking.wrought.blocks.honeycomb_coke_oven.*;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RegistryHandler {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Wrought.MODID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Wrought.MODID);
    private static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Wrought.MODID);
    private static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS,  Wrought.MODID);



    public static void init()  {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());

    }

    //Honey Comb Coke Controller
    public static final RegistryObject<Block> H_C_COKE_CONTROLLER_BLOCK = BLOCKS.register("h_c_coke_controller_block", HCCokeOvenControllerBlock::new);
    public static final RegistryObject<Item> H_C_COKE_CONTROLLER_BLOCK_ITEM = ITEMS.register("h_c_coke_controller_block", () -> new BlockItemBase(H_C_COKE_CONTROLLER_BLOCK.get()));
    public static final RegistryObject<TileEntityType<HCCokeOvenControllerTile>> H_C_COKE_CONTROLLER_TILE = TILES.register("h_c_coke_controller_block", () -> TileEntityType.Builder.create(HCCokeOvenControllerTile::new, H_C_COKE_CONTROLLER_BLOCK.get()).build(null));

    //Honey Comb Coke Frame
    public static final RegistryObject<Block> H_C_COKE_FRAME_BLOCK = BLOCKS.register("h_c_coke_frame_block", HCCokeOvenFrameBlock::new);
    public static final RegistryObject<Item> H_C_COKE_FRAME_BLOCK_ITEM = ITEMS.register("h_c_coke_frame_block", () -> new BlockItemBase(H_C_COKE_FRAME_BLOCK.get()));
    public static final RegistryObject<TileEntityType<HCCokeOvenFrameTile>> H_C_COKE_FRAME_TILE = TILES.register("h_c_coke_frame_block", () -> TileEntityType.Builder.create(HCCokeOvenFrameTile::new, H_C_COKE_FRAME_BLOCK.get()).build(null));

    //Honey Comb Coke Multi-Block
    public static final RegistryObject<ContainerType<HCCokeOvenContainer>> H_C_CONTAINER = CONTAINERS.register("h_c_coke_controller_block", () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getEntityWorld();
        return new HCCokeOvenContainer(windowId, world, pos, inv, inv.player);
    }));

}
