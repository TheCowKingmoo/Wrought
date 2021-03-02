package com.thecowking.wrought.blocks.honey_comb_coke_oven;

import com.thecowking.wrought.blocks.IMultiblockData;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

import static com.thecowking.wrought.util.RegistryHandler.*;
import static com.thecowking.wrought.util.RegistryHandler.H_C_COKE_FRAME_SLAB;

public class HCCokeOven implements IMultiblockData {
    // Members that make up the multi-block
    protected static Block frameBlock = H_C_COKE_FRAME_BLOCK.get();
    private static Block controllerBlock = H_C_COKE_CONTROLLER_BLOCK.get();
    private static Block hatchBlock = H_C_COKE_FRAME_BLOCK.get();
    private static Block frameStairs = H_C_COKE_FRAME_STAIR.get();
    private static Block frameSlab = H_C_COKE_FRAME_SLAB.get();



    /*
array holding the blocks location of all members in the multi-blocks
split up by the y level where posArray[0][x][z] = the bottom most layer
 */
    private final Block[][][] posArray = {
            // bottom level
            {
                    {null, null,       null,       null,       null,       null,       null},
                    {null, null,       frameBlock, frameBlock, frameBlock, null,       null},
                    {null, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, null},
                    {null, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, null},
                    {null, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, null},
                    {null, null,       frameBlock, frameBlock, frameBlock, null,       null},
                    {null, null,       null,       null,       null,       null,       null}
            },
            {
                    {null,  null, frameBlock, frameBlock, frameBlock, null, null},
                    {null,  frameBlock, Blocks.AIR, Blocks.AIR, Blocks.AIR, frameBlock, null},
                    {frameBlock,  Blocks.AIR, Blocks.AIR, Blocks.AIR, Blocks.AIR, Blocks.AIR, frameBlock},
                    {frameBlock,  Blocks.AIR, Blocks.AIR, Blocks.AIR, Blocks.AIR, Blocks.AIR, frameBlock},
                    {frameBlock,  Blocks.AIR, Blocks.AIR, Blocks.AIR, Blocks.AIR, Blocks.AIR, frameBlock},
                    {null,  frameBlock, Blocks.AIR, Blocks.AIR, Blocks.AIR, frameBlock, null},
                    {null,  null, frameBlock, frameBlock, frameBlock, null, null}
            },
            {
                    {null,  null, frameBlock, controllerBlock, frameBlock, null, null},
                    {null,  frameBlock, Blocks.AIR, Blocks.AIR, Blocks.AIR, frameBlock, null},
                    {frameBlock,  Blocks.AIR, Blocks.AIR, Blocks.AIR, Blocks.AIR, Blocks.AIR, frameBlock},
                    {frameBlock,  Blocks.AIR, Blocks.AIR, Blocks.AIR, Blocks.AIR, Blocks.AIR, frameBlock},
                    {frameBlock,  Blocks.AIR, Blocks.AIR, Blocks.AIR, Blocks.AIR, Blocks.AIR, frameBlock},
                    {null,  frameBlock, Blocks.AIR, Blocks.AIR, Blocks.AIR, frameBlock, null},
                    {null,  null, frameBlock, frameBlock, frameBlock, null, null}
            },
            {
                    {null,  null, frameBlock, frameBlock, frameBlock, null, null},
                    {null,  frameBlock, Blocks.AIR, Blocks.AIR, Blocks.AIR, frameBlock, null},
                    {frameBlock,  Blocks.AIR, Blocks.AIR, Blocks.AIR, Blocks.AIR, Blocks.AIR, frameBlock},
                    {frameBlock,  Blocks.AIR, Blocks.AIR, Blocks.AIR, Blocks.AIR, Blocks.AIR, frameBlock},
                    {frameBlock,  Blocks.AIR, Blocks.AIR, Blocks.AIR, Blocks.AIR, Blocks.AIR, frameBlock},
                    {null,  frameBlock, Blocks.AIR, Blocks.AIR, Blocks.AIR, frameBlock, null},
                    {null,  null, frameBlock, frameBlock, frameBlock, null, null}
            },
            {
                    {null, null, frameStairs, frameStairs, frameStairs, null, null},
                    {null, frameStairs, frameBlock, frameBlock, frameBlock, frameStairs, null},
                    {frameStairs, frameBlock, Blocks.AIR, Blocks.AIR, Blocks.AIR, frameBlock, frameStairs},
                    {frameStairs, frameBlock, Blocks.AIR, Blocks.AIR, Blocks.AIR, frameBlock, frameStairs},
                    {frameStairs, frameBlock, Blocks.AIR, Blocks.AIR, Blocks.AIR, frameBlock, frameStairs},
                    {null, frameStairs, frameBlock, frameBlock, frameBlock, frameStairs, null},
                    {null, null, frameStairs, frameStairs, frameStairs, null, null}
            },
            {
                    {null, null, null, null, null, null, null},
                    {null, null, frameSlab, frameStairs, frameSlab, null, null},
                    {null, frameSlab, frameBlock, frameBlock, frameBlock, frameSlab, null},
                    {null, frameStairs, frameBlock, hatchBlock, frameBlock, frameStairs, null},
                    {null, frameSlab, frameBlock, frameBlock, frameBlock, frameSlab, null},
                    {null, null, frameSlab, frameStairs, frameSlab, null, null},
                    {null, null, null, null, null, null, null}
            }
    };



    public int getHeight()  {
        return posArray.length;
    }
    public int getWidth()  {
        return posArray[0][0].length;
    }
    public int getLength()  {
        return posArray[0].length;
    }
    public Block[][][] getPosArray()  {return this.posArray;}
    public Block getBlockMember(int x, int y, int z)  {return this.posArray[x][y][z];}

    /*
West = -x
East = +X
North = -Z
South = +Z
this function will return the center most point based on the lengths of the mutli-blocks and the
direction that is fed in
*/
    public BlockPos calcCenterBlock(Direction inputDirection, BlockPos controllerPos, IMultiblockData data)  {
        int xCoord = controllerPos.getX();
        int yCoord = controllerPos.getY();
        int zCoord = controllerPos.getZ();
        switch(inputDirection)  {
            case NORTH:
                return new BlockPos(xCoord, yCoord, zCoord + (data.getLength() / 2));
            case SOUTH:
                return new BlockPos(xCoord, yCoord, zCoord - (data.getLength() / 2));
            case WEST:
                return new BlockPos(xCoord  + (data.getLength() / 2), yCoord, zCoord);
            case EAST:
                return new BlockPos(xCoord  - (data.getLength() / 2), yCoord, zCoord);
        }
        return null;
    }

}
