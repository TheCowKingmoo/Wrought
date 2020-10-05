package com.thecowking.wrought.blocks.honeycomb_coke_oven;

import com.thecowking.wrought.blocks.*;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.thecowking.wrought.util.RegistryHandler.H_C_COKE_CONTROLLER_TILE;



public class HCCokeOvenControllerTile extends MultiBlockControllerTile implements ITickableTileEntity {
    private static final Logger LOGGER = LogManager.getLogger();


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
    private final int TOTAL_X_LENGTH = 7;
    private final int TOTAL_Z_LENGTH = TOTAL_X_LENGTH;
    private final int TOTAL_WIDTH = 7;
    private final int TICKSPEROPERATION = 20;
    private int tickCounter = 0;

    public HCCokeOvenControllerTile() {
        super(H_C_COKE_CONTROLLER_TILE.get());
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
        BlockPos centerPos = findMultiBlockCenter();
        BlockPos lowCorner = findLowsestValueCorner(centerPos, TOTAL_X_LENGTH, TOTAL_HEIGHT, TOTAL_Z_LENGTH);
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
        LOGGER.info("FORM MULTIBLOCK");
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

    @Override
    public void openGUI(World worldIn, BlockPos pos, PlayerEntity player, IMultiBlockControllerTile tileEntity)  {
        INamedContainerProvider containerProvider = new INamedContainerProvider() {
            @Override
            public ITextComponent getDisplayName() {
                return new TranslationTextComponent("screen.wrought.h_c");
            }

            @Override
            public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                return new HCCokeOvenContainer(i, worldIn, pos, playerInventory, playerEntity);
            }
        };
        NetworkHooks.openGui((ServerPlayerEntity) player, containerProvider, ((HCCokeOvenControllerTile)tileEntity).getPos());
    }


    /*
      West = -x
      East = +X
      North = -Z
      South = +Z
      this function will return the center most point
     */
    public BlockPos findMultiBlockCenter()  {
        Direction facing = getDirectionFacing(world);
        int xCoord = pos.getX();
        int yCoord = pos.getY() - (TOTAL_HEIGHT / 2);
        int zCoord = pos.getZ();
        BlockPos centerPos = null;

        if(facing == Direction.NORTH)  {
            centerPos = new BlockPos(xCoord, yCoord, zCoord + (TOTAL_Z_LENGTH / 2));
        } else if(facing == Direction.SOUTH)  {
            centerPos = new BlockPos(xCoord, yCoord, zCoord - (TOTAL_Z_LENGTH / 2));
        } else if(facing == Direction.WEST)  {
            centerPos = new BlockPos(xCoord  + (TOTAL_X_LENGTH / 2), yCoord, zCoord);
        } else if(facing == Direction.EAST)  {
            centerPos = new BlockPos(xCoord  - (TOTAL_X_LENGTH / 2), yCoord, zCoord);
        } else  {
            LOGGER.info("find center position got a non cardinal direction!");
            return null;
        }
        return centerPos;
    }




}
