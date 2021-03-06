package com.thecowking.wrought.tileentity;

import com.thecowking.wrought.data.IMultiblockData;
import com.thecowking.wrought.data.MultiblockData;
import com.thecowking.wrought.inventory.containers.OutputFluidTank;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    public void insertFluidIntoTank(int tankIndex, FluidStack fluidStack)  {
        fluidBacklogs[tankIndex] = getSingleTank(tankIndex).internalFill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
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







}
