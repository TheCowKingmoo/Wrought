package com.thecowking.wrought.tileentity.honey_comb_coke_oven;

import com.thecowking.wrought.data.IMultiblockData;
import com.thecowking.wrought.data.HCCokeOvenData;
import com.thecowking.wrought.inventory.containers.honey_comb_coke_oven.HCCokeOvenContainer;
import com.thecowking.wrought.inventory.containers.honey_comb_coke_oven.HCCokeOvenContainerMultiblock;
import com.thecowking.wrought.data.MultiblockData;
import com.thecowking.wrought.inventory.containers.OutputFluidTank;
import com.thecowking.wrought.inventory.slots.*;
import com.thecowking.wrought.recipes.HoneyCombCokeOven.HoneyCombCokeOvenRecipe;
import com.thecowking.wrought.tileentity.MultiBlockControllerTile;
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
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
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
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
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

import static com.thecowking.wrought.data.MultiblockData.*;
import static com.thecowking.wrought.util.RegistryHandler.*;

public class HCCokeOvenControllerTile extends MultiBlockControllerTile implements INamedContainerProvider {
    private static final Logger LOGGER = LogManager.getLogger();

    // tracks if the tile entity needs a block update
    private boolean needUpdate = false;



    // used to track when we can start an operations
    // -> didnt want something that processes too fast
    private int tickCounter;

    // tracks for the light level and blockstate
    private boolean isSmelting = false;

    // input and outputs
    protected InputItemHandler primaryInputSlot;

    // Outputs
    protected OutputItemHandler primaryOutputSlot;
    protected OutputItemHandler secondaryOutputSlot;

    // Fluid Item Input-Output
    protected FluidItemInputHandler itemFluidInputSlot;
    protected FluidItemOutputHandler itemFluidOutputSlot;



    // main tank
    private OutputFluidTank fluidTank;

    // used when player is directly accessing multi-block
    private final LazyOptional<IItemHandler> everything = LazyOptional.of(() -> new CombinedInvWrapper(primaryInputSlot, itemFluidInputSlot, primaryOutputSlot, secondaryOutputSlot, itemFluidOutputSlot));

    // used when in world things interact with multi-block
    private final LazyOptional<IItemHandler> automation = LazyOptional.of(() -> new AutomationCombinedInvWrapper(primaryInputSlot, itemFluidInputSlot, primaryOutputSlot, secondaryOutputSlot, itemFluidOutputSlot));

    // used to stop operation for when an item cannot be inserted into output slot
    private ItemStack itemBacklog;

    // used to stop operation for when a fluid cannot be inserted into output slot
    private FluidStack fluidBacklog;

    // holds information about the current oven operation
    private ItemStack processingPrimaryItemStack;
    private ItemStack processingSecondaryItemStack;
    private ItemStack processingTrinaryItemStack;

    // output fluid from current recipe
    private FluidStack processingFluidStack;

    // holds the string that is displayed on the status button
    private String status;

    // used to stop operations if the bucket slot cannot be used
    private ItemStack fluidItemBacklog;

    // lets us know how long the current item has cooked for
    public int operationProgression;

    // lets us know when the current item is done cooking
    public int operationComplete;


    public int timeElapsed = 0;
    public int timeComplete = 0;





    public HCCokeOvenControllerTile() {
        super(H_C_COKE_CONTROLLER_TILE.get(), new HCCokeOvenData());

        //init item intputs
        this.primaryInputSlot = new InputItemHandler(1, this, null, "primary");

        // init item outputs
        this.primaryOutputSlot = new OutputItemHandler(1);
        this.secondaryOutputSlot = new OutputItemHandler(1);

        // init fluid item input
        this.itemFluidInputSlot = new FluidItemInputHandler(1);

        // init fluid item output
        this.itemFluidOutputSlot = new FluidItemOutputHandler(1);

        //init tank
        this.fluidTank = new OutputFluidTank(16000);

        //init internal stack for processing
        this.itemBacklog = ItemStack.EMPTY;
        this.fluidBacklog = FluidStack.EMPTY;
        this.fluidItemBacklog = ItemStack.EMPTY;
        this.processingPrimaryItemStack = ItemStack.EMPTY;
        this.processingSecondaryItemStack = ItemStack.EMPTY;
        this.processingTrinaryItemStack = ItemStack.EMPTY;


        this.processingFluidStack = FluidStack.EMPTY;

        // TODO - get rid of state data
        this.operationProgression = 0;
        this.operationComplete = 0;


        this.tickCounter = 0;

        this.status = "Standing By";
    }

    /*
        Getters
     */
    public World getWorld()  {return this.world;}
    public FluidStack getFluidInTank()  {return fluidTank.getFluid();}


