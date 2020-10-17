package com.thecowking.wrought.util;

import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemHandlerHelper;

public class InventoryUtils {
    public static boolean canItemsStack(ItemStack a, ItemStack b) {
        // Determine if the item stacks can be merged
        if (a.isEmpty() || b.isEmpty()) return true;
        return ItemHandlerHelper.canItemStacksStack(a, b) && a.getCount() + b.getCount() <= a.getMaxStackSize();
    }

    /*
      Used to move fluid out of a container into a machines fluid tank
     */
    public static boolean canAcceptFluidContainer(ItemStack input, ItemStack output, FluidStack fluidStack, FluidTank fluidTank)  {
        return !fluidStack.isEmpty()
                && fluidTank.isFluidValid(0, fluidStack)
                && fluidTank.fill(fluidStack, IFluidHandler.FluidAction.SIMULATE) == 1000
                && (output.isEmpty() || InventoryUtils.canItemsStack(input.getContainerItem(), output))
                && (output.isEmpty() || output.getCount() < output.getMaxStackSize());
    }

    public static ItemStack fillBucketOrFluidContainer(ItemStack emptyContainer, FluidStack fluidStack) {
        Item item = emptyContainer.getItem();
        if (item instanceof BucketItem) {
            return new ItemStack(fluidStack.getFluid().getFilledBucket());
        }
        return ItemStack.EMPTY;
    }
}
