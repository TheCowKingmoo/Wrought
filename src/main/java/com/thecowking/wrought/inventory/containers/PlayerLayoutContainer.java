package com.thecowking.wrought.inventory.containers;

import com.thecowking.wrought.Wrought;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

/*
    This Class is meant to layout the players inventory in a container
 */
public class PlayerLayoutContainer extends Container {
    protected World world;
    protected BlockPos controllerPos;
    protected PlayerEntity player;
    protected IItemHandler playerInventory;
    private final int TOP_ROW = 115;
    private final int LEFT_COL = 10;
    protected int numberMachineSlots = 0;
    protected int startIndex = 0;


    protected PlayerLayoutContainer(@Nullable ContainerType<?> type, int id, World world, BlockPos controllerPos, PlayerInventory playerInventory) {
        super(type, id);
        this.world = world;
        this.controllerPos = controllerPos;
        this.playerInventory = new InvWrapper(playerInventory);
        layoutPlayerInventorySlots(LEFT_COL, TOP_ROW);
        this.startIndex = 35;
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
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


    @Nonnull
    @Override
    public ItemStack transferStackInSlot(final PlayerEntity player, final int index) {
        Wrought.LOGGER.info("index == " + index);
        ItemStack returnStack = ItemStack.EMPTY;
        final Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            final ItemStack slotStack = slot.getStack();
            returnStack = slotStack.copy();
            final int containerSlots = this.inventorySlots.size() - player.inventory.mainInventory.size();
            Wrought.LOGGER.info("inv size = " + this.inventorySlots.size() + " player inv size = " + player.inventory.mainInventory.size());
            // this is going from mainInv to the machine
            if (index < player.inventory.mainInventory.size()) {




                if (!mergeItemStack(slotStack, player.inventory.mainInventory.size(), this.inventorySlots.size(), true)) {
                    Wrought.LOGGER.info("index < containerSlots  !mergeItemStack");
                    return ItemStack.EMPTY;
                }
            // this is going from the machine to the mainInv
            } else if (!mergeItemStack(slotStack, 0, player.inventory.mainInventory.size(), false)) {
                Wrought.LOGGER.info("!mergeItemStack");
                Wrought.LOGGER.info(containerSlots);

                return ItemStack.EMPTY;
            }
            if (slotStack.getCount() == 0) {
                Wrought.LOGGER.info("getCount == 0");
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
            if (slotStack.getCount() == returnStack.getCount()) {
                Wrought.LOGGER.info("slotStack count == returnStack count");
                return ItemStack.EMPTY;
            }
            slot.onTake(player, slotStack);
        }
        return returnStack;
    }


    @Override
    protected boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
        boolean flag = false;
        int i = startIndex;
        if (reverseDirection) {
            i = endIndex - 1;
        }

        if (stack.isStackable()) {
            while(!stack.isEmpty()) {
                if (reverseDirection) {
                    if (i < startIndex) {
                        break;
                    }
                } else if (i >= endIndex) {
                    break;
                }

                Slot slot = this.inventorySlots.get(i);
                ItemStack itemstack = slot.getStack();
                if (!itemstack.isEmpty() && areItemsAndTagsEqual(stack, itemstack) && slot.isItemValid(stack)) {
                    int j = itemstack.getCount() + stack.getCount();
                    int maxSize = Math.min(slot.getSlotStackLimit(), stack.getMaxStackSize());
                    if (j <= maxSize) {
                        stack.setCount(0);
                        itemstack.setCount(j);
                        slot.onSlotChanged();
                        flag = true;
                    } else if (itemstack.getCount() < maxSize) {
                        stack.shrink(maxSize - itemstack.getCount());
                        itemstack.setCount(maxSize);
                        slot.onSlotChanged();
                        flag = true;
                    }
                }

                if (reverseDirection) {
                    --i;
                } else {
                    ++i;
                }
            }
        }

        if (!stack.isEmpty()) {
            if (reverseDirection) {
                i = endIndex - 1;
            } else {
                i = startIndex;
            }

            while(true) {
                if (reverseDirection) {
                    if (i < startIndex) {
                        break;
                    }
                } else if (i >= endIndex) {
                    break;
                }

                Slot slot1 = this.inventorySlots.get(i);
                ItemStack itemstack1 = slot1.getStack();
                if (itemstack1.isEmpty() && slot1.isItemValid(stack)) {
                    if (stack.getCount() > slot1.getSlotStackLimit()) {
                        slot1.putStack(stack.split(slot1.getSlotStackLimit()));
                    } else {
                        slot1.putStack(stack.split(stack.getCount()));
                    }

                    slot1.onSlotChanged();
                    flag = true;
                    break;
                }

                if (reverseDirection) {
                    --i;
                } else {
                    ++i;
                }
            }
        }

        return flag;
    }

}
