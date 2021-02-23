package com.thecowking.wrought.inventory.slots;

import com.thecowking.wrought.tileentity.honey_comb_coke_oven.HCCokeOvenControllerTile;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.Set;

//Source - TurtWurty - https://www.youtube.com/watch?v=QUxLsZHiyA4&list=PLaevjqy3XufYmltqo0eQusnkKVN7MpTUe&index=48


public class InputItemHandler extends ItemStackHandler {
    private static final Logger LOGGER = LogManager.getLogger();
    private Set<IRecipe<?>>recipes;
    private HCCokeOvenControllerTile tile;
    public InputItemHandler(int size, HCCokeOvenControllerTile tile)  {
        super(size);
        this.tile = tile;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)  {
        if(stack.getItem() == Items.BUCKET)  {
            return stack;
        }
        return super.insertItem(slot, stack, simulate);
    }


    }

