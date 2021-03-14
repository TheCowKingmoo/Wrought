package com.thecowking.wrought.recipes.BlastFurnace;

import com.thecowking.wrought.recipes.WroughtSerializer;
import net.minecraft.util.ResourceLocation;

/*
    Used to read JSON files and convert it into a Recipe
 */
public class BlastFurnaceRecipeSerializer extends WroughtSerializer {

    public BlastFurnaceRecipeSerializer(int numInputs, int numOutputs, int numFluidInputs, int numFluidOutputs, boolean needFuel, boolean needBurnTime, ResourceLocation recipeTypeID) {
        super(numInputs, numOutputs, numFluidInputs, numFluidOutputs, needFuel, needBurnTime, recipeTypeID);
    }
}