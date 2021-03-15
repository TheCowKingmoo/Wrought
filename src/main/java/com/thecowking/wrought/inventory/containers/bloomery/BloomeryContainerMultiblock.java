package com.thecowking.wrought.inventory.containers.bloomery;

import com.thecowking.wrought.inventory.containers.MultiBlockContainer;
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

    public int MIDDLE_X = RenderHelper.X_SIZE / 2;
    public int MIDDLE_Y = 72;



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
                    addSlot(new SlotItemHandler(h, numSlot++, MIDDLE_X - RenderHelper.GUI_X_MARGIN - 2 * (RenderHelper.SLOT_SIZE + RenderHelper.SLOT_SEP), MIDDLE_Y - RenderHelper.GUI_Y_MARGIN));
                    // Add Flux Input Slot
                    addSlot(new SlotItemHandler(h, numSlot++, MIDDLE_X - RenderHelper.GUI_X_MARGIN - (RenderHelper.SLOT_SIZE + RenderHelper.SLOT_SEP),  MIDDLE_Y - RenderHelper.GUI_Y_MARGIN));

                    // Primary Item Output Slot
                     addSlot(new SlotOutput(h, numSlot++, MIDDLE_X + RenderHelper.GUI_X_MARGIN  + RenderHelper.SLOT_SEP, MIDDLE_Y - RenderHelper.GUI_Y_MARGIN));
                    // Secondary Item Output Slot
                    addSlot(new SlotOutput(h, numSlot++, MIDDLE_X + RenderHelper.GUI_X_MARGIN + RenderHelper.SLOT_SIZE + 2*RenderHelper.SLOT_SEP, MIDDLE_Y - RenderHelper.GUI_Y_MARGIN));

                    // Add Fuel Slot
                    addSlot(new SlotItemHandler(h, numSlot++, MIDDLE_X - RenderHelper.SLOT_SIZE / 2, MIDDLE_Y + RenderHelper.GUI_X_MARGIN));

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
        if (controller.timeComplete == 0)  {return 0;}
        return (double)controller.timeElapsed / (controller.timeComplete);
    }


    public String getStatus()  {
        return controller.getStatus();

    }

}
