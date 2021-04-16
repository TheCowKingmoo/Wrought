package com.thecowking.wrought.inventory.containers.blast_furnace;

import com.thecowking.wrought.client.screen.MultiblockScreen;
import com.thecowking.wrought.inventory.containers.MultiBlockContainerFluid;
import com.thecowking.wrought.inventory.slots.SlotFuelInput;
import com.thecowking.wrought.inventory.slots.SlotInputFluidContainer;
import com.thecowking.wrought.inventory.slots.SlotItemInput;
import com.thecowking.wrought.inventory.slots.SlotOutput;
import com.thecowking.wrought.tileentity.blast_furance.BlastFurnaceBrickControllerTile;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import static com.thecowking.wrought.data.BlastFurnaceData.*;

import static com.thecowking.wrought.init.RegistryHandler.BLAST_FURANCE_MULTIBLOCK_CONTAINER;


public class BlastFurnaceContainerMultiblock extends MultiBlockContainerFluid {
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

        }  else  {
            controller.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {

                this.xSlot = new int[11];
                this.ySlot = new int[11];

                int x = FUEL_X + 2* MultiblockScreen.SLOT_SIZE + MultiblockScreen.SLOT_SEP;

                // Main Input Slots
                xSlot[numSlot] = FUEL_X + 2* MultiblockScreen.SLOT_SIZE + MultiblockScreen.SLOT_SEP;
                ySlot[numSlot] = INPUTS_Y;
                addSlot(new SlotItemInput(h, numSlot, xSlot[numSlot],  ySlot[numSlot++], controller));

                xSlot[numSlot] = x + (MultiblockScreen.SLOT_SIZE + MultiblockScreen.SLOT_SEP);
                ySlot[numSlot] = INPUTS_Y;
                addSlot(new SlotItemInput(h, numSlot, xSlot[numSlot],  ySlot[numSlot++], controller));

                xSlot[numSlot] = x + 2*(MultiblockScreen.SLOT_SIZE + MultiblockScreen.SLOT_SEP);
                ySlot[numSlot] = INPUTS_Y;
                addSlot(new SlotItemInput(h, numSlot, xSlot[numSlot],  ySlot[numSlot++], controller));

                // Main Output Slots
                xSlot[numSlot] = x;
                ySlot[numSlot] = OUTPUTS_Y;
                addSlot(new SlotOutput(h, numSlot,  xSlot[numSlot], ySlot[numSlot++]));

                xSlot[numSlot] = x + (MultiblockScreen.SLOT_SIZE + MultiblockScreen.SLOT_SEP);
                ySlot[numSlot] = OUTPUTS_Y;
                addSlot(new SlotOutput(h, numSlot,  xSlot[numSlot], ySlot[numSlot++]));

                xSlot[numSlot] = x + 2*(MultiblockScreen.SLOT_SIZE + MultiblockScreen.SLOT_SEP);
                ySlot[numSlot] = OUTPUTS_Y;
                addSlot(new SlotOutput(h, numSlot,  xSlot[numSlot], ySlot[numSlot++]));

                // Fuel Input Slot
                xSlot[numSlot] = FUEL_X;
                ySlot[numSlot] = OUTPUTS_Y;
                addSlot(new SlotFuelInput(h, numSlot, xSlot[numSlot] , ySlot[numSlot++], controller));

                // fluid input
                xSlot[numSlot] = X_SIZE - SLOT_SIZE - GUI_X_MARGIN + 1;
                ySlot[numSlot] = OUTPUTS_Y - MultiblockScreen.SLOT_SIZE/2 - MultiblockScreen.SLOT_SEP/2;
                addSlot(new SlotInputFluidContainer(h, numSlot, xSlot[numSlot], ySlot[numSlot++]));

                xSlot[numSlot] = X_SIZE - 2*SLOT_SIZE - SLOT_SEP - GUI_X_MARGIN + 1;
                ySlot[numSlot] = OUTPUTS_Y - MultiblockScreen.SLOT_SIZE/2 - MultiblockScreen.SLOT_SEP/2;
                addSlot(new SlotInputFluidContainer(h, numSlot, xSlot[numSlot], ySlot[numSlot++]));

                // fluid output
                xSlot[numSlot] = X_SIZE - SLOT_SIZE  - GUI_X_MARGIN + 1;
                ySlot[numSlot] = OUTPUTS_Y + MultiblockScreen.SLOT_SIZE/2 + MultiblockScreen.SLOT_SEP/2;
                addSlot(new SlotOutput(h, numSlot, xSlot[numSlot], ySlot[numSlot++]));

                xSlot[numSlot] = X_SIZE - 2*SLOT_SIZE - SLOT_SEP - GUI_X_MARGIN + 1;
                ySlot[numSlot] = OUTPUTS_Y + MultiblockScreen.SLOT_SIZE/2 + MultiblockScreen.SLOT_SEP/2;
                addSlot(new SlotOutput(h, numSlot, xSlot[numSlot], ySlot[numSlot++]));
            });

        }
    }

    public BlastFurnaceBrickControllerTile getController()  {
        return this.controller;
    }

}
