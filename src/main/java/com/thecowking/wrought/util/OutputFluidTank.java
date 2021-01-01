package com.thecowking.wrought.util;

import net.minecraft.fluid.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OutputFluidTank extends FluidTank {
    private static final Logger LOGGER = LogManager.getLogger();



    public OutputFluidTank(int capacity) {
        super(capacity);
    }
    @Override
    public int fill(FluidStack resource, FluidAction action)
    {return 0;}

    public FluidStack internalFill(FluidStack resource, FluidAction action)  {
        Fluid f = resource.getFluid();
        LOGGER.info(f);
        LOGGER.info(resource.getAmount());

        int amountLeft = super.fill(resource, action);
        LOGGER.info(amountLeft);

        if(amountLeft != 0)  {
            return new FluidStack(f, amountLeft);
        }
        return FluidStack.EMPTY;
    }
}
