package com.thecowking.wrought.inventory.containers.blast_furnace;

import com.thecowking.wrought.inventory.containers.MultiBlockContainerFluid;
import com.thecowking.wrought.inventory.slots.SlotInputFluidContainer;
import com.thecowking.wrought.inventory.slots.SlotOutput;
import com.thecowking.wrought.tileentity.blast_furance.BlastFurnaceBrickControllerTile;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.thecowking.wrought.init.RegistryHandler.BLAST_FURANCE_MULTIBLOCK_CONTAINER;


public class BlastFurnaceContainerMultiblock extends MultiBlockContainerFluid {
    private static final Logger LOGGER = LogManager.getLogger();
    private BlastFurnaceBrickControllerTile controller;

    final static int INPUT_OUTPUT_X = 15;
    final static int SLOT_SEP_X = 22;


    //final static int X_WIDTH_OF_SLOT = 12;


    final static int FLUID_ITEM_X = 150;
    final static int INPUTS_Y = 21;
    final static int OUTPUTS_Y = 72;

    public BlastFurnaceContainerMultiblock(int windowId, World world, BlockPos pos, PlayerInventory playerInventory) {
        super(BLAST_FURANCE_MULTIBLOCK_CONTAINER.get(), windowId, world, pos, playerInventory);

        TileEntity tileEntity = world.getTileEntity(pos);
        this.controller = (BlastFurnaceBrickControllerTile)tileEntity;

        if(this.controller != null && !(controller.isFormed()))  {
            // basic auto building screen
            LOGGER.info("get builder screen");


        }  else  {
            LOGGER.info("get multiblock");
            controller.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                    int slot = 0;
                    // Add Ore Input Slot
                    addSlot(new SlotItemHandler(h, slot++, INPUT_OUTPUT_X, INPUTS_Y));
                    // Add Flux Input Slot
                    addSlot(new SlotItemHandler(h, slot++, INPUT_OUTPUT_X + SLOT_SEP_X, INPUTS_Y));
                    // Add Aux Input Slot
                    addSlot(new SlotItemHandler(h, slot++, INPUT_OUTPUT_X  + SLOT_SEP_X * 2, INPUTS_Y));

                    // Primary Item Output Slot
                     addSlot(new SlotOutput(h, slot++, INPUT_OUTPUT_X, OUTPUTS_Y));
                    // Secondary Item Output Slot
                    addSlot(new SlotOutput(h, slot++, INPUT_OUTPUT_X + SLOT_SEP_X, OUTPUTS_Y));
                    // Trinary Item Output Slot
                    addSlot(new SlotOutput(h, slot++, INPUT_OUTPUT_X  + SLOT_SEP_X*2, OUTPUTS_Y));

                    // Add Fuel Slot
                    addSlot(new SlotItemHandler(h, slot++, INPUT_OUTPUT_X  + 3*SLOT_SEP_X, OUTPUTS_Y));

                    // Add Molten Metal Fluid Item Input Slot
                    addSlot(new SlotInputFluidContainer(h, slot++, INPUT_OUTPUT_X * 2 + SLOT_SEP_X * 3, INPUTS_Y));
                    // Add Molten Slag Fluid Item Input Slot
                    addSlot(new SlotInputFluidContainer(h, slot++, INPUT_OUTPUT_X * 3 + SLOT_SEP_X * 4, INPUTS_Y));

                    // Add Molten Metal Fluid Item Output Slot
                    addSlot(new SlotOutput(h, slot++, INPUT_OUTPUT_X * 2 + SLOT_SEP_X * 3, OUTPUTS_Y));
                    // Add Molten Slag Fluid Item Output Slot
                    addSlot(new SlotOutput(h, slot++, INPUT_OUTPUT_X * 3 + SLOT_SEP_X * 4, OUTPUTS_Y));
                });

        }
    }

    public BlastFurnaceBrickControllerTile getController()  {
        return this.controller;
    }

    public double getProgress()  {
        if (controller.timeComplete == 0)  {return 0;}
        return (double)controller.timeElapsed / (controller.timeComplete);
    }


    public String getStatus()  {
        return controller.getStatus();

    }

}
