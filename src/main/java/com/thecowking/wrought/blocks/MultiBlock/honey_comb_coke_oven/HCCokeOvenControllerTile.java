package com.thecowking.wrought.blocks.MultiBlock.honey_comb_coke_oven;

import com.thecowking.wrought.blocks.MultiBlock.IMultiBlockFrame;
import com.thecowking.wrought.blocks.MultiBlock.MultiBlockControllerTile;
import com.thecowking.wrought.blocks.MultiBlock.Multiblock;
import com.thecowking.wrought.recipes.HoneyCombCokeOven.HoneyCombCokeOvenRecipe;
import com.thecowking.wrought.util.*;
import net.minecraft.block.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;


import java.util.*;
import java.util.stream.Collectors;

import static com.thecowking.wrought.blocks.MultiBlock.Multiblock.*;
import static com.thecowking.wrought.util.RegistryHandler.*;

public class HCCokeOvenControllerTile extends MultiBlockControllerTile implements ITickableTileEntity, INamedContainerProvider {
    private static final Logger LOGGER = LogManager.getLogger();

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
            {
                    {null, null, null, null, null},
                    {null, frameBlock, frameBlock, frameBlock, null},
                    {frameBlock, frameBlock, frameBlock, frameBlock, frameBlock},
                    {frameBlock, frameBlock, frameBlock, frameBlock, frameBlock},
                    {frameBlock, frameBlock, frameBlock, frameBlock, frameBlock},
                    {null, frameBlock, frameBlock, frameBlock, null},
                    {null, null, null, null, null}
            },
            {
                    {null, frameBlock, frameBlock, frameBlock, null},
                    {frameBlock, Blocks.AIR, Blocks.AIR, Blocks.AIR, frameBlock},
                    {frameBlock, Blocks.AIR, Blocks.AIR, Blocks.AIR, frameBlock},
                    {frameBlock, Blocks.AIR, Blocks.AIR, Blocks.AIR, frameBlock},
                    {frameBlock, Blocks.AIR, Blocks.AIR, Blocks.AIR, frameBlock},
                    {frameBlock, Blocks.AIR, Blocks.AIR, Blocks.AIR, frameBlock},
                    {null, frameBlock, frameBlock, frameBlock, null}
            },
            {
                    {null, frameBlock, controllerBlock, frameBlock, null},
                    {frameBlock, Blocks.AIR, Blocks.AIR, Blocks.AIR, frameBlock},
                    {frameBlock, Blocks.AIR, Blocks.AIR, Blocks.AIR, frameBlock},
                    {frameBlock, Blocks.AIR, Blocks.AIR, Blocks.AIR, frameBlock},
                    {frameBlock, Blocks.AIR, Blocks.AIR, Blocks.AIR, frameBlock},
                    {frameBlock, Blocks.AIR, Blocks.AIR, Blocks.AIR, frameBlock},
                    {null, frameBlock, frameBlock, frameBlock, null}
            },
            {
                    {null, frameBlock, frameBlock, frameBlock, null},
                    {frameBlock, Blocks.AIR, Blocks.AIR, Blocks.AIR, frameBlock},
                    {frameBlock, Blocks.AIR, Blocks.AIR, Blocks.AIR, frameBlock},
                    {frameBlock, Blocks.AIR, Blocks.AIR, Blocks.AIR, frameBlock},
                    {frameBlock, Blocks.AIR, Blocks.AIR, Blocks.AIR, frameBlock},
                    {frameBlock, Blocks.AIR, Blocks.AIR, Blocks.AIR, frameBlock},
                    {null, frameBlock, frameBlock, frameBlock, null}
            },
            {
                    {null, frameStairs, frameStairs, frameStairs, null},
                    {frameStairs, frameBlock, frameBlock, frameBlock, frameStairs},
                    {frameBlock, Blocks.AIR, Blocks.AIR, Blocks.AIR, frameBlock},
                    {frameBlock, Blocks.AIR, Blocks.AIR, Blocks.AIR, frameBlock},
                    {frameBlock, Blocks.AIR, Blocks.AIR, Blocks.AIR, frameBlock},
                    {frameStairs, frameBlock, frameBlock, frameBlock, frameStairs},
                    {null, frameStairs, frameStairs, frameStairs, null}
            },
            {
                    {null, null, null, null, null},
                    {null, frameSlab, frameSlab, frameSlab, null},
                    {frameSlab, frameBlock, frameBlock, frameBlock, frameSlab},
                    {frameSlab, frameBlock, hatchBlock, frameBlock, frameSlab},
                    {frameSlab, frameBlock, frameBlock, frameBlock, frameSlab},
                    {null, frameSlab, frameSlab, frameSlab, null},
                    {null, null, null, null, null}
            }
    };

    private final int TICKSPEROPERATION = 20;
    private int tickCounter = 0;

    private boolean isSmelting = false;
    private int smeltTime = 0;

    protected FluidHandlerItemStack fluidOutput;
    protected InputItemHandler inputSlot;
    protected OutputItemHandler outputSlot;
    protected FluidItemInputHandler itemFluidInputSlot;
    protected FluidItemOutputHandler itemFluidOutputSlot;

    private OutputFluidTank fluidTank;

    // used when player is directly accessing multi-block
    private final LazyOptional<IItemHandler> everything = LazyOptional.of(() -> new CombinedInvWrapper(inputSlot, outputSlot, itemFluidInputSlot, itemFluidOutputSlot));
    // used when in world things interact with multi-block
    private final LazyOptional<IItemHandler> automation = LazyOptional.of(() -> new AutomationCombinedInvWrapper(inputSlot, outputSlot, itemFluidInputSlot, itemFluidOutputSlot));

    // used to stop operation for when an item cannot be inserted into output slot
    private ItemStack itemBacklog;  //TODO - read/write
    // used to stop operation for when a fluid cannot be inserted into output slot
    private FluidStack fluidBacklog;

    public HCCokeOvenControllerTile() {
        super(H_C_COKE_CONTROLLER_TILE.get());
        inputSlot = new InputItemHandler(1, this);
        outputSlot = new OutputItemHandler(1);
        itemFluidInputSlot = new FluidItemInputHandler(1);
        itemFluidOutputSlot = new FluidItemOutputHandler(1);

        fluidTank = new OutputFluidTank(16000);
        itemBacklog = ItemStack.EMPTY;
        fluidBacklog = FluidStack.EMPTY;

        this.height = posArray.length;
        this.length = posArray[0].length;
        this.width = posArray[0][0].length;
    }

    public World getWorld()  {return this.world;}


    @Override
    public void tick() {
        // check if we have a multi-block
        if (!isFormed(getControllerPos())) {return; }

        // check if we are in correct instance
        if (this.world == null || this.world.isRemote) { return; }

        // Check if enough time passed for an operation
        this.markDirty();
        if (tickCounter++ < TICKSPEROPERATION) { return; }
        tickCounter = 0;

        // check if redstone is turning machine off
        if (isRedstonePowered(this.redstoneIn)) {
            machineChangeOperation(false);
            LOGGER.info("redstone turn off");
            return;
        }

        // backlog checks - note that another operation will not happen until tickCount has passed if these fail
        // items
        if(itemBacklog != ItemStack.EMPTY)  {
            itemBacklog = outputSlot.insertItem(0, itemBacklog.copy(), false);
            if(itemBacklog != ItemStack.EMPTY)  {
                LOGGER.info("item full off");
                return;
            }
        }
        // fluids
        if(fluidBacklog != FluidStack.EMPTY)  {
            fluidBacklog = fluidTank.internalFill(fluidBacklog.copy(), IFluidHandler.FluidAction.EXECUTE);
            if(fluidBacklog != FluidStack.EMPTY)  {
                LOGGER.info("fluid backlog off");
                return;
            }
        }

        // yank the current recipe for an item in
        HoneyCombCokeOvenRecipe currentRecipe = this.getRecipe(inputSlot.getStackInSlot(0));

        // check if we have a recipe for item
        if (currentRecipe == null) {
            machineChangeOperation(false);
            LOGGER.info("no recipe");
            return;
        }

        // get the fluid output from recipe
        FluidStack recipeFluidOutput = currentRecipe.getRecipeFluidStackOutput();
/*
        // check if recipe has a fluid output
        if(recipeFluidOutput != null)  {
            // check if fluid matches tank and if tank has space for fluid
            if (recipeFluidOutput != fluidTank.getFluid()  || fluidTank.getFluidAmount() >= fluidTank.getCapacity())  {
                LOGGER.info("fluid cannot insert off");
                return;
            }
        }
*/
        // check to make sure output is not full before starting another operation
        if (outputSlot.getStackInSlot(0).getCount() >= outputSlot.getStackInSlot(0).getMaxStackSize()) {
            machineChangeOperation(false);
            LOGGER.info("cannot insert item off");

            return;
        }

        // if we get here then we can run an operation!
        ovenOperation();
    }

    /*
     Method to run a single oven operation
     */
    private void ovenOperation() {

        ItemStack outputs = this.getRecipe(this.inputSlot.getStackInSlot(0)).getRecipeItemStackOutput();
        FluidStack fluidOutput = this.getRecipe(this.inputSlot.getStackInSlot(0)).getRecipeFluidStackOutput();

        //if (outputItemStack != null && outputFluidStack == null) {
        if (outputs != null && outputs.getItem() != Items.AIR) {
            if(!this.isSmelting)  {
                machineChangeOperation(true);
            }
            inputSlot.getStackInSlot(0).shrink(1);
            // TODO - stopped here last - want to uncomment line below and start testing fluid
            //fluidBacklog = fluidTank.internalFill(fluidOutput.copy(), IFluidHandler.FluidAction.EXECUTE);
            itemBacklog = outputSlot.internalInsertItem(0, outputs.copy(), false);
            markDirty();
        }
    }

    /*
      Flips states if machine is changing from off -> on or from on -> off
     */
    private void machineChangeOperation(boolean online) {
        if (online == isSmelting) {
            return;
        }
        this.isSmelting = online;
        if(online)  {
            sendOutRedstone(15);
        }  else  {
            sendOutRedstone(0);
        }
        blockUpdate();
    }




    /*
    TODO - add support for tanks
     */
    protected void processFluidContainerItem()  {
        if(fluidTank.getFluidAmount() < 1000)  return;
        if(itemFluidInputSlot.getStackInSlot(0).isEmpty())  return;
        if(!(itemFluidOutputSlot.getStackInSlot(0).isEmpty()))  return;

        ItemStack emptyContainer = itemFluidInputSlot.getStackInSlot(0);
        if(!(emptyContainer.getItem() instanceof BucketItem))  {
            return;
        }

        ItemStack fluidBucket = InventoryUtils.fillBucketOrFluidContainer(emptyContainer, fluidTank.getFluid());
        if(fluidBucket.isEmpty())  return;

        itemFluidInputSlot.getStackInSlot(0).shrink(1);
        ItemStack filledContainer = InventoryUtils.fillBucketOrFluidContainer(emptyContainer, fluidTank.getFluid());
        fluidTank.drain(1000, IFluidHandler.FluidAction.EXECUTE);
        itemFluidOutputSlot.insertItem(0, filledContainer, false);
    }


    protected BlockPos getControllerPos() {
        return this.pos;
    }

    // override the always return true method
    // TODO - learn a better way to throw this info upward
    @Override
    public boolean checkIfCorrectFrame(Block block) {
        return (block instanceof HCCokeOvenFrameBlock);
    }


    public void openGUI(World worldIn, BlockPos pos, PlayerEntity player, HCCokeOvenControllerTile tileEntity) {
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
        NetworkHooks.openGui((ServerPlayerEntity) player, containerProvider, ((HCCokeOvenControllerTile) tileEntity).getPos());
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        inputSlot.deserializeNBT(nbt.getCompound(INVENTORY_IN));
        outputSlot.deserializeNBT(nbt.getCompound(INVENTORY_OUT));
        this.tickCounter = nbt.getInt(NUM_TICKS);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag = super.write(tag);
        tag.put(INVENTORY_IN, inputSlot.serializeNBT());
        tag.put(INVENTORY_OUT, outputSlot.serializeNBT());
        tag.putInt(NUM_TICKS, tickCounter);
        return tag;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull final Capability<T> cap, @Nullable final Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (world != null && world.getBlockState(pos).getBlock() != this.getBlockState().getBlock()) {//if the blocks at myself isn't myself, allow full access (Block Broken)
                return everything.cast();
            }
            if (side == null) {
                return everything.cast();
            } else {
                return automation.cast();
            }
        }
        if(cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)  {
            return LazyOptional.of(() -> fluidTank).cast();
        }
        return super.getCapability(cap, side);
    }

    @Nullable
    @Override
    public Container createMenu(final int windowID, final PlayerInventory playerInv, final PlayerEntity playerIn) {
        return new HCCokeOvenContainer(windowID, this.world, getControllerPos(), playerInv, playerIn);
    }

    @Override
    public ITextComponent getDisplayName() {
        return null;
    }


    @Nullable
    public HoneyCombCokeOvenRecipe getRecipe(ItemStack stack) {
        if (stack == null) {
            return null;
        }

        Set<IRecipe<?>> recipes = findRecipesByType(RecipeSerializerInit.HONEY_COMB_OVEN_TYPE, this.world);
        for (IRecipe<?> iRecipe : recipes) {
            HoneyCombCokeOvenRecipe recipe = (HoneyCombCokeOvenRecipe) iRecipe;
            if (recipe.matches(new RecipeWrapper(this.inputSlot), this.world)) {
                return recipe;
            }
        }

        return null;
    }

    public boolean isValidInputItem(InputItemHandler handler)  {
        Set<IRecipe<?>> recipes = findRecipesByType(RecipeSerializerInit.HONEY_COMB_OVEN_TYPE, this.world);
        LOGGER.info("valid check");
        for (IRecipe<?> iRecipe : recipes) {
            HoneyCombCokeOvenRecipe recipe = (HoneyCombCokeOvenRecipe) iRecipe;
            if (recipe.matches(new RecipeWrapper(handler), this.world)) {
                return true;
            }
        }
        return false;
    }


    public static Set<IRecipe<?>> findRecipesByType(IRecipeType<?> typeIn, World world) {
        LOGGER.info("findRecipesByType - server");
        return world != null ? world.getRecipeManager().getRecipes().stream()
                .filter(recipe -> recipe.getType() == typeIn).collect(Collectors.toSet()) : Collections.emptySet();
    }

    @SuppressWarnings("resource")
    @OnlyIn(Dist.CLIENT)
    public static Set<IRecipe<?>> findRecipesByType(IRecipeType<?> typeIn) {
        LOGGER.info("findRecipesByType - client");
        ClientWorld world = Minecraft.getInstance().world;
        return world != null ? world.getRecipeManager().getRecipes().stream()
                .filter(recipe -> recipe.getType() == typeIn).collect(Collectors.toSet()) : Collections.emptySet();
    }
