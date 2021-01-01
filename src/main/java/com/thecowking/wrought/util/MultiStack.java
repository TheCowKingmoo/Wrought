package com.thecowking.wrought.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class MultiStack {
    private ItemStack itemStack;
    private FluidStack fluidStack;

    public MultiStack(ItemStack itemStack, FluidStack fluidStack)  {
        this.itemStack = itemStack;
        this.fluidStack = fluidStack;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public FluidStack getFluidStack() {
        return fluidStack;
    }
}
