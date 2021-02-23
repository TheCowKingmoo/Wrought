package com.thecowking.wrought.blocks.MultiBlock.honey_comb_coke_oven;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IIntArray;
import net.minecraftforge.fluids.FluidStack;

/*
  Source - https://github.com/TheGreyGhost/MinecraftByExample/blob/master/src/main/java/minecraftbyexample/mbe31_inventory_furnace/FurnaceStateData.java
 */


public class HCStateData implements IIntArray {

    public int timeElapsed = 0;
    public int timeComplete = 1;
    public FluidStack fluidStack;


    private final int ELAPSED_INDEX = 0;
    private final int COMPLETE_INDEX = 1;
    private final int FLUID_STACK_INDEX = 2;

    public HCStateData()  {
    }


    public void putIntoNBT(CompoundNBT nbtTagCompound) {
        nbtTagCompound.putInt("timeElapsed", timeElapsed);
        nbtTagCompound.putInt("CookTimeForCompletion", timeComplete);
    }

    public void readFromNBT(CompoundNBT nbtTagCompound) {
        // Trim the arrays (or pad with 0) to make sure they have the correct number of elements
        timeElapsed = nbtTagCompound.getInt("CookTimeElapsed");
        timeComplete = nbtTagCompound.getInt("CookTimeForCompletion");
    }

    @Override
    public int get(int index) {
        switch (index) {
            case ELAPSED_INDEX:
                return timeElapsed;
            case COMPLETE_INDEX:
                return timeComplete;
        }
        return 0;
    }

    @Override
    public void set(int index, int value) {

        switch (index) {
            case ELAPSED_INDEX:
                timeElapsed = value;
                break;
            case COMPLETE_INDEX:
                timeComplete = value;
                break;
        }
    }

    @Override
    public int size() {
        return 2;
    }
}


