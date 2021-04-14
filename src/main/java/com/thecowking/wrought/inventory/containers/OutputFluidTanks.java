package com.thecowking.wrought.inventory.containers;

import com.ibm.icu.util.Output;
import net.minecraft.fluid.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;


/*
    Class based off of the FluidTank Class
    Essentially its a wrapper for multiple FluidTanks to expose all those tanks under one FluidHandler
 */

public class OutputFluidTanks implements IFluidHandler {
    private static final Logger LOGGER = LogManager.getLogger();

    private int maxSize = 0;
    private int numTanks;
    private FluidTank[] tanks;
    private int maxDrain = 1000;

    public OutputFluidTanks(int capacity, int numTanks) {
        this.maxSize = capacity;
        this.numTanks = numTanks;
        tanks = new FluidTank[numTanks];
        for(int i = 0; i < numTanks; i++)  {
            tanks[i] = new FluidTank(capacity);
        }
    }

    public OutputFluidTanks(FluidTank[] inputTanks)  {
        this.numTanks = inputTanks.length;
        this.tanks = inputTanks;
    }

    @Override
    public int getTanks() {
        return numTanks;
    }

    public FluidTank getFluidTank(int index)  {
        return this.tanks[index];
    }

    @NotNull
    @Override
    public FluidStack getFluidInTank(int index) {
        return tanks[index].getFluid();
    }

    @Override
    public int getTankCapacity(int index) {
        return tanks[index].getCapacity();
    }

    // output tank so we never accept
    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return false;
    }

    // output tank so we never accept
    @Override
    public int fill(FluidStack resource, FluidAction action)
    {return 0;}


    @NotNull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        for(int i = 0; i < this.numTanks; i++)  {
            if (resource.isEmpty() || !resource.isFluidEqual(getFluidInTank(i)))  {
                continue;
            }
            int drained = maxDrain;
            FluidStack fluid = getFluidInTank(i);
            if (fluid.getAmount() < drained)  {
                drained = fluid.getAmount();
            }
            FluidStack stack = new FluidStack(fluid, drained);
            if (action.execute() && drained > 0)  {
                fluid.shrink(drained);
                onContentsChanged();
            }
            if(stack != FluidStack.EMPTY)  return stack;

            //return drain(resource.getAmount(), action);
        }
        return FluidStack.EMPTY;
    }

    @NotNull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        for(int i = 0; i < this.numTanks; i++)  {
            int drained = maxDrain;
            FluidStack fluid = getFluidInTank(i);
            if (fluid.getAmount() < drained)
            {
                drained = fluid.getAmount();
            }
            FluidStack stack = new FluidStack(fluid, drained);
            if (action.execute() && drained > 0)  {
                fluid.shrink(drained);
                onContentsChanged();
            }
            if(stack != FluidStack.EMPTY)  return stack;
        }
        return FluidStack.EMPTY;
    }

    protected void onContentsChanged()  {

    }


    public int getCapacityInBuckets()  {
        return this.maxSize / 1000;
    }


    public FluidStack internalFill(FluidStack resource, FluidAction action, int tankIndex)  {
        if(resource == FluidStack.EMPTY)  {return FluidStack.EMPTY;}
        int amountToFill = resource.getAmount();
        //LOGGER.info("amount to insert = " + resource.getAmount());
        Fluid f = resource.getFluid();

        int amountFilled = tanks[tankIndex].fill(resource, action);
        int amountLeft = amountToFill - amountFilled;

        if(amountLeft != 0)  {
            //LOGGER.info("insert amoutn left = " + amountLeft);
            return new FluidStack(f, amountLeft);
        }
        return FluidStack.EMPTY;
    }
}
