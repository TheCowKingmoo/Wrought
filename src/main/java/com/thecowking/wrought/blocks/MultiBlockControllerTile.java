package com.thecowking.wrought.blocks;

import com.thecowking.wrought.blocks.honeycomb_coke_oven.HCCokeOvenFrameBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MultiBlockControllerTile extends TileEntity implements IMultiBlockControllerTile {

    public MultiBlockControllerTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }


    // returns the blockstate of the multiblocks controllers "Formed" state
    public boolean isFormed(World worldIn) {return worldIn.getBlockState(pos).get(Multiblock.FORMED);}

    // sets the blockstate "formed" for the multiblocks controller
    public void setFormed(World worldIn, boolean b)  {worldIn.setBlockState(pos, getBlockState().with(Multiblock.FORMED, b));}

    public boolean isValidMultiBlockFormer(Item item) {
        return false;
    }

    public Direction getDirectionFacing(World worldIn)  {return worldIn.getBlockState(pos).get(BlockStateProperties.FACING);}


    public void tryToFormMultiBlock(World worldIn, BlockPos pos) {}

    public void openGUI(World worldIn, BlockPos pos, PlayerEntity player, IMultiBlockControllerTile tileEntity) {
    }

    public void setDirty(boolean b) {
    }

    // TODO - bubbleup
    public MultiBlockFrameTile getFrameTile(BlockPos posIn)  {
        TileEntity te = world.getTileEntity(posIn);
        if(te != null)  {
            if(checkIfCorrectFrame(posIn))  {
                return (MultiBlockFrameTile) te;
            }  else  {
                //LOGGER.info("getDrillFrameTile got a non DrillFrameTile at" + te.getPos());
            }
        }  else {
            //LOGGER.info("getDrillFrameTile got a non entity pos");
        }
        return null;
    }
    /*
    Does the needed checks and casting to see if current BlockPos holds a correct member of multi-block
 */
    // TODO - bubble this up
    private boolean checkIfCorrectFrame(BlockPos currentPos)  {
        Block currentBlock = world.getBlockState(currentPos).getBlock();
        BlockState currentState = world.getBlockState(currentPos);
        if( currentState.hasTileEntity() || !(currentState.isAir(world, currentPos))) {
            return checkIfCorrectFrame(currentBlock);
        }
        return false;
    }

    /*
      Should be overwritten by extending subclasses
     */
    public boolean checkIfCorrectFrame(Block block)  {
        return true;
    }
}
