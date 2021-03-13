package com.thecowking.wrought.recipes.HoneyCombCokeOven;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.thecowking.wrought.recipes.BlastFurnace.BlastFurnaceRecipe;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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

import java.util.ArrayList;
import java.util.List;


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
        ItemStack primaryItemOutput = CraftingHelper.getItemStack(JSONUtils.getJsonObject(json, "primary_output"), true);
        if (primaryItemOutput == null || primaryItemOutput == ItemStack.EMPTY) {
            throw new JsonSyntaxException("Unknown Primary Output Itemstack: " + primaryItemOutput);
        }
        ItemStack secondaryItemOutput = CraftingHelper.getItemStack(JSONUtils.getJsonObject(json, "secondary_output"), true);

        ArrayList<ItemStack> itemOutputs = new ArrayList<>();
        itemOutputs.add(primaryItemOutput);
        itemOutputs.add(secondaryItemOutput);

        // inputs
        Ingredient primaryItemInput = Ingredient.deserialize(JSONUtils.getJsonObject(json, "primary_input"));
        if (primaryItemInput == null || primaryItemInput.equals(Items.AIR)) {
            throw new JsonSyntaxException("Unknown Primary Input: " + primaryItemInput);
        }

        ArrayList<Ingredient> itemInputs  = new ArrayList<>();
        itemInputs.add(primaryItemInput);

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

        ArrayList<FluidStack> fluidOutputs = new ArrayList<>();
        fluidOutputs.add(fluidStackOutput);


        // get burn time
        int burnTime = JSONUtils.getInt(json, "burntime");
        return new HoneyCombCokeOvenRecipe(recipeId, itemInputs, itemOutputs, fluidOutputs, burnTime);
    }

    /*
        Read information from the server
     */
    public HoneyCombCokeOvenRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {

        // Outputs
        ItemStack primaryItemOutput = buffer.readItemStack();
        ItemStack secondaryItemOutput = buffer.readItemStack();

        List<ItemStack> itemOutputs = new ArrayList<>();
        itemOutputs.add(primaryItemOutput);
        itemOutputs.add(secondaryItemOutput);

        // Inputs
        Ingredient primaryItemInput = Ingredient.read(buffer);
        ArrayList<Ingredient> itemInputs  = new ArrayList<>();
        itemInputs.add(primaryItemInput);

        // Fluid Outputs
        ArrayList<FluidStack> fluidOutputs = new ArrayList<>();

        FluidStack primaryFluidStackOutput = buffer.readFluidStack();
        fluidOutputs.add(primaryFluidStackOutput);

        int burnTime = buffer.readInt();
        return new HoneyCombCokeOvenRecipe(recipeId, itemInputs, itemOutputs, fluidOutputs, burnTime);
    }

    @Override
    public void write(PacketBuffer buffer, HoneyCombCokeOvenRecipe recipe) {
        // Outputs
        List<ItemStack> itemOutputs = recipe.getItemOuputs();
        for(int i = 0; i < itemOutputs.size(); i++)  {
            buffer.writeItemStack(itemOutputs.get(i));
        }

        // Inputs
        List<Ingredient> itemInputs = recipe.getItemInputs();
        for(int i = 0; i < itemInputs.size(); i++)  {
            itemInputs.get(i).write(buffer);
        }


        // Fluid Outputs
        List<FluidStack> fluidOutputs = recipe.getFluidOutputs();
        for(int i = 0; i < fluidOutputs.size(); i++)  {
            buffer.writeFluidStack(fluidOutputs.get(i));
        }

        // Burn
        buffer.writeInt(recipe.getBurnTime());

    }
}