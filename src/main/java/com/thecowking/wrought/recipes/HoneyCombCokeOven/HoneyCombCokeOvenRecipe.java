package com.thecowking.wrought.recipes.HoneyCombCokeOven;

import com.thecowking.wrought.util.MultiStack;
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

public class HoneyCombCokeOvenRecipe implements IHoneyCombCokeOvenRecipe {
    private static final Logger LOGGER = LogManager.getLogger();


    private final ResourceLocation id;
    private Ingredient input;
    private FluidStack fluidStack;
    private final ItemStack output;
    private int burnTime = 0;

    public HoneyCombCokeOvenRecipe(ResourceLocation id, Ingredient input, ItemStack output, FluidStack fluidStack, int burnTime) {
        this.id = id;
        this.output = output;
        this.input = input;
        this.fluidStack = fluidStack;
        this.burnTime = burnTime;
    }

    @Override
    public boolean matches(RecipeWrapper inv, World worldIn) {
        if (this.input.test(inv.getStackInSlot(0))) {
            return true;
        }
        return false;
    }

    @Override
    public ItemStack getCraftingResult(RecipeWrapper inv) {
        return this.output;
    }

   @Override
   public ItemStack getRecipeOutput() {
        return this.output;
    }

    public ItemStack getRecipeItemStackOutput()  {return this.output;}
    public FluidStack getRecipeFluidStackOutput()  {return this.fluidStack;}


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
        return this.input;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.from(null, this.input);
    }

    public int getBurnTime() {
        return burnTime;
    }
}
