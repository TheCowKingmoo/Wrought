package com.thecowking.wrought.inventory.slots;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

public class FluidItemInputHandler extends ItemStackHandler {
    private static final Logger LOGGER = LogManager.getLogger();

    public FluidItemInputHandler(int size, ItemStack... stacks) {
        super(size);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)  {
        if(stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent()) {
            return super.insertItem(slot, stack, simulate);
        }
        return stack;
    }
}
