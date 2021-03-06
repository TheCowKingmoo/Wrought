package com.thecowking.wrought.recipes.HoneyCombCokeOven;

import com.thecowking.wrought.recipes.IWroughtRecipe;
import com.thecowking.wrought.util.RecipeSerializerInit;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HoneyCombCokeOvenRecipe implements IWroughtRecipe {
    private static final Logger LOGGER = LogManager.getLogger();
    NonNullList<Ingredient> ingredientList;


    private final ResourceLocation id;
    private Ingredient primaryInput;

    private FluidStack fluidStack;
    private final ItemStack primaryOutput;
    private ItemStack secondaryOutput;


    private int burnTime = 0;

    public HoneyCombCokeOvenRecipe(ResourceLocation id, Ingredient primaryInput, ItemStack primaryOutput, ItemStack secondaryOutput, FluidStack fluidStack, int burnTime) {
        this.id = id;
        this.primaryInput = primaryInput;
        this.primaryOutput = primaryOutput;
        this.secondaryOutput = secondaryOutput;
        this.fluidStack = fluidStack;
        this.burnTime = burnTime;
        ingredientList = NonNullList.create();
        ingredientList.add(primaryInput);
    }

    public ItemStack getPrimaryOutput()  {
        return this.primaryOutput;
    }

    public ItemStack getSecondaryOutput()  {
        return this.secondaryOutput;
    }

    public Ingredient getPrimaryInput()  {return this.primaryInput;}

    public int getNumInput()  {
        return this.ingredientList.size();
    }


    @Override
    public boolean matches(RecipeWrapper inv, World worldIn) {
        return this.primaryInput.test(inv.getStackInSlot(0));
    }

    public boolean matches(ItemStack stack) {
        return this.primaryInput.test(stack);
    }


    @Override
    public ItemStack getCraftingResult(RecipeWrapper inv) {
        return this.primaryOutput;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return null;
    }

    public ItemStack getRecipeItemStackOutput()  {return this.primaryOutput;}
    public FluidStack getRecipeFluidStackOutput()  {return this.fluidStack;}

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RecipeSerializerInit.EXAMPLE_SERIALIZER.get();
    }

    @Override
    public Ingredient getInput() {
        return this.primaryInput;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return ingredientList;
    }

    public int getBurnTime() {
        return burnTime;
    }
}
