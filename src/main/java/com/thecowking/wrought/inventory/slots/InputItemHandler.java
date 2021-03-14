package com.thecowking.wrought.inventory.slots;

import com.thecowking.wrought.tileentity.MultiBlockControllerTile;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.Set;

import static com.thecowking.wrought.util.InventoryUtils.findRecipesByType;

//Source - TurtWurty - https://www.youtube.com/watch?v=QUxLsZHiyA4&list=PLaevjqy3XufYmltqo0eQusnkKVN7MpTUe&index=48


public class InputItemHandler extends ItemStackHandler {
    private static final Logger LOGGER = LogManager.getLogger();
    private MultiBlockControllerTile tile;
    private InputItemHandler primary;
    private Set<IRecipe<?>> recipes;
    private String id;


    public InputItemHandler(int size, MultiBlockControllerTile tile, InputItemHandler primary, String id)  {
        super(size);
        this.tile = tile;
        this.primary = primary;
        this.id = id;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)  {

        //if( slot > tile.numInputSlots)  {
            if(this.tile.itemUsedInRecipe(stack, slot))  {
                return super.insertItem(slot, stack, simulate);
            }
        //}
        return stack;
    }







}

