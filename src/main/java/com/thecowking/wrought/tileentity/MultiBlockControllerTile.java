package com.thecowking.wrought.tileentity;

import com.thecowking.wrought.data.IMultiblockData;
import com.thecowking.wrought.data.MultiblockData;
import com.thecowking.wrought.inventory.containers.honey_comb_coke_oven.HCCokeOvenContainer;
import com.thecowking.wrought.tileentity.honey_comb_coke_oven.HCCokeOvenControllerTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.NetworkHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.thecowking.wrought.data.MultiblockData.*;

public class MultiBlockControllerTile extends MultiBlockTile implements IMultiBlockControllerTile, ITickableTileEntity {
    private static final Logger LOGGER = LogManager.getLogger();
    protected BlockPos redstoneIn;
    protected BlockPos redstoneOut;

    private int needsUpdate = 0;

    protected IMultiblockData data;

    protected final int TICKSPEROPERATION = 20;


    public MultiBlockControllerTile(TileEntityType<?> tileEntityTypeIn, IMultiblockData data) {
        super(tileEntityTypeIn);
        this.data = data;
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
        Launches the GUI for the completed multiblock
     */
    public void openGUI(World world, PlayerEntity player, boolean isFormed) {

        NetworkHooks.openGui((ServerPlayerEntity) player, this.data.getContainerProvider(world, this.pos, isFormed), this.pos);
    }




    public IMultiblockData getData()  {
        return this.data;
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
        return this.getBlockState().get(MultiblockData.FORMED);
    }


    @Override
    public void tick() {
    }



}
