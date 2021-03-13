package com.thecowking.wrought.inventory.slots;

import com.thecowking.wrought.init.RecipeSerializerInit;
import com.thecowking.wrought.recipes.HoneyCombCokeOven.HoneyCombCokeOvenRecipe;
import com.thecowking.wrought.recipes.IWroughtRecipe;
import com.thecowking.wrought.tileentity.MultiBlockControllerTile;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.Set;

import static com.thecowking.wrought.util.InventoryUtils.findRecipesByType;


public class InputFuelHandler extends ItemStackHandler {
    private static final Logger LOGGER = LogManager.getLogger();
    private MultiBlockControllerTile tile;
    private String id;


    public InputFuelHandler(int size, MultiBlockControllerTile tile, InputFuelHandler primary, String id)  {
        super(size);
        this.tile = tile;
        this.id = id;

    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)  {
        if(stack.getBurnTime() > 0)  {
            return super.insertItem(slot, stack, simulate);
        }
        return stack;
    }




}

