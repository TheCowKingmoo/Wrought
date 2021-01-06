package com.thecowking.wrought.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class FluidItemOutputHandler extends ItemStackHandler {
    public FluidItemOutputHandler(int size, ItemStack... stacks) {
        super(size);
    }
    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)  {
        return stack;
    }

    public ItemStack internalInsertItem(int slot, @Nonnull ItemStack stack, boolean simulate)  {
        return super.insertItem(slot, stack, simulate);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate)  {
        return super.extractItem(slot, amount, simulate);
    }
}
