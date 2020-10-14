package com.thecowking.wrought.blocks.honeycomb_coke_oven;

import com.thecowking.wrought.blocks.*;

import com.thecowking.wrought.util.AutomationCombinedInvWrapper;
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
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

import static com.thecowking.wrought.blocks.Multiblock.*;
import static com.thecowking.wrought.util.RegistryHandler.H_C_COKE_CONTROLLER_TILE;
import static com.thecowking.wrought.util.RegistryHandler.H_C_COKE_FRAME_BLOCK;


public class HCCokeOvenControllerTile extends MultiBlockControllerTile implements ITickableTileEntity, INamedContainerProvider {
    private static final Logger LOGGER = LogManager.getLogger();

    private static Block frameBlock = H_C_COKE_FRAME_BLOCK.get();


    private final Block[][] posArray = {
            {null, null, frameBlock, frameBlock, frameBlock, null, null, null},
            {null, frameBlock, Blocks.AIR, Blocks.AIR, Blocks.AIR, frameBlock, null, null},
            {frameBlock, Blocks.AIR, Blocks.AIR, Blocks.AIR, Blocks.AIR, Blocks.AIR, frameBlock},
            {frameBlock, Blocks.AIR, Blocks.AIR, Blocks.AIR, Blocks.AIR, Blocks.AIR, frameBlock},
            {frameBlock, Blocks.AIR, Blocks.AIR, Blocks.AIR, Blocks.AIR, Blocks.AIR, frameBlock},
            {null, frameBlock, Blocks.AIR, Blocks.AIR, Blocks.AIR, frameBlock, null, null},
            {null, null, frameBlock, frameBlock, frameBlock, null, null, null}
    };

    private final int CENTRAL_BODY_HEIGHT = 3;
    private final int TICKSPEROPERATION = 20;
    private int tickCounter = 0;


    private boolean isSmelting = false;
    private int smeltTime = 0;

    protected ItemStackHandler inputSlot;
    protected ItemStackHandler outputSlot;
    private final LazyOptional<IItemHandler> everything = LazyOptional.of(() -> new CombinedInvWrapper(inputSlot, outputSlot));
    private final LazyOptional<IItemHandler> automation = LazyOptional.of(() -> new AutomationCombinedInvWrapper(inputSlot, outputSlot));


    public HCCokeOvenControllerTile() {
        super(H_C_COKE_CONTROLLER_TILE.get());
        inputSlot = new WroughtItemHandler(1);
        outputSlot = new ItemStackHandler();
        this.xLength = 7;
        this.zLength = xLength;
        this.yLength = 5;
    }

    @Override
    public void tick() {
        // check if we have a multiblock - TODO - not tickable until multiblock?
        if (!isFormed(this.pos)) {return; }
        // check if we are in correct instance
        if (this.world == null || this.world.isRemote) { return; }
        // Check if enough time passed for an operation
        this.markDirty();
        if (tickCounter++ < TICKSPEROPERATION) { return; }
        tickCounter = 0;
        // check if redstone is turning machine off
        if ((this.world.isBlockPowered(getRedstoneInBlockPos()))) {
            LOGGER.info("redstone turned off");
            machineChangeOperation(false);
            return;
        }

        // get input item -> if no recipe exists then jump out
        if (this.getRecipe(inputSlot.getStackInSlot(0)) == null) {
            LOGGER.info("no recipe turn off");
            machineChangeOperation(false);
            return;
        }
        // check to make sure output is not full before starting another operation
        if (outputSlot.getStackInSlot(0).getCount() >= outputSlot.getStackInSlot(0).getMaxStackSize()) {
            LOGGER.info("full output turned off");
            machineChangeOperation(false);
            return;
        }
        ovenOperation();
    }

    private void machineChangeOperation(boolean online) {
        if (online == isSmelting) {
            return;
        }
        this.isSmelting = online;
        LOGGER.info(this.isSmelting);
        if(online)  {
            sendOutRedstone(15);
        }  else  {
            sendOutRedstone(0);
        }
        setLit(online);
        blockUpdate();
    }

