package com.thecowking.wrought.recipes.HoneyCombCokeOven;

import com.thecowking.wrought.Wrought;
import com.thecowking.wrought.recipes.IWroughtRecipe;
import com.thecowking.wrought.init.RecipeSerializerInit;
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

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class HoneyCombCokeOvenRecipe implements IWroughtRecipe {

    private static final Logger LOGGER = LogManager.getLogger();
    NonNullList<Ingredient> ingredientList;


    private final ResourceLocation id;
    private List<FluidStack> fluidStack
            ;
    private List<Ingredient> inputs;
    private List<ItemStack> outputs;


    private int burnTime = 0;

    public HoneyCombCokeOvenRecipe(ResourceLocation id, Ingredient primaryInput, ItemStack primaryOutput, ItemStack secondaryOutput, FluidStack fluidStack, int burnTime) {
        this.id = id;
        this.inputs = new ArrayList<>();
        this.inputs.add(primaryInput);
        this.outputs = new ArrayList<>();
        this.outputs.add(primaryOutput);
        this.outputs.add(secondaryOutput);
        this.fluidStack = new ArrayList<>();
        this.fluidStack.add(fluidStack);
        this.burnTime = burnTime;
        ingredientList = NonNullList.create();
        ingredientList.add(primaryInput);
    }

    public ItemStack getPrimaryOutput()  {
        return this.outputs.get(0);
    }

    public ItemStack getSecondaryOutput()  {
        return this.outputs.get(1);
    }

    public int getNumInput()  {
        return this.ingredientList.size();
    }
    public List<Ingredient> getItemInputs() { return this.inputs;}

    public List<ItemStack> getItemOutputs() {
        return this.outputs;
    }

    public int getNumInputs() {
        return inputs.size();
    }
    public int getNumOutputs() {
        return outputs.size();
    }


    public int getNumFluidOutputs() {
        return this.fluidStack.size();
    }

    public List<FluidStack> getFluidOutputs() {
        return this.fluidStack;
    }


    public boolean matches(RecipeWrapper inv, World worldIn) {
        for(int i = 0; i < getNumInputs(); i++)  {
            if(i > this.inputs.size())  return false;
            if(!this.inputs.get(i).test(inv.getStackInSlot(i)))  return false;
        }
        return true;
    }

    public boolean matches(ItemStack stack) {
        return this.inputs.get(0).test(stack);
    }


    @Override
    public ItemStack getCraftingResult(RecipeWrapper inv) {
        return this.outputs.get(0);
    }

    @Override
    public ItemStack getRecipeOutput() {
        return null;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RecipeSerializerInit.HONEY_COMB_SERIALIZER.get();
    }

    @Override
    public Ingredient getInput() {
        return this.inputs.get(0);
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return ingredientList;
    }

    public int getBurnTime() {
        return burnTime;
    }



    @Nonnull
    @Override
    public IRecipeType<?> getType() {
        return Registry.RECIPE_TYPE.getOrDefault(RecipeSerializerInit.HONEY_COMB_RECIPE_TYPE_ID);
    }

    public List<FluidStack> getFluidStackOutput() {
        return this.fluidStack;
    }
    public Ingredient getInput(int index)  {return  this.inputs.get(index); }

    @Override
    public ItemStack getOutput(int index) {
        return this.outputs.get(index);
    }

    @Override
    public ItemStack getInputItemStack(int index) {
        return this.inputs.get(index).getMatchingStacks()[0];
    }

    @Override
    public FluidStack getFluidOutput(int index) {
        return this.fluidStack.get(index);
    }
}
