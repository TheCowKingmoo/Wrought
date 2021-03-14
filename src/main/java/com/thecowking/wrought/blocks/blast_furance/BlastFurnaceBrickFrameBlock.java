package com.thecowking.wrought.blocks.blast_furance;

import com.thecowking.wrought.blocks.MultiBlockFrameBlock;
import com.thecowking.wrought.data.MultiblockData;
import com.thecowking.wrought.tileentity.blast_furance.BlastFurnaceBrickControllerTile;
import com.thecowking.wrought.tileentity.blast_furance.BlastFurnaceBrickFrameTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import static com.thecowking.wrought.data.MultiblockData.FORMED;

public class BlastFurnaceBrickFrameBlock extends MultiBlockFrameBlock {


    public BlastFurnaceBrickFrameBlock()  {
        super();
    }
    // creates the tile entity
    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        if(state.get(MultiblockData.FORMED))  {
            return new BlastFurnaceBrickFrameTile();
        }
        return null;
    }

}
