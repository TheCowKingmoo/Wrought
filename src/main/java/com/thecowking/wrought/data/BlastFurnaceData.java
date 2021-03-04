package com.thecowking.wrought.data;

import com.thecowking.wrought.data.IMultiblockData;
import com.thecowking.wrought.inventory.containers.blast_furnace.BlastFurnaceContainerBuilder;
import com.thecowking.wrought.inventory.containers.blast_furnace.BlastFurnaceContainerMultiblock;
import com.thecowking.wrought.inventory.containers.honey_comb_coke_oven.HCCokeOvenContainerMultiblock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.thecowking.wrought.util.RegistryHandler.*;

public class BlastFurnaceData implements IMultiblockData {
    private static final Logger LOGGER = LogManager.getLogger();


    protected static Block frameBlock = BLAST_FURANCE_BRICK_FRAME.get();
    protected static Block secondaryFrameBlock = REFACTORY_BRICK.get();
    protected static Block secondaryStairBlock = REFACTORY_BRICK_STAIR.get();
    protected static Block secondarySlabBlock = REFACTORY_BRICK_SLAB.get();


    private static Block controllerBlock = BLAST_FURANCE_BRICK_CONTROLLER.get();
    private static Block airBlock = Blocks.AIR;




    private final Block[][][] posArray =  {
            {       // Level 0 -  5 wide circle - Solid
                    {null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null},
                    {null, null, null, frameBlock, frameBlock, frameBlock, null, null, null},
                    {null, null, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, null, null},
                    {null, null, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, null, null},
                    {null, null, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, null, null},
                    {null, null, null, frameBlock, frameBlock, frameBlock, null, null, null},
                    {null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null}
            },
            {       // Level 1 - 5 wide circle - Hollow
                    {null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null},
                    {null, null, null, frameBlock, frameBlock, frameBlock, null, null, null},
                    {null, null, frameBlock, airBlock, airBlock, airBlock, frameBlock, null, null},
                    {null, null, frameBlock, airBlock, airBlock, airBlock, frameBlock, null, null},
                    {null, null, frameBlock, airBlock, airBlock, airBlock, frameBlock, null, null},
                    {null, null, null, frameBlock, frameBlock, frameBlock, null, null, null},
                    {null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null}
            },
            {       // Level 2 - 5 wide circle - Hollow
                    {null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null},
                    {null, null, null, frameBlock, controllerBlock, frameBlock, null, null, null},
                    {null, null, frameBlock, airBlock, airBlock, airBlock, frameBlock, null, null},
                    {null, null, frameBlock, airBlock, airBlock, airBlock, frameBlock, null, null},
                    {null, null, frameBlock, airBlock, airBlock, airBlock, frameBlock, null, null},
                    {null, null, null, frameBlock, frameBlock, frameBlock, null, null, null},
                    {null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null}
            },
            {       // Level 3 - 5 wide circle - Hollow
                    {null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null},
                    {null, null, null, frameBlock, frameBlock, frameBlock, null, null, null},
                    {null, null, frameBlock, airBlock, airBlock, airBlock, frameBlock, null, null},
                    {null, null, frameBlock, airBlock, airBlock, airBlock, frameBlock, null, null},
                    {null, null, frameBlock, airBlock, airBlock, airBlock, frameBlock, null, null},
                    {null, null, null, frameBlock, frameBlock, frameBlock, null, null, null},
                    {null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null}
            },
            {       // Level 4 - 7 wide circle
                    {null, null, null, null, null, null, null, null, null},
                    {null, null, null, frameBlock, frameBlock, frameBlock, null, null, null},
                    {null, null, frameBlock, airBlock, airBlock, airBlock, frameBlock, null, null},
                    {null, frameBlock, airBlock, airBlock, airBlock, airBlock, airBlock, frameBlock, null},
                    {null, frameBlock, airBlock, airBlock, airBlock, airBlock, airBlock, frameBlock, null},
                    {null, frameBlock, airBlock, airBlock, airBlock, airBlock, airBlock, frameBlock, null},
                    {null, null, frameBlock, airBlock, airBlock, airBlock, frameBlock, null, null},
                    {null, null, null, frameBlock, frameBlock, frameBlock, null, null, null},
                    {null, null, null, null, null, null, null, null, null}
            },
            {       // Level 5 - 7 wide circle
                    {null, null, null, secondarySlabBlock, secondarySlabBlock, secondarySlabBlock, null, null, null},
                    {null, null, secondarySlabBlock, frameBlock, frameBlock, frameBlock, secondarySlabBlock, null, null},
                    {null, secondarySlabBlock, frameBlock, airBlock, airBlock, airBlock, frameBlock, secondarySlabBlock, null},
                    {secondarySlabBlock, frameBlock, airBlock, airBlock, airBlock, airBlock, airBlock, frameBlock, secondarySlabBlock},
                    {secondarySlabBlock, frameBlock, airBlock, airBlock, airBlock, airBlock, airBlock, frameBlock, secondarySlabBlock},
                    {secondarySlabBlock, frameBlock, airBlock, airBlock, airBlock, airBlock, airBlock, frameBlock, secondarySlabBlock},
                    {null, secondarySlabBlock, frameBlock, airBlock, airBlock, airBlock, frameBlock, secondarySlabBlock, null},
                    {null, null, secondarySlabBlock, frameBlock, frameBlock, frameBlock, secondarySlabBlock, null, null},
                    {null, null, null, secondarySlabBlock, secondarySlabBlock, secondarySlabBlock, null, null, null}
            },
            {       // Level 6 - 9 wide circle
                    {null, null, frameBlock, frameBlock, secondaryFrameBlock, frameBlock, frameBlock, null, null},
                    {null, frameBlock, airBlock, airBlock, airBlock, airBlock, airBlock, frameBlock, null},
                    {frameBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, frameBlock},
                    {frameBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, frameBlock},
                    {secondaryFrameBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, secondaryFrameBlock},
                    {frameBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, frameBlock},
                    {frameBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, frameBlock},
                    {null, frameBlock, airBlock, airBlock, airBlock, airBlock, airBlock, frameBlock, null},
                    {null, null, frameBlock, frameBlock, secondaryFrameBlock, frameBlock, frameBlock, null, null}
            },
            {       // Level 7 - 7 wide circle
                    {null, null, null, secondarySlabBlock, secondaryStairBlock, secondarySlabBlock, null, null, null},
                    {null, null, secondarySlabBlock, frameBlock, secondaryFrameBlock, frameBlock, secondarySlabBlock, null, null},
                    {null, secondarySlabBlock, frameBlock, airBlock, airBlock, airBlock, frameBlock, secondarySlabBlock, null},
                    {secondarySlabBlock, frameBlock, airBlock, airBlock, airBlock, airBlock, airBlock, frameBlock, secondarySlabBlock},
                    {secondaryStairBlock, secondaryFrameBlock, airBlock, airBlock, airBlock, airBlock, airBlock, secondaryFrameBlock, secondaryStairBlock},
                    {secondarySlabBlock, frameBlock, airBlock, airBlock, airBlock, airBlock, airBlock, frameBlock, secondarySlabBlock},
                    {null, secondarySlabBlock, frameBlock, airBlock, airBlock, airBlock, frameBlock, secondarySlabBlock, null},
                    {null, null, secondarySlabBlock, frameBlock, secondaryFrameBlock, frameBlock, secondarySlabBlock, null, null},
                    {null, null, null, secondarySlabBlock, secondaryStairBlock, secondarySlabBlock, null, null, null}
            },
            {       // Level 8 - 7 wide circle
                    {null, null, null, null, null, null, null, null, null},
                    {null, null, null, frameBlock, secondaryFrameBlock, frameBlock, null, null, null},
                    {null, null, frameBlock, airBlock, airBlock, airBlock, frameBlock, null, null},
                    {null, frameBlock, airBlock, airBlock, airBlock, airBlock, airBlock, frameBlock, null},
                    {null, secondaryFrameBlock, airBlock, airBlock, airBlock, airBlock, airBlock, secondaryFrameBlock, null},
                    {null, frameBlock, airBlock, airBlock, airBlock, airBlock, airBlock, frameBlock, null},
                    {null, null, frameBlock, airBlock, airBlock, airBlock, frameBlock, null, null},
                    {null, null, null, frameBlock, secondaryFrameBlock, frameBlock, null, null, null},
                    {null, null, null, null, null, null, null, null, null}
            },
            {       // Level 9 - 5 wide circle - Hollow
                    {null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, secondaryStairBlock, null, null, null, null},
                    {null, null, null, frameBlock, secondaryFrameBlock, frameBlock, null, null, null},
                    {null, null, frameBlock, airBlock, airBlock, airBlock, frameBlock, null, null},
                    {null, secondaryStairBlock, secondaryFrameBlock, airBlock, airBlock, airBlock, secondaryFrameBlock, secondaryStairBlock, null},
                    {null, null, frameBlock, airBlock, airBlock, airBlock, frameBlock, null, null},
                    {null, null, null, frameBlock, secondaryFrameBlock, frameBlock, null, null, null},
                    {null, null, null, null, secondaryStairBlock, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null}
            },
            {       // Level 10 - 5 wide circle - Hollow
                    {null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null},
                    {null, null, null, frameBlock, secondaryFrameBlock, frameBlock, null, null, null},
                    {null, null, frameBlock, airBlock, airBlock, airBlock, frameBlock, null, null},
                    {null, null, secondaryFrameBlock, airBlock, airBlock, airBlock, secondaryFrameBlock, null, null},
                    {null, null, frameBlock, airBlock, airBlock, airBlock, frameBlock, null, null},
                    {null, null, null, frameBlock, secondaryFrameBlock, frameBlock, null, null, null},
                    {null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null}
            },
            {       // Level 11 - 5 wide circle - Hollow
                    {null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null},
                    {null, null, null, frameBlock, secondaryFrameBlock, frameBlock, null, null, null},
                    {null, null, frameBlock, airBlock, airBlock, airBlock, frameBlock, null, null},
                    {null, null, secondaryFrameBlock, airBlock, airBlock, airBlock, secondaryFrameBlock, null, null},
                    {null, null, frameBlock, airBlock, airBlock, airBlock, frameBlock, null, null},
                    {null, null, null, frameBlock, secondaryFrameBlock, frameBlock, null, null, null},
                    {null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null}
            },

            {       // Level 12 - 3 wide circle - Hollow
                    {null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, secondaryStairBlock, null, null, null, null},
                    {null, null, null, secondarySlabBlock, secondaryFrameBlock, secondarySlabBlock, null, null, null},
                    {null, null, secondaryStairBlock, secondaryFrameBlock, airBlock, secondaryFrameBlock, secondaryStairBlock, null, null},
                    {null, null, null, secondarySlabBlock, secondaryFrameBlock, secondarySlabBlock, null, null, null},
                    {null, null, null, null, secondaryStairBlock, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null}
            },
            {       // Level 15 - 3 wide circle - Hollow
                    {null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, secondaryFrameBlock, null, null, null, null},
                    {null, null, null, secondaryFrameBlock, airBlock, secondaryFrameBlock, null, null, null},
                    {null, null, null, null, secondaryFrameBlock, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null}
            },
            {       // Level 15 - 3 wide circle - Hollow
                    {null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, secondaryFrameBlock, null, null, null, null},
                    {null, null, null, secondaryFrameBlock, airBlock, secondaryFrameBlock, null, null, null},
                    {null, null, null, null, secondaryFrameBlock, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null}
            },
            {       // Level 15 - 3 wide circle - Hollow
                    {null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, secondaryFrameBlock, null, null, null, null},
                    {null, null, null, secondaryFrameBlock, airBlock, secondaryFrameBlock, null, null, null},
                    {null, null, null, null, secondaryFrameBlock, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null}
            },
            {       // Level 16 - 3 wide circle - Hollow
                    {null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, secondaryStairBlock, null, null, null, null},
                    {null, null, null, secondaryStairBlock, secondarySlabBlock, secondaryStairBlock, null, null, null},
                    {null, null, null, null, secondaryStairBlock, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null}
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
    public Block[][][] getPosArray() { return this.posArray; }
    public Block getBlockMember(int x, int y, int z)  {return this.posArray[x][y][z];}

    public int getControllerYIndex()  {
        return 3;
    }



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
                return new BlockPos(xCoord, yCoord, zCoord + 2);
            case SOUTH:
                return new BlockPos(xCoord, yCoord, zCoord - 2);
            case WEST:
                return new BlockPos(xCoord  + 2, yCoord, zCoord);
            case EAST:
                return new BlockPos(xCoord  - 2, yCoord, zCoord);
        }
        return null;
    }


