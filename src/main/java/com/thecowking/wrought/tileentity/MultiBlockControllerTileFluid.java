package com.thecowking.wrought.tileentity;

import com.thecowking.wrought.data.IMultiblockData;
import com.thecowking.wrought.inventory.containers.OutputFluidTank;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fluids.FluidStack;

public class MultiBlockControllerTileFluid extends MultiBlockControllerTile {

    // main tank
    protected OutputFluidTank fluidTank;

    public MultiBlockControllerTileFluid(TileEntityType<?> tileEntityTypeIn, IMultiblockData data, int tankCapacity) {
        super(tileEntityTypeIn, data);
        //init tank
        this.fluidTank = new OutputFluidTank(tankCapacity);
    }
    public FluidStack getFluidInTank()  {return fluidTank.getFluid();}
    public int getTankMaxSize()  {
        return fluidTank.getCapacity();
    }


}