    /*
        Runs every 1/20 seconds
     */
    @Override
    public void tick()  {
        super.tick();
        // check if we are in correct instance
        if (this.world == null || this.world.isRemote) {
            finishOperation();
            return;
        }

        // check if we have a multi-block
        if (!isFormed(getControllerPos())) {
            finishOperation();
            return;
        }

        // Check if enough time passed for an operation
        // note - don't really care about writing this to mem
        if(tickCounter < this.TICKSPEROPERATION)  {
            tickCounter++;
            finishOperation();
            return;
        }
        tickCounter = 0;

        attemptRunOperation();

        finishOperation();
    }

    /*
        Main operation driver - checks all instances to make sure that we can actually run an operation
     */
    public void attemptRunOperation() {
        // Checks if we can fill the item in the fluid input with the tanks fluid
        processFluidContainerItem();

        // Check if the item output is clogged - note that another operation will not happen until tickCount has passed if these fail
        if(!processItemBackLog())  { return; }

        // check if redstone is turning machine off
        if(redstonePowered())  { return; }

        // either increment how long current item has cooked or get ready to move onto next item
        if(!processItem())  { return; }

        // check to make sure output is not full before starting another operation
        if (primaryOutputSlot.getStackInSlot(0).getCount() >= primaryOutputSlot.getStackInSlot(0).getMaxStackSize()) {
            machineChangeOperation(false);
            this.status = "Not enough room to process current recipe";
            return;
        }

        if(getPrimaryItemInput() == ItemStack.EMPTY || getPrimaryItemInput().getItem() == Items.AIR)  {
            this.status = "Standing By";
            return;
        }

        // yank the current recipe for an item in - primary input is treated as key
        HoneyCombCokeOvenRecipe currentRecipe = this.getRecipe(getPrimaryItemInput());

        // check if we have a recipe for item
        if (!recipeChecker(currentRecipe)) { return; }
        if (!fluidRecipeChecker(currentRecipe))  { return; }

        ovenOperation(currentRecipe);
    }


    /*
        Attempts to clear item backlogs
        false if it could not - thus i want the machine to halt
        true if we should continue on
     */
    private boolean processItemBackLog()  {


        if(this.itemBacklog == ItemStack.EMPTY)  {return true;}

        // used to check if anything was processed
        ItemStack oldBacklog = this.itemBacklog;

        // attempt to insert backlog into the item output slot
        // whatever is leftover is saved into itemBacklog
        this.itemBacklog = primaryOutputSlot.internalInsertItem(0, this.itemBacklog.copy(), false);


        // check for changes
        if(this.itemBacklog != oldBacklog)  {this.needUpdate = true;}

        if(this.itemBacklog == ItemStack.EMPTY)  {return true;}

        this.status = "Output is full";
        return false;
    }

    /*
        Checks if redstone signal is on / off and turns off machine if on
     */
    private boolean redstonePowered()  {

        if(isRedstonePowered(this.redstoneIn)) {
            machineChangeOperation(false);
            this.status = "Red stone Turning off";
            return true;
        }
        return false;
    }

    /*
        Increments the timer for an operation if it has not finished yet
        If finished attempts to move item+fluid into output slot
     */

    private boolean processItem()  {

        // Check if there is a previous item and the item has "cooked" long enough
        if (processingPrimaryItemStack != ItemStack.EMPTY && this.timeElapsed++ < this.timeComplete) {

            this.needUpdate = true;
            this.timeElapsed++;
            return false;

            // item has cooked long enough -> insert outputs and move onto next operation
        }  else if(processingPrimaryItemStack != ItemStack.EMPTY) {
            this.needUpdate = true;
            this.timeElapsed = 0;
            // attempt to insert fluid from craft and fill leftovers into backlog tank
            fluidBacklog = fluidTank.internalFill(processingFluidStack, IFluidHandler.FluidAction.EXECUTE);
            // attempt to insert item from craft and fill leftovers into backlog container

            // unsure why but if i do not .copy() the outputs randomly multiplies by two on each successful operation
            itemBacklog = primaryOutputSlot.internalInsertItem(0, processingPrimaryItemStack.copy(), false);
            secondaryOutputSlot.internalInsertItem(0, processingSecondaryItemStack.copy(), false);
            processingPrimaryItemStack = ItemStack.EMPTY;
            processingSecondaryItemStack = ItemStack.EMPTY;

            processingFluidStack = FluidStack.EMPTY;
        }
        return true;
    }


    protected boolean isMachineRunning()  {
        return this.isSmelting;
    }



    public String getStatus()  {
        return this.status;
    }

