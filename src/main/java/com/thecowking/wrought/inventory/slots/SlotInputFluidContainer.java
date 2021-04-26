package com.thecowking.wrought.inventory.slots;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

public class SlotInputFluidContainer extends SlotItemHandler {
    private static final Logger LOGGER = LogManager.getLogger();

    public SlotInputFluidContainer(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }
    @Override
    public boolean isItemValid(@Nullable ItemStack stack)  {
        //TODO - i know i can just return this - i think i need to add more stuffs
        if(stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent()) {
            return true;
        }
        return false;
    }
}
