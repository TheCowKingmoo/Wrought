package com.thecowking.wrought.util;

import com.thecowking.wrought.blocks.IMultiBlockFrame;
import com.thecowking.wrought.data.IMultiblockData;
import com.thecowking.wrought.tileentity.MultiBlockControllerTile;
import com.thecowking.wrought.tileentity.honey_comb_coke_oven.HCCokeOvenFrameTile;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.properties.SlabType;
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

import static com.thecowking.wrought.data.MultiblockData.getTileFromPos;

public class MultiBlockHelper {
    private static final Logger LOGGER = LogManager.getLogger();

    /*
        Updates blockstates of all members in mutlti-blocks
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
        This attempts to find all the frame blocks in the multi-blocks to determine if we should form the multi-blocks or used to update frame blocks
        that the multi-blocks is being formed or destroyed.
    */
    public static List<BlockPos> getMultiBlockMembers(World world, PlayerEntity player, boolean destroy, Direction direction, BlockPos controllerPos, IMultiblockData data) {
        BlockPos centerPos = data.calcCenterBlock(direction, controllerPos);
        BlockPos lowCorner = data.findLowsestValueCorner(centerPos, direction, data.getLength(), data.getWidth());
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
        used for the controllers gui
        checks for blocks in world that are missing are returns them as a hash map
     */
    public static HashMap<Block, Integer> getMissingBlocks(World world, BlockPos controllerPos, IMultiblockData data) {
        MultiBlockControllerTile controllerTile = getControllerTile(world, controllerPos);
        Direction direction = controllerTile.getDirectionFacing();

        BlockPos centerPos = data.calcCenterBlock(direction, controllerPos);
        BlockPos lowCorner = data.findLowsestValueCorner(centerPos, direction, data.getLength(), data.getWidth());
        LOGGER.info("low = " + lowCorner);
        BlockPos correctLowCorner = new BlockPos(lowCorner.getX(), lowCorner.getY() + 1, lowCorner.getZ());
        HashMap<Block, Integer> missingMembers= new HashMap<>();

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
                    if (currentBlock != correctBlock) {
                        if(missingMembers == null)  {
                            LOGGER.info("somehow missing");
                        }
                        if(missingMembers.get(correctBlock) == null)  {
                            missingMembers.put(correctBlock, 1);
                        }  else  {
                            missingMembers.put(correctBlock, missingMembers.get(correctBlock)  + 1);
                        }
                    }
                }
            }
        }  //end loop
        return missingMembers;
    }

    /*
  Moves what blocks we are looking at with respect to the posArray
  this essentially shifts what block in the world we are looking at with respect to direction and the posArray
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

    /*
      Driver for destroying multi-blocks
     */
    public static void destroyMultiBlock(World world, BlockPos controllerPos) {
        MultiBlockControllerTile controllerTile = getControllerTile(world, controllerPos);
        if(controllerTile == null)  return;
        if(!(controllerTile.isFormed())) return;
        controllerTile.setFormed(false);
        List<BlockPos> multiblockMembers = getMultiBlockMembers(world, null, true, controllerTile.getDirectionFacing(), controllerPos, controllerTile.getData());
        if (multiblockMembers != null) {
            updateMultiBlockMemberTiles(world, controllerPos, multiblockMembers, true);
        }
    }

    /*
        Conv method to get the controller TE
     */
    public static MultiBlockControllerTile getControllerTile(World world, BlockPos controllerPos)  {
        TileEntity te = world.getTileEntity(controllerPos);
        if(te == null)  return null;
        if(!(te instanceof MultiBlockControllerTile))   return null;
        return (MultiBlockControllerTile)te;

    }

    /*
    Driver for forming and double checking correct blocks are in the place for the multi block
    */
    public static void tryToFormMultiBlock(World world, PlayerEntity player, BlockPos controllerPos, IMultiblockData data) {
        MultiBlockControllerTile controllerTile = getControllerTile(world, controllerPos);
        if(world.isRemote())  return;

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


    /*
        Method that will check a players inventory for correct blocks to build the multi block structure with
     */
    public static void autoBuildMultiblock(World world, PlayerEntity player, BlockPos controllerPos, IMultiblockData data)  {
        MultiBlockControllerTile controllerTile = getControllerTile(world, controllerPos);
        Direction direction = controllerTile.getDirectionFacing();

        BlockPos centerPos = data.calcCenterBlock(direction, controllerPos);
        BlockPos lowCorner = data.findLowsestValueCorner(centerPos, direction, data.getLength(), data.getWidth());
        BlockPos correctLowCorner = new BlockPos(lowCorner.getX(), lowCorner.getY() + 1, lowCorner.getZ());

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
                    if (currentBlock != correctBlock && currentBlock == Blocks.AIR) {
                        // find the index of the item in inventory
                        int index = InventoryUtils.getIndexOfSingleItemInPlayerInventory(player, correctBlock.asItem());
                        if(index != -1)  {
                            player.inventory.mainInventory.get(index).shrink(1);
                            LOGGER.info(correctBlock);

                            // TODO - new method for placing that checks players permissons and other edge cases
                            if(correctBlock instanceof StairsBlock)  {
                                LOGGER.info("at stairs");
                                Direction stairsDirection = data.getStairsDirection(controllerPos, current, direction,z,x);
                                LOGGER.info(stairsDirection);
                                world.setBlockState(current, correctBlock.getDefaultState().with(StairsBlock.FACING, stairsDirection));
                            }  else if(correctBlock instanceof SlabBlock)  {
                                world.setBlockState(current, correctBlock.getDefaultState().with(SlabBlock.TYPE, data.getSlabDirection(y)));
                            }   else  {
                                world.setBlockState(current, correctBlock.getDefaultState());
                            }

                        }  else  {
                            LOGGER.info("Cannot find item in players inventory");
                        }


                    }
                }
            }
        }
    }

}
