package com.thecowking.wrought.blocks.honeycomb_coke_oven;

import com.thecowking.wrought.Wrought;
import com.thecowking.wrought.blocks.*;

import com.thecowking.wrought.util.RegistryHandler;
import com.thecowking.wrought.util.WroughtItemHandler;
import net.minecraft.block.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RangedWrapper;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

import static com.thecowking.wrought.blocks.Multiblock.getTileFromPos;
import static com.thecowking.wrought.util.RegistryHandler.H_C_COKE_CONTROLLER_TILE;
import static com.thecowking.wrought.util.RegistryHandler.H_C_COKE_FRAME_BLOCK;


public class HCCokeOvenControllerTile extends MultiBlockControllerTile implements ITickableTileEntity, INamedContainerProvider {
    private static final Logger LOGGER = LogManager.getLogger();

    private static Block frameBlock = H_C_COKE_FRAME_BLOCK.get();


    private final Direction[] POSSIBLE_DIRECTIONS = {Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};

    private final Block[][] posArray = {
            {null, null, frameBlock, frameBlock,  frameBlock, null, null, null},
            {null, frameBlock, Blocks.AIR, Blocks.AIR, Blocks.AIR, frameBlock, null, null},
            {frameBlock, Blocks.AIR, Blocks.AIR, Blocks.AIR, Blocks.AIR, Blocks.AIR, frameBlock},
            {frameBlock, Blocks.AIR, Blocks.AIR, Blocks.AIR, Blocks.AIR, Blocks.AIR, frameBlock},
            {frameBlock, Blocks.AIR, Blocks.AIR, Blocks.AIR, Blocks.AIR, Blocks.AIR, frameBlock},
            {null, frameBlock, Blocks.AIR, Blocks.AIR, Blocks.AIR, frameBlock, null, null},
            {null, null, frameBlock, frameBlock,  frameBlock, null, null, null}
    };

    private final int CENTRAL_BODY_HEIGHT = 3;
    private final int TOTAL_HEIGHT = 5;
    private final int TOTAL_X_LENGTH = 7;
    private final int TOTAL_Z_LENGTH = TOTAL_X_LENGTH;
    private final int TOTAL_WIDTH = 7;
    private final int TICKSPEROPERATION = 20;
    private int tickCounter = 0;


    private Direction facingDirection;
    private BlockPos itemInputBlock;
    private BlockPos itemOutputBlock;
    private BlockPos redstoneInBlock;
    private BlockPos redstoneOutBlock;
    private boolean isSmelting = false;
    private int smeltTime = 0;


