package com.thecowking.wrought.blocks;

import com.thecowking.wrought.blocks.honeycomb_coke_oven.HCCokeOvenFrameBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MultiBlockControllerTile extends MultiBlockTile implements IMultiBlockControllerTile {

    Direction directionFacing;

    public MultiBlockControllerTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    // sets the blockstate "formed" for the multiblocks controller
    public Direction getDirectionFacing(World worldIn)  {return worldIn.getBlockState(pos).get(BlockStateProperties.FACING);}

    @Override
    public void setDirty(boolean b) {

    }

    /*
    Does the needed checks and casting to see if current BlockPos holds a correct member of multi-block
 */
    private boolean checkIfCorrectFrame(BlockPos currentPos)  {
        Block currentBlock = world.getBlockState(currentPos).getBlock();
        BlockState currentState = world.getBlockState(currentPos);
        if( currentState.hasTileEntity() || !(currentState.isAir(world, currentPos))) {
            return checkIfCorrectFrame(currentBlock);
        }
        return false;
    }

    /*
      This is called when a controller is right clicked by a player when the multi-block is not formed
      Checks to make sure that the player is holding the correct item in hand to form the multi-block.
     */
    public boolean isValidMultiBlockFormer(Item item)  {
        return item == Items.STICK;
    }

    @Override
    public void tryToFormMultiBlock(World worldIn, BlockPos pos) {

    }

    @Override
    public void openGUI(World worldIn, BlockPos pos, PlayerEntity player, IMultiBlockControllerTile tileEntity) {

    }

    /*
      Should be overwritten by extending subclasses
     */
    public boolean checkIfCorrectFrame(Block block)  {
        return true;
    }


}
