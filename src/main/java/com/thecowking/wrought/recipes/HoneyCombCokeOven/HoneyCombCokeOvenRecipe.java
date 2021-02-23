package com.thecowking.wrought.recipes.HoneyCombCokeOven;

import com.thecowking.wrought.util.MultiStack;
import com.thecowking.wrought.util.RecipeSerializerInit;
import net.minecraft.fluid.Fluid;
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

import java.util.List;

public class HoneyCombCokeOvenRecipe implements IHoneyCombCokeOvenRecipe {
    private static final Logger LOGGER = LogManager.getLogger();


    private final ResourceLocation id;
    private Ingredient input;
    private FluidStack fluidStack;
    private final ItemStack primaryOutput;
    private ItemStack secondaryOutput;


    private int burnTime = 0;

    public HoneyCombCokeOvenRecipe(ResourceLocation id, Ingredient input, ItemStack primaryOutput, ItemStack secondaryOutput, FluidStack fluidStack, int burnTime) {
        this.id = id;
        this.primaryOutput = primaryOutput;
        this.secondaryOutput = secondaryOutput;
        this.input = input;
        this.fluidStack = fluidStack;
        this.burnTime = burnTime;
    }

    public ItemStack getPrimaryOutput()  {
        return this.primaryOutput;
    }

    public ItemStack getSecondaryOutput()  {
        return this.secondaryOutput;
    }

    @Override
    public boolean matches(RecipeWrapper inv, World worldIn) {
        return this.input.test(inv.getStackInSlot(0));
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
        return this.input;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.from(null, this.input);
    }

    public int getBurnTime() {
        return burnTime;
    }
}
