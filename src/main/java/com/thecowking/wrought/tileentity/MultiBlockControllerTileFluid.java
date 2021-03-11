package com.thecowking.wrought.tileentity;

import com.thecowking.wrought.data.IMultiblockData;
import com.thecowking.wrought.data.MultiblockData;
import com.thecowking.wrought.inventory.containers.OutputFluidTank;
import com.thecowking.wrought.inventory.slots.FluidItemInputHandler;
import com.thecowking.wrought.inventory.slots.FluidItemOutputHandler;
import com.thecowking.wrought.recipes.IWroughtRecipe;
import com.thecowking.wrought.util.InventoryUtils;
import net.minecraft.block.BlockState;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static com.thecowking.wrought.data.MultiblockData.BURN_TIME;
import static com.thecowking.wrought.data.MultiblockData.FLUID_TANK;

/*
    USed if the Multi-block has internal fluid tanks
    Main idea is to have each tank have its own fluidbacklog due to the way insertion works with fluids
        -> insert method returns a FluidStack of things it couldnt insert. This is set to backlog. To check
           if the tank is clogged it is a simple check to see if the backlog is empty
 */

public class MultiBlockControllerTileFluid extends MultiBlockControllerTile {

    // NBT Key(s)
    private String FLUID_BACKLOG = "FLUID_BACKLOG";
    private String NUM_OUTPUT_TANKS = "NUM_OUTPUT_TANKS";
    private String TANK_CAP = "TANK_CAP";

    private static final Logger LOGGER = LogManager.getLogger();

    // Fluid Item Input
    protected FluidItemInputHandler fluidItemInputSlots;

    // Fluid Item Output
    protected FluidItemOutputHandler fluidItemOutputSlots;

    // output fluid from current recipe
    private FluidStack[] processingFluidStacks;

    // used to stop operations if the bucket slot cannot be used
    protected ItemStack[] fluidItemBacklogs;


    // Output Tank
    protected int numOutputTanks;
    protected OutputFluidTank[] outputFluidTanks;
    protected FluidStack[] fluidBacklogs;
    protected int[] tankCapacities;


    public MultiBlockControllerTileFluid(TileEntityType<?> tileEntityTypeIn, IMultiblockData data, int numOutputTanks, int defaultCapacity) {
        super(tileEntityTypeIn, data);
        //init tank
        this.numOutputTanks = numOutputTanks;
        init(defaultCapacity);
    }

    @Override
    public void initSlots()  {
        super.initSlots();
        // init stuffs for tanks
        this.fluidItemInputSlots = new FluidItemInputHandler(this.numOutputTanks);
        this.fluidItemOutputSlots = new FluidItemOutputHandler(this.numOutputTanks);
        this.fluidItemBacklogs = new ItemStack[this.numOutputTanks];
        this.processingFluidStacks = new FluidStack[this.numOutputTanks];
    }

    @Override
    public void buildAllHandlers()  {
        super.buildAllHandlers();
        allHandlers.add(fluidItemInputSlots);
        allHandlers.add(fluidItemOutputSlots);
    }

    private void init(int defaultCapacity)  {
        outputFluidTanks = new OutputFluidTank[this.numOutputTanks];
        fluidBacklogs = new FluidStack[this.numOutputTanks];
        tankCapacities = new int[this.numOutputTanks];

        for(int i = 0; i < this.numOutputTanks; i++) {
            outputFluidTanks[i] = new OutputFluidTank(defaultCapacity);
            fluidBacklogs[i] = FluidStack.EMPTY;
            tankCapacities[i] = defaultCapacity;
        }
    }

    // getters
    public FluidStack getFluidInTank(int tankIndex)  {return getSingleTank(tankIndex).getFluid();}
    public FluidStack getFluidBackLog(int tankIndex)  {return this.fluidBacklogs[tankIndex];}
    public int getTankMaxSize(int tankIndex)  { return getSingleTank(tankIndex).getCapacity(); }
    public OutputFluidTank getSingleTank(int tankIndex)  {return outputFluidTanks[tankIndex];}
    public double getPercentageInTank(int tankIndex)  { return ((double)getFluidInTank(tankIndex).getAmount() / (double)getTankMaxSize(tankIndex)); }
    public OutputFluidTank[] getFluidTanks()  {
        return this.outputFluidTanks;
    }

    //setters
    public void setFluidBackLog(int tankIndex, FluidStack fluidStack)  { this.fluidBacklogs[tankIndex] = fluidStack; }

    public FluidStack insertFluidIntoTank(int tankIndex, FluidStack fluidStack)  {
        return getSingleTank(tankIndex).internalFill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
    }


    @Override
    protected boolean recipeChecker(IWroughtRecipe currentRecipe)  {
        if(!super.recipeChecker(currentRecipe))  {return false;}

        // get the fluid outputs from recipe
        List<FluidStack> fluidOutputs = currentRecipe.getFluidStackOutput();

        if(fluidOutputs.size() > this.numOutputTanks)  {return false;}
        if( fluidOutputs == null)  {return false;}

        // check if recipe has a fluid output
        for(int i = 0; i < fluidOutputs.size(); i++)  {
            // check to see if that fluids match
            if (fluidOutputs.get(i).getFluid() != this.outputFluidTanks[i].getFluid().getFluid()  )  {
                finishOperation();
                this.status = "Output Fluid does not match fluid in tank";
                return false;
            }
            // check if tank has space for fluid
            if(this.outputFluidTanks[i].getFluidAmount() + fluidOutputs.get(i).getAmount() > this.outputFluidTanks[i].getCapacity())  {
                finishOperation();
                this.status = "Not enough space in tank to process current recipe";
                return false;
            }
        }  //end loop
        return true;
    }

