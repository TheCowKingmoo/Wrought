package com.thecowking.wrought.inventory.containers.blast_furnace;

import com.thecowking.wrought.data.BlastFurnaceData;
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
import static com.thecowking.wrought.data.BlastFurnaceData.*;

import static com.thecowking.wrought.init.RegistryHandler.BLAST_FURANCE_MULTIBLOCK_CONTAINER;


public class BlastFurnaceContainerMultiblock extends MultiBlockContainerFluid {
    private static final Logger LOGGER = LogManager.getLogger();
    private BlastFurnaceBrickControllerTile controller;

    final static int INPUT_OUTPUT_X = 10;

    final static int SIZE_SLOT = 18;

   // final static int X_WIDTH_OF_SLOT = 18;



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

                    // Add Ore Input Slot
                    addSlot(new SlotItemHandler(h, numSlot++, GUI_X_MARGIN, GUI_Y_MARGIN));
                    // Add Flux Input Slot
                    addSlot(new SlotItemHandler(h, numSlot++, GUI_X_MARGIN + SLOT_SIZE + SLOT_SEP, GUI_Y_MARGIN));
                    // Add Aux Input Slot
                    addSlot(new SlotItemHandler(h, numSlot++, GUI_X_MARGIN  + 2 *(SLOT_SIZE + SLOT_SEP), GUI_Y_MARGIN));

                    // Primary Item Output Slot
                     addSlot(new SlotOutput(h, numSlot++, GUI_X_MARGIN, OUTPUTS_Y));
                    // Secondary Item Output Slot
                    addSlot(new SlotOutput(h, numSlot++, GUI_X_MARGIN + SLOT_SIZE + SLOT_SEP, OUTPUTS_Y));
                    // Trinary Item Output Slot
                    addSlot(new SlotOutput(h, numSlot++, GUI_X_MARGIN  + 2 *(SLOT_SIZE + SLOT_SEP), OUTPUTS_Y));

                    // Add Fuel Slot
                    addSlot(new SlotItemHandler(h, numSlot++, GUI_X_MARGIN  + 3 * (SLOT_SIZE + SLOT_SEP), 45));



                    // Add Molten Metal Fluid Item Input Slot
                    addSlot(new SlotInputFluidContainer(h, numSlot++, X_SIZE - SLOT_SIZE - GUI_X_MARGIN + 1, GUI_Y_MARGIN));
                    // Add Molten Slag Fluid Item Input Slot
                    addSlot(new SlotInputFluidContainer(h, numSlot++, X_SIZE - 3 * SLOT_SIZE - 2*SLOT_SEP - GUI_X_MARGIN + 1, GUI_Y_MARGIN));

                    // Add Molten Metal Fluid Item Output Slot
                    addSlot(new SlotOutput(h, numSlot++, X_SIZE - SLOT_SIZE  - GUI_X_MARGIN + 1, OUTPUTS_Y));
                    // Add Molten Slag Fluid Item Output Slot
                    addSlot(new SlotOutput(h, numSlot++, X_SIZE - 3 * SLOT_SIZE - 2*SLOT_SEP - GUI_X_MARGIN + 1, OUTPUTS_Y));
                });

        }
    }

    public BlastFurnaceBrickControllerTile getController()  {
        return this.controller;
    }

}
