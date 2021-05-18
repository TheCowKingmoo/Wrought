package com.thecowking.wrought.tileentity;

import com.thecowking.wrought.Wrought;
import com.thecowking.wrought.data.IMultiblockData;
import com.thecowking.wrought.data.MultiblockData;
import com.thecowking.wrought.inventory.containers.InputFluidTank;
import com.thecowking.wrought.inventory.containers.OutputFluidTanks;
import com.thecowking.wrought.inventory.slots.FluidItemOutputHandler;
import com.thecowking.wrought.inventory.slots.InputFluidHandler;
import com.thecowking.wrought.recipes.IWroughtRecipe;
import com.thecowking.wrought.util.InventoryUtils;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
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
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.thecowking.wrought.data.MultiblockData.*;

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


    // Fluid Item Input
    protected InputFluidHandler fluidItemInputSlots;

    // Fluid Item Output
    protected FluidItemOutputHandler fluidItemOutputSlots;



    // used to stop operations if the bucket slot cannot be used
    protected ItemStack[] fluidItemBacklogs;

    // Input Tanks
    protected int numInputTanks;
    protected InputFluidTank inputFluidTanks;
    protected FluidStack[] inputProcessingFluidStacks;
    protected int[] inputTankCapacities;




    // Output Tank
    protected int numOutputTanks;
    protected OutputFluidTanks outputFluidTanks;
    protected FluidStack[] fluidBacklogs;
    protected int[] outputTankCapacities;
    private FluidStack[] outputProcessingFluidStacks;

    public MultiBlockControllerTileFluid(TileEntityType<?> tileEntityTypeIn,
                                         int numberInputSlots,
                                         int numberOutputSlots,
                                         boolean fuelSlot,
                                         IMultiblockData data,
                                         int numOutputTanks,
                                         int numInputTanks,
                                         int defaultCapacity
    ) {
        super(tileEntityTypeIn, numberInputSlots, numberOutputSlots, fuelSlot, data);
        //init tank
        this.numOutputTanks = numOutputTanks;
        this.numInputTanks = numInputTanks;

        initFluidSlots(defaultCapacity);
    }


    @Override
    public void buildAllHandlers()  {
        super.buildAllHandlers();
        this.allHandlers.add(fluidItemInputSlots);
        this.allHandlers.add(fluidItemOutputSlots);
    }

    private void initFluidSlots(int defaultCapacity)  {
        // fluid item input slots
        this.fluidItemInputSlots = new InputFluidHandler(this.numOutputTanks + this.numInputTanks, this, null, "fluid_item_input");
        // fluid item output slots
        this.fluidItemOutputSlots = new FluidItemOutputHandler(this.numOutputTanks + this.numInputTanks);


        this.fluidItemBacklogs = new ItemStack[this.numOutputTanks];
        this.outputFluidTanks = new OutputFluidTanks(defaultCapacity, numOutputTanks);
        this.fluidBacklogs = new FluidStack[this.numOutputTanks];
        this.outputTankCapacities = new int[this.numOutputTanks];
        this.outputProcessingFluidStacks = new FluidStack[this.numOutputTanks];

        // fluid output tanks
        for(int i = 0; i < this.numOutputTanks; i++) {
            this.fluidBacklogs[i] = FluidStack.EMPTY;
            this.outputProcessingFluidStacks[i] = FluidStack.EMPTY;
            this.outputTankCapacities[i] = defaultCapacity;
            this.fluidItemBacklogs[i] = ItemStack.EMPTY;
        }

        // fluid input tanks
        this.inputFluidTanks = new InputFluidTank(defaultCapacity, this.numInputTanks);
        this.inputProcessingFluidStacks = new FluidStack[this.numInputTanks];
        this.inputTankCapacities = new int[this.numInputTanks];
        this.inputFluidTanks = new InputFluidTank(defaultCapacity, numInputTanks);

        for(int i = 0; i < this.numInputTanks; i++) {
            this.inputTankCapacities[i] = defaultCapacity;
            this.inputProcessingFluidStacks[i] = FluidStack.EMPTY;
        }
    }

    // getters
    public FluidStack getFluidInOutputTank(int tankIndex)  {return getOutputTank(tankIndex).getFluid();}
    public FluidStack getFluidInInputTank(int tankIndex)  {return getInputTank(tankIndex).getFluid();}

    public FluidStack getFluidBackLog(int tankIndex)  {return this.fluidBacklogs[tankIndex];}
    public int getOutputTankMaxSize(int tankIndex)  { return this.outputTankCapacities[tankIndex]; }
    public int getInputTankMaxSize(int tankIndex)  { return this.inputTankCapacities[tankIndex]; }

    public FluidTank getOutputTank(int tankIndex)  {return outputFluidTanks.getFluidTank(tankIndex);}
    public FluidTank getInputTank(int tankIndex)  {return inputFluidTanks.getFluidTank(tankIndex);}

    /*
        As the fluid item input / output are in the same wrapper, i need to be able to tell
        if the slot is attached to an output or an input tank so that i can allow/deny items
     */
    public boolean isSlotAttachedToOutputTank(int index)  {
        return index > this.numInputTanks-1;
    }

    public double getPercentageInOutputTank(int tankIndex)  { return ((double)getFluidInOutputTank(tankIndex).getAmount() / (double)getOutputTankMaxSize(tankIndex)); }
    public double getPercentageInInputTank(int tankIndex)  { return ((double)getFluidInInputTank(tankIndex).getAmount() / (double)getInputTankMaxSize(tankIndex)); }

    public OutputFluidTanks getFluidTanks()  {
        return this.outputFluidTanks;
    }
    public boolean hasFluidInputSlot()  { return (this.inputFluidTanks != null && this.inputFluidTanks.getTanks() > 0); }

    //setters
    public void setFluidBackLog(int tankIndex, FluidStack fluidStack)  { this.fluidBacklogs[tankIndex] = fluidStack; }

    public FluidStack insertFluidIntoTank(int tankIndex, FluidStack fluidStack)  {
        return this.outputFluidTanks.internalFill(fluidStack, IFluidHandler.FluidAction.EXECUTE, tankIndex);
    }

    @Override
    public boolean recipeChecker(IWroughtRecipe currentRecipe)  {
        if(currentRecipe == null) {
            Wrought.LOGGER.info("fluid recipe is null");
            return false;
        }

        // check if we have any input slots
        if(this.hasItemInputSlot())  {
            if(!super.recipeChecker(currentRecipe))  {
                return false;
            }
        }  else if(!hasFluidInputSlot())  {
            return false;
        }




        // get the fluid outputs from recipe
        List<FluidStack> fluidOutputs = currentRecipe.getFluidStackOutput();

        if(fluidOutputs.size() > this.numOutputTanks)  {return false;}
        if( fluidOutputs == null)  {return false;}


        // check if recipe has a fluid output
        for(int i = 0; i < fluidOutputs.size(); i++)  {
            // check to see if that fluids match
            Fluid fluidInTank = this.getFluidInOutputTank(i).getFluid();
            if (fluidInTank != Fluids.EMPTY && fluidOutputs.get(i).getFluid() != fluidInTank)  {
                finishOperation();
                this.status = "Output Fluid does not match fluid in tank";
                return false;
            }
            // check if tank has space for fluid
            if(this.outputFluidTanks.getFluidTank(i).getFluidAmount() + fluidOutputs.get(i).getAmount() > this.outputFluidTanks.getFluidTank(i).getCapacity())  {
                finishOperation();
                this.status = "Not enough space in tank to process current recipe";
                return false;
            }
        }  //end loop
        return true;
    }


    @Override
    protected boolean proccessInputsOutputs()  {
        if(!super.proccessInputsOutputs())  {
            return false;
        }
        for(int i = 0; i < fluidBacklogs.length; i++)  {
            if(!processFluidBackLog(i))  {
                return false;
            }
        }
        return true;
    }

    private boolean processFluidBackLog(int index)  {
        if(this.fluidBacklogs[index] == FluidStack.EMPTY)  {return true;}

        // used to check if anything was processed
        FluidStack oldBacklog = this.fluidBacklogs[index];

        // attempt to insert backlog into the item output slot
        // whatever is leftover is saved into itemBacklog
        this.fluidBacklogs[index] = insertFluidIntoTank(index, this.fluidBacklogs[index]);

        // check for changes
        if(this.fluidBacklogs[index] != oldBacklog)  {this.needUpdate = true;}

        if(this.fluidBacklogs[index] == FluidStack.EMPTY)  {return true;}

        this.status = "Fluid Output is full";
        return false;
    }


    protected void processAllFluidContainerItems()  {
        int i = 0;
        while(i < this.numOutputTanks)  {
            processFluidOutputContainerItem(i++);
        }
        while(i < this.numInputTanks)  {
            processFluidInputContainerItem(i++);
        }
    }

    /*
        Used when we have an input tank with slots attached to it
        so that we can move a buckets fluid into the internal tank
     */

    protected void processFluidInputContainerItem(int index)  {
        // not a fluid item input slot
        if(!this.hasFluidInputSlot())  return;

        // only try to process if we ahve room for ones bucket worth
        if(getInputTank(index).getFluidAmount() + 1000 > getInputTank(index).getCapacity())  { return; }

        // only process if there is an item to process
        if(this.fluidItemInputSlots.getStackInSlot(index).isEmpty())  { return; }

        // get the item in the fluid item input slot
        ItemStack fluidContainer = fluidItemInputSlots.getStackInSlot(index);

        LazyOptional<IFluidHandlerItem> itemFluidCapability = fluidContainer.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY);

        // check to see if somehow a non fluid container got in -> this check is also in SlotInputFluid
        if(!itemFluidCapability.isPresent())  { return; }

        IFluidHandlerItem fluidItemHandler = itemFluidCapability.resolve().get();


        if(fluidItemHandler.getTanks() < 1)  return;

        // check that that intput tanks current fluid is either equal to empty or to whats in the container
        if(this.getInputTank(index).getFluid().getFluid() != Fluids.EMPTY && this.getInputTank(index).getFluid().getFluid() != fluidItemHandler.getFluidInTank(0).getFluid()) return;


        FluidStack back = FluidUtil.tryFluidTransfer(getInputTank(index), fluidItemHandler, fluidItemHandler.getFluidInTank(0), true);
        if (back.isEmpty())  {

            ItemStack f = fluidItemHandler.getContainer().copy();
            fluidItemInputSlots.getStackInSlot(index).shrink(1);
            fluidItemOutputSlots.internalInsertItem(index, f, false);
            this.needUpdate = true;
        }
    }

    /*
        Used when we have an output tank where we want to fill an empty bucket with the output tanks fluid
     */
    protected void processFluidOutputContainerItem(int index)  {

        if(this.fluidItemBacklogs[index] != ItemStack.EMPTY)  {
            this.fluidItemBacklogs[index] = fluidItemOutputSlots.internalInsertItem(index, this.fluidItemBacklogs[index].copy(), false);
        }

        if(this.fluidItemBacklogs[index] != ItemStack.EMPTY) return;

        // only try to process if we have at least one buckets worth
        if(getOutputTank(index).getFluidAmount() < 1000)  { return; }

        // only process if there is an item to process
        if(this.fluidItemInputSlots.getStackInSlot(index).isEmpty())  { return; }

        // only proces if no other item is in the output (things like buckets dont stack)
        //if(!(fluidItemOutputSlots.getStackInSlot(index).isEmpty())) { return; }

        // get the item in the fluid item input slot
        ItemStack fluidContainer = fluidItemInputSlots.getStackInSlot(index);

        LazyOptional<IFluidHandlerItem> itemFluidCapability = fluidContainer.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY);


        // check to see if somehow a non fluid container got in -> this check is also in SlotInputFluid
        if(!itemFluidCapability.isPresent())  { return; }

        // if we have a bucket
        if(fluidContainer.getItem() instanceof BucketItem)  {

            ItemStack fluidBucket = InventoryUtils.fillBucketOrFluidContainer(fluidContainer, getOutputTank(index).getFluid());
            if(fluidBucket.isEmpty())  return;

            ItemStack filledContainer = InventoryUtils.fillBucketOrFluidContainer(fluidContainer, getOutputTank(index).getFluid());
            if (filledContainer.isEmpty())  {
                return;
            }

            fluidItemInputSlots.getStackInSlot(index).shrink(1);

            getOutputTank(index).drain(1000, IFluidHandler.FluidAction.EXECUTE);

            this.fluidItemBacklogs[index] = fluidItemOutputSlots.internalInsertItem(index, filledContainer.copy(), false);
            this.needUpdate = true;

            // we have some sort of container
        }  else  {
            IFluidHandlerItem fluidItemHandler = itemFluidCapability.resolve().get();
            FluidStack back = FluidUtil.tryFluidTransfer(fluidItemHandler, getOutputTank(index), getOutputTank(index).getFluid(), true);
            if (back.isEmpty())  {
                ItemStack f = fluidItemInputSlots.getStackInSlot(index).copy();
                fluidItemInputSlots.getStackInSlot(index).shrink(1);
                fluidItemOutputSlots.internalInsertItem(index, f, false);
                this.needUpdate = true;
            }
        }
    }

    /*
        Override to allow for checks to include fluid input tanks as recipes
     */
    @Override
    @Nullable
    public IWroughtRecipe getRecipe() {
        if(hasFluidInputSlot())  {
            Wrought.LOGGER.info("has fluid in slots");
            Set<IRecipe<?>> recipes = data.getRecipesByType(this.world);
            for (IRecipe<?> iRecipe : recipes) {
                IWroughtRecipe recipe = (IWroughtRecipe) iRecipe;
                Wrought.LOGGER.info(recipe.getItemOutputs().get(0).getDisplayName());
                if (recipe.matches(this.inputFluidTanks, this.world, this)) {
                    return recipe;
                }
            }
            return null;
        }
        return super.getRecipe();
    }

    @Override
    public boolean attemptRunOperation() {
        processAllFluidContainerItems();
        super.attemptRunOperation();
        return true;
    }

    @Override
    protected boolean areOutputsFull(IWroughtRecipe recipe)  {
        if(super.areOutputsFull(recipe)) return true;
        for(int i = 0; i < recipe.getNumFluidOutputs(); i++)  {
            // check that both are the same fluid and that there is enough room in tank for the output
            if(getFluidInOutputTank(i).getAmount() + recipe.getFluidOutput(i).getAmount() > getOutputTankMaxSize(i))  {
                this.status = "Not enough fluid output room to process current recipe";
                return true;
            }
        }
        return false;
    }

    @Override
    public void mutliBlockOperation(IWroughtRecipe currentRecipe)  {
        super.mutliBlockOperation(currentRecipe);
        if(currentRecipe == null)  {return;}

        List<FluidStack> fluidOutputs = currentRecipe.getFluidOutputs();

        // consume all inputs that are needed
        for(int i = 0; i < currentRecipe.getNumFluidInputs(); i++)  {
            // TODO - shrink by dynamic num
            getInputTank(i).drain(1000, IFluidHandler.FluidAction.EXECUTE);
        }

        for(int i = 0; i < fluidOutputs.size(); i++)  {
            this.outputProcessingFluidStacks[i] = fluidOutputs.get(i);
        }
    }

    @Override
    public boolean processing()  {
        boolean localClog = !super.processing();    // a bit counter intuatiuve but if the item has a clog it returns false

        if(this.isRunning) {
            for(int i = 0; i < outputProcessingFluidStacks.length; i++)  {
                fluidBacklogs[i] = outputFluidTanks.internalFill(outputProcessingFluidStacks[i], IFluidHandler.FluidAction.EXECUTE, i);
                outputProcessingFluidStacks[i] = FluidStack.EMPTY;
                if(fluidBacklogs[i] != FluidStack.EMPTY)  {
                    localClog = true;
                }
            }
        }
        if(localClog)  {
            this.clogged = true;
            this.status = "fluid clogged";
            return false;
        }


        return true;
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        this.numOutputTanks = nbt.getInt(NUM_OUTPUT_TANKS);
        fluidItemInputSlots.deserializeNBT(nbt.getCompound(FLUID_ITEM_INPUT_SLOTS));
        fluidItemOutputSlots.deserializeNBT(nbt.getCompound(FLUID_ITEM_OUTPUT_SLOTS));

        // output tanks
        FluidTank[] outputTanks = new FluidTank[this.numOutputTanks];
        for(int i = 0; i < this.numOutputTanks; i++)  {
            outputTankCapacities[i] = nbt.getInt(OUTPUT_TANK_CAP + i);
            outputTanks[i] = new FluidTank(outputTankCapacities[i]);
            outputTanks[i].readFromNBT(nbt.getCompound(OUTPUT_FLUID_TANK + i));
            fluidBacklogs[i] = FluidStack.loadFluidStackFromNBT(nbt.getCompound(FLUID_BACKLOG + i));
        }
        this.outputFluidTanks = new OutputFluidTanks(outputTanks);

        // input tanks
        FluidTank[] inputTanks = new FluidTank[this.numInputTanks];
        for(int i = 0; i < this.numInputTanks; i++)  {
            inputTankCapacities[i] = nbt.getInt(INPUT_TANK_CAP + i);
            inputTanks[i] = new FluidTank(inputTankCapacities[i]);
            inputTanks[i].readFromNBT(nbt.getCompound(INPUT_FLUID_TANK + i));
        }
        this.inputFluidTanks = new InputFluidTank(inputTanks);


    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag = super.write(tag);
        tag.putInt(NUM_OUTPUT_TANKS, this.numOutputTanks);
        tag.put(FLUID_ITEM_INPUT_SLOTS, fluidItemInputSlots.serializeNBT());
        tag.put(FLUID_ITEM_OUTPUT_SLOTS, fluidItemOutputSlots.serializeNBT());

        for(int i = 0; i < numOutputTanks; i++)  {
            tag.put(OUTPUT_FLUID_TANK + i, outputFluidTanks.getFluidTank(i).writeToNBT(new CompoundNBT()));
            tag.put(FLUID_BACKLOG + i, fluidBacklogs[i].writeToNBT(new CompoundNBT()));
            tag.putInt(OUTPUT_TANK_CAP + i, outputTankCapacities[i]);
        }
        for(int i = 0; i < numInputTanks; i++)  {
            tag.put(INPUT_FLUID_TANK + i, inputFluidTanks.getFluidTank(i).writeToNBT(new CompoundNBT()));
            tag.putInt(INPUT_TANK_CAP + i, inputTankCapacities[i]);
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
  //      if(cap == CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY)  {
  //          return LazyOptional.of(() -> getFluidTanks()).cast();
  //      }
        return super.getCapability(cap, side);
    }





}
