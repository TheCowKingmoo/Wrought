package com.thecowking.wrought.tileentity.bloomery;

import com.thecowking.wrought.data.BlastFurnaceData;
import com.thecowking.wrought.data.BloomeryData;
import com.thecowking.wrought.inventory.containers.blast_furnace.BlastFurnaceContainerMultiblock;
import com.thecowking.wrought.inventory.containers.bloomery.BloomeryContainerMultiblock;
import com.thecowking.wrought.inventory.slots.AutomationCombinedInvWrapper;
import com.thecowking.wrought.tileentity.MultiBlockControllerTile;
import com.thecowking.wrought.tileentity.MultiBlockControllerTileFluid;
import com.thecowking.wrought.tileentity.WroughtMutliblock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import javax.annotation.Nullable;

import static com.thecowking.wrought.init.RegistryHandler.BLAST_FURNACE_BRICK_CONTROLLER_TILE;
import static com.thecowking.wrought.init.RegistryHandler.BLOOMERY_CONTROLLER_TILE;


public class BloomeryControllerTile extends MultiBlockControllerTile implements INamedContainerProvider, WroughtMutliblock {

    private static final int NUMBER_ITEM_INPUT_SLOTS = 2;
    private static final int NUMBER_ITEM_OUTPUT_SLOTS = 2;
    private static boolean NEEDS_FUEL = true;


    public BloomeryControllerTile() {

        super(BLOOMERY_CONTROLLER_TILE.get(), NUMBER_ITEM_INPUT_SLOTS, NUMBER_ITEM_OUTPUT_SLOTS, NEEDS_FUEL, new BloomeryData());
        this.status = "Standing By";

        this.everything = LazyOptional.of(() -> new CombinedInvWrapper(this.inputSlots, this.outputSlots, this.fuelInputSlot));
        this.automation = LazyOptional.of(() -> new AutomationCombinedInvWrapper(this.inputSlots, this.outputSlots, this.fuelInputSlot));
    }

    @Nullable
    @Override
    public Container createMenu(final int windowID, final PlayerInventory playerInv, final PlayerEntity playerIn) {
        return new BloomeryContainerMultiblock(windowID, this.world, getControllerPos(), playerInv);
    }

    /*
        Name that is displayed on the GUI
     */
    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("Bloomery");
    }


}
