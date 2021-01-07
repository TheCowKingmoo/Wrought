package com.thecowking.wrought.blocks.MultiBlock.honey_comb_coke_oven;

import com.thecowking.wrought.util.RegistryHandler;
import com.thecowking.wrought.util.SlotInput;
import com.thecowking.wrought.util.SlotInputFluidContainer;
import com.thecowking.wrought.util.SlotOutput;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

import static com.thecowking.wrought.blocks.MultiBlock.Multiblock.INDEX_ITEM_INPUT;
import static com.thecowking.wrought.blocks.MultiBlock.Multiblock.INDEX_ITEM_OUTPUT;
import static com.thecowking.wrought.util.RegistryHandler.H_C_CONTAINER;


public class HCCokeOvenContainer extends Container {
    private TileEntity tileEntity;
    private IItemHandler playerInventory;
    private static final Logger LOGGER = LogManager.getLogger();
    private HCCokeOvenControllerTile controller;

    public HCCokeOvenContainer(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
        super(H_C_CONTAINER.get(), windowId);
        tileEntity = world.getTileEntity(pos);
        this.playerInventory = new InvWrapper(playerInventory);
        this.controller = (HCCokeOvenControllerTile) tileEntity;


        if (tileEntity != null) {
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                addSlot(new SlotItemHandler(h, 0, 64, 24));     // oven item input
                addSlot(new SlotOutput(h, 1, 64, 48));          // oven item ouput
                addSlot(new SlotInputFluidContainer(h, 2, 128, 24));  // fluid item input
                addSlot(new SlotOutput(h, 3, 128, 48));         // fluid item output
            });
        }
        layoutPlayerInventorySlots(10, 70);
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

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos()), playerIn, RegistryHandler.H_C_COKE_CONTROLLER_BLOCK.get());
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
