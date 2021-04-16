package com.thecowking.wrought.init;

import com.thecowking.wrought.Wrought;
import com.thecowking.wrought.blocks.blast_furance.*;
import com.thecowking.wrought.blocks.bloomery.BloomeryControllerBlock;
import com.thecowking.wrought.blocks.casting_machine.CastingMachineControllerBlock;
import com.thecowking.wrought.blocks.refractory_brick.RefractoryBrickBlock;
import com.thecowking.wrought.blocks.refractory_brick.RefractoryBrickSlab;
import com.thecowking.wrought.blocks.refractory_brick.RefractoryBrickStairs;
import com.thecowking.wrought.blocks.coke_block.CokeBlock;
import com.thecowking.wrought.blocks.honey_comb_coke_oven.*;
import com.thecowking.wrought.data.MetalData;
import com.thecowking.wrought.inventory.containers.blast_furnace.BlastFurnaceContainerBuilder;
import com.thecowking.wrought.inventory.containers.blast_furnace.BlastFurnaceContainerMultiblock;
import com.thecowking.wrought.inventory.containers.bloomery.BloomeryContainerBuilder;
import com.thecowking.wrought.inventory.containers.bloomery.BloomeryContainerMultiblock;
import com.thecowking.wrought.inventory.containers.casting_machine.CastingMachineContainerBuilder;
import com.thecowking.wrought.inventory.containers.casting_machine.CastingMachineContainerMultiblock;
import com.thecowking.wrought.inventory.containers.honey_comb_coke_oven.HCCokeOvenContainer;
import com.thecowking.wrought.inventory.containers.honey_comb_coke_oven.HCCokeOvenContainerMultiblock;
import com.thecowking.wrought.items.blocks.BlockItemBase;
import com.thecowking.wrought.items.blocks.CokeBlockItem;
import com.thecowking.wrought.items.items.*;
import com.thecowking.wrought.tileentity.blast_furance.BlastFurnaceBrickControllerTile;
import com.thecowking.wrought.tileentity.blast_furance.BlastBrickTile;
import com.thecowking.wrought.tileentity.bloomery.BloomeryControllerTile;
import com.thecowking.wrought.tileentity.casting_machine.CastingMachineControllerTile;
import com.thecowking.wrought.tileentity.honey_comb_coke_oven.HCCokeOvenControllerTile;
import com.thecowking.wrought.tileentity.honey_comb_coke_oven.CokeBrickTile;
import com.thecowking.wrought.tileentity.refractory_brick.RefractoryBrickFrameTile;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
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

import java.util.HashMap;

