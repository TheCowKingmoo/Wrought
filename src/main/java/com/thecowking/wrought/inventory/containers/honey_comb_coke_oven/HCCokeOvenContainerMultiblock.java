package com.thecowking.wrought.inventory.containers.honey_comb_coke_oven;

import com.thecowking.wrought.inventory.containers.MultiBlockContainer;
import com.thecowking.wrought.inventory.containers.MultiBlockContainerFluid;
import com.thecowking.wrought.tileentity.MultiBlockControllerTile;
import com.thecowking.wrought.tileentity.honey_comb_coke_oven.HCCokeOvenControllerTile;
import com.thecowking.wrought.inventory.slots.SlotInputFluidContainer;
import com.thecowking.wrought.inventory.slots.SlotOutput;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import static com.thecowking.wrought.data.MultiblockData.*;
import static com.thecowking.wrought.util.RegistryHandler.H_C_CONTAINER;


public class HCCokeOvenContainerMultiblock extends MultiBlockContainerFluid {
    private TileEntity tileEntity;

    public HCCokeOvenControllerTile controller;

    final static int ITEM_X = 15;
    final static int FLUID_ITEM_X = 150;
    final static int INPUTS_Y = 21;
    final static int OUTPUTS_Y = 72;
    final static int SLOT_SEP_X = 22;

    public HCCokeOvenContainerMultiblock(int windowId, World world, BlockPos pos, PlayerInventory playerInventory) {
        super(H_C_CONTAINER.get(), windowId, world, pos, playerInventory);

        this.tileEntity = world.getTileEntity(pos);
        this.controller = (HCCokeOvenControllerTile)tileEntity;

        if(this.controller != null && !(controller.isFormed()))  {
            // basic auto building screen


        }  else  {

            if (tileEntity != null) {

                tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                    // Primary Item Input Slot

                    addSlot(new SlotItemHandler(h, PRIMARY_INPUT_ITEM_IDX, ITEM_X, INPUTS_Y));
                    // Primary Item Output Slot
                    addSlot(new SlotOutput(h, PRIMARY_OUTPUT_ITEM_SLOT_IDX, ITEM_X, OUTPUTS_Y));
                    // Secondary Item Output Slot
                    addSlot(new SlotOutput(h, SECONDARY_OUTPUT_ITEM_SLOT_IDX, ITEM_X+SLOT_SEP_X, OUTPUTS_Y));
                    // Fluid Item Input Slot
                    addSlot(new SlotInputFluidContainer(h, FLUID_INPUT_ITEM_SLOT_IDX, FLUID_ITEM_X, INPUTS_Y));
                    // Fluid Item Output Slot
                    addSlot(new SlotOutput(h, FLUID_OUTPUT_ITEM_SLOT_IDX, FLUID_ITEM_X, OUTPUTS_Y));
                });
            }

        }

    }

    public double getProgress()  {
        if (controller.timeComplete == 0)  {return 0;}
        return (double)controller.timeElapsed / (controller.timeComplete);
    }


}
