package com.thecowking.wrought.recipes;

import com.thecowking.wrought.Wrought;
import com.thecowking.wrought.inventory.containers.InputFluidTank;
import com.thecowking.wrought.tileentity.MultiBlockControllerTile;
import com.thecowking.wrought.tileentity.MultiBlockControllerTileFluid;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nonnull;
import java.util.List;

public interface IWroughtRecipe extends IRecipe<RecipeWrapper> {


    @Nonnull
    @Override
    public IRecipeType<?> getType();

    public List<FluidStack> getFluidStackOutput();

    @Override
    default boolean canFit(int width, int height) {
        return false;
    }

    public List<Ingredient> getItemInputs();
    public List<ItemStack> getItemOutputs();
    public int getNumInputs();
    public int getNumOutputs();
    public int getNumFluidOutputs();
    public int getNumFluidInputs();

    public List<FluidStack> getFluidOutputs();
    public List<Ingredient> getFluidInputs();

    public Ingredient getInput(int index);
    public ItemStack getOutput(int index);
    public ItemStack getInputItemStack(int index);
    public FluidStack getFluidOutput(int index);
    public int getBurnTime();
    public Ingredient getFuel();
    public int getHeat();


    Ingredient getInput();

    boolean matches(InputFluidTank inputFluidTanks, World world, MultiBlockControllerTile multiBlockControllerTileFluid);
}