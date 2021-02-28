package com.thecowking.wrought.recipes.HoneyCombCokeOven;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
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
        // get outputs
        ItemStack primaryOutput = CraftingHelper.getItemStack(JSONUtils.getJsonObject(json, "primary_output"), true);
        if (primaryOutput == null || primaryOutput == ItemStack.EMPTY) {
            throw new JsonSyntaxException("Unknown Primary Output Itemstack: " + primaryOutput);
        }
        ItemStack secondaryOutput = CraftingHelper.getItemStack(JSONUtils.getJsonObject(json, "secondary_output"), true);
        // get inputs
        Ingredient primaryInput = Ingredient.deserialize(JSONUtils.getJsonObject(json, "primary_input"));
        if (secondaryOutput == null || secondaryOutput == ItemStack.EMPTY) {
            throw new JsonSyntaxException("Unknown Primary Input: " + primaryInput);
        }
        // get fluid
        ResourceLocation fluidId = new ResourceLocation(JSONUtils.getString(json, "fluid"));
        // get amount of fluid
        int fluidAmount = JSONUtils.getInt(json, "fluidamount");

        // create fluid stack
        FluidStack fluidStackOutput = FluidStack.EMPTY;
        if(fluidAmount != 0)  {
            Fluid fluid = ForgeRegistries.FLUIDS.getValue(fluidId);
            if (fluid == null || fluid == FluidStack.EMPTY.getFluid()) {
                throw new JsonSyntaxException("Unknown fluid: " + fluidId);
            }
            fluidStackOutput = new FluidStack(fluid, fluidAmount);
        }

        // get burn time
        int burnTime = JSONUtils.getInt(json, "burntime");
        return new HoneyCombCokeOvenRecipe(recipeId, primaryInput, primaryOutput, secondaryOutput, fluidStackOutput, burnTime);
    }

    /*
        Read information from the server
     */
    public HoneyCombCokeOvenRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {

        ItemStack primaryOutput = buffer.readItemStack();
        ItemStack secondaryOutput = buffer.readItemStack();

        LOGGER.info("read with output of  -> " + primaryOutput );

        FluidStack fluidStack = buffer.readFluidStack();
        int burntime = buffer.readInt();
        Ingredient primaryInput = Ingredient.read(buffer);
        return new HoneyCombCokeOvenRecipe(recipeId, primaryInput, primaryOutput, secondaryOutput, fluidStack, burntime);
    }

    @Override
    public void write(PacketBuffer buffer, HoneyCombCokeOvenRecipe recipe) {
        Ingredient primaryInput = recipe.getIngredients().get(0);

        primaryInput.write(buffer);
        buffer.writeItemStack(recipe.getPrimaryOutput());
        buffer.writeItemStack(recipe.getSecondaryOutput());
        buffer.writeFluidStack(recipe.getRecipeFluidStackOutput());
        buffer.writeInt(recipe.getBurnTime());
    }
}