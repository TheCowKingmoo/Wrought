package com.thecowking.wrought.init;

import com.thecowking.wrought.Wrought;
import com.thecowking.wrought.util.RegistryHandler;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.ref.WeakReference;
import java.rmi.registry.Registry;

public class FluidInit {
    public static final ResourceLocation CREOSOTE_STILL_RL = new ResourceLocation(Wrought.MODID, "blocks/creosote_still");
    public static final ResourceLocation CREOSOTE_FLOWING_RL = new ResourceLocation(Wrought.MODID, "blocks/creosote_flowing");
    public static final ResourceLocation CREOSOTE_OVERLAY_RL = new ResourceLocation(Wrought.MODID, "blocks/creosote_overlay");



    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, Wrought.MODID);

    public static final RegistryObject<FlowingFluid> CREOSOTE_FLUID = FLUIDS.register("creosote_fluid", () -> new ForgeFlowingFluid.Source(FluidInit.CREOSOTE_PROPERTIES));
    public static final RegistryObject<FlowingFluid> CREOSOTE_FLOWING = FLUIDS.register("creosote_flowing", () -> new ForgeFlowingFluid.Flowing(FluidInit.CREOSOTE_PROPERTIES));

    public static final ForgeFlowingFluid.Properties CREOSOTE_PROPERTIES = new ForgeFlowingFluid.Properties(() -> CREOSOTE_FLUID.get(),
            () -> CREOSOTE_FLOWING.get(),
            FluidAttributes.builder(CREOSOTE_STILL_RL, CREOSOTE_FLOWING_RL).density(5).luminosity(10).overlay(CREOSOTE_OVERLAY_RL)).block(() -> FluidInit.CREOSOTE_BLOCK.get());


    public static final RegistryObject<FlowingFluidBlock> CREOSOTE_BLOCK = RegistryHandler.BLOCKS.register("creosote",
            () -> new FlowingFluidBlock(() -> FluidInit.CREOSOTE_FLUID.get(), Block.Properties.create(Material.WATER).doesNotBlockMovement().hardnessAndResistance(100.0f).noDrops()));

}
