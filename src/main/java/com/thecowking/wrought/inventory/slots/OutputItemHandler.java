package com.thecowking.wrought.inventory.slots;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class OutputItemHandler extends ItemStackHandler {
    public OutputItemHandler(int size, ItemStack... stacks) {
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
