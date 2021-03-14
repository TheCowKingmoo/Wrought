package com.thecowking.wrought.recipes.HoneyCombCokeOven;

import com.thecowking.wrought.Wrought;
import com.thecowking.wrought.recipes.IWroughtRecipe;
import com.thecowking.wrought.init.RecipeSerializerInit;
import com.thecowking.wrought.recipes.WroughtRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;


public class HoneyCombCokeOvenRecipe extends WroughtRecipe  {
    private static final Logger LOGGER = LogManager.getLogger();


    public HoneyCombCokeOvenRecipe(ResourceLocation id, List<Ingredient> itemInputs,  List<ItemStack> itemOuputs, List<FluidStack> fluidOutputs, int burnTime) {
        super(id, itemInputs, itemOuputs, fluidOutputs, Ingredient.EMPTY, burnTime, RecipeSerializerInit.HONEY_COMB_SERIALIZER.get(), new ResourceLocation(Wrought.MODID, "honey_comb_coke_oven"));
    }


}