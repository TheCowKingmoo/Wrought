package com.thecowking.wrought.blocks.honey_comb_coke_oven;

import com.thecowking.wrought.blocks.MultiBlockFrameBlock;
import com.thecowking.wrought.tileentity.honey_comb_coke_oven.CokeBrickTile;
import com.thecowking.wrought.data.MultiblockData;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class CokeBrickBlock extends MultiBlockFrameBlock {

    public CokeBrickBlock()  {

    }

    // creates the tile entity
    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        if(state.get(MultiblockData.FORMED))  {
            return new CokeBrickTile();
        }
        return null;
    }

}
