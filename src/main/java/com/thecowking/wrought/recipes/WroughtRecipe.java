package com.thecowking.wrought.recipes;

import com.thecowking.wrought.Wrought;
import com.thecowking.wrought.init.RecipeSerializerInit;
import com.thecowking.wrought.recipes.BlastFurnace.BlastFurnaceRecipe;
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

public class WroughtRecipe implements IWroughtRecipe {

    protected ResourceLocation id;
    protected List<Ingredient> itemInputs;
    protected List<ItemStack> itemOuputs;
    protected List<FluidStack> fluidOutputs;
    protected Ingredient fuel;                  // if this is empty than any burnable thing will do
    protected int burnTime = 0;
    protected IRecipeSerializer<?>  seralizer;
    protected ResourceLocation recipeTypeID;

    public WroughtRecipe(ResourceLocation id, List<Ingredient> itemInputs,  List<ItemStack> itemOuputs, List<FluidStack> fluidOutputs,
                              Ingredient fuel, int burnTime, IRecipeSerializer<?>  seralizer, ResourceLocation recipeTypeID) {
        this.id = id;
        this.itemInputs = itemInputs;
        this.itemOuputs = itemOuputs;
        this.fluidOutputs = fluidOutputs;
        this.fuel = fuel;
        if(fuel == null)  {
            this.fuel = Ingredient.EMPTY;
        }
        this.burnTime = burnTime;
        this.seralizer = seralizer;
        this.recipeTypeID = recipeTypeID;
    }

    public List<Ingredient> getItemInputs()  {return this.itemInputs;}
    public List<ItemStack> getItemOutputs() { return this.itemOuputs; }
    public int getNumInputs() {return itemInputs.size(); }
    public int getNumOutputs() { return itemOuputs.size(); }
    public int getNumFluidOutputs() {
        return fluidOutputs.size();
    }
    public List<ItemStack> getItemOuputs()  {return  this.itemOuputs;}
    public List<FluidStack> getFluidOutputs()  {return this.fluidOutputs;}
    public Ingredient getInput(int index) {
        return this.itemInputs.get(index);
    }
    public ItemStack getOutput(int index) { return this.itemOuputs.get(index); }
    public ItemStack getInputItemStack(int index) {
        return this.itemInputs.get(index).getMatchingStacks()[0];
    }
    public FluidStack getFluidOutput(int index) {
        return null;
    }
    public Ingredient getFuel()  {return this.fuel;}
    public int getBurnTime()  {return  this.burnTime;}

    @Override
    public Ingredient getInput() {
        return null;
    }

    public ResourceLocation getId() {
        return this.id;
    }
    public IRecipeSerializer<?> getSerializer() {
        return this.seralizer;
    }
    public IRecipeType<?> getType() {
        return Registry.RECIPE_TYPE.getOrDefault(recipeTypeID);
    }
    public List<FluidStack> getFluidStackOutput() {
        return this.fluidOutputs;
    }

    public boolean matches(RecipeWrapper inv, World worldIn) {
        for(int i = 0; i < getNumInputs(); i++)  {
            if(i > this.itemInputs.size())  return false;
            if(!this.itemInputs.get(i).test(inv.getStackInSlot(i)))  return false;
        }
        return true;
    }

    public ItemStack getCraftingResult(RecipeWrapper inv) {
        return null;
    }
    public ItemStack getRecipeOutput() {
        return null;
    }

}
