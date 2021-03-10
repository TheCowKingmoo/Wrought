package com.thecowking.wrought.fluids;

import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.state.StateContainer;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public class MoltenMetalFluid extends ForgeFlowingFluid {
    private final float EXPLOSION_RESIST = 100.0f;
    private final int TICK_RATE = 60;

    public MoltenMetalFluid(Properties properties) {
        super(properties);
    }

//    public Properties(Supplier<? extends Fluid> still, Supplier<? extends Fluid> flowing, FluidAttributes.Builder attributes)

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