    private WroughtItemHandler itemHandler = createHandler();
    public LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);
    private final LazyOptional<IItemHandlerModifiable> inventoryCapabilityInput = LazyOptional.of(() -> new RangedWrapper(this.itemHandler, Multiblock.INDEX_ITEM_INPUT, Multiblock.INDEX_ITEM_INPUT + 1));
    private final LazyOptional<IItemHandlerModifiable> inventoryCapabilityOutput = LazyOptional.of(() -> new RangedWrapper(this.itemHandler, Multiblock.INDEX_ITEM_OUTPUT, Multiblock.INDEX_ITEM_OUTPUT + 2));



    public HCCokeOvenControllerTile() {
        super(H_C_COKE_CONTROLLER_TILE.get());
    }


    @Override
    public void tick() {
        // check if we have a multiblock
        if(!isFormed(this.pos))  {return;}
        // check if we are in correct instance
        if(this.world == null || this.world.isRemote)  {return;}
        // Check if enough time passed for an operation
        this.markDirty();
        if(tickCounter++ < TICKSPEROPERATION)  {return;}
        tickCounter = 0;
        // check if redstone is turning machine off
        if(this.world.isBlockPowered(this.pos))  {machineChangeOperation(false); return; }
        // get input item -> if no recipe exists then jump out
        if(this.getRecipe(itemHandler.getStackInSlot(Multiblock.INDEX_ITEM_INPUT)) == null)  {machineChangeOperation(false); return; }
        // check to make sure output is not full before starting another operation
        if(itemHandler.getStackInSlot(Multiblock.INDEX_ITEM_OUTPUT).getCount() >= itemHandler.getStackInSlot(Multiblock.INDEX_ITEM_OUTPUT).getMaxStackSize())  {machineChangeOperation(false); return;}
        ovenOperation();
    }

    private void machineChangeOperation(boolean online)  {
        if(online == isSmelting)  {return;}
        this.isSmelting = online;
        setLit(online);
        blockUpdate();
    }

    private void blockUpdate()  {
        this.world.notifyBlockUpdate(this.pos, this.getBlockState(), this.getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
    }

    private void ovenOperation()  {
        ItemStack stack = itemHandler.getStackInSlot(Multiblock.INDEX_ITEM_INPUT);
        ItemStack outputStack = getRecipe(stack);

        if(outputStack != null)  {
            machineChangeOperation(true);
            itemHandler.extractItem(Multiblock.INDEX_ITEM_INPUT, 1, false);
            itemHandler.insertItem(Multiblock.INDEX_ITEM_OUTPUT, outputStack, false);
            markDirty();
        }
    }

    /*
      This attempts to find all the frame blocks in the multi-block to determine if we should form the multi-block or used to update frame blocks
      that the multi-block is being destoryed.

      posIn - if null then we are using this method to form a multi block. This means that if we find a non
            - frame block then we stop the method and return null.
            - if not null then its for destroying, and we want to continue if we find an error in case of
            - some strange issue such as an explosion wiped away other blocks.
     */

    public List<BlockPos> getMultiBlockMembers(World worldIn, BlockPos posIn, Direction direction)  {
        BlockPos centerPos = findMultiBlockCenter(direction);
        BlockPos correctLowCorner = Multiblock.findLowsestValueCorner(centerPos, TOTAL_X_LENGTH, TOTAL_HEIGHT, TOTAL_Z_LENGTH);
        BlockPos lowCorner = new BlockPos(correctLowCorner.getX(), correctLowCorner.getY()+1, correctLowCorner.getZ());
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
        for(int i = 0; i < POSSIBLE_DIRECTIONS.length; i++)  {

            Direction currentDirection = POSSIBLE_DIRECTIONS[i];
            LOGGER.info(currentDirection);

            List<BlockPos> multiblockMembers = getMultiBlockMembers(worldIn, null, currentDirection);
            if(multiblockMembers != null)  {
                this.facingDirection = currentDirection;
                // get this far and we should form multi block
                LOGGER.info("FORM MULTIBLOCK");
                setFormed(true);
                updateMultiBlockMemberTiles(multiblockMembers, false);
                assignJobs();
                return;
            }
        }
    }

    public void assignJobs()  {
        BlockPos center = findMultiBlockCenter(this.facingDirection);
        BlockPos inputPos = new BlockPos(center.getX(), center.getY()+2, center.getZ());
        BlockPos outputPos = new BlockPos(center.getX(), center.getY()-2, center.getZ());
        TileEntity te = Multiblock.getTileFromPos(this.world, inputPos);
        if(te instanceof HCCokeOvenFrameTile)  {
            LOGGER.info("assigned input to");
            LOGGER.info(inputPos);
            ((HCCokeOvenFrameTile)te).setJob(Multiblock.JOB_ITEM_IN);
        }  else  {
            LOGGER.info("error - could not assign input item job");
        }
        te = Multiblock.getTileFromPos(this.world, outputPos);
        if(te instanceof HCCokeOvenFrameTile)  {
            LOGGER.info("assigned output to");
            LOGGER.info(outputPos);
            ((HCCokeOvenFrameTile)te).setJob(Multiblock.JOB_ITEM_OUT);
        }  else  {
            LOGGER.info("error - could not assign output item job");
        }

    }


    public void destroyMultiBlock(World worldIn, BlockPos posIn)  {
        if(!isFormed(this.pos))  {
            return;
        }
        LOGGER.info(this.facingDirection);
        List<BlockPos> multiblockMembers = getMultiBlockMembers(worldIn, posIn, this.facingDirection);
        if(multiblockMembers != null)  {
            updateMultiBlockMemberTiles(multiblockMembers, true);
            setFormed(false);
            setLit(false);
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
    public BlockPos findMultiBlockCenter(Direction facing)  {
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
        super.read(state, nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inv"));
        this.tickCounter = nbt.getInt("NUMTICKS");


    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag = super.write(tag);
        tag.put("inv", itemHandler.serializeNBT());
        tag.putInt("NUMTICKS", tickCounter);
        return tag;
    }

    private WroughtItemHandler createHandler() {
        return new WroughtItemHandler(3);
    }



    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull final Capability<T> cap, @Nullable final Direction side)
    {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (side == null)
                return handler.cast();
            switch (side) {
                case DOWN:
                    return inventoryCapabilityOutput.cast();
                case UP:
                    return inventoryCapabilityInput.cast();
                case NORTH:
                case SOUTH:
                case WEST:
                case EAST:
                    return handler.cast();
            }
        }
        return super.getCapability(cap, side);
    }


    public BlockPos getItemInputBlockPos()  {
        if(itemInputBlock == null && this.facingDirection != null)  {
            BlockPos center = findMultiBlockCenter(this.facingDirection);
            itemInputBlock = new BlockPos(center.getX(), center.getY() + (TOTAL_HEIGHT / 2), center.getZ());
        }
        return itemInputBlock;
    }

    public BlockPos getItemOutputBlockPos()  {
        if(itemOutputBlock == null && this.facingDirection != null)  {
            BlockPos center = findMultiBlockCenter(this.facingDirection);
            itemOutputBlock = new BlockPos(center.getX(), center.getY() - (TOTAL_HEIGHT / 2), center.getZ());
        }
        return itemOutputBlock;
    }

    public void setDirty(boolean b)  {
        if(b)  {
            markDirty();
        }
    }

    @Nullable
    @Override
    public Container createMenu(final int windowID, final PlayerInventory playerInv, final PlayerEntity playerIn) {
        return new HCCokeOvenContainer(windowID, this.world, this.pos, playerInv, playerIn);
    }

    @Override
    public ITextComponent getDisplayName() {
        return null;
    }

    @Nullable
    private ItemStack getRecipe(ItemStack stack)  {
        if(stack.getItem() == Items.COAL)  {
           return new ItemStack(RegistryHandler.COKE.get(), 1);
        }
        return null;
    }

    public static Set<IRecipe<?>> findRecipesByType(IRecipeType<?> typeIn, World world) {
        return world != null ? world.getRecipeManager().getRecipes().stream()
                .filter(recipe -> recipe.getType() == typeIn).collect(Collectors.toSet()) : Collections.emptySet();
    }

    @SuppressWarnings("resource")
    @OnlyIn(Dist.CLIENT)
    public static Set<IRecipe<?>> findRecipesByType(IRecipeType<?> typeIn) {
        ClientWorld world = Minecraft.getInstance().world;
        return world != null ? world.getRecipeManager().getRecipes().stream()
                .filter(recipe -> recipe.getType() == typeIn).collect(Collectors.toSet()) : Collections.emptySet();
    }

    public static Set<ItemStack> getAllRecipeInputs(IRecipeType<?> typeIn, World worldIn) {
        Set<ItemStack> inputs = new HashSet<ItemStack>();
        Set<IRecipe<?>> recipes = findRecipesByType(typeIn, worldIn);
        for (IRecipe<?> recipe : recipes) {
            NonNullList<Ingredient> ingredients = recipe.getIngredients();
            ingredients.forEach(ingredient -> {
                for (ItemStack stack : ingredient.getMatchingStacks()) {
                    inputs.add(stack);
                }
            });
        }
        return inputs;
    }

    public final IItemHandlerModifiable getInvetory()  {
        return this.itemHandler;
    }


}