public class RegistryHandler {


    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Wrought.MODID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Wrought.MODID);
    private static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Wrought.MODID);
    private static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS,  Wrought.MODID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, Wrought.MODID);


    public static void init()  {
        FLUIDS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
        RecipeSerializerInit.RECIPE_SERIALIZERS.register(FMLJavaModLoadingContext.get().getModEventBus());
        FluidInit.initFluids();
    }

    //Honey Comb Coke Controller
    public static final RegistryObject<Block> H_C_COKE_CONTROLLER_BLOCK = BLOCKS.register("h_c_coke_controller_block", HCCokeOvenControllerBlock::new);
    public static final RegistryObject<Item> H_C_COKE_CONTROLLER_BLOCK_ITEM = ITEMS.register("h_c_coke_controller_block", () -> new BlockItemBase(H_C_COKE_CONTROLLER_BLOCK.get()));
    public static final RegistryObject<TileEntityType<HCCokeOvenControllerTile>> H_C_COKE_CONTROLLER_TILE = TILES.register("h_c_coke_controller_block", () -> TileEntityType.Builder.create(HCCokeOvenControllerTile::new, H_C_COKE_CONTROLLER_BLOCK.get()).build(null));

    //Honey Comb Coke Frame
    public static final RegistryObject<Block> COKE_BRICK_BLOCK = BLOCKS.register("coke_brick_block", CokeBrickBlock::new);
    public static final RegistryObject<Item> COKE_BRICK_BLOCK_ITEM = ITEMS.register("coke_brick_block", () -> new BlockItemBase(COKE_BRICK_BLOCK.get()));
    public static final RegistryObject<TileEntityType<CokeBrickTile>> COKE_BRICK_TILE = TILES.register("coke_brick_block", () -> TileEntityType.Builder.create(CokeBrickTile::new, COKE_BRICK_BLOCK.get()).build(null));


    //Honey Comb Coke Frame Slab
    public static final RegistryObject<Block> COKE_BRICK_SLAB = BLOCKS.register("coke_brick_slab", CokeBrickSlab::new);
    public static final RegistryObject<Item> COKE_BRICK_SLAB_ITEM = ITEMS.register("coke_brick_slab", () -> new BlockItemBase(COKE_BRICK_SLAB.get()));

    //Honey Comb Coke Frame Stairs
    public static final RegistryObject<Block> COKE_BRICK_STAIR = BLOCKS.register("coke_brick_stair", CokeBrickStairs::new);
    public static final RegistryObject<Item> COKE_BRICK_STAIR_ITEM = ITEMS.register("coke_brick_stair", () -> new BlockItemBase(COKE_BRICK_STAIR.get()));

    //Honey Comb Coke Multi-Block
    public static final RegistryObject<ContainerType<HCCokeOvenContainerMultiblock>> H_C_CONTAINER = CONTAINERS.register("h_c_coke_controller_block", () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getEntityWorld();
        return new HCCokeOvenContainerMultiblock(windowId, world, pos, inv);
    }));

    //Honey Comb Coke Multi-Block
    public static final RegistryObject<ContainerType<HCCokeOvenContainer>> H_C_CONTAINER_BUILDER = CONTAINERS.register("h_c_coke_controller_block_builder", () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getEntityWorld();
        return new HCCokeOvenContainer(windowId, world, pos, inv);
    }));



    //Coke Item
    public static final RegistryObject<Item> COKE = ITEMS.register("coke_item", CokeItem::new);

    // Unfired Carbon Brick
    public static final RegistryObject<Item> UNFIRED_CARBON_BRICK_ITEM = ITEMS.register("unfired_carbon_brick_item", UnfiredCarbonBrickItem::new);
    // Carbon Brick Item
    public static final RegistryObject<Item> CARBON_BRICK_ITEM = ITEMS.register("carbon_brick_item", CarbonBrickItem::new);

    // Unfired Refractory Brick
    public static final RegistryObject<Item> UNFIRED_REFRACTORY_BRICK_ITEM = ITEMS.register("unfired_refractory_brick_item", UnfiredRefractoryBrickItem::new);
    // Refractory Brick Item
    public static final RegistryObject<Item> REFRACTORY_BRICK_ITEM = ITEMS.register("refractory_brick_item", RefractoryBrickItem::new);

    // Unfired Coke Brick
    public static final RegistryObject<Item> UNFIRED_COKE_BRICK_ITEM = ITEMS.register("unfired_coke_brick_item", UnfiredCokeBrickItem::new);
    //CokeBrick Item
    public static final RegistryObject<Item> COKE_BRICK_ITEM = ITEMS.register("coke_brick_item", CokeBrickItem::new);

    //Slag Item
    public static final RegistryObject<Item> SLAG = ITEMS.register("slag_item", SlagItem::new);

    //Soot Item
    public static final RegistryObject<Item> SOOT = ITEMS.register("soot_item", SootItem::new);

    //Ash Item
    public static final RegistryObject<Item> ASH = ITEMS.register("ash_item", AshItem::new);

    //Calcium Carbonate
    public static final RegistryObject<Item> CALCIUM_CARBONATE = ITEMS.register("calcium_carbonate_item", CalciumCarbonate::new);

    //Coke Block
    public static final RegistryObject<Block> COKE_BLOCK = BLOCKS.register("coke_block", CokeBlock::new);
    public static final RegistryObject<Item> COKE_BLOCK_ITEM = ITEMS.register("coke_block", () -> new CokeBlockItem(COKE_BLOCK.get()));

    //Blast Furnace Controller
    public static final RegistryObject<Block> BLAST_FURANCE_BRICK_CONTROLLER = BLOCKS.register("blast_furnace_brick_controller", BlastFurnaceBrickControllerBlock::new);
    public static final RegistryObject<Item> BLAST_FURANCE_BRICK_CONTROLLER_ITEM = ITEMS.register("blast_furnace_brick_controller", () -> new BlockItemBase(BLAST_FURANCE_BRICK_CONTROLLER.get()));
    public static final RegistryObject<TileEntityType<BlastFurnaceBrickControllerTile>> BLAST_FURNACE_BRICK_CONTROLLER_TILE = TILES.register("blast_furnace_brick_controller", () -> TileEntityType.Builder.create(BlastFurnaceBrickControllerTile::new, BLAST_FURANCE_BRICK_CONTROLLER.get()).build(null));

    //Blast Furnace Frame
    public static final RegistryObject<Block> BLAST_BRICK_BLOCK = BLOCKS.register("blast_brick_block", BlastBrickBlock::new);
    public static final RegistryObject<Item> BLAST_BRICK_BLOCK_ITEM = ITEMS.register("blast_brick_block", () -> new BlockItemBase(BLAST_BRICK_BLOCK.get()));
    public static final RegistryObject<TileEntityType<BlastBrickTile>> BLAST_BRICK_BLOCK_TILE = TILES.register("blast_brick_block", () -> TileEntityType.Builder.create(BlastBrickTile::new, BLAST_BRICK_BLOCK.get()).build(null));


    //Bloomery Controller
    public static final RegistryObject<Block> BLOOMERY_CONTROLLER = BLOCKS.register("bloomery_controller", BloomeryControllerBlock::new);
    public static final RegistryObject<Item> BLOOMERY_CONTROLLER_ITEM = ITEMS.register("bloomery_controller", () -> new BlockItemBase(BLOOMERY_CONTROLLER.get()));
    public static final RegistryObject<TileEntityType<BloomeryControllerTile>> BLOOMERY_CONTROLLER_TILE = TILES.register("bloomery_controller", () -> TileEntityType.Builder.create(BloomeryControllerTile::new, BLOOMERY_CONTROLLER.get()).build(null));

    public static final RegistryObject<ContainerType<BloomeryContainerBuilder>> BLOOMERY_BUILDER_CONTAINER = CONTAINERS.register("bloomery_builder_container", () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getEntityWorld();
        return new BloomeryContainerBuilder(windowId, world, pos, inv);
    }));

    public static final RegistryObject<ContainerType<BloomeryContainerMultiblock>> BLOOMERY_MULTIBLOCK_CONTAINER = CONTAINERS.register("bloomery_multiblock_container", () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getEntityWorld();
        return new BloomeryContainerMultiblock(windowId, world, pos, inv);
    }));


    //Casting Machine Controller
    public static final RegistryObject<Block> CASTING_MACHINE_CONTROLLER = BLOCKS.register("casting_machine_controller", CastingMachineControllerBlock::new);
    public static final RegistryObject<Item> CASTING_MACHINE_CONTROLLER_ITEM = ITEMS.register("casting_machine_controller", () -> new BlockItemBase(CASTING_MACHINE_CONTROLLER.get()));
    public static final RegistryObject<TileEntityType<CastingMachineControllerTile>> CASTING_MACHINE_CONTROLLER_TILE = TILES.register("casting_machine_controller", () -> TileEntityType.Builder.create(CastingMachineControllerTile::new, CASTING_MACHINE_CONTROLLER.get()).build(null));

    public static final RegistryObject<ContainerType<CastingMachineContainerBuilder>> CASTING_MACHINE_CONTAINER = CONTAINERS.register("casting_machine_controller_container", () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getEntityWorld();
        return new CastingMachineContainerBuilder(windowId, world, pos, inv);
    }));

    public static final RegistryObject<ContainerType<CastingMachineContainerMultiblock>> CASTING_MACHINE_MULTIBLOCK_CONTAINER = CONTAINERS.register("casting_machine_multiblock_container", () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getEntityWorld();
        return new CastingMachineContainerMultiblock(windowId, world, pos, inv);
    }));


    //Refractory Brick
    public static final RegistryObject<Block> REFRACTORY_BRICK = BLOCKS.register("refractory_brick_block", RefractoryBrickBlock::new);
    public static final RegistryObject<Item> REFRACTORY_BRICK_BLOCK_ITEM = ITEMS.register("refractory_brick_block", () -> new BlockItemBase(REFRACTORY_BRICK.get()));
    public static final RegistryObject<TileEntityType<RefractoryBrickFrameTile>> REFRACTORY_BRICK_FRAME_TILE = TILES.register("refractory_brick_block", () -> TileEntityType.Builder.create(RefractoryBrickFrameTile::new, REFRACTORY_BRICK.get()).build(null));


    //Refractory Brick Stairs
    public static final RegistryObject<Block> REFRACTORY_BRICK_STAIR = BLOCKS.register("refractory_brick_stair", RefractoryBrickStairs::new);
    public static final RegistryObject<Item> REFRACTORY_BRICK_STAIR_ITEM = ITEMS.register("refractory_brick_stair", () -> new BlockItemBase(REFRACTORY_BRICK_STAIR.get()));

    //Refractory Brick Slab
    public static final RegistryObject<Block> REFRACTORY_BRICK_SLAB = BLOCKS.register("refractory_brick_slab", RefractoryBrickSlab::new);
    public static final RegistryObject<Item> REFRACTORY_BRICK_SLAB_ITEM = ITEMS.register("refractory_brick_slab", () -> new BlockItemBase(REFRACTORY_BRICK_SLAB.get()));


    //Blast Furnace Container Auto Builder
    public static final RegistryObject<ContainerType<BlastFurnaceContainerBuilder>> BLAST_FURNACE_BUILDER_CONTAINER = CONTAINERS.register("blast_furance_builder_container", () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getEntityWorld();
        return new BlastFurnaceContainerBuilder(windowId, world, pos, inv);
    }));


    //Blast Furnace Container Auto Builder
    public static final RegistryObject<ContainerType<BlastFurnaceContainerMultiblock>> BLAST_FURANCE_MULTIBLOCK_CONTAINER = CONTAINERS.register("blast_furance_multiblock_container", () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getEntityWorld();
        return new BlastFurnaceContainerMultiblock(windowId, world, pos, inv);
    }));

    /*
    public static void registerModdedMaterials()  {
        for(int i = 0; i < RecipeCompatSetup.moddedMaterials.length; i++)  {
            ITEMS.register(RecipeCompatSetup.moddedMaterials[i] + "_ingot", ItemBase::new);

        }
    }

     */


}
