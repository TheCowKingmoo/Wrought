package com.thecowking.wrought.recipes.HoneyCombCokeOven;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/*
    Used to read JSON files and convert it into a Recipe
 */
public class HoneyCombCokeOvenRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>>
        implements IRecipeSerializer<HoneyCombCokeOvenRecipe> {

    private static final Logger LOGGER = LogManager.getLogger();


    /*
        Read the JSON File
     */

    @Override
    public HoneyCombCokeOvenRecipe read(ResourceLocation recipeId, JsonObject json) {
        ItemStack output = CraftingHelper.getItemStack(JSONUtils.getJsonObject(json, "output"), true);
        Ingredient input = Ingredient.deserialize(JSONUtils.getJsonObject(json, "input"));
        ResourceLocation fluidId = new ResourceLocation(JSONUtils.getString(json, "fluid"));
        int fluidAmount = JSONUtils.getInt(json, "fluidamount");
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(fluidId);
        if (fluid == null || fluid == FluidStack.EMPTY.getFluid()) {
            throw new JsonSyntaxException("Unknown fluid: " + fluidId);
        }
        FluidStack fluidStackOutput = new FluidStack(fluid, fluidAmount);
        int burnTime = JSONUtils.getInt(json, "burntime");
        return new HoneyCombCokeOvenRecipe(recipeId, input, output, fluidStackOutput, burnTime);
    }

    /*
        Read information from the server - TODO -> fluids
     */
    public HoneyCombCokeOvenRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {

        ItemStack output = buffer.readItemStack();
        LOGGER.info("read with output of  -> " + output );

        FluidStack fluidStack = buffer.readFluidStack();
        int burntime = buffer.readInt();
        Ingredient input = Ingredient.read(buffer);
        return new HoneyCombCokeOvenRecipe(recipeId, input, output, fluidStack, burntime);
    }

    @Override
    public void write(PacketBuffer buffer, HoneyCombCokeOvenRecipe recipe) {
        Ingredient input = recipe.getIngredients().get(0);
        input.write(buffer);
        buffer.writeItemStack(recipe.getRecipeItemStackOutput());
        buffer.writeFluidStack(recipe.getRecipeFluidStackOutput());
        buffer.writeInt(recipe.getBurnTime());

    }
}