    public boolean processFluid()  {
        boolean localClog = false;
        for(int i = 0; i < processingFluidStacks.length; i++)  {
            // skip if we are looking at air
            if(this.processingFluidStacks[i] == FluidStack.EMPTY)  {continue;}
            // actual insert
            this.fluidBacklogs[i] = insertFluidIntoTank(i , processingFluidStacks[i]);
            // alert if some sort of clogging happened
            if(this.fluidBacklogs[i] != FluidStack.EMPTY)  {
                localClog = true;
            }
        }  //end loop

        if(localClog)  {
            this.status = "Fluid Clogged";
            this.clogged = true;
            return false;
        }
        return true;
    }


    protected ItemStack[] processAllFluidItems(int numTanks, FluidItemInputHandler[] itemFluidInputSlots, FluidItemOutputHandler[] itemFluidOutputSlots, ItemStack[] fluidItemBacklogs)  {
        ItemStack[] newBacklogs = new ItemStack[numTanks];
        for(int i = 0 ; i < numTanks; i++)  {
            newBacklogs[i] = processFluidContainerItem(i, itemFluidInputSlots[i], itemFluidOutputSlots[i], fluidItemBacklogs[i]);
        }
        return newBacklogs;
    }


    protected ItemStack processFluidContainerItem(int tankIndex, FluidItemInputHandler itemFluidInputSlot, FluidItemOutputHandler itemFluidOutputSlot, ItemStack fluidItemBacklog)  {

        // only try to process if we have at least one buckets worth
        if(getSingleTank(tankIndex).getFluidAmount() < 1000)  { return fluidItemBacklog; }

        // only process if there is an item to process
        if(itemFluidInputSlot.getStackInSlot(0).isEmpty())  { return fluidItemBacklog; }

        // only proces if no other item is in the output (things like buckets dont stack)
        if(!(itemFluidOutputSlot.getStackInSlot(0).isEmpty())) { return fluidItemBacklog; }

        // get the item in the fluid item input slot
        ItemStack fluidContainer = itemFluidInputSlot.getStackInSlot(0);

        LazyOptional<IFluidHandlerItem> itemFluidCapability = fluidContainer.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY);

        // check to see if somehow a non fluid container got in -> this check is also in SlotInputFluid
        if(!itemFluidCapability.isPresent())  { return fluidItemBacklog; }

        // if we have a bucket
        if(fluidContainer.getItem() instanceof BucketItem)  {

            ItemStack fluidBucket = InventoryUtils.fillBucketOrFluidContainer(fluidContainer, getSingleTank(tankIndex).getFluid());
            if(fluidBucket.isEmpty())  return fluidItemBacklog;

            itemFluidInputSlot.getStackInSlot(0).shrink(1);

            ItemStack filledContainer = InventoryUtils.fillBucketOrFluidContainer(fluidContainer, getSingleTank(tankIndex).getFluid());
            if (filledContainer.isEmpty())  {
                return fluidItemBacklog;
            }

            getSingleTank(tankIndex).drain(1000, IFluidHandler.FluidAction.EXECUTE);
            fluidItemBacklog = itemFluidOutputSlot.internalInsertItem(0, filledContainer.copy(), false);
            this.needUpdate = true;

            // we have some sort of container
        }  else  {
            IFluidHandlerItem fluidItemHandler = itemFluidCapability.resolve().get();
            FluidStack back = FluidUtil.tryFluidTransfer(fluidItemHandler, getSingleTank(tankIndex), getSingleTank(tankIndex).getFluid(), true);
            if (back.isEmpty())  {
                ItemStack f = itemFluidInputSlot.getStackInSlot(0).copy();
                itemFluidInputSlot.getStackInSlot(0).shrink(1);
                itemFluidOutputSlot.internalInsertItem(0, f, false);
                this.needUpdate = true;
            }
        }
        return fluidItemBacklog;
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        this.numOutputTanks = nbt.getInt(NUM_OUTPUT_TANKS);

        for(int i = 0; i < this.numOutputTanks; i++)  {
            tankCapacities[i] = nbt.getInt(TANK_CAP + i);
            OutputFluidTank tank = new OutputFluidTank(tankCapacities[i]);
            tank.readFromNBT(nbt.getCompound(MultiblockData.FLUID_TANK + i));
            outputFluidTanks[i] = tank;
            fluidBacklogs[i] = FluidStack.loadFluidStackFromNBT(nbt.getCompound(FLUID_BACKLOG + i));
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag = super.write(tag);
        tag.putInt(NUM_OUTPUT_TANKS, this.numOutputTanks);
        for(int i = 0; i < numOutputTanks; i++)  {
            tag.put(MultiblockData.FLUID_TANK + i, outputFluidTanks[i].writeToNBT(new CompoundNBT()));
            tag.put(FLUID_BACKLOG + i, fluidBacklogs[i].writeToNBT(new CompoundNBT()));
            tag.putInt(TANK_CAP + i, tankCapacities[i]);
        }
        return tag;
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


    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull final Capability<T> cap, @Nullable final Direction side) {
        if(cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)  {
            return LazyOptional.of(() -> getFluidTanks()).cast();
        }
        return super.getCapability(cap, side);
    }




}
