package com.thecowking.wrought.blocks.MultiBlock;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IMultiBlockFrame {
    public void addingToMultblock(BlockState blockState, BlockPos posIn, World worldIn);
    public void removeFromMultiBlock(BlockState blockState,BlockPos posIn, World worldIn);
}
