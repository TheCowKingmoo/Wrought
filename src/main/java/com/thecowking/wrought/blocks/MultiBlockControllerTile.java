package com.thecowking.wrought.blocks;

import com.thecowking.wrought.blocks.honeycomb_coke_oven.HCCokeOvenFrameBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static com.thecowking.wrought.blocks.Multiblock.*;

public class MultiBlockControllerTile extends MultiBlockTile implements IMultiBlockControllerTile {

    protected int facingDirection = -1;
    protected final Direction[] POSSIBLE_DIRECTIONS = {Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};
    protected BlockPos redstoneIn;
    protected BlockPos redstoneOut;
    protected BlockPos centerBlock;

    protected int xLength;
    protected int yLength;
    protected int zLength;

    public MultiBlockControllerTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public boolean isLit()  {return this.world.getBlockState(this.pos).get(Multiblock.LIT);}
    public void setLit(boolean b)  {this.world.setBlockState(this.pos, getBlockState().with(Multiblock.LIT, b));}

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


    private MultiBlockFrameTile getFrameTile(BlockPos posIn)  {
        TileEntity te = getTileFromPos(this.world, posIn);
        if( te != null && te instanceof MultiBlockFrameTile) {
            return (MultiBlockFrameTile) te;
        }
        return null;
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
    public void openGUI(World worldIn, BlockPos pos, PlayerEntity player, IMultiBlockControllerTile tileEntity) {}

    /*
      Should be overwritten by extending subclasses
     */
    public boolean checkIfCorrectFrame(Block block)  {
        return true;
    }

    public void  setFacingDirection(int dir)  {this.facingDirection = dir;}

    public Direction getDirectionFacing()  {
        if (this.facingDirection == -1)  {
            return null;
        }
        return POSSIBLE_DIRECTIONS[facingDirection];
    }




    /*
      Used a simple getter for the center block
      Instead of read/writing the center block we can just calculate its position when needed as long as we have
      the correct facingDirection of the multi-block
     */

    public BlockPos getCenterBlock()  {
        if(this.centerBlock == null && this.facingDirection != -1)  {
            this.centerBlock = calcCenterBlock(POSSIBLE_DIRECTIONS[this.facingDirection]);
        }
        return this.centerBlock;
    }

    /*
      West = -x
      East = +X
      North = -Z
      South = +Z
      this function will return the center most point based on the lengths of the mutli-block and the
      direction that is fed in
     */
    public BlockPos calcCenterBlock(Direction inputDirection)  {
        int xCoord = this.pos.getX();
        int yCoord = this.pos.getY();
        int zCoord = this.pos.getZ();
        switch(inputDirection)  {
            case NORTH:
                return new BlockPos(xCoord, yCoord, zCoord + (zLength / 2));
            case SOUTH:
                return new BlockPos(xCoord, yCoord, zCoord - (zLength / 2));
            case WEST:
                return new BlockPos(xCoord  + (xLength / 2), yCoord, zCoord);
            case EAST:
                return new BlockPos(xCoord  - (xLength / 2), yCoord, zCoord);
        }
        return null;
    }

    /*
      Checks a frame blocks blockstate to see if it is powered by redstone
     */
    public boolean isRedstonePowered()  {
        if(this.redstoneIn != null)  {
            MultiBlockFrameTile frameTile = getFrameTile(this.redstoneIn);
            return frameTile.isRedstonePowered(this.redstoneIn);
        }
        return false;
    }

    /*
      Sets the value for the frame blocks REDSTONE blockstate
     */
    public void sendOutRedstone(int power)  {
        if(this.redstoneOut != null)  {
            MultiBlockFrameTile frameTile = getFrameTile(this.redstoneOut);
            frameTile.setRedstonePower(power);
        }
    }


    public void setCenterBlock(BlockPos posIn)  {this.centerBlock = posIn;}

    public void setDirty(boolean b)  {
        if(b)  {
            markDirty();
        }
    }





}
