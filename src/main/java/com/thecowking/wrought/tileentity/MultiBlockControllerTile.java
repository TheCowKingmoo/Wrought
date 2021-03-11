package com.thecowking.wrought.tileentity;

import com.thecowking.wrought.data.IMultiblockData;
import com.thecowking.wrought.data.MultiblockData;
import com.thecowking.wrought.init.RecipeSerializerInit;
import com.thecowking.wrought.inventory.containers.OutputFluidTank;
import com.thecowking.wrought.inventory.containers.honey_comb_coke_oven.HCCokeOvenContainer;
import com.thecowking.wrought.inventory.slots.*;
import com.thecowking.wrought.recipes.HoneyCombCokeOven.HoneyCombCokeOvenRecipe;
import com.thecowking.wrought.recipes.IWroughtRecipe;
import com.thecowking.wrought.tileentity.honey_comb_coke_oven.HCCokeOvenControllerTile;
import com.thecowking.wrought.tileentity.honey_comb_coke_oven.HCCokeOvenFrameTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.thecowking.wrought.data.MultiblockData.*;

public class MultiBlockControllerTile extends MultiBlockTile implements ITickableTileEntity {
    private static final Logger LOGGER = LogManager.getLogger();
    protected BlockPos redstoneIn;
    protected BlockPos redstoneOut;

    // tracks if the tile entity needs a block update
    protected boolean needUpdate = false;

    // holds the string that is displayed on the status button
    protected String status;

    // used to track when we can start an operations
    // -> didnt want something that processes too fast
    protected int tickCounter;

    public int timeElapsed = 0;
    public int timeComplete = 0;

    protected int needsUpdate = 0;

    protected IMultiblockData data;

    protected final int TICKSPEROPERATION = 20;

    protected boolean isRunning = false;
    protected boolean clogged = false;


    //Input Slots
    protected InputItemHandler inputSlots;
    //Output Slots
    protected OutputItemHandler outputSlots;
    //Backlog Items
    protected ItemStack[] itemBacklogs;

    //Fuel Slot
    protected InputItemHandler fuelInputSlot;

    //Holds all the handlers
    protected List<IItemHandlerModifiable> allHandlers;
    //Handlers for when the user uses the GUI to interact with slots
    protected LazyOptional<IItemHandler> everything;
    //Handlers when the world interacts with the multiblock
    protected LazyOptional<IItemHandler> automation;

    protected int numInputOutputSlots = 0;


    protected ItemStack[] processingItemStacks;




    public MultiBlockControllerTile(TileEntityType<?> tileEntityTypeIn, IMultiblockData data) {
        super(tileEntityTypeIn);
        this.data = data;
        this.status = "not init";

        initSlots();

        // build all slots that will be inserted/outputted via gui or world
        buildAllHandlers();

        IItemHandlerModifiable[] arr = new IItemHandlerModifiable[allHandlers.size()];
        this.allHandlers.toArray(arr);

        this.everything = LazyOptional.of(() -> new CombinedInvWrapper(arr));
        this.automation = LazyOptional.of(() -> new AutomationCombinedInvWrapper(arr));
    }

    public void initSlots()  {

        // input
        this.inputSlots = new InputItemHandler(data.getNumberItemInputSlots(), this, null, "input_slots");
        // output
        this.outputSlots = new OutputItemHandler(data.getNumberItemOutputSlots());

        // processing slots
        this.processingItemStacks = new ItemStack[data.getNumberItemOutputSlots()];

        //backlogs
        this.itemBacklogs = new ItemStack[data.getNumberItemOutputSlots()];

        //Fuel Input Slot
        this.fuelInputSlot = new InputItemHandler(1, this, null, "fuel");
    }

    public void buildAllHandlers()  {
        List<IItemHandlerModifiable> allHandlers = new ArrayList<>();
        allHandlers.add(inputSlots);
        allHandlers.add(outputSlots);
        allHandlers.add(fuelInputSlot);
    }





    /*
    Does the needed checks and casting to see if current BlockPos holds a correct member of multi-blocks
 */
    private boolean checkIfCorrectFrame(BlockPos currentPos)  {
        Block currentBlock = world.getBlockState(currentPos).getBlock();
        BlockState currentState = world.getBlockState(currentPos);
        if( currentState.hasTileEntity() || !(currentState.isAir(world, currentPos))) {
            return checkIfCorrectFrame(currentBlock);
        }
        return false;
    }

    public String getStatus()  {
        return this.status;
    }



