package com.thecowking.wrought.util;

import com.thecowking.wrought.blocks.IMultiBlockControllerBlock;
import com.thecowking.wrought.blocks.IMultiBlockFrame;
import com.thecowking.wrought.blocks.IMultiblockData;
import com.thecowking.wrought.blocks.honey_comb_coke_oven.HCCokeOvenFrameBlock;
import com.thecowking.wrought.tileentity.MultiBlockControllerTile;
import com.thecowking.wrought.tileentity.honey_comb_coke_oven.HCCokeOvenFrameTile;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.thecowking.wrought.blocks.Multiblock.getTileFromPos;

public class MultiBlockHelper {
    private static final Logger LOGGER = LogManager.getLogger();

    /*
Updates all information in all multiblock members
*/
    private static void updateMultiBlockMemberTiles(World world, BlockPos controllerPos, List<BlockPos> memberArray, boolean destroy) {

        for (int i = 0; i < memberArray.size(); i++) {
            BlockPos current = memberArray.get(i);
            Block currentBlock = world.getBlockState(current).getBlock();
            if(currentBlock instanceof IMultiBlockFrame)  {
                IMultiBlockFrame frameBlock = (IMultiBlockFrame) currentBlock;
                if(destroy)  {
                    frameBlock.removeFromMultiBlock(world.getBlockState(current),current, world);
                }  else  {
                    frameBlock.addingToMultblock(world.getBlockState(current), current, world);
                }
                TileEntity currentTile = getTileFromPos(world, current);
                if (currentTile instanceof HCCokeOvenFrameTile) {
                    HCCokeOvenFrameTile castedCurrent = (HCCokeOvenFrameTile) currentTile;
                    if (destroy) {
                        castedCurrent.destroyMultiBlock();
                    } else {
                        castedCurrent.setupMultiBlock(controllerPos);
                    }
                }
            }
        }
    }

    /*
West = -x
East = +X
North = -Z
South = +Z
this function will return the North-Western corner of the multi blocks to be formed
*/
    public static BlockPos findLowsestValueCorner(BlockPos centerPos, Direction inputDirection, int longerSide, int height, int shorterSide)  {
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
This attempts to find all the frame blocks in the multi-blocks to determine if we should form the multi-blocks or used to update frame blocks
that the multi-blocks is being formed or destroyed.
*/
    public static List<BlockPos> getMultiBlockMembers(World world, PlayerEntity player, boolean destroy, Direction direction, BlockPos controllerPos, IMultiblockData data) {
        BlockPos centerPos = data.calcCenterBlock(direction, controllerPos, data);
        BlockPos lowCorner = findLowsestValueCorner(centerPos, direction, data.getLength(), data.getHeight(), data.getWidth());
        BlockPos correctLowCorner = new BlockPos(lowCorner.getX(), lowCorner.getY() + 1, lowCorner.getZ());
        List<BlockPos> multiblockMembers = new ArrayList();

        // checks the central slice part of the structure to ensure the correct blocks exist
        for (int y = 0; y < data.getHeight(); y++) {
            for (int z = 0; z < data.getLength(); z++) {
                for (int x = 0; x < data.getWidth(); x++) {
                    // get block that should be at these coords
                    Block correctBlock = data.getBlockMember(y,z,x);
                    if (correctBlock == null) {                                // skip the "null" positions (don't care whats in here)
                        continue;
                    }
                    // get current blocks - adjusted for Direction
                    BlockPos current = indexShifterBlockPos(direction, correctLowCorner, x, y, z, data.getLength(), data.getWidth());
                    Block currentBlock = world.getBlockState(current).getBlock();   // get the actual blocks at pos
                    if (currentBlock != correctBlock && !destroy) {
                        if (!destroy) {
                            if (player != null) {
                                String msg = "Could not form because of block at Coord at X:" + current.getX() + " Y:" + current.getY() + " Z:" + current.getZ();
                                player.sendStatusMessage(new TranslationTextComponent(msg), false);
                                msg = "Should be " + correctBlock.getBlock() + " not " + currentBlock.getBlock();
                                player.sendStatusMessage(new TranslationTextComponent(msg), true);
                                player.sendStatusMessage(new TranslationTextComponent(msg), false);
                            }
                            LOGGER.info("Could not form because of " + current);
                            LOGGER.info("should be " + correctBlock + " not " + currentBlock);
                            return null;
                        }
                    }  else  {
                        // add blocks of things to be formed/deleted
                        multiblockMembers.add(current);
                    }
                }
            }
        }  //end loop
        return multiblockMembers;
    }


    /*
  Moves what blocks we are looking at with respect to the posArray
 */
    public static BlockPos indexShifterBlockPos(Direction inputDirection, BlockPos low, int x, int y, int z, int length, int width)  {

        switch (inputDirection)  {
            case NORTH:
                return new BlockPos(low.getX() + x, low.getY() + y, low.getZ() + z);
            case SOUTH:
                return new BlockPos(low.getX() + x, low.getY() + y, low.getZ() + length - z - 1);
            case WEST:
                return new BlockPos(low.getX() + z, low.getY() + y, low.getZ() + x);
            case EAST:
                return new BlockPos(low.getX() + length - z - 1, low.getY() + y, low.getZ() + width - x - 1);
        }
        return null;
    }

    public boolean checkIfCorrectFrame(Block block) {
        return (block instanceof HCCokeOvenFrameBlock);
    }


    /*
      Driver for destroying multi-blocks
     */
    public static void destroyMultiBlock(World world, BlockPos controllerPos, IMultiblockData data) {
        MultiBlockControllerTile controllerTile = getControllerTile(world, controllerPos);
        if(controllerTile == null)  return;
        if(!(controllerTile.isFormed())) return;
        controllerTile.setFormed(false);
        List<BlockPos> multiblockMembers = getMultiBlockMembers(world, null, true, controllerTile.getDirectionFacing(), controllerPos, data);
        if (multiblockMembers != null) {
            updateMultiBlockMemberTiles(world, controllerPos, multiblockMembers, true);
        }
    }
    public static MultiBlockControllerTile getControllerTile(World world, BlockPos controllerPos)  {
        TileEntity te = world.getTileEntity(controllerPos);
        if(te == null)  return null;
        if(!(te instanceof MultiBlockControllerTile))   return null;
        return (MultiBlockControllerTile)te;

    }

    /*
    Driver for forming the multiblock
    */
    public static void tryToFormMultiBlock(World world, PlayerEntity player, BlockPos controllerPos, IMultiblockData data) {
        MultiBlockControllerTile controllerTile = getControllerTile(world, controllerPos);

        // null check
        if(controllerTile == null)  return;

        // ensure that the controller is not already formed
        if(controllerTile.isFormed()) return;

        // get list of all future multiblock members -> not that this will be null if any problems are detected
        List<BlockPos> multiblockMembers = getMultiBlockMembers(world, player,false, controllerTile.getDirectionFacing(), controllerPos, data);
        if (multiblockMembers != null) {
            controllerTile.setFormed(true);
            updateMultiBlockMemberTiles(world, controllerPos, multiblockMembers, false);
            controllerTile.assignJobs();
        }
    }


}
