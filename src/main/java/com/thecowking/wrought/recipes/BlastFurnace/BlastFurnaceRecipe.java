package com.thecowking.wrought.recipes.BlastFurnace;

import com.thecowking.wrought.Wrought;
import com.thecowking.wrought.recipes.IWroughtRecipe;
import com.thecowking.wrought.init.RecipeSerializerInit;
import com.thecowking.wrought.recipes.WroughtRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nonnull;
import java.util.List;

public class BlastFurnaceRecipe extends WroughtRecipe  {
    public BlastFurnaceRecipe(ResourceLocation id, List<Ingredient> itemInputs,  List<ItemStack> itemOuputs, List<FluidStack> fluidOutputs,
                              Ingredient fuel, int burnTime) {
        super(id, itemInputs, itemOuputs, fluidOutputs, fuel, burnTime, RecipeSerializerInit.BLAST_FURNACE_SERIALIZER.get(), new ResourceLocation(Wrought.MODID, "blast_furnace"));
    }
}
