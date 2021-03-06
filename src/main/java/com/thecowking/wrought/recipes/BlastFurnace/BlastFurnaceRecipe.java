package com.thecowking.wrought.recipes.BlastFurnace;

import com.thecowking.wrought.recipes.IWroughtRecipe;
import com.thecowking.wrought.util.RecipeSerializerInit;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class BlastFurnaceRecipe implements IWroughtRecipe {

    private final int ORE_INDEX = 0;
    private final int FLUX_INDEX = 1;
    private final int AUX_INDEX = 2;

    private final ResourceLocation id;
    private List<Ingredient> itemInputs;

    private List<ItemStack> itemOuputs;

    private List<FluidStack> fluidOutputs;

    private ItemStack fuel;

    private int burnTime = 0;

    public BlastFurnaceRecipe(ResourceLocation id, List<Ingredient> itemInputs,  List<ItemStack> itemOuputs, List<FluidStack> fluidOutputs,
                              ItemStack fuel, int burnTime) {
        this.id = id;

        this.itemInputs = itemInputs;
        this.itemOuputs = itemOuputs;
        this.fluidOutputs = fluidOutputs;

        this.fuel = fuel;
        this.burnTime = burnTime;
    }

    public ItemStack getPrimaryItemOutput()  {
        return this.itemOuputs.get(FLUX_INDEX);
    }
    public ItemStack getSecondaryItemOutput()  {
        return this.itemOuputs.get(FLUX_INDEX);
    }
    public ItemStack getTrinaryItemOutput()  {
        return this.itemOuputs.get(AUX_INDEX);
    }

    public FluidStack getPrimaryFluidOutput()  {
        return this.fluidOutputs.get(ORE_INDEX);
    }
    public FluidStack getSecondaryFluidOutput()  {
        return this.fluidOutputs.get(FLUX_INDEX);
    }

    public Ingredient getPrimaryItemInput()  {
        return this.itemInputs.get(FLUX_INDEX);
    }
    public Ingredient getSecondaryItemInput()  {
        return this.itemInputs.get(FLUX_INDEX);
    }
    public Ingredient getTrinaryItemInput()  {
        return this.itemInputs.get(AUX_INDEX);
    }


    public List<Ingredient> getItemInputs()  {return this.itemInputs;}
    public List<ItemStack> getItemOuputs()  {return  this.itemOuputs;}
    public List<FluidStack> getFluidOutputs()  {return this.fluidOutputs;}
    public ItemStack getFuel()  {return this.fuel;}
    public int getBurnTime()  {return  this.burnTime;}

    @Override
    public boolean matches(RecipeWrapper inv, World worldIn) {
        return getPrimaryItemInput().test(inv.getStackInSlot(0));
    }

    //public boolean matches(ItemStack stack) {
    //    return this.primaryInput.test(stack);
   //}


    @Override
    public ItemStack getCraftingResult(RecipeWrapper inv) {
        return this.getPrimaryItemOutput();
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
        return RecipeSerializerInit.EXAMPLE_SERIALIZER.get();
    }


    @Override
    public Ingredient getInput() {
        return getPrimaryItemInput();
    }
}
