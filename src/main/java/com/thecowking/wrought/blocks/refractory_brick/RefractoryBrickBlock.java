package com.thecowking.wrought.blocks.refractory_brick;

import com.thecowking.wrought.blocks.MultiBlockFrameBlock;
import com.thecowking.wrought.data.MultiblockData;
import com.thecowking.wrought.tileentity.refractory_brick.RefractoryBrickFrameTile;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class RefractoryBrickBlock extends MultiBlockFrameBlock {

    // creates the tile entity
    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        if(state.get(MultiblockData.FORMED))  {
            return new RefractoryBrickFrameTile();
        }
        return null;
    }
}
