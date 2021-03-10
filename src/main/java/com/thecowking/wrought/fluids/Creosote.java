package com.thecowking.wrought.fluids;

import com.thecowking.wrought.Wrought;
import com.thecowking.wrought.init.FluidInit;
import com.thecowking.wrought.init.RegistryHandler;
import com.thecowking.wrought.util.RenderHelper;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Creosote extends ForgeFlowingFluid {

    public static final ResourceLocation CREOSOTE_STILL_RL = new ResourceLocation(Wrought.MODID, "blocks/creosote_still");
    public static final ResourceLocation CREOSOTE_FLOWING_RL = new ResourceLocation(Wrought.MODID, "blocks/creosote_flowing");
    public static final ResourceLocation CREOSOTE_OVERLAY_RL = new ResourceLocation(Wrought.MODID, "blocks/creosote_overlay");


    public static final ForgeFlowingFluid.Properties CREOSOTE_PROPERTIES = new ForgeFlowingFluid.Properties(() -> RegistryHandler.CREOSOTE_FLUID.get(),
            () -> RegistryHandler.CREOSOTE_FLOWING.get(),
            FluidAttributes.builder(CREOSOTE_STILL_RL, CREOSOTE_FLOWING_RL)
                    .translationKey("Creosote")
                    .density(5)
                    .color(RenderHelper.convertARGBToInt(255,255,61, 0.5))
                    .luminosity(10)
                    .overlay(CREOSOTE_OVERLAY_RL))
            .block(() -> RegistryHandler.CREOSOTE_BLOCK.get()).bucket(() -> RegistryHandler.CREOSOTE_BUCKET.get());


    public Creosote() {
        super(CREOSOTE_PROPERTIES);
    }

    @Override
    public boolean isSource(FluidState state) {
        return false;
    }

    @Override
    public int getLevel(FluidState state) {
        return state.get(LEVEL_1_8);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Fluid, FluidState> builder) {
        super.fillStateContainer(builder);
        builder.add(LEVEL_1_8);
    }
}
