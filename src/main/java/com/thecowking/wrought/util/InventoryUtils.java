package com.thecowking.wrought.util;

import com.thecowking.wrought.init.RecipeSerializerInit;
import com.thecowking.wrought.recipes.HoneyCombCokeOven.HoneyCombCokeOvenRecipe;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class InventoryUtils {

    public static int lastIndex;
    private static final Logger LOGGER = LogManager.getLogger();


    public static boolean canItemsStack(ItemStack a, ItemStack b) {

        // Determine if the item stacks can be merged
        if (a.isEmpty() || b.isEmpty()) return true;
        return ItemHandlerHelper.canItemStacksStack(a, b) && a.getCount() + b.getCount() <= a.getMaxStackSize();
    }

    /*
      Used to move fluid out of a container into a machines fluid tank
     */
    public static boolean canAcceptFluidContainer(ItemStack input, ItemStack output, FluidStack fluidStack, FluidTank fluidTank)  {
        return !fluidStack.isEmpty()
                && fluidTank.isFluidValid(0, fluidStack)
                && fluidTank.fill(fluidStack, IFluidHandler.FluidAction.SIMULATE) == 1000
                && (output.isEmpty() || InventoryUtils.canItemsStack(input.getContainerItem(), output))
                && (output.isEmpty() || output.getCount() < output.getMaxStackSize());
    }

    public static ItemStack fillBucketOrFluidContainer(ItemStack emptyContainer, FluidStack fluidStack) {
        Item item = emptyContainer.getItem();
        if (item instanceof BucketItem) {
            return new ItemStack(fluidStack.getFluid().getFilledBucket());
        }
        return ItemStack.EMPTY;
    }

    @Nullable
    public static HoneyCombCokeOvenRecipe getRecipe(ItemStack stack, ItemStackHandler inputSlot, World world) {
        if (stack == null) {
            return null;
        }

        Set<IRecipe<?>> recipes = findRecipesByType(RecipeSerializerInit.HONEY_COMB_OVEN_TYPE, world);
        for (IRecipe<?> iRecipe : recipes) {
            HoneyCombCokeOvenRecipe recipe = (HoneyCombCokeOvenRecipe) iRecipe;
            if (recipe.matches(new RecipeWrapper(inputSlot), world)) {
                return recipe;
            }
        }
        return null;
    }
    public static Set<IRecipe<?>> findRecipesByType(IRecipeType<?> typeIn, World world) {
        return world != null ? world.getRecipeManager().getRecipes().stream()
                .filter(recipe -> recipe.getType() == typeIn).collect(Collectors.toSet()) : Collections.emptySet();
    }

    public static int getTotalCount(IInventory inventory, Predicate<ItemStack> ingredient) {
        int total = 0;
        for (int i = 0; i < inventory.getSizeInventory(); ++i) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (!stack.isEmpty() && ingredient.test(stack)) {
                total += stack.getCount();
            }
        }
        return total;
    }

    public static void consumeItems(IInventory inventory, Predicate<ItemStack> ingredient, int amount) {
        int amountLeft = amount;
        for (int i = 0; i < inventory.getSizeInventory(); ++i) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (!stack.isEmpty() && ingredient.test(stack)) {
                int toRemove = Math.min(amountLeft, stack.getCount());

                stack.shrink(toRemove);
                if (stack.isEmpty()) {
                    inventory.setInventorySlotContents(i, ItemStack.EMPTY);
                }

                amountLeft -= toRemove;
                if (amountLeft == 0) {
                    return;
                }
            }
        }
    }


    public static int getIndexOfSingleItemInPlayerInventory(PlayerEntity player, Item item)  {
        if(lastIndex < player.inventory.mainInventory.size() && lastIndex > -1)  {
            if(player.inventory.mainInventory.get(lastIndex).getItem() == item)  {
                return lastIndex;
            }
        }

        int index = -1;
        for(int i = 0; i < player.inventory.mainInventory.size(); i++)  {
            ItemStack currentPlayerItem = player.inventory.mainInventory.get(i);
            if(currentPlayerItem.getItem() == item)  {
                index = i;
                break;
            }
        }
        lastIndex = index;
        return index;
    }



    public static HashMap<Block, Integer> checkVsPlayerInventory(HashMap<Block, Integer> missingBlocks, PlayerEntity player)  {
        NonNullList<ItemStack> playerInventory = player.inventory.mainInventory;
        HashMap<Block, Integer> inInventory = new HashMap<>();


        for(Map.Entry<Block, Integer> e: missingBlocks.entrySet())  {
            for(int i = 0; i < e.getValue(); i++)  {
                int index = getIndexOfSingleItemInPlayerInventory(player, e.getKey().asItem());
                if(index != -1)  {
                    playerInventory.get(index).shrink(1);
                    if(inInventory.get(e.getKey()) != null )  {
                        inInventory.put(e.getKey(), inInventory.get(e.getKey())+1);
                    }  else  {
                        inInventory.put(e.getKey(), 1);
                    }
                }
            }
        }
        return inInventory;

    }
}
