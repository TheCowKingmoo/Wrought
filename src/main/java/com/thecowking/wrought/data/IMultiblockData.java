package com.thecowking.wrought.data;

import net.minecraft.block.Block;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Set;

public interface IMultiblockData {
    public int getHeight();
    public int getLength();
    public int getWidth();
    public int getNumberItemInputSlots();
    public int getNumberItemOutputSlots();
    public Block[][][] getPosArray();
    public Block getBlockMember(int x, int y, int z);
    public BlockPos calcCenterBlock(Direction inputDirection, BlockPos controllerPos);
    public Direction getStairsDirection(BlockPos controllerPos, BlockPos blockPos, Direction controllerDirection, int x, int z);
    public INamedContainerProvider getContainerProvider(World world, BlockPos controllerPos, boolean isFormed);
    public SlabType getSlabDirection(int y);
    public BlockPos findLowsestValueCorner(BlockPos centerPos, Direction inputDirection, int longerSide, int shorterSide);
    public BlockPos getRedstoneInBlockPos(BlockPos controllerPos);
    public BlockPos getRedstoneOutBlockPos(BlockPos controllerPos);
    public Set<IRecipe<?>> getRecipesByType(World world);




    }
