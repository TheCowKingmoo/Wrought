package com.thecowking.wrought.inventory.containers.bloomery;

import com.thecowking.wrought.inventory.containers.MultiBlockContainer;
import com.thecowking.wrought.inventory.slots.SlotItemInput;
import com.thecowking.wrought.inventory.slots.SlotOutput;
import com.thecowking.wrought.tileentity.bloomery.BloomeryControllerTile;
import com.thecowking.wrought.util.RenderHelper;
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


    private int SLOTS_0_X =  RenderHelper.BLANK_X_SIZE - RenderHelper.GUI_X_MARGIN - 2*RenderHelper.SLOT_SIZE - RenderHelper.SLOT_SEP;
    private int SLOTS_1_X = RenderHelper.BLANK_X_SIZE - RenderHelper.GUI_X_MARGIN - RenderHelper.SLOT_SIZE;
    private int INPUTS_Y = RenderHelper.GUI_Y_MARGIN;
    private int OUTPUTS_Y = RenderHelper.BLANK_ACTUAL_HEIGHT - RenderHelper.GUI_Y_MARGIN - RenderHelper.SLOT_SIZE;

    // Margin + Tank Width + Sep
    private int FUEL_X = RenderHelper.GUI_X_MARGIN + RenderHelper.SLOT_SIZE + RenderHelper.SLOT_SEP;


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
                    addSlot(new SlotItemHandler(h, numSlot++, FUEL_X, OUTPUTS_Y));

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
