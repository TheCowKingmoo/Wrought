package com.thecowking.wrought.inventory.containers.bloomery;

import com.thecowking.wrought.inventory.containers.MultiBlockContainerFluid;
import com.thecowking.wrought.inventory.containers.PlayerLayoutContainer;
import com.thecowking.wrought.inventory.slots.SlotInputFluidContainer;
import com.thecowking.wrought.inventory.slots.SlotOutput;
import com.thecowking.wrought.tileentity.blast_furance.BlastFurnaceBrickControllerTile;
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


public class BloomeryContainerMultiblock extends PlayerLayoutContainer {
    private static final Logger LOGGER = LogManager.getLogger();
    private BloomeryControllerTile controller;

    final static int INPUT_OUTPUT_X = 15;
    final static int SLOT_SEP_X = 22;


    //final static int X_WIDTH_OF_SLOT = 12;


    final static int FLUID_ITEM_X = 150;
    final static int INPUTS_Y = 21;
    final static int OUTPUTS_Y = 72;

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
                    int slot = 0;
                    // Add Ore Input Slot
                    addSlot(new SlotItemHandler(h, slot++, INPUT_OUTPUT_X, INPUTS_Y));
                    // Add Flux Input Slot
                    addSlot(new SlotItemHandler(h, slot++, INPUT_OUTPUT_X + SLOT_SEP_X, INPUTS_Y));

                    // Primary Item Output Slot
                     addSlot(new SlotOutput(h, slot++, INPUT_OUTPUT_X, OUTPUTS_Y));
                    // Secondary Item Output Slot
                    addSlot(new SlotOutput(h, slot++, INPUT_OUTPUT_X + SLOT_SEP_X, OUTPUTS_Y));

                    // Add Fuel Slot
                    addSlot(new SlotItemHandler(h, slot++, INPUT_OUTPUT_X  + 3*SLOT_SEP_X, OUTPUTS_Y));

                });

        }
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
