package com.thecowking.wrought.inventory.slots;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class FluidItemInputHandler extends ItemStackHandler {
    public FluidItemInputHandler(int size, ItemStack... stacks) {
        super(size);
    }
    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)  {
        if(stack.getItem() != Items.BUCKET)  {
            return stack;
        }
        return super.insertItem(slot, stack, simulate);
    }
}
