package com.thecowking.wrought.tileentity.blast_furance;

import com.thecowking.wrought.data.BlastFurnaceData;
import com.thecowking.wrought.inventory.slots.*;
import com.thecowking.wrought.tileentity.MultiBlockControllerTile;
import com.thecowking.wrought.tileentity.MultiBlockControllerTileFluid;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import static com.thecowking.wrought.util.RegistryHandler.BLAST_FURNACE_BRICK_CONTROLLER_TILE;


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


    public BlastFurnaceBrickControllerTile() {
        super(BLAST_FURNACE_BRICK_CONTROLLER_TILE.get(), new BlastFurnaceData(), 1, 16000);
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


}