/*
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

 */


    // ------------------------MULTI-BLOCK STUFFS ------------------------------------------------


    /*
  Updates all information in all multiblock members
 */
    private void updateMultiBlockMemberTiles(List<BlockPos> memberArray, boolean destroy) {
        for (int i = 0; i < memberArray.size(); i++) {
            BlockPos current = memberArray.get(i);
            Block currentBlock = world.getBlockState(current).getBlock();                   //check blocks
            if(currentBlock instanceof IMultiBlockFrame)  {
                IMultiBlockFrame frameBlock = (IMultiBlockFrame) currentBlock;
                if(destroy)  {
                    frameBlock.removeFromMultiBlock(world.getBlockState(current),current, world);
                }  else  {
                    frameBlock.addingToMultblock(world.getBlockState(current), current, world);          // change blockstate and create TE
                }
                TileEntity currentTile = getTileFromPos(world, current);                    // get new TE
                if (currentTile instanceof HCCokeOvenFrameTile) {
                    HCCokeOvenFrameTile castedCurrent = (HCCokeOvenFrameTile) currentTile;
                    if (destroy) {
                        castedCurrent.destroyMultiBlock();   // remove blockstate
                    } else {
                        castedCurrent.setupMultiBlock(getControllerPos());  // dp TE things needed for multiblock setup
                    }
                }
            }
        }
    }

        /*
      This attempts to find all the frame blocks in the multi-blocks to determine if we should form the multi-blocks or used to update frame blocks
      that the multi-blocks is being formed or destroyed.
     */

    public List<BlockPos> getMultiBlockMembers(World worldIn, boolean destroy, Direction direction) {
        BlockPos centerPos = calcCenterBlock(direction);
        BlockPos lowCorner = findLowsestValueCorner(centerPos, direction, this.length, this.height, this.width);
        BlockPos correctLowCorner = new BlockPos(lowCorner.getX(), lowCorner.getY() + 1, lowCorner.getZ());
        List<BlockPos> multiblockMembers = new ArrayList();

        // checks the central slice part of the structure to ensure the correct blocks exist
        for (int y = 0; y < posArray.length; y++) {
            for (int z = 0; z < posArray[0].length; z++) {
                for (int x = 0; x < posArray[0][0].length; x++) {
                    Block correctBlock = posArray[y][z][x];                            // get the blocks that should be at these coord's
                    if (correctBlock == null) {                                // skip the "null" positions (don't care whats in here)
                        continue;
                    }
                    // get current blocks - adjusted for Direction
                    BlockPos current = indexShifterBlockPos(getDirectionFacing(), correctLowCorner, x, y, z, length, width);
                    Block currentBlock = world.getBlockState(current).getBlock();   // get the actual blocks at pos
                    if (currentBlock != correctBlock && !destroy) {
                        if (!destroy) {
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
    public BlockPos indexShifterBlockPos(Direction inputDirection, BlockPos low, int x, int y, int z, int length, int width)  {

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
      Driver for forming the multiblock
     */
    public void tryToFormMultiBlock(World worldIn, BlockPos posIn) {
        List<BlockPos> multiblockMembers = getMultiBlockMembers(worldIn, false, getDirectionFacing());             // calc if every location has correct blocks
        if (multiblockMembers != null) {                                                                            // if above check has no errors then it will not be null
            setFormed(true);                                                                                        // change blocks state of controller
            updateMultiBlockMemberTiles(multiblockMembers, false);                                            // change blocks state of frames
            assignJobs();                                                                                           // sets "jobs" on frame members as needed
        }
    }

    /*
      Driver for destroying multi-blocks
     */
    public void destroyMultiBlock(World worldIn, BlockPos posIn) {
        if (!isFormed(getControllerPos())) {
            return;
        }
        setFormed(false);
        List<BlockPos> multiblockMembers = getMultiBlockMembers(worldIn, true, getDirectionFacing());
        if (multiblockMembers != null) {
            updateMultiBlockMemberTiles(multiblockMembers, true);
        }
    }

    /*
      Assigns out "jobs" to frame blocks that the controller needs to keep track of
     */
    public void assignJobs() {
        BlockPos inputPos = getRedstoneInBlockPos();
        BlockPos outputPos = getRedstoneOutBlockPos();
        TileEntity te = Multiblock.getTileFromPos(this.world, inputPos);
        if (te instanceof HCCokeOvenFrameTile) {
            ((HCCokeOvenFrameTile) te).setJob(JOB_REDSTONE_IN);
        }
        te = Multiblock.getTileFromPos(this.world, outputPos);
        if (te instanceof HCCokeOvenFrameTile) {
            ((HCCokeOvenFrameTile) te).setJob(JOB_REDSTONE_OUT);
        }
    }


    /*
      Calc's the position of the redstone input frame
     */
    public BlockPos getRedstoneInBlockPos() {
        if (this.redstoneIn == null) {
            this.redstoneIn = new BlockPos(getControllerPos().getX(), getControllerPos().getY() + 1, getControllerPos().getZ());
        }
        return this.redstoneIn;
    }

    /*
  Calc's the position of the redstone output frame
  */
    public BlockPos getRedstoneOutBlockPos() {
        if (this.redstoneOut == null) {
            this.redstoneOut = new BlockPos(getControllerPos().getX(), getControllerPos().getY() - 1, getControllerPos().getZ());
        }
        return this.redstoneOut;
    }
}
