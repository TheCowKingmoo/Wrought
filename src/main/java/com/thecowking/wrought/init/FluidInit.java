package com.thecowking.wrought.init;

import com.thecowking.wrought.Wrought;
import com.thecowking.wrought.util.MutableSupplier;
import com.thecowking.wrought.util.RenderHelper;
import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;

import java.util.ArrayList;
import java.util.List;

public class FluidInit {





    public static void createNewMoltenMetalFluid()  {
        List<String> metals = new ArrayList<>();
        metals.add("iron");

        for(int i = 0; i < metals.size(); i++)  {
            String metal = metals.get(i);
            createFluidBlock(metal, Material.LAVA, RenderHelper.convertARGBToInt(255,0,0,0.5), 1200, 1200);
        }
    }


    // Source - https://github.com/Tslat/Advent-Of-Ascension/blob/1.15.2/source/util/BlockUtil.java
    public static RegistryObject<FlowingFluidBlock> createFluidBlock(String id, Material material, int colour, int viscosity, int density) {
        return createFluidBlock(id, material, colour, viscosity, density, new ResourceLocation("block/" + id + "_still"), new ResourceLocation("block/" + id + "_flow"), new ResourceLocation("block/" + id + "_overlay"));
    }

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

        return block;
    }




    /*
                ForgeFlowingFluid.Properties properties = null;

            RegistryObject<FlowingFluid> MOLTEN_FLUID = RegistryHandler.FLUIDS.register(metal + "_fluid", () -> new ForgeFlowingFluid.Source(null));
            RegistryObject<FlowingFluidBlock> MOLTEN_BLOCK = RegistryHandler.BLOCKS.register(metal,
                    () -> new FlowingFluidBlock(() -> MOLTEN_FLUID.get(), Block.Properties.create(Material.LAVA).doesNotBlockMovement().hardnessAndResistance(100.0f).noDrops()));
            RegistryObject<FlowingFluid> MOLTEN_FLOWING = RegistryHandler.FLUIDS.register(metal + "_flowing", () -> null);

            //Molten Iron Bucket
            RegistryObject<BucketItem> MOLTEN_BUCKET = RegistryHandler.ITEMS.register("molten_" + metal + "_bucket", () -> new BucketItem(() -> MOLTEN_FLUID.get(), new Item.Properties().maxStackSize(1)) );

            properties = new ForgeFlowingFluid.Properties(() -> MOLTEN_FLUID.get(),
                    () -> MOLTEN_FLOWING.get(),
                    FluidAttributes.builder(STILL_RL, FLOWING_RL)
                            .translationKey(name)
                            .density(5)
                            .color(RenderHelper.convertARGBToInt(255,255,255, 0.5))
                            .luminosity(10)
                            .overlay(OVERLAY_RL))
                    .bucket(MOLTEN_BUCKET)
                    .block(() -> MOLTEN_BLOCK.get()).bucket(() -> RegistryHandler.CREOSOTE_BUCKET.get());
     */



}
