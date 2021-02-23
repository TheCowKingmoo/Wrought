package com.thecowking.wrought.inventory.containers;

import com.thecowking.wrought.tileentity.honey_comb_coke_oven.HCStateData;
import com.thecowking.wrought.tileentity.honey_comb_coke_oven.HCCokeOvenControllerTile;
import com.thecowking.wrought.util.RegistryHandler;
import com.thecowking.wrought.inventory.slots.SlotInputFluidContainer;
import com.thecowking.wrought.inventory.slots.SlotOutput;
import jdk.nashorn.internal.ir.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

import static com.thecowking.wrought.util.RegistryHandler.H_C_CONTAINER;


public class HCCokeOvenContainer extends Container {
    private TileEntity tileEntity;
    private IItemHandler playerInventory;
    private static final Logger LOGGER = LogManager.getLogger();
    private HCCokeOvenControllerTile controller;
    private World world;
    private BlockPos controllerPos;
    private HCStateData stateData;
    private PlayerEntity player;

    final static int ITEM_X = 14;
    final static int FLUID_ITEM_X = 149;
    final static int INPUTS_Y = 22;
    final static int OUTPUTS_Y = 72;


    final static int INPUT_ITEM_SLOT_IDX = 0;
    final static int PRIMARY_OUTPUT_ITEM_SLOT_IDX = 1;
    final static int SECONDARY_OUTPUT_ITEM_SLOT_IDX = 2;
    final static int FLUID_INPUT_ITEM_SLOT_IDX = 3;
    final static int FLUID_OUTPUT_ITEM_SLOT_IDX = 4;




    public HCCokeOvenContainer(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, HCStateData stateData) {
        super(H_C_CONTAINER.get(), windowId);
        tileEntity = world.getTileEntity(pos);
        this.playerInventory = new InvWrapper(playerInventory);
        this.world = world;
        this.controllerPos = pos;
        this.controller = (HCCokeOvenControllerTile)tileEntity;
        tileEntity = world.getTileEntity(pos);
        this.stateData = stateData;
        this.player = playerInventory.player;

        if (tileEntity != null) {
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                addSlot(new SlotItemHandler(h, INPUT_ITEM_SLOT_IDX, ITEM_X, INPUTS_Y));             // oven item input
                addSlot(new SlotOutput(h, PRIMARY_OUTPUT_ITEM_SLOT_IDX, ITEM_X, OUTPUTS_Y));                  // oven item ouput
                addSlot(new SlotOutput(h, SECONDARY_OUTPUT_ITEM_SLOT_IDX, ITEM_X+10, OUTPUTS_Y));                  // oven item ouput
                addSlot(new SlotInputFluidContainer(h, FLUID_INPUT_ITEM_SLOT_IDX, FLUID_ITEM_X, INPUTS_Y));    // fluid item input
                addSlot(new SlotOutput(h, FLUID_OUTPUT_ITEM_SLOT_IDX, FLUID_ITEM_X, OUTPUTS_Y));                 // fluid item output
            });
        }
        layoutPlayerInventorySlots(10, 115);
        trackIntArray(stateData);
    }



    private int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0 ; i < amount ; i++) {
            addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    private int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0 ; j < verAmount ; j++) {
            index = addSlotRange(handler, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }

    private void layoutPlayerInventorySlots(int leftCol, int topRow) {
        // Player inventory
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

        // Hotbar
        topRow += 58;
        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
    }

    public double getProgress()  {
        if (stateData.timeComplete == 0)  {return 0;}
        return (double)stateData.timeElapsed / (stateData.timeComplete);
    }

    public FluidStack getFluid()  {
        return controller.getFluidInTank();
    }

    public double getPercentageInTank()  {
       return ((double)getFluid().getAmount() / (double)getTankMaxSize());
    }

    public int getTankMaxSize()  {
        return controller.getTankMaxSize();
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
       //BlockPos targetBlock = new BlockPos(playerIn.getLookVec());
        return isWithinUsableDistance(IWorldPosCallable.of(tileEntity.getWorld(), controllerPos), playerIn, RegistryHandler.H_C_COKE_CONTROLLER_BLOCK.get());

        //return isWithinUsableDistance(IWorldPosCallable.of(tileEntity.getWorld(), targetBlock), playerIn, RegistryHandler.H_C_COKE_CONTROLLER_BLOCK.get());
    }

    @Nonnull
    @Override
    public ItemStack transferStackInSlot(final PlayerEntity player, final int index) {
        ItemStack returnStack = ItemStack.EMPTY;
        final Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            final ItemStack slotStack = slot.getStack();
            returnStack = slotStack.copy();
            final int containerSlots = this.inventorySlots.size() - player.inventory.mainInventory.size();
            if (index < containerSlots) {
                if (!mergeItemStack(slotStack, containerSlots, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!mergeItemStack(slotStack, 0, containerSlots, false)) {
                return ItemStack.EMPTY;
            }
            if (slotStack.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
            if (slotStack.getCount() == returnStack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(player, slotStack);
        }
        return returnStack;
    }


}