    private void blockUpdate() {
        this.world.notifyBlockUpdate(this.pos, this.getBlockState(), this.getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
    }

    private void ovenOperation() {
        ItemStack stack = inputSlot.getStackInSlot(0);
        ItemStack outputStack = getRecipe(stack);

        if (outputStack != null) {
            if(!this.isSmelting)  {
                machineChangeOperation(true);
            }
            inputSlot.extractItem(0, 1, false);
            outputSlot.insertItem(0, outputStack, false);
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

    public List<BlockPos> getMultiBlockMembers(World worldIn, BlockPos posIn, Direction direction) {
        BlockPos centerPos = calcCenterBlock(direction);
        BlockPos correctLowCorner = Multiblock.findLowsestValueCorner(centerPos, this.xLength, this.yLength, this.zLength);
        BlockPos lowCorner = new BlockPos(correctLowCorner.getX(), correctLowCorner.getY() + 1, correctLowCorner.getZ());
        List<BlockPos> multiblockMembers = new ArrayList();

        // checks the central slice part of the structure to ensure the correct blocks exist
        for (int y = 0; y < CENTRAL_BODY_HEIGHT; y++) {
            for (int x = 0; x < xLength; x++) {
                for (int z = 0; z < zLength; z++) {
                    Block correctBlock = posArray[x][z];                            // get the block that should be at these coord's
                    if (correctBlock == null) {                                // skip the "null" positions (don't care whats in here)
                        continue;
                    }
                    // get current block
                    BlockPos current = new BlockPos(lowCorner.getX() + x, lowCorner.getY() + y, lowCorner.getZ() + z);
                    if (current.equals(getControllerPos()) || current.equals(posIn)) {                                           // skip the controller
                        continue;
                    }

                    Block currentBlock = world.getBlockState(current).getBlock();   // get the actual block at pos
                    if (currentBlock != correctBlock) {                             // check vs what should be there
                        // do not form multiblock
                        LOGGER.info("wrong block - got ");
                        LOGGER.info(currentBlock);
                        LOGGER.info(" should be ");
                        LOGGER.info(correctBlock);
                        if (posIn == null) {
                            return null;
                        }
                    }
                    // add this block to correct
                    multiblockMembers.add(current);

                    // this if-elif checks verify that the bottom and top are covered by frame blocks
                    if (correctBlock == Blocks.AIR && y == 0) {
                        // TODO - check for world lower limit
                        current = new BlockPos(lowCorner.getX() + x, lowCorner.getY() + y - 1, lowCorner.getZ() + z);
                        currentBlock = world.getBlockState(current).getBlock();
                        if (currentBlock != frameBlock) {
                            LOGGER.info("wrong block - got at lower");
                            if (posIn == null) {
                                return null;
                            }
                        }
                        multiblockMembers.add(current);
                    } else if (correctBlock == Blocks.AIR && y == CENTRAL_BODY_HEIGHT - 1) {
                        // TODO - check for world upper limit
                        current = new BlockPos(lowCorner.getX() + x, lowCorner.getY() + y + 1, lowCorner.getZ() + z);
                        currentBlock = world.getBlockState(current).getBlock();
                        if (currentBlock != frameBlock) {
                            LOGGER.info("wrong block - got at upper");
                            if (posIn == null) {
                                return null;
                            }
                        }
                        multiblockMembers.add(current);
                    }
                    // add correct blocks to array
                }
            }
        }  //end loop
        return multiblockMembers;
    }

    public void tryToFormMultiBlock(World worldIn, BlockPos posIn) {

        for (int i = 0; i < POSSIBLE_DIRECTIONS.length; i++) {                                                          // iterate over ever direction the multiblock can be in
            Direction currentDirection = POSSIBLE_DIRECTIONS[i];
            List<BlockPos> multiblockMembers = getMultiBlockMembers(worldIn, null, currentDirection);             // calc if every location has correct block
            if (multiblockMembers != null) {                                                                            // if above check has no errors then it will not be null
                setFacingDirection(i);                                                                                  // saves the current direction index
                LOGGER.info("FORM MULTIBLOCK");                                                                         // get this far and we should form multi block
                setFormed(true);                                                                                        // change block state of controller
                updateMultiBlockMemberTiles(multiblockMembers, false);                                            // change block state of frames
                assignJobs();                                                                                           // sets "jobs" on frame members as needed
                return;
            }
        }
    }

    public void destroyMultiBlock(World worldIn, BlockPos posIn) {
        if (!isFormed(this.pos)) {
            return;
        }
        setFormed(false);
        setLit(false);
        Direction d = getDirectionFacing();
        if(d == null)  {
            // something went super wrong - the direction was already lost
            LOGGER.info("attemped to destory multiblock without the direction!");
        }  else  {
            List<BlockPos> multiblockMembers = getMultiBlockMembers(worldIn, posIn, d);
            if (multiblockMembers != null) {
                updateMultiBlockMemberTiles(multiblockMembers, true);
            }
        }
            LOGGER.info("Multiblock Destroyed");
    }

    public void assignJobs() {
        BlockPos inputPos = getRedstoneInBlockPos();
        BlockPos outputPos = getRedstoneOutBlockPos();
        TileEntity te = Multiblock.getTileFromPos(this.world, inputPos);
        if (te instanceof HCCokeOvenFrameTile) {
            LOGGER.info("assigned red in to");
            LOGGER.info(inputPos);
            ((HCCokeOvenFrameTile) te).setJob(JOB_REDSTONE_IN);
        }
        te = Multiblock.getTileFromPos(this.world, outputPos);
        if (te instanceof HCCokeOvenFrameTile) {
            LOGGER.info("assigned red out to");
            LOGGER.info(outputPos);
            ((HCCokeOvenFrameTile) te).setJob(JOB_REDSTONE_OUT);
        }
    }


    public BlockPos getRedstoneInBlockPos() {
        if (this.redstoneIn == null) {
            this.redstoneIn = new BlockPos(this.pos.getX(), this.pos.getY() + 1, this.pos.getZ());
        }
        return this.redstoneIn;
    }

    public BlockPos getRedstoneOutBlockPos() {
        if (this.redstoneOut == null) {
            this.redstoneOut = new BlockPos(this.pos.getX(), this.pos.getY() - 1, this.pos.getZ());
        }
        return this.redstoneOut;
    }


    /*
      Updates all information in all multiblock members
     */
    private void updateMultiBlockMemberTiles(List<BlockPos> memberArray, boolean destroy) {
        for (int i = 0; i < memberArray.size(); i++) {
            BlockPos current = memberArray.get(i);
            TileEntity currentTile = getTileFromPos(world, current);
            if (currentTile instanceof HCCokeOvenFrameTile) {
                HCCokeOvenFrameTile castedCurrent = (HCCokeOvenFrameTile) currentTile;
                if (destroy) {
                    castedCurrent.destroyMultiBlock();
                } else {
                    castedCurrent.setupMultiBlock(getControllerPos());
                }
            }
        }
    }


    private BlockPos getControllerPos() {
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
        this.facingDirection = nbt.getInt(DIRECTION_FACING);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag = super.write(tag);
        tag.put(INVENTORY_IN, inputSlot.serializeNBT());
        tag.put(INVENTORY_OUT, outputSlot.serializeNBT());
        tag.putInt(NUM_TICKS, tickCounter);
        tag.putInt(DIRECTION_FACING, facingDirection);
        return tag;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull final Capability<T> cap, @Nullable final Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (world != null && world.getBlockState(pos).getBlock() != this.getBlockState().getBlock()) {//if the block at myself isn't myself, allow full access (Block Broken)
                return everything.cast();
            }
            if (side == null) {
                return everything.cast();
            } else {
                return automation.cast();
            }
        }
        return super.getCapability(cap, side);
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
    private ItemStack getRecipe(ItemStack stack) {
        if (stack.getItem() == Items.COAL) {
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
}
