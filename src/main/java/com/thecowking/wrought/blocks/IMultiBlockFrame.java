package com.thecowking.wrought.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import static com.thecowking.wrought.blocks.Multiblock.FORMED;

public interface IMultiBlockFrame {
    public void addingToMultblock(BlockState blockState, BlockPos posIn, World worldIn);
}
