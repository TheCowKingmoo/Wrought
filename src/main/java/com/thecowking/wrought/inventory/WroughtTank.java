package com.thecowking.wrought.inventory;

import net.minecraft.fluid.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;




/*
    Class based off of the FluidTank Class
    Essentially its a wrapper for multiple FluidTanks to expose all those tanks under one FluidHandler
        this is a parent class for input/output
 */



public abstract class WroughtTank implements IFluidHandler {
    protected int maxSize = 0;
    protected int numTanks;
    protected FluidTank[] tanks;
    protected int defaultMaxDrain = 1000;
    protected boolean tanksAreEmpty = true;   // latch for automation exposure so that we don't iterate over every tank every tick

    public WroughtTank(int capacity, int numTanks)  {
        this.maxSize = capacity;
        this.numTanks = numTanks;
        tanks = new FluidTank[numTanks];
        for(int i = 0; i < numTanks; i++)  {
            tanks[i] = new FluidTank(capacity);
        }
    }

    public WroughtTank(FluidTank[] tanks)  {
        this.tanksAreEmpty = true;
        this.numTanks = tanks.length;
        this.tanks = tanks;
        setIfTanksAreEmpty();
    }

    private void setIfTanksAreEmpty()  {
        tanksAreEmpty = true;
        for(int i = 0; i < numTanks; i++)  {
            if(!tanks[i].isEmpty())  {
                tanksAreEmpty = false;
                i = numTanks;
            }
        }
    }

    public boolean isTankEmpty(int index)  {return this.tanks[index].isEmpty();}

    protected void onContentsChanged()  {}

    public FluidTank getFluidTank(int index)  {
        return this.tanks[index];
    }


    public FluidStack internalFill(FluidStack resource, FluidAction action, int tankIndex)  {
        if(resource == FluidStack.EMPTY)  {return FluidStack.EMPTY;}
        int amountToFill = resource.getAmount();
        Fluid f = resource.getFluid();

        int amountFilled = tanks[tankIndex].fill(resource, action);
        int amountLeft = amountToFill - amountFilled;

        if(amountLeft != 0)  {
            tanksAreEmpty = false;
            return new FluidStack(f, amountLeft);
        }
        return FluidStack.EMPTY;
    }

    @Override
    public int getTanks() {
        return this.numTanks;
    }

    @NotNull
    @Override
    public FluidStack getFluidInTank(int tank) {
        return this.tanks[tank].getFluid();
    }

    @Override
    public int getTankCapacity(int tank) {
        return this.tanks[tank].getCapacity();
    }



    // default implementation is to let no filling/draining
    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) { return false; }
    @Override
    public int fill(FluidStack resource, FluidAction action) { return 0; }
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action)  {
        return FluidStack.EMPTY;
    }
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        return FluidStack.EMPTY;
    }
}
