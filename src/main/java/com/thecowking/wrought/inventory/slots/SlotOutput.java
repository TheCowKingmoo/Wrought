package com.thecowking.wrought.inventory.slots;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.Nullable;

public class SlotOutput extends SlotItemHandler {
    public SlotOutput(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }
    @Override
    public boolean isItemValid(@Nullable ItemStack stack)  {return false;}

}