    /*
      Grabs the Frame Tile Entity
     */
    private MultiBlockFrameTile getFrameTile(BlockPos posIn)  {
        TileEntity te = getTileFromPos(this.world, posIn);
        if( te != null && te instanceof MultiBlockFrameTile) {
            return (MultiBlockFrameTile) te;
        }
        return null;
    }



    /*
        Launches the GUI for the completed multiblock
     */
    public void openGUI(World world, PlayerEntity player, boolean isFormed) {

        NetworkHooks.openGui((ServerPlayerEntity) player, this.data.getContainerProvider(world, this.pos, isFormed), this.pos);
    }




    public IMultiblockData getData()  {
        return this.data;
    }
    public BlockPos getPos()  {return this.pos;}


    /*
      This is called when a controller is right clicked by a player when the multi-blocks is not formed
      Checks to make sure that the player is holding the correct item in hand to form the multi-blocks.
     */
    public boolean isValidMultiBlockFormer(Item item)  {
        return item == Items.STICK;
    }

    /*
      Returns the controllers facing direction
     */
    public Direction getDirectionFacing()  {
        return this.world.getBlockState(this.pos).get(BlockStateProperties.FACING);
    }

    /*
      Checks a frame blocks blockstate to see if it is powered by redstone
     */
    public boolean isRedstonePowered(BlockPos posIn)  {
        if(this.redstoneIn != null)  {
            MultiBlockFrameTile frameTile = getFrameTile(this.redstoneIn);
            return this.world.isBlockPowered(posIn);
            }
        return false;
    }

