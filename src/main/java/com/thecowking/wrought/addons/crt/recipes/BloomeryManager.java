package com.thecowking.wrought.addons.crt.recipes;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.IFluidStack;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe;
import com.thecowking.wrought.Wrought;
import com.thecowking.wrought.init.RecipeSerializerInit;
import com.thecowking.wrought.recipes.WroughtRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import org.openzen.zencode.java.ZenCodeType;

import java.util.ArrayList;
import java.util.List;

@ZenRegister
@ZenCodeType.Name("mods.wrought.bloomery")
public class BloomeryManager implements IRecipeManager {
    @Override
    public IRecipeType getRecipeType() {
        return RecipeSerializerInit.BLOOMERY_TYPE;
    }

    @ZenCodeType.Method
    public void addRecipe(String name, IIngredient input_0, IIngredient input_1, IIngredient output_0, IIngredient output_1, int burnTime, int heat)  {
        name = fixRecipeName(name);

        List<Ingredient> itemInputs = new ArrayList<>();
        itemInputs.add(input_0.asVanillaIngredient());
        itemInputs.add(input_1.asVanillaIngredient());

        List<Ingredient> itemOutputs = new ArrayList<>();
        itemOutputs.add(output_0.asVanillaIngredient());
        itemOutputs.add(output_1.asVanillaIngredient());

        WroughtRecipe r = new WroughtRecipe(new ResourceLocation("crafttweaker", name), itemInputs, itemOutputs, null, null, null, burnTime, heat, RecipeSerializerInit.BLOOMERY_RECIPE_TYPE_ID);
        CraftTweakerAPI.apply(new ActionAddRecipe(this, r, ""));
    }
    @ZenCodeType.Method
    public void removeRecipe(String name)  {
        removeByName(name);
    }
}
