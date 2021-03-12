package com.thecowking.wrought.inventory.containers;

import net.minecraft.fluid.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;




public class OutputFluidTank extends FluidTank {
    private static final Logger LOGGER = LogManager.getLogger();

    private int maxSize = 0;

    public OutputFluidTank(int capacity) {

        super(capacity);
        this.maxSize = capacity;
    }
    @Override
    public int fill(FluidStack resource, FluidAction action)
    {return 0;}


    public int getCapacityInBuckets()  {
        return this.maxSize / 1000;
    }


    public FluidStack internalFill(FluidStack resource, FluidAction action)  {
        if(resource == FluidStack.EMPTY)  {return FluidStack.EMPTY;}
        int amountToFill = resource.getAmount();
        LOGGER.info("amount to insert = " + resource.getAmount());
        Fluid f = resource.getFluid();

        int amountFilled = super.fill(resource, action);
        int amountLeft = amountToFill - amountFilled;

        if(amountLeft != 0)  {
            LOGGER.info("insert amoutn left = " + amountLeft);
            return new FluidStack(f, amountLeft);
        }
        return FluidStack.EMPTY;
    }
}
