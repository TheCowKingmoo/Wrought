package com.thecowking.wrought.data;

import com.thecowking.wrought.inventory.containers.honey_comb_coke_oven.HCCokeOvenContainer;
import com.thecowking.wrought.inventory.containers.honey_comb_coke_oven.HCCokeOvenContainerMultiblock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import static com.thecowking.wrought.util.RegistryHandler.*;
import static com.thecowking.wrought.util.RegistryHandler.H_C_COKE_FRAME_SLAB;

public class HCCokeOvenData implements IMultiblockData {
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
    public BlockPos calcCenterBlock(Direction inputDirection, BlockPos controllerPos)  {
        int xCoord = controllerPos.getX();
        int yCoord = controllerPos.getY();
        int zCoord = controllerPos.getZ();
        switch(inputDirection)  {
            case NORTH:
                return new BlockPos(xCoord, yCoord, zCoord + (getLength() / 2));
            case SOUTH:
                return new BlockPos(xCoord, yCoord, zCoord - (getLength() / 2));
            case WEST:
                return new BlockPos(xCoord  + (getLength() / 2), yCoord, zCoord);
            case EAST:
                return new BlockPos(xCoord  - (getLength() / 2), yCoord, zCoord);
        }
        return null;
    }


    public Direction getStairsDirection(Direction controllerDirection, int x, int z)  {
        if(x < 2)  {
            return controllerDirection.getOpposite();
        } else if( x > 4)  {
            return controllerDirection;
        } else if(z < 2)  {

            if(controllerDirection == Direction.NORTH)  {
                return controllerDirection.rotateY();
            }  else  {
                return controllerDirection.rotateY().getOpposite();
            }
        } else  {
            if(controllerDirection == Direction.NORTH)  {
                return controllerDirection.rotateY().getOpposite();
            }  else  {
                return controllerDirection.rotateY();
            }
        }
    }

    public INamedContainerProvider getContainerProvider(World world, BlockPos controllerPos, boolean isFormed)  {
        return new INamedContainerProvider() {
            @Override
            public ITextComponent getDisplayName() {
                return new TranslationTextComponent("Honey Comb Coke Oven");
            }

            @Override
            public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                if(isFormed)  {
                    // Multiblock container
                    return new HCCokeOvenContainerMultiblock(i, world, controllerPos, playerInventory);
                }
                // autobuilding container
                return new HCCokeOvenContainer(i, world, controllerPos, playerInventory);
            }
        };
    }


}
