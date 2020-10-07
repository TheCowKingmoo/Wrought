package com.thecowking.wrought.blocks.honeycomb_coke_oven;

import com.thecowking.wrought.blocks.*;
import com.thecowking.wrought.util.Util;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static com.thecowking.wrought.blocks.Multiblock.getTileFromPos;
import static com.thecowking.wrought.util.RegistryHandler.H_C_COKE_CONTROLLER_TILE;
import static com.thecowking.wrought.util.RegistryHandler.H_C_COKE_FRAME_BLOCK;




public class HCCokeOvenControllerTile extends MultiBlockControllerTile implements ITickableTileEntity {
    private static final Logger LOGGER = LogManager.getLogger();

    private static Block frameBlock = H_C_COKE_FRAME_BLOCK.get();

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


    private BlockPos itemInputBlock;
    private BlockPos itemOutputBlock;


    private ItemStackHandler itemHandler = createHandler();
    public LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);



    public HCCokeOvenControllerTile() {
        super(H_C_COKE_CONTROLLER_TILE.get());
    }


    @Override
    public void tick() {

        if(world.isRemote)  {return;}
        if(!isFormed())  {return;}
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

    /*
      This attempts to find all the frame blocks in the multi-block to determine if we should form the multi-block or used to update frame blocks
      that the multi-block is being destoryed.

      posIn - if null then we are using this method to form a multi block. This means that if we find a non
            - frame block then we stop the method and return null.
            - if not null then its for destroying, and we want to continue if we find an error in case of
            - some strange issue such as an explosion wiped away other blocks.
     */

    public List<BlockPos> getMultiBlockMembers(World worldIn, BlockPos posIn)  {
        Direction facing = getDirectionFacing(worldIn);
        BlockPos centerPos = findMultiBlockCenter();
        BlockPos correctLowCorner = Multiblock.findLowsestValueCorner(centerPos, TOTAL_X_LENGTH, TOTAL_HEIGHT, TOTAL_Z_LENGTH);
        BlockPos lowCorner = new BlockPos(correctLowCorner.getX(), correctLowCorner.getY()+1, correctLowCorner.getZ());
        LOGGER.info(getControllerPos());
        List<BlockPos> multiblockMembers = new ArrayList();

        // checks the central slice part of the structure to ensure the correct blocks exist
        for(int y = 0; y < CENTRAL_BODY_HEIGHT; y++)  {
            for(int x = 0; x < TOTAL_WIDTH; x++)  {
                for(int z = 0; z < TOTAL_WIDTH; z++)  {
                    LOGGER.info(x);
                    LOGGER.info(y);
                    LOGGER.info(z);

                    Block correctBlock = posArray[x][z];                            // get the block that should be at these coord's
                    if( correctBlock == null)  {                                // skip the "null" positions (don't care whats in here)
                        LOGGER.info("skipping block - dont care");
                        continue;
                    }

                    // get current block
                    BlockPos current = new BlockPos(lowCorner.getX() + x, lowCorner.getY() + y, lowCorner.getZ() + z);
                    LOGGER.info(current);

                    if(current.equals(getControllerPos()) || current.equals(posIn))  {                                           // skip the controller
                        continue;
                    }

                    Block currentBlock = world.getBlockState(current).getBlock();   // get the actual block at pos
                    if(currentBlock != correctBlock)  {                             // check vs what should be there
                        // do not form multiblock
                        LOGGER.info("wrong block - got ");
                        LOGGER.info(currentBlock);
                        LOGGER.info(" should be ");
                        LOGGER.info(correctBlock);
                        if(posIn == null)  {
                            return null;
                        }
                    }
                    // add this block to correct
                    multiblockMembers.add(current);

                    // this if-elif checks verify that the bottom and top are covered by frame blocks
                    if(correctBlock == Blocks.AIR && y == 0)  {
                        // TODO - check for world lower limit
                        current = new BlockPos(lowCorner.getX() + x, lowCorner.getY() + y - 1, lowCorner.getZ() + z);
                        currentBlock = world.getBlockState(current).getBlock();
                        if(currentBlock != frameBlock) {
                            LOGGER.info("wrong block - got at lower");
                            if(posIn == null)  {
                                return null;
                            }                        }
                        multiblockMembers.add(current);
                    }  else if(correctBlock == Blocks.AIR && y == CENTRAL_BODY_HEIGHT-1) {
                        // TODO - check for world upper limit
                        current = new BlockPos(lowCorner.getX() + x, lowCorner.getY() + y + 1, lowCorner.getZ() + z);
                        currentBlock = world.getBlockState(current).getBlock();
                        if(currentBlock != frameBlock) {
                            LOGGER.info("wrong block - got at upper");
                            if(posIn == null)  {
                                return null;
                            }                        }
                        multiblockMembers.add(current);
                    }
                    // add correct blocks to array
                }
            }
        }  //end loop
        return multiblockMembers;
    }

    public void tryToFormMultiBlock(World worldIn, BlockPos posIn) {
        List<BlockPos> multiblockMembers = getMultiBlockMembers(worldIn, null);
        if(multiblockMembers != null)  {
            // get this far and we should form multi block
            LOGGER.info("FORM MULTIBLOCK");
            setFormed(true);
            updateMultiBlockMemberTiles(multiblockMembers, false);
        }
    }

    public void destroyMultiBlock(World worldIn, BlockPos posIn)  {
        if(!isFormed())  {
            return;
        }
        List<BlockPos> multiblockMembers = getMultiBlockMembers(worldIn, posIn);
        if(multiblockMembers != null)  {
            updateMultiBlockMemberTiles(multiblockMembers, true);
            setFormed(false);
            LOGGER.info("Multiblock Destroyed");
        }  else  {
            LOGGER.info("failed to destory multiblock -> list was null");
        }
    }

    /*
      Updates all information in all multiblock members
     */
    private void updateMultiBlockMemberTiles(List<BlockPos> memberArray, boolean destroy)  {
        for(int i = 0; i < memberArray.size(); i++)  {
            LOGGER.info("up");
            BlockPos current = memberArray.get(i);
            TileEntity currentTile = getTileFromPos(world, current);
            if(currentTile instanceof HCCokeOvenFrameTile)  {
                HCCokeOvenFrameTile castedCurrent = (HCCokeOvenFrameTile) currentTile;
                if(destroy)  {
                    castedCurrent.destroyMultiBlock();
                }  else  {
                    castedCurrent.setupMultiBlock(getControllerPos());
                }
            }
        }
    }


    private BlockPos getControllerPos()  {
        return this.pos;
    }

    // override the always return true method
    // TODO - learn a better way to throw this info upward
    @Override
    public boolean checkIfCorrectFrame(Block block)  {
        return (block instanceof HCCokeOvenFrameBlock);
    }

    public void openGUI(World worldIn, BlockPos pos, PlayerEntity player, HCCokeOvenControllerTile tileEntity)  {
        INamedContainerProvider containerProvider = new INamedContainerProvider() {
            @Override
            public ITextComponent getDisplayName() {
                return new TranslationTextComponent("screen.wrought.h_c");
            }

            @Override
            public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                return new HCCokeOvenContainer(i, worldIn, getControllerPos(), playerInventory, playerEntity);
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
        int xCoord = getControllerPos().getX();
        int yCoord = getControllerPos().getY();
        int zCoord = getControllerPos().getZ();
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



    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        itemHandler.deserializeNBT(nbt.getCompound("inv"));
        super.read(state, nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag = super.write(tag);
        tag.put("inv", itemHandler.serializeNBT());
        return tag;
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(64) {

            @Override
            protected void onContentsChanged(int slot) {
                // To make sure the TE persists when the chunk is saved later we need to
                // mark it dirty every time the item handler changes
                markDirty();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return true;
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                return super.insertItem(slot, stack, simulate);
            }
        };
    }


        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            if (cap.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)) {
                return handler.cast();
            }
            //if (cap.equals(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)) {
            //    return this.fluidTank.cast();

            //}
            //if (cap.equals(CapabilityEnergy.ENERGY)) {
            //    return energy.cast();
            // }
            return super.getCapability(cap, side);
        }

    public BlockPos getItemInputBlockPos()  {
        if(itemInputBlock == null)  {
            BlockPos center = findMultiBlockCenter();
            itemInputBlock = new BlockPos(center.getX(), center.getY() + (TOTAL_HEIGHT / 2), center.getZ());
        }
        return itemInputBlock;
    }

    public BlockPos getItemOutputBlockPos()  {
        if(itemOutputBlock == null)  {
            BlockPos center = findMultiBlockCenter();
            itemOutputBlock = new BlockPos(center.getX(), center.getY() - (TOTAL_HEIGHT / 2), center.getZ());
        }
        return itemOutputBlock;
    }

    public void setDirty(boolean b)  {
        if(b)  {
            markDirty();
        }
    }

}
