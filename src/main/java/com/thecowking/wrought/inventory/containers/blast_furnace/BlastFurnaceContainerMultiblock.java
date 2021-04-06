package com.thecowking.wrought.inventory.containers.blast_furnace;

import com.thecowking.wrought.client.screen.MultiblockScreen;
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

    private int FUEL_X = MultiblockScreen.GUI_X_MARGIN + MultiblockScreen.SLOT_SIZE + MultiblockScreen.SLOT_SEP;

    final static int OUTPUTS_Y = MultiblockScreen.BLANK_ACTUAL_HEIGHT - MultiblockScreen.GUI_Y_MARGIN - MultiblockScreen.SLOT_SIZE;
    final static int INPUTS_Y = MultiblockScreen.GUI_Y_MARGIN;

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

                    int x = FUEL_X + 2* MultiblockScreen.SLOT_SIZE + MultiblockScreen.SLOT_SEP;

                    // Add Ore Input Slot
                    addSlot(new SlotItemHandler(h, numSlot++, x, INPUTS_Y));
                    addSlot(new SlotOutput(h, numSlot++, x, OUTPUTS_Y));
                    x += MultiblockScreen.SLOT_SIZE + MultiblockScreen.SLOT_SEP;

                    // Add Flux Input Slot
                    addSlot(new SlotItemHandler(h, numSlot++, x, INPUTS_Y));
                    addSlot(new SlotOutput(h, numSlot++, x, OUTPUTS_Y));
                    x += MultiblockScreen.SLOT_SIZE + MultiblockScreen.SLOT_SEP;

                    // Add Aux Input Slot
                    addSlot(new SlotItemHandler(h, numSlot++, x, INPUTS_Y));
                    addSlot(new SlotOutput(h, numSlot++, x, OUTPUTS_Y));

                    // Primary Item Output Slot
                    // Secondary Item Output Slot
                    // Trinary Item Output Slot

                    // Add Fuel Slot
                    addSlot(new SlotItemHandler(h, numSlot++, FUEL_X, OUTPUTS_Y));

                    // Add Molten Metal Fluid Item Input Slot
                    addSlot(new SlotInputFluidContainer(h, numSlot++, X_SIZE - SLOT_SIZE - GUI_X_MARGIN + 1, MultiblockScreen.BLANK_ACTUAL_HEIGHT - 2*MultiblockScreen.SLOT_SIZE - 3*MultiblockScreen.SLOT_SEP));
                    // Add Molten Slag Fluid Item Input Slot
                    addSlot(new SlotInputFluidContainer(h, numSlot++, X_SIZE - 2*SLOT_SIZE - SLOT_SEP - GUI_X_MARGIN + 1, MultiblockScreen.BLANK_ACTUAL_HEIGHT - 2*MultiblockScreen.SLOT_SIZE - 3*MultiblockScreen.SLOT_SEP));

                    // Add Molten Metal Fluid Item Output Slot
                    addSlot(new SlotOutput(h, numSlot++, X_SIZE - SLOT_SIZE  - GUI_X_MARGIN + 1, MultiblockScreen.BLANK_ACTUAL_HEIGHT - MultiblockScreen.SLOT_SIZE - 2*MultiblockScreen.SLOT_SEP));
                    // Add Molten Slag Fluid Item Output Slot
                    addSlot(new SlotOutput(h, numSlot++, X_SIZE - 2*SLOT_SIZE - SLOT_SEP - GUI_X_MARGIN + 1, MultiblockScreen.BLANK_ACTUAL_HEIGHT - MultiblockScreen.SLOT_SIZE - 2*MultiblockScreen.SLOT_SEP));
                });

        }
    }

    public BlastFurnaceBrickControllerTile getController()  {
        return this.controller;
    }

}
