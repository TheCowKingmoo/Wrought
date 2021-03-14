package com.thecowking.wrought.inventory.slots;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
        if(stack.getItem() != Items.BUCKET)  {
            LOGGER.info("this isnt a bucket");
            return stack;
        }
        LOGGER.info("this isnt a bucket");

        return super.insertItem(slot, stack, simulate);
    }
}
