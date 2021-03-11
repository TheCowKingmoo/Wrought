package com.thecowking.wrought.recipes.BlastFurnace;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.thecowking.wrought.recipes.HoneyCombCokeOven.HoneyCombCokeOvenRecipe;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
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
public class BlastFurnaceRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>>
        implements IRecipeSerializer<BlastFurnaceRecipe> {

    private static final Logger LOGGER = LogManager.getLogger();


    /*
        Read the JSON File
     */

    @Override
    public BlastFurnaceRecipe read(ResourceLocation recipeId, JsonObject json) {
        // get outputs
        ItemStack primaryItemOutput = CraftingHelper.getItemStack(JSONUtils.getJsonObject(json, "primary_item_output"), true);
        ItemStack secondaryItemOutput = CraftingHelper.getItemStack(JSONUtils.getJsonObject(json, "secondary_item_output"), true);
        ItemStack trinaryItemOutputOutput = CraftingHelper.getItemStack(JSONUtils.getJsonObject(json, "trinary_item_output"), true);

        ArrayList<ItemStack> itemOutputs = new ArrayList<>();
        if(primaryItemOutput != null && primaryItemOutput != ItemStack.EMPTY)  {
            itemOutputs.add(primaryItemOutput);
            if(secondaryItemOutput != null && secondaryItemOutput != ItemStack.EMPTY)  {
                itemOutputs.add(secondaryItemOutput);
                if(trinaryItemOutputOutput != null && trinaryItemOutputOutput != ItemStack.EMPTY)  {
                    itemOutputs.add(trinaryItemOutputOutput);
                }
            }
        }


        // get inputs
        Ingredient primaryItemInput = Ingredient.deserialize(JSONUtils.getJsonObject(json, "primary_item_input"));
        if (primaryItemInput == null || primaryItemInput.equals(Items.AIR)) {
            throw new JsonSyntaxException("Unknown Primary Input: " + primaryItemInput);
        }

        Ingredient secondaryItemInput = Ingredient.deserialize(JSONUtils.getJsonObject(json, "secondary_item_input"));
        Ingredient trinaryItemInput = Ingredient.deserialize(JSONUtils.getJsonObject(json, "primary_item_input"));

        ArrayList<Ingredient> itemInputs  = new ArrayList<>();
        itemInputs.add(primaryItemInput);
        if (secondaryItemInput != null && !(secondaryItemInput.equals(Items.AIR))) {
            itemInputs.add(secondaryItemInput);
            if (trinaryItemInput != null && !(trinaryItemInput.equals(Items.AIR)))  {
                itemInputs.add(trinaryItemInput);
            }
        }

        // Fuel
        Ingredient fuel = Ingredient.deserialize(JSONUtils.getJsonObject(json, "fuel"));

        // get fluid
        ResourceLocation primaryFluidOutputID = new ResourceLocation(JSONUtils.getString(json, "primaryFluidOutput"));
        int primaryFluidOutputAmount = JSONUtils.getInt(json, "primaryFluidOutputAmount");
        FluidStack primaryFluidStackOutput = getFluidStackFromID(primaryFluidOutputID, primaryFluidOutputAmount);

        ResourceLocation secondaryFluidOutputID = new ResourceLocation(JSONUtils.getString(json, "secondaryFluidOutput"));
        int secondaryFluidOutputAmount = JSONUtils.getInt(json, "secondaryFluidOutputAmount");
        FluidStack secondaryFluidStackOutput = getFluidStackFromID(secondaryFluidOutputID, secondaryFluidOutputAmount);

        ArrayList<FluidStack> fluidOutputs = new ArrayList<>();

        if(primaryFluidStackOutput != FluidStack.EMPTY)  {
            fluidOutputs.add(primaryFluidStackOutput);
            if(secondaryFluidStackOutput != FluidStack.EMPTY)  {
                fluidOutputs.add(secondaryFluidStackOutput);
            }
        }

        // get burn time
        int burnTime = JSONUtils.getInt(json, "burntime");

        return new BlastFurnaceRecipe(recipeId, itemInputs, itemOutputs, fluidOutputs, fuel, burnTime);
    }


    public FluidStack getFluidStackFromID(ResourceLocation fluidID, int amount)  {
        FluidStack fluidStack = FluidStack.EMPTY;
        if(amount != 0)  {
            Fluid fluid = ForgeRegistries.FLUIDS.getValue(fluidID);
            if (fluid == null || fluid == FluidStack.EMPTY.getFluid()) {
                throw new JsonSyntaxException("Unknown fluid: " + fluidID);
            }
            fluidStack = new FluidStack(fluid, amount);
        }
        return fluidStack;
    }



    /*
        Read information from the server
     */
    public BlastFurnaceRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {

        ItemStack primaryItemOutput = buffer.readItemStack();
        ItemStack secondaryItemOutput = buffer.readItemStack();
        ItemStack trinaryItemOutput = buffer.readItemStack();

        List<ItemStack> itemOutputs = new ArrayList<>();
        if(primaryItemOutput != null && primaryItemOutput != ItemStack.EMPTY)  {
            itemOutputs.add(primaryItemOutput);
            if(secondaryItemOutput != null && secondaryItemOutput != ItemStack.EMPTY)  {
                itemOutputs.add(secondaryItemOutput);
                if(trinaryItemOutput != null && trinaryItemOutput!= ItemStack.EMPTY)  {
                    itemOutputs.add(trinaryItemOutput);
                }
            }
        }

        Ingredient fuel = Ingredient.read(buffer);

        Ingredient primaryItemInput = Ingredient.read(buffer);
        Ingredient secondaryItemInput = Ingredient.read(buffer);
        Ingredient trinaryItemInput = Ingredient.read(buffer);


        ArrayList<Ingredient> itemInputs  = new ArrayList<>();
        itemInputs.add(primaryItemInput);
        if (secondaryItemInput != null && !(secondaryItemInput.equals(Items.AIR))) {
            itemInputs.add(secondaryItemInput);
            if (trinaryItemInput != null && !(trinaryItemInput.equals(Items.AIR)))  {
                itemInputs.add(trinaryItemInput);
            }
        }


        ArrayList<FluidStack> fluidOutputs = new ArrayList<>();

        FluidStack primaryFluidStackOutput = buffer.readFluidStack();
        FluidStack secondaryFluidStackOutput = buffer.readFluidStack();

        if(primaryFluidStackOutput != FluidStack.EMPTY)  {
            fluidOutputs.add(primaryFluidStackOutput);
            if(secondaryFluidStackOutput != FluidStack.EMPTY)  {
                fluidOutputs.add(secondaryFluidStackOutput);
            }
        }

        int burnTime = buffer.readInt();
        return new BlastFurnaceRecipe(recipeId, itemInputs, itemOutputs, fluidOutputs, fuel, burnTime);
    }

    @Override
    public void write(PacketBuffer buffer, BlastFurnaceRecipe recipe) {
        List<ItemStack> itemOutputs = recipe.getItemOuputs();
        for(int i = 0; i < itemOutputs.size(); i++)  {
            buffer.writeItemStack(itemOutputs.get(i));
        }

        recipe.getFuel().write(buffer);

        List<Ingredient> itemInputs = recipe.getItemInputs();
        for(int i = 0; i < itemInputs.size(); i++)  {
            itemInputs.get(i).write(buffer);
        }

        List<FluidStack> fluidOutputs = recipe.getFluidOutputs();
        for(int i = 0; i < fluidOutputs.size(); i++)  {
            buffer.writeFluidStack(fluidOutputs.get(i));
        }

        buffer.writeInt(recipe.getBurnTime());

    }
}