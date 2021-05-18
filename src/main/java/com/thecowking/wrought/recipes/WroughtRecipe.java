package com.thecowking.wrought.recipes;

import com.thecowking.wrought.inventory.WroughtTank;
import com.thecowking.wrought.inventory.containers.InputFluidTank;
import com.thecowking.wrought.inventory.slots.InputItemHandler;
import com.thecowking.wrought.tileentity.MultiBlockControllerTile;
import com.thecowking.wrought.tileentity.MultiBlockControllerTileFluid;
import com.thecowking.wrought.util.RecipeUtil;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.fixes.FurnaceRecipes;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class WroughtRecipe implements IWroughtRecipe {
    private static final Logger LOGGER = LogManager.getLogger();


    protected ResourceLocation id;
    protected List<Ingredient> itemInputs;
    protected List<ItemStack> itemOuputs;
    protected List<FluidStack> fluidOutputs;
    protected List<Ingredient> fluidInputs;
    protected int heat;

    protected Ingredient fuel;                  // if this is empty than any burnable thing will do
    protected int burnTime = 0;
    protected IRecipeSerializer<?>  seralizer;
    protected ResourceLocation recipeTypeID;

    public WroughtRecipe(ResourceLocation id, List<Ingredient> itemInputs,  List<Ingredient> itemOuputsIngredient, List<FluidStack> fluidOutputs,
                              List<Ingredient> fluidInputs, Ingredient fuel, int burnTime, int heat, ResourceLocation recipeTypeID) {
        this.id = id;
        this.itemInputs = itemInputs;

        this.itemOuputs = new ArrayList<>();
        for(int i = 0; i < itemOuputsIngredient.size(); i++)  {
            itemOuputs.add(RecipeUtil.getPreferredITemStackFromTag(itemOuputsIngredient.get(i)));
        }
        this.fluidOutputs = fluidOutputs;

        this.fluidInputs = fluidInputs;

        // in case there were no item inputs
        if(itemInputs == null)  itemInputs = new ArrayList<>();

        this.fuel = fuel;
        if(fuel == null)  {
            this.fuel = Ingredient.EMPTY;
        }
        this.heat = heat;
        this.burnTime = burnTime;
        this.recipeTypeID = recipeTypeID;


    }

    public List<Ingredient> getItemInputs()  {return this.itemInputs;}
    public List<ItemStack> getItemOutputs() { return this.itemOuputs; }
    public int getNumInputs() {return itemInputs.size(); }
    public int getNumOutputs() { return itemOuputs.size(); }
    public int getNumFluidInput()  {return fluidInputs.size();}

    public int getNumFluidOutputs() {
        return fluidOutputs.size();
    }

    public int getNumFluidInputs() {
        return this.fluidInputs.size();
    }
    public int getHeat()  {return this.heat;}

    public List<ItemStack> getItemOuputs()  {return  this.itemOuputs;}
    public List<FluidStack> getFluidOutputs()  {return this.fluidOutputs;}
    public List<Ingredient> getFluidInputs()  {return this.fluidInputs;}

    public Ingredient getInput(int index) {
        if(index >= this.itemInputs.size())  {
            return this.fluidInputs.get(index);
        }
        return this.itemInputs.get(index);
    }


    public ItemStack getOutput(int index) { return this.itemOuputs.get(index); }
    public ItemStack getInputItemStack(int index) {
        return this.itemInputs.get(index).getMatchingStacks()[0];
    }
    public FluidStack getFluidOutput(int index) {
        return fluidOutputs.get(index);
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
        if(inv.getSizeInventory() < 1)  {
            return false;
        }
        if(inv.getStackInSlot(0) == ItemStack.EMPTY) {
            return false;
        }

        for(int i = 0; i < getNumInputs(); i++)  {
            if(i > this.itemInputs.size())  {
                return false;
            }

            if(!this.itemInputs.get(i).test(inv.getStackInSlot(i)))  {
                return false;
            }
        }
        return true;
    }


    public boolean matches(InputFluidTank inputFluidTanks, World worldIn, MultiBlockControllerTile tileIn)  {
        InputItemHandler tempWrapper = new InputItemHandler(inputFluidTanks.getTanks(), tileIn, null, "fluid_input");
        for(int i = 0; i < inputFluidTanks.getTanks(); i++)  {
            FluidStack currentFluidStack = inputFluidTanks.getFluidInTank(i);
            ItemStack bucketOfFluidStack = FluidUtil.getFilledBucket(currentFluidStack);
            tempWrapper.insertItem(i, bucketOfFluidStack, false);
        }
        return this.matches(new RecipeWrapper(tempWrapper), worldIn);
    }

    public ItemStack getCraftingResult(RecipeWrapper inv) {
        return null;
    }

    public ItemStack getRecipeOutput() {
        if(itemOuputs.size() == 0) return null;
        return this.itemOuputs.get(0);
    }

}
