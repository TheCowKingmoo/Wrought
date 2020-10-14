package com.thecowking.wrought.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.Nullable;


// src -https://github.com/Draco18s/ReasonableRealism/blob/1.14.4/src/main/java/com/draco18s/harderores/inventory/SifterContainer.java#L29


public class SlotOutput extends SlotItemHandler {
    public SlotOutput(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }
    @Override
    public boolean isItemValid(@Nullable ItemStack stack)  {return false;}

}
