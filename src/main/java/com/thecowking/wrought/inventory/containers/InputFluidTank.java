package com.thecowking.wrought.inventory.containers;

import net.minecraft.fluid.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;


public class InputFluidTank extends FluidTank {
    private static final Logger LOGGER = LogManager.getLogger();

    private int maxSize = 0;

    public InputFluidTank(int capacity) {

        super(capacity);
        this.maxSize = capacity;
    }

    public int getCapacityInBuckets()  {
        return this.maxSize / 1000;
    }


    // Override Draining Methods to prevent outside automation from draining it
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action)  {
        return FluidStack.EMPTY;
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        return FluidStack.EMPTY;
    }
}
