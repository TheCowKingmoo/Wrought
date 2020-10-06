package com.thecowking.wrought.blocks.honeycomb_coke_oven;

import com.thecowking.wrought.blocks.MultiBlockFrameBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import java.util.logging.Logger;

import static com.thecowking.wrought.blocks.Multiblock.getTileFromPos;

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
    }

    @Override
    public void onExplosionDestroy(World worldIn, BlockPos posIn, Explosion explosionIn) {
        updateMultiBlock(worldIn, posIn);
    }

    private void updateMultiBlock(World worldIn, BlockPos posIn)  {
        // We need the Tile Entity to grab the controller position
        TileEntity tileEntity = getTileFromPos(worldIn, posIn);

        if(tileEntity instanceof HCCokeOvenFrameTile)  {
            HCCokeOvenFrameTile castedTile = (HCCokeOvenFrameTile) tileEntity;
            if(!(castedTile.isFormed(worldIn)))  {
                LOGGER.info("multiblock is not formed");
                return;
            }
            BlockPos controllerPos = castedTile.getController();
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


}
