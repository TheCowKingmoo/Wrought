package com.thecowking.wrought.blocks;

import com.thecowking.wrought.blocks.honeycomb_coke_oven.HCCokeOvenFrameBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.thecowking.wrought.blocks.Multiblock.*;

public class MultiBlockControllerTile extends MultiBlockTile implements IMultiBlockControllerTile {
    private static final Logger LOGGER = LogManager.getLogger();


    protected int facingDirection = -1;
    protected final Direction[] POSSIBLE_DIRECTIONS = {Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};
    protected BlockPos redstoneIn;
    protected BlockPos redstoneOut;
    protected BlockPos centerBlock;

    protected int length;
    protected int width;
    protected int height;

    public MultiBlockControllerTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
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
        return this.world.getBlockState(this.pos).get(BlockStateProperties.FACING);
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
            if(frameTile != null)  {
                frameTile.setRedstonePower(power);
            }  else  {
                LOGGER.info("redstone out block is null");
            }
        }
    }


    public void setCenterBlock(BlockPos posIn)  {this.centerBlock = posIn;}

    public void setDirty(boolean b)  {
        if(b)  {
            markDirty();
        }
    }





}
