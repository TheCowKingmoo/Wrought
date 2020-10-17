package com.thecowking.wrought.util;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.Nullable;

public class SlotInputFluidContainer extends SlotItemHandler {
    public SlotInputFluidContainer(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }
    @Override
    public boolean isItemValid(@Nullable ItemStack stack)  {
        // i know this is dumb -> change to detect if item can hold >= 1 buckets worth
        if(stack.getItem() == Items.BUCKET)  {
            return true;
        }
        return false;
    }
}
