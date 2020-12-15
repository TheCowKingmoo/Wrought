package com.thecowking.wrought.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

import static com.thecowking.wrought.blocks.MultiBlock.Multiblock.*;

public class AutomationCombinedInvWrapper extends CombinedInvWrapper {
    private static final Logger LOGGER = LogManager.getLogger();

    public AutomationCombinedInvWrapper(IItemHandlerModifiable... itemHandler)  {
        super(itemHandler);
    }

    @Override
    @Nonnull
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
    {
        // Note - outputs will not let anything insert into them because of
        // SlotOutput
        int index = getIndexForSlot(slot);
        IItemHandlerModifiable handler = getHandlerFromIndex(index);

        slot = getSlotFromIndex(slot, index);

        if(handler instanceof FluidItemInputHandler)  {

            return ((FluidItemInputHandler)handler).insertItem(slot, stack, simulate);
        }  else if(handler instanceof InputItemHandler)  {
        }

        //if(!isItemValid(slot, stack))  {
        //    return stack;
        //}
        return handler.insertItem(slot, stack, simulate);
    }

    @Override
    @Nonnull
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if(slot == INDEX_ITEM_INPUT || slot == INDEX_FLUID_ITEM_INPUT)  {
            return ItemStack.EMPTY;
        }
        int index = getIndexForSlot(slot);
        IItemHandlerModifiable handler = getHandlerFromIndex(index);
        slot = getSlotFromIndex(slot, index);
        return handler.extractItem(slot, amount, simulate);
    }


}