    /*
        West = -x
        East = +X
        North = -Z
        South = +Z
     */
    public Direction getStairsDirection(BlockPos controllerPos, BlockPos blockPos, Direction controllerDirection, int x, int z)  {
        BlockPos centerBlock = calcCenterBlock(controllerDirection, controllerPos);
        int calcX = blockPos.getX() - centerBlock.getX();
        int calcZ = blockPos.getZ() - centerBlock.getZ();
        // Care more about West-East
        if(Math.abs(calcX) > Math.abs(calcZ))  {
            if(calcX > 0)  {
                //Point East
                return Direction.WEST;
            }  else  {
                //Point West
                return Direction.EAST;

            }

        // Care more about North_south
        }  else  {
            if(calcZ > 0)  {
                //Point South
                return Direction.NORTH;

            }  else  {
                //Point North
                return Direction.SOUTH;
            }
        }
    }

    public SlabType getSlabDirection(int y)  {
        if(y < 6)  {
            return SlabType.TOP;
        }
        return SlabType.BOTTOM;

    }



    public INamedContainerProvider getContainerProvider(World world, BlockPos controllerPos, boolean isFormed) {
        return new INamedContainerProvider() {
            @Override
            public ITextComponent getDisplayName() {
                return new TranslationTextComponent("Honey Comb Coke Oven");
            }

            @Override
            public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                LOGGER.info(isFormed);
                if(isFormed)  {
                    return new BlastFurnaceContainerMultiblock(i, world, controllerPos, playerInventory);
                }
                return new BlastFurnaceContainerBuilder(i, world, controllerPos, playerInventory);
            }
        };
    }


    /*
    West = -x
    East = +X
    North = -Z
    South = +Z
    this function will return the North-Western corner of the multi blocks to be formed
    this is the lowest int value of the multiblock which makes it easier to iterate over when forming
  */
    public BlockPos findLowsestValueCorner(BlockPos centerPos, Direction inputDirection, int longerSide, int shorterSide)  {
        if(centerPos == null)  return null;

        int xCoord = centerPos.getX();
        int yCoord = centerPos.getY();
        int zCoord = centerPos.getZ();

        LOGGER.info("center = " + centerPos);

        switch(inputDirection)  {
            case NORTH:
                return new BlockPos(xCoord - (shorterSide / 2), yCoord - getControllerYIndex() , zCoord - (longerSide / 2));
            case SOUTH:
                return new BlockPos(xCoord  - (shorterSide / 2), yCoord  - getControllerYIndex(), zCoord - (longerSide / 2));
            case WEST:
                return new BlockPos(xCoord  - (longerSide / 2), yCoord  - getControllerYIndex(), zCoord  - (shorterSide / 2));
            case EAST:
                return new BlockPos(xCoord  - (longerSide / 2), yCoord  - getControllerYIndex(), zCoord  - (shorterSide / 2));
            default:
                return null;
        }
    }
}
