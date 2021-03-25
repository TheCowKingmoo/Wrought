package com.thecowking.wrought.blocks.blast_furance;

import com.thecowking.wrought.blocks.MultiBlockFrameBlock;
import com.thecowking.wrought.data.MultiblockData;
import com.thecowking.wrought.tileentity.blast_furance.BlastBrickTile;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class BlastBrickBlock extends MultiBlockFrameBlock {


    public BlastBrickBlock()  {
        super();
    }
    // creates the tile entity
    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        if(state.get(MultiblockData.FORMED))  {
            return new BlastBrickTile();
        }
        return null;
    }

}
