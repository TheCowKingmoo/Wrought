package com.thecowking.wrought.inventory.containers;

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
        if(resource == FluidStack.EMPTY)  {return FluidStack.EMPTY;}
        Fluid f = resource.getFluid();
        LOGGER.info("fluid = " + f);
        LOGGER.info("fluid amount to fill" + resource.getAmount());

        int amountLeft = super.fill(resource, action);
        LOGGER.info("amount left = " + amountLeft);

        if(amountLeft != 0)  {
            return new FluidStack(f, amountLeft);
        }
        return FluidStack.EMPTY;
    }
}
