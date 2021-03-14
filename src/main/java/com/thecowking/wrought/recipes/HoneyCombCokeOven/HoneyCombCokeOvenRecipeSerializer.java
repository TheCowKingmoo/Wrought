package com.thecowking.wrought.recipes.HoneyCombCokeOven;

import com.thecowking.wrought.Wrought;
import com.thecowking.wrought.recipes.WroughtSerializer;
import net.minecraft.util.ResourceLocation;


/*
    Used to read JSON files and convert it into a Recipe
 */
public class HoneyCombCokeOvenRecipeSerializer extends WroughtSerializer  {

    public HoneyCombCokeOvenRecipeSerializer() {
        super(1, 2, 0, 1, false, true, new ResourceLocation(Wrought.MODID, "honey_comb_coke_oven"));
    }
}