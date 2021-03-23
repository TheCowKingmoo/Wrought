package com.thecowking.wrought.blocks.refactory_brick;

import com.thecowking.wrought.blocks.MultiBlockFrameBlock;
import com.thecowking.wrought.data.MultiblockData;
import com.thecowking.wrought.tileentity.MultiBlockFrameTile;
import com.thecowking.wrought.tileentity.blast_furance.BlastFurnaceBrickFrameTile;
import com.thecowking.wrought.tileentity.honey_comb_coke_oven.HCCokeOvenFrameTile;
import com.thecowking.wrought.tileentity.refactory_brick.RefactoryBrickFrameTile;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class RefactoryBrickBlock extends MultiBlockFrameBlock {

    // creates the tile entity
    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        if(state.get(MultiblockData.FORMED))  {
            return new RefactoryBrickFrameTile();
        }
        return null;
    }
}
