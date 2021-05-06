package com.thecowking.wrought.inventory.slots;

import com.thecowking.wrought.tileentity.MultiBlockControllerTile;
import com.thecowking.wrought.tileentity.MultiBlockControllerTileFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.Set;

import static com.thecowking.wrought.util.InventoryUtils.findRecipesByType;

//Source - TurtWurty - https://www.youtube.com/watch?v=QUxLsZHiyA4&list=PLaevjqy3XufYmltqo0eQusnkKVN7MpTUe&index=48


public class InputFluidHandler extends ItemStackHandler {
    private static final Logger LOGGER = LogManager.getLogger();
    private MultiBlockControllerTileFluid tile;
    private InputFluidHandler primary;
    private Set<IRecipe<?>> recipes;
    private String id;


    public InputFluidHandler(int size, MultiBlockControllerTileFluid tile, InputFluidHandler primary, String id)  {
        super(size);
        this.tile = tile;
        this.primary = primary;
        this.id = id;
    }

    /*
        As this is for an input fluid tank slot we want the item being put in to have some sort of fluid that is going to
        be transferred to the tank
     */
    public ItemStack insertItemForInputTank(int slot, @Nonnull ItemStack stack, boolean simulate)  {
        Optional<FluidStack> op = FluidUtil.getFluidContained(stack);

        if(op == null || !(op.isPresent()) || op.get().getFluid() == Fluids.EMPTY)  return stack;
        Fluid tankFluid = tile.getFluidInInputTank(slot).getFluid();
        if(tankFluid != Fluids.EMPTY && tankFluid != op.get().getFluid())  return stack;
        return super.insertItem(slot, stack, simulate);
    }

    /*
        As this is for an output fluid tank slot we want the item being put in to have no fluids
        TODO - change this to also add in a tank that has some of the correct fluid already in it
     */
    public ItemStack insertItemForOutputTank(int slot, @Nonnull ItemStack stack, boolean simulate)  {
        Optional<FluidStack> op = FluidUtil.getFluidContained(stack);
        if(op == null || !(op.isPresent()) || op.get().getFluid() == Fluids.EMPTY)  return super.insertItem(slot, stack, simulate);
        return stack;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)  {
        // empty + check if it can hold fluids
        if(stack == null || stack.isEmpty() || FluidUtil.getFluidHandler(stack) == null) return stack;
        if(tile.isSlotAttachedToOutputTank(slot)) return insertItemForOutputTank(slot, stack, simulate);
        return insertItemForInputTank(slot, stack, simulate);
    }




}