    /*
        Check if a new item has a recipe that the oven can use
     */
    private boolean recipeChecker(HoneyCombCokeOvenRecipe currentRecipe)  {
        //LOGGER.info("Recipe Check");

        // check if we have a recipe for item
        if (currentRecipe == null) {
            machineChangeOperation(false);
            this.status = "No Recipe for Item";
            LOGGER.info(getPrimaryItemInput());
            return false;
        }
        return true;
    }

    /*
        Check if we can store a fluid for a new operation
     */
    private boolean fluidRecipeChecker(HoneyCombCokeOvenRecipe currentRecipe)  {

        // get the fluid output from recipe
        FluidStack recipeFluidOutput = currentRecipe.getRecipeFluidStackOutput();

        // check if recipe has a fluid output
        if(recipeFluidOutput != null && !fluidTank.isEmpty())  {

            // check if fluid matches tank and if tank has space for fluid
            if(fluidTank.getFluidAmount() + recipeFluidOutput.getAmount() > fluidTank.getCapacity())  {
                finishOperation();
                this.status = "Not enough space in tank to process current recipe";
                return false;
            }

            // check to see if that fluids match
            if (recipeFluidOutput.getFluid() != fluidTank.getFluid().getFluid()  )  {
                finishOperation();
                this.status = "Output Fluid does not match fluid in tank";
                return false;
            }

        }
        return true;
    }

    private ItemStack getPrimaryItemInput()  {
        return primaryInputSlot.getStackInSlot(0);
    }


    /*
     Method to run a single oven operation
     */
    private void ovenOperation(HoneyCombCokeOvenRecipe currentRecipe) {
        if(currentRecipe == null)  {return;}

        ItemStack primaryOutput = currentRecipe.getPrimaryOutput();

        FluidStack fluidOutput = this.getRecipe(getPrimaryItemInput()).getRecipeFluidStackOutput();
        this.timeComplete = currentRecipe.getBurnTime();
        this.operationComplete = currentRecipe.getBurnTime();


        if (primaryOutput != null && primaryOutput.getItem() != Items.AIR) {

            if(!this.isSmelting)  {
                machineChangeOperation(true);
            }

            // take item out of input
            // TODO - multi item input
            getPrimaryItemInput().shrink(1);

            this.processingPrimaryItemStack = currentRecipe.getPrimaryOutput();
            this.processingSecondaryItemStack = currentRecipe.getSecondaryOutput();

            this.processingFluidStack = fluidOutput;
            this.needUpdate = true;
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
        setOn(online);
        if(online)  {
            sendOutRedstone(15);
            this.status = "Processing";
        }  else  {
            sendOutRedstone(0);
        }
    }

    /*
    Processes items in the "bucket" slot
     */
    protected void processFluidContainerItem()  {
        LOGGER.info(fluidTank.getFluidAmount());

        // only try to process if we have at least one buckets worth
        if(fluidTank.getFluidAmount() < 1000)  { return; }

        // only process if there is an item to process
        if(itemFluidInputSlot.getStackInSlot(0).isEmpty())  { return; }

        // only proces if no other item is in the output (things like buckets dont stack)
        if(!(itemFluidOutputSlot.getStackInSlot(0).isEmpty())) { return; }

        // get the item in the fluid item input slot
        ItemStack fluidContainer = itemFluidInputSlot.getStackInSlot(0);

        LazyOptional <net.minecraftforge.fluids.capability.IFluidHandlerItem> itemFluidCapability = fluidContainer.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY);

        // check to see if somehow a non fluid container got in -> this check is also in SlotInputFluid
        if(!itemFluidCapability.isPresent())  { return; }



        // if we have a bucket
        if(fluidContainer.getItem() instanceof BucketItem)  {

            ItemStack fluidBucket = InventoryUtils.fillBucketOrFluidContainer(fluidContainer, fluidTank.getFluid());
            if(fluidBucket.isEmpty())  return;

            itemFluidInputSlot.getStackInSlot(0).shrink(1);

            ItemStack filledContainer = InventoryUtils.fillBucketOrFluidContainer(fluidContainer, fluidTank.getFluid());
            if (filledContainer.isEmpty())  {
                return;
            }
            LOGGER.info(filledContainer);

            fluidTank.drain(1000, IFluidHandler.FluidAction.EXECUTE);
            fluidItemBacklog = itemFluidOutputSlot.internalInsertItem(0, filledContainer.copy(), false);
            this.needUpdate = true;

            // we have some sort of container
        }  else  {
            IFluidHandlerItem fluidItemHandler = itemFluidCapability.resolve().get();
            FluidStack back = FluidUtil.tryFluidTransfer(fluidItemHandler, fluidTank, fluidTank.getFluid(), true);
            LOGGER.info(back.getDisplayName());
            if (back.isEmpty())  {
                ItemStack f = itemFluidInputSlot.getStackInSlot(0).copy();
                itemFluidInputSlot.getStackInSlot(0).shrink(1);
                itemFluidOutputSlot.internalInsertItem(0, f, false);
                this.needUpdate = true;
            }
        }
    }

