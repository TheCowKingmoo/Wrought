package com.thecowking.wrought.inventory.containers.casting_machine;

import com.thecowking.wrought.client.screen.MultiblockScreen;
import com.thecowking.wrought.inventory.containers.MultiBlockContainer;
import com.thecowking.wrought.inventory.containers.MultiBlockContainerFluid;
import com.thecowking.wrought.inventory.slots.SlotFuelInput;
import com.thecowking.wrought.inventory.slots.SlotItemInput;
import com.thecowking.wrought.inventory.slots.SlotOutput;
import com.thecowking.wrought.tileentity.casting_machine.CastingMachineControllerTile;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.thecowking.wrought.init.RegistryHandler.CASTING_MACHINE_MULTIBLOCK_CONTAINER;


public class CastingMachineContainerMultiblock extends MultiBlockContainerFluid {
    private static final Logger LOGGER = LogManager.getLogger();
    private CastingMachineControllerTile controller;


    private int SLOTS_0_X =  MultiblockScreen.BLANK_X_SIZE - MultiblockScreen.GUI_X_MARGIN - 2*MultiblockScreen.SLOT_SIZE - MultiblockScreen.SLOT_SEP;
    private int OUTPUTS_Y = MultiblockScreen.BLANK_ACTUAL_HEIGHT - MultiblockScreen.GUI_Y_MARGIN - MultiblockScreen.SLOT_SIZE;

    // Margin + Tank Width + Sep
    private int FUEL_X = MultiblockScreen.GUI_X_MARGIN + MultiblockScreen.SLOT_SIZE + MultiblockScreen.SLOT_SEP;


    public CastingMachineContainerMultiblock(int windowId, World world, BlockPos pos, PlayerInventory playerInventory) {
        super(CASTING_MACHINE_MULTIBLOCK_CONTAINER.get(), windowId, world, pos, playerInventory);

        TileEntity tileEntity = world.getTileEntity(pos);
        this.controller = (CastingMachineControllerTile)tileEntity;



        if(this.controller != null && !(controller.isFormed()))  {
            // basic auto building screen

        }  else  {
            this.xSlot = new int[1];
            this.ySlot = new int[1];

            controller.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                    // Primary Item Output Slot
                xSlot[numSlot] = SLOTS_0_X;
                ySlot[numSlot] = OUTPUTS_Y;
                addSlot(new SlotOutput(h, numSlot,  xSlot[numSlot], ySlot[numSlot++]));
            });
        }
    }

    public int getNumMachineSlots()  {
        return this.numSlot;
    }

    public CastingMachineControllerTile getController()  {
        return this.controller;
    }

    public double getProgress()  {
        return controller.getCurrentCookingPercentge();
    }


    public String getStatus()  {
        return controller.getStatus();
    }

}
