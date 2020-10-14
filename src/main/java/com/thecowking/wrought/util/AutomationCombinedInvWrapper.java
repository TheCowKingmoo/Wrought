package com.thecowking.wrought.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import javax.annotation.Nonnull;

import static com.thecowking.wrought.blocks.Multiblock.INDEX_ITEM_INPUT;
import static com.thecowking.wrought.blocks.Multiblock.INDEX_ITEM_OUTPUT;

public class AutomationCombinedInvWrapper extends CombinedInvWrapper {
    public AutomationCombinedInvWrapper(IItemHandlerModifiable... itemHandler)  {
        super(itemHandler);
    }

    @Override
    @Nonnull
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
    {
        if(slot == INDEX_ITEM_OUTPUT)  {
            return stack;
        }
        int index = getIndexForSlot(slot);
        IItemHandlerModifiable handler = getHandlerFromIndex(index);

        slot = getSlotFromIndex(slot, index);
        return handler.insertItem(slot, stack, simulate);
    }

    @Override
    @Nonnull
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if(slot == INDEX_ITEM_INPUT)  {
            return ItemStack.EMPTY;
        }
        int index = getIndexForSlot(slot);
        IItemHandlerModifiable handler = getHandlerFromIndex(index);
        slot = getSlotFromIndex(slot, index);
        return handler.extractItem(slot, amount, simulate);
    }


}
