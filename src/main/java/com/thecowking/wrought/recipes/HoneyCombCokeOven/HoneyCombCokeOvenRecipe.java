package com.thecowking.wrought.recipes.HoneyCombCokeOven;

import com.thecowking.wrought.recipes.WroughtRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class HoneyCombCokeOvenRecipe extends WroughtRecipe {
    public HoneyCombCokeOvenRecipe(ResourceLocation id, List<Ingredient> itemInputs, List<Ingredient> itemOuputsIngredient, List<FluidStack> fluidOutputs, List<FluidStack> fluidInputs, Ingredient fuel, int burnTime, int heat, ResourceLocation recipeTypeID) {
        super(id, itemInputs, itemOuputsIngredient, fluidOutputs, fluidInputs, fuel, burnTime, heat, recipeTypeID);
    }
}
