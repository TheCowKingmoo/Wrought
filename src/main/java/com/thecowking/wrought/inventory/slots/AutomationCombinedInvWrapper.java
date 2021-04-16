package com.thecowking.wrought.inventory.slots;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

import static com.thecowking.wrought.data.MultiblockData.*;

public class AutomationCombinedInvWrapper extends CombinedInvWrapper {

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
            return handler.insertItem(slot, stack, simulate);
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
        int index = getIndexForSlot(slot);
        IItemHandlerModifiable handler = getHandlerFromIndex(index);
        if(handler instanceof OutputItemHandler || handler instanceof FluidItemOutputHandler)  {
            slot = getSlotFromIndex(slot, index);
            return handler.extractItem(slot, amount, simulate);
        }
        return ItemStack.EMPTY;
    }


}
