package com.thecowking.wrought.blocks.blast_furance;

import com.thecowking.wrought.blocks.IMultiblockData;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlastFurnaceData implements IMultiblockData {
    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public int getLength() {
        return 0;
    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public Block[][][] getPosArray() {
        return new Block[0][][];
    }

    @Override
    public Block getBlockMember(int x, int y, int z) {
        return null;
    }

    @Override
    public BlockPos calcCenterBlock(Direction inputDirection, BlockPos controllerPos, IMultiblockData data) {
        return null;
    }

    @Override
    public Direction getStairsDirection(Direction controllerDirection, int x, int z) {
        return null;
    }

    @Override
    public INamedContainerProvider getContainerProvider(World world, BlockPos controllerPos) {
        return null;
    }
}
