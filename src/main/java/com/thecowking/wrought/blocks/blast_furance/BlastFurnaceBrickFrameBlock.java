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
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

import static com.thecowking.wrought.data.MultiblockData.FORMED;

public class BlastFurnaceBrickFrameBlock extends MultiBlockFrameBlock {


    public BlastFurnaceBrickFrameBlock()  {
        super();
        setDefaultState(this.getDefaultState().with(MultiblockData.JOB, false));
    }
    /*
    If part of a multi-blocks it finds the controller to start the process of taking the multi-blocks
    apart.
     */
    @Override
    public void onBlockHarvested(World worldIn, BlockPos posIn, BlockState state, PlayerEntity player) {
        super.onBlockHarvested(worldIn, posIn, state, player);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(BlockStateProperties.FACING, MultiblockData.JOB);
    }


    public BlockPos searcDownwardNeighbors(World world, BlockPos current)  {

        int x = current.getX();
        int y = current.getY()-1;
        int z = current.getZ();

        for(int i = 0; i < 5; i++)  {
            int tX = x;
            int tZ = z;
            switch (i)  {
                case 0:
                    break;
                case 1:
                    tZ = z - 1;
                    break;
                case 2:
                    tZ = z + 1;
                    break;
                case 3:
                    tX = x - 1;
                    break;
                case 4:
                    tX = x + 1;
                    break;
            }
            BlockPos attempt = new BlockPos(tX, y, tZ);
            if(world.getBlockState(attempt).getBlock() instanceof BlastFurnaceBrickFrameBlock && world.getBlockState(attempt).get(FORMED) ||
                    world.getBlockState(attempt).getBlock() instanceof BlastFurnaceBrickControllerBlock && world.getBlockState(attempt).get(FORMED) )  {
                return attempt;
            }
        }
        return null;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return state.get(MultiblockData.JOB);
    }


    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos posIn, PlayerEntity player, Hand hand, BlockRayTraceResult trace) {
        if (player instanceof ServerPlayerEntity && state.get(MultiblockData.FORMED)) {
            if(state.get(MultiblockData.JOB))  {
                // hit a watcher
                BlastFurnaceBrickControllerTile controller = ((BlastFurnaceBrickFrameTile)world.getTileEntity(posIn)).getControllerTile();
                controller.openGUI(world, posIn, player, controller);
            }

            BlockPos nextBlockPos =  searcDownwardNeighbors(world, new BlockPos(posIn.getX(), posIn.getY()-1, posIn.getZ()));
            return world.getBlockState(nextBlockPos).onBlockActivated(world, player, hand, trace);

        }  else  {
            super.onBlockActivated(state, world, posIn, player, hand, trace);
            return ActionResultType.SUCCESS;
        }
    }
}
