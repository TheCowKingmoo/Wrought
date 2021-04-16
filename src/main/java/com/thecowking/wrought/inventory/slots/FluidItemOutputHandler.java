package com.thecowking.wrought.inventory.slots;

import com.thecowking.wrought.Wrought;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.logging.Logger;

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
        Wrought.LOGGER.info("inserting " + stack.getTranslationKey() + " into slot " + slot);
        return super.insertItem(slot, stack, simulate);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate)  {
        return super.extractItem(slot, amount, simulate);
    }
}