    /*
        Method to set off updates
        This way markDirty is not called multiple times in one operation
     */
    public void finishOperation()  {
        if (this.needUpdate)  {
            this.needUpdate = false;
            blockUpdate();
            markDirty();
        }
    }

    /*
        Tells the server what to save to disk
     */
    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        this.getPrimaryItemInput().deserializeNBT(nbt.getCompound(PRIMARY_INVENTORY_IN));
        primaryOutputSlot.deserializeNBT(nbt.getCompound(PRIMARY_INVENTORY_OUT));
        secondaryOutputSlot.deserializeNBT(nbt.getCompound(SECONDARY_INVENTORY_OUT));
        itemFluidInputSlot.deserializeNBT(nbt.getCompound(FLUID_INVENTORY_IN));
        itemFluidOutputSlot.deserializeNBT(nbt.getCompound(FLUID_INVENTORY_OUT));
        fluidTank.readFromNBT(nbt.getCompound(FLUID_TANK));
        this.timeElapsed = nbt.getInt(BURN_TIME);
        this.timeComplete = nbt.getInt(BURN_COMPLETE_TIME);
        this.status = nbt.getString(STATUS);
    }

    /*
        Tells the server what to read from disk on chunk load
     */
    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag = super.write(tag);
        tag.put(PRIMARY_INVENTORY_IN, getPrimaryItemInput().serializeNBT());
        tag.put(PRIMARY_INVENTORY_OUT, primaryOutputSlot.serializeNBT());
        tag.put(SECONDARY_INVENTORY_OUT, secondaryOutputSlot.serializeNBT());
        tag.put(FLUID_INVENTORY_IN, itemFluidInputSlot.serializeNBT());
        tag.put(FLUID_INVENTORY_OUT, itemFluidOutputSlot.serializeNBT());
        tag.put(FLUID_TANK, fluidTank.writeToNBT(new CompoundNBT()));
        tag.putInt(BURN_TIME, this.timeElapsed);
        tag.putInt(BURN_COMPLETE_TIME, this.timeComplete);
        tag.putString(STATUS, this.status);
        return tag;
    }

    /*
        lets the world around it know what can be automated
     */
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
        return new HCCokeOvenContainerMultiblock(windowID, this.world, getControllerPos(), playerInv);
    }

    /*
        Name that is displayed on the GUI
     */
    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("Coke Oven Controller");
    }

    /*
        Finds a recipe for a given input
     */
    @Nullable
    public HoneyCombCokeOvenRecipe getRecipe(ItemStack stack) {
        if (stack == null) {
            return null;
        }
        Set<IRecipe<?>> recipes = findRecipesByType(RecipeSerializerInit.HONEY_COMB_OVEN_TYPE, this.world);

        for (IRecipe<?> iRecipe : recipes) {
            HoneyCombCokeOvenRecipe recipe = (HoneyCombCokeOvenRecipe) iRecipe;
            if (recipe.matches(new RecipeWrapper(this.primaryInputSlot), this.world)) {
                return recipe;
            }
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


    /*
      Assigns out "jobs" to frame blocks that the controller needs to keep track of
      eg: what blocks output / watch input for redstone
     */
    @Override
    public void assignJobs() {
        BlockPos inputPos = getRedstoneInBlockPos();
        BlockPos outputPos = getRedstoneOutBlockPos();
        TileEntity te = MultiblockData.getTileFromPos(this.world, inputPos);
        if (te instanceof HCCokeOvenFrameTile) {
            ((HCCokeOvenFrameTile) te).setJob(JOB_REDSTONE_IN);
        }
        te = MultiblockData.getTileFromPos(this.world, outputPos);
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


    public int getTankMaxSize()  {
        return fluidTank.getCapacity();
    }


    @Override
    public CompoundNBT getUpdateTag()  {
        return this.write(new CompoundNBT());
    }
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT nbt = new CompoundNBT();
        this.write(nbt);

        // the number here is generally ignored for non-vanilla TileEntities, 0 is safest
        return new SUpdateTileEntityPacket(this.getPos(), 0, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        this.read(world.getBlockState(packet.getPos()), packet.getNbtCompound());
    }


}
