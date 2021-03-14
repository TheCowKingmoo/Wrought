package com.thecowking.wrought.blocks;

import com.thecowking.wrought.data.HCCokeOvenData;
import com.thecowking.wrought.data.MultiblockData;
import com.thecowking.wrought.tileentity.MultiBlockControllerTile;
import com.thecowking.wrought.tileentity.MultiBlockFrameTile;
import com.thecowking.wrought.tileentity.honey_comb_coke_oven.HCCokeOvenControllerTile;
import com.thecowking.wrought.tileentity.honey_comb_coke_oven.HCCokeOvenFrameTile;
import com.thecowking.wrought.util.MultiBlockHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;

import static com.thecowking.wrought.data.MultiblockData.*;


public class MultiBlockFrameBlock extends Block implements IMultiBlockFrame {

    public MultiBlockFrameBlock() {
        super(Properties.create(Material.IRON)
                .sound(SoundType.METAL)
                .hardnessAndResistance(2.0f)
                .harvestTool(ToolType.PICKAXE)
        );
        setDefaultState(this.getDefaultState().with(FORMED, false));
        setDefaultState(this.getDefaultState().with(BlockStateProperties.POWER_0_15, 0));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return state.get(MultiblockData.FORMED);
    }


    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FORMED);
        builder.add(REDSTONE);
    }


    /*
      Will send out a downwards redstone signal
     */
    @SuppressWarnings("deprecation")
    @Override
    public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
         if(side == Direction.UP)   {
             return blockState.get(REDSTONE);
         }
         return 0;
    }

    @SuppressWarnings("deprecation")
    @Override
    public int getStrongPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
        return getWeakPower(blockState, blockAccess, pos, side);
    }

    @SuppressWarnings("deprecation")
    @Deprecated
    public boolean canProvidePower(BlockState state) {
        return true;
    }


    @Override
    public void addingToMultblock(BlockState blockState, BlockPos posIn, World worldIn) {
        worldIn.setBlockState(posIn, blockState.with(FORMED, true));
        this.createTileEntity(blockState, worldIn);
    }

    @Override
    public void removeFromMultiBlock(BlockState blockState, BlockPos posIn, World worldIn) {
        worldIn.setBlockState(posIn, blockState.with(FORMED, false));
    }

    public void updateMultiBlock(World worldIn, BlockPos posIn)  {
        // We need the Tile Entity to grab the controller position
        TileEntity tileEntity = getTileFromPos(worldIn, posIn);

        if(tileEntity instanceof MultiBlockFrameTile)  {
            MultiBlockFrameTile castedTile = (MultiBlockFrameTile) tileEntity;
            if(!(castedTile.isFormed(castedTile.getPos())))  {
                LOGGER.info("multiblock is not formed");
                return;
            }
            BlockPos controllerPos = castedTile.frameGetControllerPos();
            // if this is null then we have some serious problems
            if(controllerPos == null)  {
                LOGGER.info("err - does not have controller pos");
                return;
            }
            MultiBlockHelper.destroyMultiBlock(worldIn, controllerPos);
        }
    }



    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos posIn, PlayerEntity player, Hand hand, BlockRayTraceResult trace) {

        // make sure correct side
        if (player instanceof ServerPlayerEntity && state.get(MultiblockData.FORMED)) {
            // get TE
            TileEntity tileEntity = worldIn.getTileEntity(posIn);
            // make sure TE is correct TE
            if (tileEntity instanceof MultiBlockFrameTile) {
                // cast
                MultiBlockFrameTile frameTile = (MultiBlockFrameTile) tileEntity;
                // check if the multi-structure is even formed
                if(frameTile.isFormed(frameTile.getPos()))  {
                    // contorller pos
                    BlockPos controllerPos = frameTile.frameGetControllerPos();
                    if(controllerPos != null)  {
                        // get the TE at controller pos
                        TileEntity ctileEntity = worldIn.getTileEntity(controllerPos);
                        // make sure correct tile
                        if(ctileEntity instanceof MultiBlockControllerTile)  {
                            MultiBlockControllerTile controllerTile = (MultiBlockControllerTile) ctileEntity;
                            if(controllerTile.isFormed(controllerPos))  {
                                // OPEN GUI
                                controllerTile.openGUI(worldIn, player, true);
                                return ActionResultType.SUCCESS;
                            }

                        }  else  {
                            LOGGER.info("blocks at contr pos is not correct TE");
                        }
                    }  else  {
                        LOGGER.info("no controller pos");
                    }
                    // need to deconstruct frame blocks
                    frameTile.destroyMultiBlock();

                }  else  {
                    LOGGER.info("frame is not formed");
                }
            } else {
                LOGGER.info(posIn);
                LOGGER.info(tileEntity);
                throw new IllegalStateException("Our named container provider is missing!");
            }
        }
        super.onBlockActivated(state, worldIn, posIn, player, hand, trace);
        return ActionResultType.SUCCESS;
    }


    /*
If part of a multi-blocks it finds the controller to start the process of taking the multi-blocks
apart.
 */
    @Override
    public void onBlockHarvested(World worldIn, BlockPos posIn, BlockState state, PlayerEntity player) {
        // Jump out if we are not Server Side
        if(!(player instanceof ServerPlayerEntity))  {
            return;
        }
        if(state.get(MultiblockData.FORMED))  {
            updateMultiBlock(worldIn, posIn);
        }
        super.onBlockHarvested(worldIn, posIn, state, player);
    }

}