    /*
      Sets the value for the frame blocks REDSTONE blockstate
     */
    public void sendOutRedstone(int power)  {
        if(this.redstoneOut != null)  {
            MultiBlockFrameTile frameTile = getFrameTile(this.redstoneOut);
            if(frameTile != null)  {
                frameTile.setRedstonePower(power);
            }  else  {
                LOGGER.info("redstone out blocks is null");
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
      Lets nearby blocks know we need an update
     */
    protected void blockUpdate() {
        this.world.notifyBlockUpdate(this.pos, this.getBlockState(), this.getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
    }

    public boolean checkIfCorrectFrame(Block block)  {
        return true;
    }

    public BlockPos getControllerPos() {
        return this.pos;
    }

    public boolean isFormed()  {
        return this.getBlockState().get(MultiblockData.FORMED);
    }


    @Override
    public void tick() {
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


    protected boolean processAllItemBacklogs()  {
        for(int i = 0; i < itemBacklogs.length; i++)  {
            if(!(processItemBackLog(i)))  {
                return false;
            }
        }
        return true;
    }

    protected boolean processItemBackLog(int index)  {
        if(this.itemBacklogs[index] == ItemStack.EMPTY)  {return true;}

        // used to check if anything was processed
        ItemStack oldBacklog = this.itemBacklogs[index];

        // attempt to insert backlog into the item output slot
        // whatever is leftover is saved into itemBacklog
        this.itemBacklogs[index] = outputSlots.internalInsertItem(index, this.itemBacklogs[index].copy(), false);

        // check for changes
        if(this.itemBacklogs[index] != oldBacklog)  {this.needUpdate = true;}

        if(this.itemBacklogs[index] == ItemStack.EMPTY)  {return true;}

        this.status = "Output is full";
        return false;
    }


    /*
        Called to check if the processing item(s) have cooked long enough to be finished
     */
    protected boolean processing()  {
        boolean localClog = false;
        if(this.isRunning) {
            this.needUpdate = true;
            this.timeElapsed = 0;
            // go thru all processingItemStacks and move into output slots
            for(int i = 0; i < processingItemStacks.length; i++)  {
                this.itemBacklogs[i] = outputSlots.internalInsertItem(i, processingItemStacks[i].copy(), false);
                // check if somehow something got left over
                if(this.itemBacklogs[i] != ItemStack.EMPTY)  {
                    localClog = true;
                }
                processingItemStacks[i] = ItemStack.EMPTY;
            }
        }
        if(localClog)  {
            this.clogged = true;
            this.status = "clogged";
            return false;
        }
        return true;
    }


    public boolean finishedProcessingCurrentOperation()  {
        // Check if there is a previous item and the item has "cooked" long enough
        if (this.isRunning && this.timeElapsed++ < this.timeComplete) {

            this.needUpdate = true;
            this.timeElapsed++;
            return false;

        }
        // item has cooked long enough -> insert outputs and move onto next operation
        return true;
    }

    // should overwrite
    protected boolean areOutputsFull()  {
        for(int i = 0; i < data.getNumberItemOutputSlots(); i++)  {
            if(outputSlots.getStackInSlot(i).getCount() >= outputSlots.getStackInSlot(i).getMaxStackSize())  {
                this.status = "Not enough output room to process current recipe";
                return true;
            }
        }
        return false;
    }




    /*
  Flips states if machine is changing from off -> on or from on -> off
 */
    protected void machineChangeOperation(boolean online) {
        if (online == this.isRunning) {
            return;
        }
        this.isRunning = online;
        setOn(online);
        if(online)  {
            sendOutRedstone(15);
            this.status = "Processing";
        }  else  {
            sendOutRedstone(0);
        }
    }

    protected boolean isRunning()  {
        return this.isRunning;
    }



    /*
    Checks if redstone signal is on / off and turns off machine if on
 */
    protected boolean redstonePowered()  {

        if(isRedstonePowered(this.redstoneIn)) {
            machineChangeOperation(false);
            this.status = "Red stone Turning off";
            return true;
        }
        return false;
    }

    /*
      Assigns out "jobs" to frame blocks that the controller needs to keep track of
      eg: what blocks output / watch input for redstone
     */
    public void assignJobs() {
        BlockPos inputPos = data.getRedstoneInBlockPos(this.pos);
        BlockPos outputPos = data.getRedstoneOutBlockPos(this.pos);
        TileEntity te = MultiblockData.getTileFromPos(this.world, inputPos);
        if (te instanceof HCCokeOvenFrameTile) {
            ((HCCokeOvenFrameTile) te).setJob(JOB_REDSTONE_IN);
        }
        te = MultiblockData.getTileFromPos(this.world, outputPos);
        if (te instanceof HCCokeOvenFrameTile) {
            ((HCCokeOvenFrameTile) te).setJob(JOB_REDSTONE_OUT);
        }
    }

    public boolean isPrimarySlotEmpty()  {
        return this.inputSlots.getStackInSlot(0).isEmpty();
    }


    @Nullable
    public IWroughtRecipe getRecipe() {
        Set<IRecipe<?>> recipes = data.getRecipesByType(this.world);

        for (IRecipe<?> iRecipe : recipes) {
            HoneyCombCokeOvenRecipe recipe = (HoneyCombCokeOvenRecipe) iRecipe;
            if (recipe.matches(new RecipeWrapper(this.inputSlots), this.world)) {
                return recipe;
            }
        }
        return null;
    }

    /*
    Check if a new item has a recipe that the oven can use
 */
    protected boolean recipeChecker(IWroughtRecipe currentRecipe)  {

        // check if we have a recipe for item
        if (currentRecipe == null) {
            machineChangeOperation(false);
            this.status = "No Recipe for Item";
            return false;
        }
        return true;
    }


    //TODO - cut down lag by making state machine based off of insertion

    /*

     */
    public void attemptRunOperation() {
        // Check if any of the item backlogs is clogged - note that another operation will not happen until tickCount has passed if these fail
        if(!processAllItemBacklogs())  { return; }
        // check if redstone is turning machine off
        if(redstonePowered())  { return; }
        // increment how long current item has cooked
        if(finishedProcessingCurrentOperation())  { return; }

        // check to make sure output is not full before starting another operation
        if(areOutputsFull())  {return; }
        // moves things in processingItemStacks into OutputSlots
        if(!processing())  {return; }

        // New operation and new recipe
        IWroughtRecipe currentRecipe = this.getRecipe();
        if (!recipeChecker(currentRecipe)) { return; }
        mutliBlockOperation(currentRecipe);


    }

    /*
        Moves Recipes into processingItemStacks
     */
    public void mutliBlockOperation(IWroughtRecipe currentRecipe)  {
        if(currentRecipe == null)  {return;}

        List<ItemStack> outputs = currentRecipe.getItemOutputs();

        //FluidStack fluidOutput = this.getRecipe(getPrimaryItemInput()).getRecipeFluidStackOutput();
        this.timeComplete = currentRecipe.getBurnTime();

        for(int i = 0; i < outputs.size(); i++)  {
            this.processingItemStacks[i] = outputs.get(i);
        }
        this.needUpdate = true;
        if(!this.isRunning)  {
            machineChangeOperation(true);
        }
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


    /*
        Tells the server what to save to disk
     */
    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
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
        return super.getCapability(cap, side);
    }



}
