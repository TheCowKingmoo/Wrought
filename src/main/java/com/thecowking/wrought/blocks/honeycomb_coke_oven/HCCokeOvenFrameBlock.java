package com.thecowking.wrought.blocks.honeycomb_coke_oven;

import com.thecowking.wrought.blocks.*;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

import static com.thecowking.wrought.blocks.Multiblock.*;

public class HCCokeOvenFrameBlock extends MultiBlockFrameBlock  {
    /*
    If part of a multi-block it finds the controller to start the process of taking the multi-block
    apart.
     */
    @Override
    public void onBlockHarvested(World worldIn, BlockPos posIn, BlockState state, PlayerEntity player) {
        // Jump out if we are not Server Side
        if(!(player instanceof ServerPlayerEntity))  {
            return;
        }
        updateMultiBlock(worldIn, posIn);
        super.onBlockHarvested(worldIn, posIn, state, player);
    }


    private void updateMultiBlock(World worldIn, BlockPos posIn)  {
        // We need the Tile Entity to grab the controller position
        TileEntity tileEntity = getTileFromPos(worldIn, posIn);

        if(tileEntity instanceof HCCokeOvenFrameTile)  {
            HCCokeOvenFrameTile castedTile = (HCCokeOvenFrameTile) tileEntity;
            if(!(castedTile.isFormed(castedTile.getPos())))  {
                LOGGER.info("multiblock is not formed");
                return;
            }
            BlockPos controllerPos = castedTile.getControllerPos();
            // if this is null then we have some serious problems
            if(controllerPos == null)  {
                LOGGER.info("err - does not have controller pos");
                return;
            }

            // can finally get the controller tile entity
            tileEntity = getTileFromPos(worldIn, controllerPos);
            if(tileEntity instanceof HCCokeOvenControllerTile)  {
                HCCokeOvenControllerTile controllerTile = (HCCokeOvenControllerTile) tileEntity;
                controllerTile.destroyMultiBlock(worldIn, posIn);
            }
        }

    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos posIn, PlayerEntity player, Hand hand, BlockRayTraceResult trace) {
        // make sure correct side
        if (player instanceof ServerPlayerEntity) {
            // get TE
            TileEntity tileEntity = worldIn.getTileEntity(posIn);
            // make sure TE is correct TE
            if (tileEntity instanceof MultiBlockFrameTile) {
                // cast
                MultiBlockFrameTile frameTile = (MultiBlockFrameTile) tileEntity;
                // check if the multi-structure is even formed
                if(frameTile.isFormed(frameTile.getPos()))  {
                    // contorller pos
                    BlockPos controllerPos = frameTile.getControllerPos();
                    if(controllerPos != null)  {
                        // get the TE at controller pos
                        tileEntity = worldIn.getTileEntity(controllerPos);
                        // make sure correct tile
                        if(tileEntity instanceof HCCokeOvenControllerTile)  {
                            HCCokeOvenControllerTile controllerTile = (HCCokeOvenControllerTile) tileEntity;
                            if(controllerTile.isFormed(controllerPos))  {
                                // OPEN GUI
                                controllerTile.openGUI(worldIn, posIn, player, controllerTile);
                                return super.onBlockActivated(state, worldIn, posIn, player, hand, trace);
                            }

                        }  else  {
                            LOGGER.info("block at contr pos is not correct TE");
                        }
                    }  else  {
                        LOGGER.info("no controller pos");
                    }
                    // need to deconstruct frame block
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
        return super.onBlockActivated(state, worldIn, posIn, player, hand, trace);
    }
}
