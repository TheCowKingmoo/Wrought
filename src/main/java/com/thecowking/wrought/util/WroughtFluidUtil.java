package com.thecowking.wrought.util;

import com.thecowking.wrought.Wrought;
import com.thecowking.wrought.init.RegistryHandler;
import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// Source - https://github.com/Tslat/Advent-Of-Ascension/blob/1.15.2/source/util/BlockUtil.java


public class WroughtFluidUtil {
    private static final Logger LOGGER = LogManager.getLogger();

    public static RegistryObject<FlowingFluidBlock> createFluidBlock(String id, Material material, int colour, int viscosity, int density, ResourceLocation stillTexture, ResourceLocation flowingTexture, ResourceLocation overlay) {
        MutableSupplier<ForgeFlowingFluid.Source> sourceFluid = new MutableSupplier<ForgeFlowingFluid.Source>(null);
        MutableSupplier<ForgeFlowingFluid.Flowing> flowingFluid = new MutableSupplier<ForgeFlowingFluid.Flowing>(null);
        RegistryObject<FlowingFluidBlock> block = RegistryHandler.BLOCKS.register(id, () -> new FlowingFluidBlock(flowingFluid, Block.Properties.create(material).doesNotBlockMovement().hardnessAndResistance(100.0F).noDrops()));
        ForgeFlowingFluid.Properties fluidProperties = new ForgeFlowingFluid.Properties(sourceFluid, flowingFluid,
                FluidAttributes.builder(stillTexture, flowingTexture)
                        .overlay(overlay)
                        .translationKey("block." + Wrought.MODID + "." + id)
                        .color(colour)
                        .viscosity(viscosity)
                        .density(density)
        )
                .bucket(RegistryHandler.ITEMS.register(id + "_bucket", () -> new BucketItem(sourceFluid, new Item.Properties().maxStackSize(16))))  // TODO - 16 + Item Group
                .block(block);

        sourceFluid.update(RegistryHandler.FLUIDS.register(id, () -> new ForgeFlowingFluid.Source(fluidProperties)));
        flowingFluid.update(RegistryHandler.FLUIDS.register(id + "_flowing", () -> new ForgeFlowingFluid.Flowing(fluidProperties)));

        // add a tag
        FluidTags.createOptional(block.getId());
        return block;
    }

}
