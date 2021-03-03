package com.thecowking.wrought.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public interface IMultiblockData {
    public int getHeight();
    public int getLength();
    public int getWidth();
    public Block[][][] getPosArray();
    public Block getBlockMember(int x, int y, int z);
    public BlockPos calcCenterBlock(Direction inputDirection, BlockPos controllerPos, IMultiblockData data);
    public Direction getStairsDirection(Direction controllerDirection, int x, int z);



}
