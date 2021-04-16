package com.thecowking.wrought.inventory.slots;

import com.thecowking.wrought.tileentity.MultiBlockControllerTile;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.Nullable;

public class SlotFuelInput extends SlotItemHandler {
    MultiBlockControllerTile tile;
    int slotIndex;
    public SlotFuelInput(IItemHandler itemHandler, int index, int xPosition, int yPosition, MultiBlockControllerTile tile) {
        super(itemHandler, index, xPosition, yPosition);
        this.tile = tile;
        this.slotIndex = index;
    }

    @Override
    public boolean isItemValid(@Nullable ItemStack stack)  {
        return this.tile.isValidFuel(stack);
    }

}
