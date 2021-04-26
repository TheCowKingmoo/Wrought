package com.thecowking.wrought.inventory.containers;

import com.thecowking.wrought.inventory.WroughtTank;
import net.minecraftforge.fluids.capability.templates.FluidTank;


public class InputFluidTank extends WroughtTank {
    public InputFluidTank(int capacity, int numTanks)  {
        super(capacity, numTanks);
    }
    public InputFluidTank(FluidTank[] inputTanks)  {
        super(inputTanks);
    }

    // TODO - override filling and valid fluid

}
