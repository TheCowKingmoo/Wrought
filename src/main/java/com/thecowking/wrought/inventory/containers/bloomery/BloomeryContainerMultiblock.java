package com.thecowking.wrought.inventory.containers.bloomery;

import com.thecowking.wrought.client.screen.MultiblockScreen;
import com.thecowking.wrought.inventory.containers.MultiBlockContainer;
import com.thecowking.wrought.inventory.slots.SlotFuelInput;
import com.thecowking.wrought.inventory.slots.SlotItemInput;
import com.thecowking.wrought.inventory.slots.SlotOutput;
import com.thecowking.wrought.tileentity.bloomery.BloomeryControllerTile;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.thecowking.wrought.init.RegistryHandler.BLOOMERY_MULTIBLOCK_CONTAINER;


public class BloomeryContainerMultiblock extends MultiBlockContainer {
    private static final Logger LOGGER = LogManager.getLogger();
    private BloomeryControllerTile controller;


    private int SLOTS_0_X =  MultiblockScreen.BLANK_X_SIZE - MultiblockScreen.GUI_X_MARGIN - 2*MultiblockScreen.SLOT_SIZE - MultiblockScreen.SLOT_SEP;
    private int SLOTS_1_X = MultiblockScreen.BLANK_X_SIZE - MultiblockScreen.GUI_X_MARGIN - MultiblockScreen.SLOT_SIZE;
    private int INPUTS_Y = MultiblockScreen.GUI_Y_MARGIN;
    private int OUTPUTS_Y = MultiblockScreen.BLANK_ACTUAL_HEIGHT - MultiblockScreen.GUI_Y_MARGIN - MultiblockScreen.SLOT_SIZE;

    // Margin + Tank Width + Sep
    private int FUEL_X = MultiblockScreen.GUI_X_MARGIN + MultiblockScreen.SLOT_SIZE + MultiblockScreen.SLOT_SEP;


    public BloomeryContainerMultiblock(int windowId, World world, BlockPos pos, PlayerInventory playerInventory) {
        super(BLOOMERY_MULTIBLOCK_CONTAINER.get(), windowId, world, pos, playerInventory);

        TileEntity tileEntity = world.getTileEntity(pos);
        this.controller = (BloomeryControllerTile)tileEntity;



        if(this.controller != null && !(controller.isFormed()))  {
            // basic auto building screen
            LOGGER.info("get builder screen");


        }  else  {

            LOGGER.info("get multiblock");
            controller.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                    // Add Ore Input Slot
                    addSlot(new SlotItemInput(h, numSlot++, SLOTS_0_X, INPUTS_Y, controller));
                    // Add Flux Input Slot
                    addSlot(new SlotItemInput(h, numSlot++, SLOTS_1_X,  INPUTS_Y, controller));

                    // Primary Item Output Slot
                     addSlot(new SlotOutput(h, numSlot++, SLOTS_0_X, OUTPUTS_Y));
                    // Secondary Item Output Slot
                    addSlot(new SlotOutput(h, numSlot++, SLOTS_1_X, OUTPUTS_Y));

                    // Add Fuel Slot
                    addSlot(new SlotFuelInput(h, numSlot++, FUEL_X, OUTPUTS_Y, controller));

                });


        }
    }

    public int getNumMachineSlots()  {
        return this.numSlot;
    }

    public BloomeryControllerTile getController()  {
        return this.controller;
    }

    public double getProgress()  {
        return controller.getCurrentCookingPercentge();
    }


    public String getStatus()  {
        return controller.getStatus();

    }

}
