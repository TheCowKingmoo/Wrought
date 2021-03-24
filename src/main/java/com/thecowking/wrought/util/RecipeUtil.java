package com.thecowking.wrought.util;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class RecipeUtil {
    public static Set<IRecipe<?>> findRecipesByType(IRecipeType<?> typeIn, World world) {
        return world != null ? world.getRecipeManager().getRecipes().stream()
                .filter(recipe -> recipe.getType() == typeIn).collect(Collectors.toSet()) : Collections.emptySet();
    }


    public static ItemStack getPreferredITemStackFromTag(Ingredient input)  {
        if(input.getMatchingStacks().length == 0)  return ItemStack.EMPTY;
        return input.getMatchingStacks()[0];
    }

}
