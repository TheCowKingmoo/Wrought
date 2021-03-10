package com.thecowking.wrought.tileentity.blast_furance;

import com.thecowking.wrought.data.BlastFurnaceData;
import com.thecowking.wrought.inventory.slots.*;
import com.thecowking.wrought.tileentity.MultiBlockControllerTileFluid;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.thecowking.wrought.init.RegistryHandler.BLAST_FURNACE_BRICK_CONTROLLER_TILE;


public class BlastFurnaceBrickControllerTile extends MultiBlockControllerTileFluid {

    // input and outputs
    protected InputItemHandler oreInputSlot;
    protected InputItemHandler fluxInputSlot;
    protected InputItemHandler auxInputSlot;

    // Outputs
    protected OutputItemHandler primaryOutputSlot;
    protected OutputItemHandler secondaryOutputSlot;
    protected OutputItemHandler trinaryOutputSlot;

    // Fluid Item Input
    protected FluidItemInputHandler metalFluidItemInputSlot;
    protected FluidItemInputHandler slagFluidItemInputSlot;

    // Fluid Item Output
    protected FluidItemOutputHandler metalFluidItemOutputSlot;
    protected FluidItemOutputHandler slagFluidItemOutputSlot;

    protected InputItemHandler fuelInputSlot;


    // used when player is directly accessing multi-block
    private final LazyOptional<IItemHandler> everything = LazyOptional.of(() -> new CombinedInvWrapper(oreInputSlot, fluxInputSlot, auxInputSlot, primaryOutputSlot, secondaryOutputSlot, trinaryOutputSlot, metalFluidItemInputSlot, slagFluidItemInputSlot, metalFluidItemOutputSlot, slagFluidItemOutputSlot, fuelInputSlot));


    // used when in world things interact with multi-block
    private final LazyOptional<IItemHandler> automation = LazyOptional.of(() -> new AutomationCombinedInvWrapper(oreInputSlot, fluxInputSlot, auxInputSlot, primaryOutputSlot, secondaryOutputSlot, trinaryOutputSlot, metalFluidItemInputSlot, slagFluidItemInputSlot, metalFluidItemOutputSlot, slagFluidItemOutputSlot, fuelInputSlot));

    private final int tankIndex = 0;


    private int ORE_FLUID_TANK_INDEX = 0;
    private int FLUX_FLUID_TANK_INDEX = 1;
    private int AUX_FLUID_TANK_INDEX = 2;
    private static int NUMBER_INTERNAL_TANKS = 3;
    private static int DEFAULT_TANK_SIZE = 16000;


    public BlastFurnaceBrickControllerTile() {
        super(BLAST_FURNACE_BRICK_CONTROLLER_TILE.get(), new BlastFurnaceData(), NUMBER_INTERNAL_TANKS, DEFAULT_TANK_SIZE);
        initSlots();
    }

    public void initSlots()  {
        //Item Inputs
        this.oreInputSlot = new InputItemHandler(1, this, null, "ore");
        this.fluxInputSlot = new InputItemHandler(1, this, null, "flux");
        this.auxInputSlot = new InputItemHandler(1, this, null, "aux");

        //Item Outputs
        this.primaryOutputSlot = new OutputItemHandler(1);
        this.secondaryOutputSlot = new OutputItemHandler(1);
        this.trinaryOutputSlot = new OutputItemHandler(1);

        //Fluid Item Inputs
        this.metalFluidItemInputSlot = new FluidItemInputHandler(1);
        this.slagFluidItemInputSlot = new FluidItemInputHandler(1);

        //Fluid Item Outputs
        this.metalFluidItemOutputSlot = new FluidItemOutputHandler(1);
        this.slagFluidItemOutputSlot = new FluidItemOutputHandler(1);

        //Fuel Input Slot
        this.fuelInputSlot = new InputItemHandler(1, this, null, "fuel");
    }


    @Override
    public void tick() {
        super.tick();
        this.status = "Blast Furnace";
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
            return LazyOptional.of(() -> getFluidTanks()).cast();
        }

        return super.getCapability(cap, side);
    }


}
