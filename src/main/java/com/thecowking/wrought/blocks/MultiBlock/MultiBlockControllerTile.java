package com.thecowking.wrought.blocks.MultiBlock;

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
import net.minecraftforge.common.util.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.thecowking.wrought.blocks.MultiBlock.Multiblock.*;

public class MultiBlockControllerTile extends MultiBlockTile implements IMultiBlockControllerTile {
    private static final Logger LOGGER = LogManager.getLogger();
    protected BlockPos redstoneIn;
    protected BlockPos redstoneOut;

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

    /*
      Grabs the Frame Tile Entity
     */
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

    /*
      Returns the controllers facing direction
     */
    public Direction getDirectionFacing()  {
        return this.world.getBlockState(this.pos).get(BlockStateProperties.FACING);
    }

    /*
      Checks a frame blocks blockstate to see if it is powered by redstone
     */
    public boolean isRedstonePowered(BlockPos posIn)  {
        if(this.redstoneIn != null)  {
            MultiBlockFrameTile frameTile = getFrameTile(this.redstoneIn);
            return this.world.isBlockPowered(posIn);
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

    /*
      Marks if we need to save data
     */

    public void setDirty(boolean b)  {
        if(b)  {
            markDirty();
        }
    }

    /*
West = -x
East = +X
North = -Z
South = +Z
this function will return the North-Western corner of the multi block to be formed
*/
    public BlockPos findLowsestValueCorner(BlockPos centerPos, Direction inputDirection, int longerSide, int height, int shorterSide)  {
        if(centerPos == null)  return null;

        int xCoord = centerPos.getX();
        int yCoord = centerPos.getY();
        int zCoord = centerPos.getZ();

        switch(inputDirection)  {
            case NORTH:
                return new BlockPos(xCoord - (shorterSide / 2), yCoord - (height / 2) , zCoord - (longerSide / 2));
            case SOUTH:
                return new BlockPos(xCoord  - (shorterSide / 2), yCoord  - (height / 2), zCoord - (longerSide / 2));
            case WEST:
                return new BlockPos(xCoord  - (longerSide / 2), yCoord  - (height / 2), zCoord  - (shorterSide / 2));
            case EAST:
                return new BlockPos(xCoord  - (longerSide / 2), yCoord  - (height / 2), zCoord  - (shorterSide / 2));
            default:
                return null;
        }
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
                return new BlockPos(xCoord, yCoord, zCoord + (length / 2));
            case SOUTH:
                return new BlockPos(xCoord, yCoord, zCoord - (length / 2));
            case WEST:
                return new BlockPos(xCoord  + (length / 2), yCoord, zCoord);
            case EAST:
                return new BlockPos(xCoord  - (length / 2), yCoord, zCoord);
        }
        return null;
    }


    /*
      Lets nearby blocks know we need an update
     */
    protected void blockUpdate() {
        this.world.notifyBlockUpdate(this.pos, this.getBlockState(), this.getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
    }

    /*
     From here on all the following classes should be overwritten
     */
    @Override
    public void tryToFormMultiBlock(World worldIn, BlockPos pos) {}

    @Override
    public void openGUI(World worldIn, BlockPos pos, PlayerEntity player, IMultiBlockControllerTile tileEntity) {}

    public boolean checkIfCorrectFrame(Block block)  {
        return true;
    }
}
