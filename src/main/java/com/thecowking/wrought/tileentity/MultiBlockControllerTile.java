package com.thecowking.wrought.tileentity;

import com.thecowking.wrought.blocks.Multiblock;
import com.thecowking.wrought.tileentity.honey_comb_coke_oven.HCCokeOvenFrameTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.thecowking.wrought.blocks.Multiblock.*;

public class MultiBlockControllerTile extends MultiBlockTile implements IMultiBlockControllerTile, ITickableTileEntity {
    private static final Logger LOGGER = LogManager.getLogger();
    protected BlockPos redstoneIn;
    protected BlockPos redstoneOut;

    private int needsUpdate = 0;


    protected int length;
    protected int width;
    protected int height;

    protected final int TICKSPEROPERATION = 20;


    public MultiBlockControllerTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    /*
    Does the needed checks and casting to see if current BlockPos holds a correct member of multi-blocks
 */
    private boolean checkIfCorrectFrame(BlockPos currentPos)  {
        Block currentBlock = world.getBlockState(currentPos).getBlock();
        BlockState currentState = world.getBlockState(currentPos);
        if( currentState.hasTileEntity() || !(currentState.isAir(world, currentPos))) {
            return checkIfCorrectFrame(currentBlock);
        }
        return false;
    }

    public void assignJobs() {
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
      This is called when a controller is right clicked by a player when the multi-blocks is not formed
      Checks to make sure that the player is holding the correct item in hand to form the multi-blocks.
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
                LOGGER.info("redstone out blocks is null");
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
      Lets nearby blocks know we need an update
     */
    protected void blockUpdate() {
        this.world.notifyBlockUpdate(this.pos, this.getBlockState(), this.getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
    }


    @Override
    public void openGUI(World worldIn, BlockPos pos, PlayerEntity player, IMultiBlockControllerTile tileEntity) {}

    public boolean checkIfCorrectFrame(Block block)  {
        return true;
    }

    public BlockPos getControllerPos() {
        return this.pos;
    }


    public boolean isFormed()  {
        return this.getBlockState().get(Multiblock.FORMED);
    }


    @Override
    public void tick() {
    }



}
