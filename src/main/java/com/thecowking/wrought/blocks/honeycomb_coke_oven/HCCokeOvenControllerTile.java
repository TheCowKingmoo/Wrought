package com.thecowking.wrought.blocks.honeycomb_coke_oven;

import com.thecowking.wrought.blocks.MultiBlockControllerBlock;
import com.thecowking.wrought.blocks.MultiBlockControllerTile;
import com.thecowking.wrought.blocks.MultiBlockFrameBlock;
import com.thecowking.wrought.blocks.MultiBlockFrameTile;
import net.minecraft.block.*;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class HCCokeOvenControllerTile extends MultiBlockControllerTile implements ITickableTileEntity {

    private final Block frameBlock = new HCCokeOvenFrameBlock();

    private final Block[][] posArray = {
            {null, null, frameBlock, frameBlock,  frameBlock, null, null, null},
            {null, frameBlock, Blocks.AIR, Blocks.AIR, Blocks.AIR, frameBlock, null, null},
            {frameBlock, Blocks.AIR, Blocks.AIR, Blocks.AIR, Blocks.AIR, Blocks.AIR, frameBlock},
            {frameBlock, Blocks.AIR, Blocks.AIR, Blocks.AIR, Blocks.AIR, Blocks.AIR, frameBlock},
            {frameBlock, Blocks.AIR, Blocks.AIR, Blocks.AIR, Blocks.AIR, Blocks.AIR, frameBlock},
            {null, frameBlock, Blocks.AIR, Blocks.AIR, Blocks.AIR, frameBlock, null, null},
            {null, null, frameBlock, frameBlock,  frameBlock, null, null, null}
    };

    private final int RADIUS = 3;
    private final int CENTRAL_BODY_HEIGHT = 3;
    private final int TOTAL_HEIGHT = 5;
    private final int TOTAL_WIDTH = 7;
    private final int TICKSPEROPERATION = 20;
    private int tickCounter = 0;

    public HCCokeOvenControllerTile() {
        super(null);
    }


    public HCCokeOvenControllerTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Override
    public void tick() {
        if(world.isRemote)  {return;}
        if(!isFormed(world))  {return;}
        if(tickCounter < TICKSPEROPERATION)  {
            tickCounter++;
            return;
        }
        tickCounter = 0;
        // work
        ovenOperation();
    }

    private void ovenOperation()  {
    }


    // TODO - util or bubble up?
    @Override
    public void tryToFormMultiBlock(World worldIn, BlockPos pos) {
        Direction facing = getDirectionFacing(worldIn);
        int modX = findModX(worldIn, facing);
        int modZ = findModZ(worldIn, facing);
        BlockPos centerPoint = new BlockPos(pos.getX() + modX, pos.getY(), pos.getZ() + modZ);
        BlockPos lowCorner = new BlockPos(pos.getX()-3 + modX, pos.getY()-1, pos.getZ() - 3);
        List<BlockPos> multiblockMembers = new ArrayList();

        // checks the central slice part of the structure to ensure the correct blocks exist
        for(int y = 0; y < CENTRAL_BODY_HEIGHT; y++)  {
            for(int x = 0; x < TOTAL_WIDTH; x++)  {
                for(int z = 0; z < TOTAL_WIDTH; z++)  {

                    Block correctBlock = posArray[x][z];                            // get the block that should be at these coord's
                        if( correctBlock == null)  {                                // skip the "null" positions (don't care whats in here)
                        continue;
                    }

                    // get current block
                    BlockPos current = new BlockPos(lowCorner.getX() + x, lowCorner.getY() + y, lowCorner.getZ() + z);

                    if(current == pos)  {                                           // skip the controller
                        continue;
                    }

                    Block currentBlock = world.getBlockState(current).getBlock();   // get the actual block at pos
                    if(currentBlock != correctBlock)  {                             // check vs what should be there
                        // do not form multiblock
                        return;
                    }

                    // this if-elif checks verify that the bottom and top are covered by frame blocks
                    if(y == 0)  {
                        // TODO - check for world lower limit
                        current = new BlockPos(lowCorner.getX() + x, lowCorner.getY() - 1, lowCorner.getZ() + z);
                        currentBlock = world.getBlockState(current).getBlock();
                        if(currentBlock != frameBlock) {
                            return;
                        }
                    }  else if( y == CENTRAL_BODY_HEIGHT - 1) {
                        // TODO - check for world upper limit
                        current = new BlockPos(lowCorner.getX() + x, lowCorner.getY() + 1, lowCorner.getZ() + z);
                        currentBlock = world.getBlockState(current).getBlock();
                        if(currentBlock != frameBlock) {
                            return;
                        }
                    }
                    // add correct blocks to array
                    multiblockMembers.add(current);

                    }
                }
            }  //end loop

        // get this far and we should form multi block
        setFormed(worldIn, true);
        updateMultiBlockMemberTiles(multiblockMembers);

        }  //end trytoform

    /*
      Updates all information in all multiblock members
     */
    private void updateMultiBlockMemberTiles(List<BlockPos> memberArray)  {
        for(int i = 0; i < memberArray.size(); i++)  {
            BlockPos current = memberArray.get(i);
            Block currentBlock = world.getBlockState(current).getBlock();
            if(currentBlock instanceof HCCokeOvenFrameBlock)  {
                HCCokeOvenFrameBlock castedCurrent = (HCCokeOvenFrameBlock)currentBlock;
            }
        }
    }

    // override the always return true method
    // TODO - learn a better way to throw this info upward
    @Override
    public boolean checkIfCorrectFrame(Block block)  {
        return (block instanceof HCCokeOvenFrameBlock);
    }

    /*
      Calculates if the x-coord should be negative or not based on what direction the
      controller faces.
     */
    private int findModX(World worldIn, Direction dir)  {
        Direction facing = getDirectionFacing(worldIn);
        if( facing == Direction.NORTH )  {
            return 1;
        }  else if( facing == Direction.SOUTH)  {
            return 1;
        }  else if( facing == Direction.WEST)  {
            return 1;
        }  else if( facing == Direction.EAST) {
            return 1;
        }
        return 0;
    }

    /*
        Calculates if the z-coord should be negative or not based on what direction the
        controller faces.
    */
    private int findModZ(World worldIn, Direction dir)  {
        Direction facing = getDirectionFacing(worldIn);
        if( facing == Direction.NORTH )  {
            return 1;
        }  else if( facing == Direction.SOUTH)  {
            return 1;
        }  else if( facing == Direction.WEST)  {
            return 1;
        }  else if( facing == Direction.EAST) {
            return 1;
        }
        return 0;
    }

}
