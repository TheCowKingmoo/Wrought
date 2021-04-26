package com.thecowking.wrought.inventory.containers;

import com.ibm.icu.util.Output;
import com.thecowking.wrought.Wrought;
import com.thecowking.wrought.inventory.WroughtTank;
import net.minecraft.fluid.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;


public class OutputFluidTanks extends WroughtTank {

    public OutputFluidTanks(int capacity, int numTanks) {
        super(capacity, numTanks);
    }

    public OutputFluidTanks(FluidTank[] inputTanks)  {
        super(inputTanks);
    }

    // expose draining to the world

    @NotNull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        for(int i = 0; i < this.numTanks; i++)  {
            if (resource.isEmpty() || !resource.isFluidEqual(getFluidInTank(i)) || isTankEmpty(i))  {
                continue;
            }
            int drained = defaultMaxDrain;
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

    /*
        This is the one most pipes call
     */
    @NotNull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        if(tanksAreEmpty)  return FluidStack.EMPTY;


        for(int i = 0; i < this.numTanks; i++)  {
            if (isTankEmpty(i))  {
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
        }
        tanksAreEmpty = true;
        return FluidStack.EMPTY;
    }

}
